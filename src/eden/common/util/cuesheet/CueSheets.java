package eden.common.util.cuesheet;

import eden.common.excep.BadCueSheetException;
import eden.common.excep.BadISRCException;
import eden.common.excep.BadQuotationException;
import eden.common.excep.EDENRuntimeException;
import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;
import eden.common.model.cd.Session;
import eden.common.model.cd.Track;
import eden.common.model.text.LineEnding;
import eden.common.model.text.TextFile;
import eden.common.object.Nullifiable;
import eden.common.util.CDDAFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static eden.common.shared.Constants.DOUBLE_QUOTE;
import static eden.common.shared.Constants.SPACE;
import static eden.common.util.CDDAFrame.FPS;
import static eden.common.util.CDDAFrame.stringifyInt2Digits;
import static java.lang.Integer.parseInt;

/**
 * Consists of utility methods and constants for operating on cuesheets.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.model.cd.CueSheet
 */
public final class CueSheets {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Time code upper bound. */
  public static final int MAX_TIMECODE = ((60 * 99) + 59) * FPS;

  /** Maximum number of indexes in a CD. */
  public static final int MAX_INDEXES = Index.MAX_NUMBER - Index.MIN_NUMBER + 1;

  /** Maximum number of tracks in a CD. */
  public static final int MAX_TRACKS = Track.MAX_NUMBER - Track.MIN_NUMBER + 1;

  private static final Pattern REGEX_FILE
      = Pattern.compile("^FILE .+$");
  private static final Pattern REGEX_FLAGS
      = Pattern.compile("^FLAGS .+$");
  private static final Pattern REGEX_TITLE
      = Pattern.compile("^TITLE .+$");
  private static final Pattern REGEX_CDTEXTFILE
      = Pattern.compile("^CDTEXTFILE .+$");
  private static final Pattern REGEX_PERFORMER
      = Pattern.compile("^PERFORMER .+$");
  private static final Pattern REGEX_SONGWRITER
      = Pattern.compile("^SONGWRITER .+$");
  private static final Pattern REGEX_CATALOG
      = Pattern.compile("^CATALOG \\p{Alnum}{13}$");
  private static final Pattern REGEX_INDEX
      = Pattern.compile("^INDEX \\p{Digit}{2} [\\p{Digit}:]{8}$");
  private static final Pattern REGEX_ISRC
      = Pattern.compile("^ISRC \\p{Alnum}{12}$");
  private static final Pattern REGEX_POSTGAP
      = Pattern.compile("^POSTGAP [\\p{Digit}:]{8}$");
  private static final Pattern REGEX_PREGAP
      = Pattern.compile("^PREGAP [\\p{Digit}:]{8}$");
  private static final Pattern REGEX_TRACK
      = Pattern.compile("^TRACK \\p{Digit}{2} .+$");
  private static final Pattern REGEX_REM
      = Pattern.compile("^REM.*$");
  private static final Pattern REGEX_CATALOG_FORMAT
      = Pattern.compile("^\\p{Alnum}{13}$");
  private static final Pattern REGEX_ISRC_FORMAT
      = Pattern.compile("^\\p{Alnum}{5}\\p{Digit}{7}$");

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** To prevent instantiations of this class. */
  private CueSheets() {
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Returns whether the given string is properly enclosed in quotation marks.
   */
  public static boolean checkQuotations(String string) {
    return (!string.contains(SPACE)
        && !(string.startsWith(DOUBLE_QUOTE) ^ string.endsWith(DOUBLE_QUOTE))
        || (string.startsWith(DOUBLE_QUOTE) && string.endsWith(DOUBLE_QUOTE)));
  }

  /** Checks the given {@code Session} for errors then returns them. */
  public static List<EDENRuntimeException> checkSession(Session session) {
    List<EDENRuntimeException> out = new LinkedList<>();
    if (session.hasCatalog())
      if (session.getCatalog().length() != 13)
        out.add(new BadCueSheetException(
            "Session", "CATALOG is not 13 characters long"));
      else if (!REGEX_CATALOG_FORMAT.matcher(session.getCatalog()).matches())
        out.add(new BadCueSheetException(
            "Session", "CATALOG format is invalid."));
    if (session.hasCdTextFile()) {
      String file = session.getCdTextFile();
      if (!checkQuotations(file))
        out.add(makeExcepQuotation("Session", "CDTEXTFILE"));
    }
    session.forEachStringStatement((command, argument) -> {
      if (argument != null && !checkQuotations(argument))
        out.add(makeExcepQuotation("Session", command));
      return argument;
    });
    return out;
  }

  /**
   * Checks the given {@code Session}'s {@code Track}s for errors then returns
   * them.
   */
  public static List<EDENRuntimeException> checkTracks(Session session) {
    List<EDENRuntimeException> out = new LinkedList<>();
    if (!session.hasTracks())
      out.add(new BadCueSheetException("Session", "No TRACKs"));
    List<Track> tracks = session.getTracks();
    if (tracks.size() > MAX_TRACKS)
      out.add(new BadCueSheetException("Session", "Too many TRACKs"));
    int expected = 0;
    for (Track track : tracks) {
      int number = track.getNumber();
      if (!Track.isValidTrackNumber(number))
        out.add(makeExcepInvalid(stringifyTrack(number)));
      if (number != ++expected)
        out.add(makeExcepMissing(stringifyTrack(expected)));
      if (track.hasIsrc())
        if (track.getIsrc().length() != 12)
          out.add(new BadCueSheetException(
              stringifyTrack(number), "ISRC is not 12 characters long"));
        else if (!REGEX_ISRC_FORMAT.matcher(track.getIsrc()).matches())
          out.add(new BadISRCException(stringifyTrack(number)));
      track.forEachStringStatement((command, argument) -> {
        if (argument != null && !checkQuotations(argument))
          out.add(makeExcepQuotation(stringifyTrack(number), command));
        return argument;
      });
    }
    return out;
  }

  /**
   * Checks the given {@code Track}'s {@code Index}es for errors then returns
   * them.
   *
   * @param lastIndex the last {@code Index} to check time code against.
   */
  public static List<EDENRuntimeException> checkIndexes(
      Track track, Index lastIndex) {
    List<EDENRuntimeException> out = new LinkedList<>();
    int trackNumber = track.getNumber();
    if (!track.hasIndexes())
      out.add(new BadCueSheetException(
          stringifyTrack(trackNumber), "No INDEXes"));
    List<Index> indexes = track.getIndexes();
    if (indexes.size() > MAX_INDEXES)
      out.add(new BadCueSheetException(
          stringifyTrack(trackNumber), "Too many INDEXes"));
    String file;
    int expected = -1;
    for (Index index : indexes) {
      int number = index.getNumber(),
          frame = index.getFrame();
      if (index.hasFilePath()) {
        file = index.getFilePath();
        if (!checkQuotations(file))
          out.add(
              makeExcepQuotation(stringifyIndex(trackNumber, number), "FILE"));
      }
      if (!Index.isValidIndexNumber(number))
        out.add(makeExcepInvalid(stringifyIndex(trackNumber, number)));
      if (expected == -1) {
        if (number > 1 || (number == 0 && indexes.size() == 1))
          out.add(makeExcepMissing(stringifyIndex(trackNumber, number)));
        if (!index.hasFilePath()
            && lastIndex != null && frame < lastIndex.getFrame())
          out.add(new BadCueSheetException(
              stringifyIndex(trackNumber, number),
              "Time code is less than or equal to "
              + stringifyIndex(trackNumber - 1, lastIndex.getNumber())));
        expected = number;
      } else {
        if (number != ++expected)
          out.add(makeExcepMissing(stringifyIndex(trackNumber, expected)));
        if (!index.hasFilePath()
            && lastIndex != null && frame < lastIndex.getFrame())
          out.add(new BadCueSheetException(
              stringifyIndex(trackNumber, number),
              "Time code is less than INDEX " + stringifyInt(number - 1)));
      }
      lastIndex = index;
    }
    return out;
  }

  /**
   * Parses a {@code CueSheet} from the given {@code Reader}. Wraps the {@code
   * Reader} into a {@code LineNumberReader}, and then passes it to {@link
   * #parse(java.io.LineNumberReader)}, neither of them will be closed.
   *
   * @see #parse(java.io.LineNumberReader)
   */
  public static CueSheet parse(Reader reader) throws IOException {
    return parse(new LineNumberReader(reader));
  }

  /**
   * Parses a {@code CueSheet} from the given {@code LineNumberReader}. Does not
   * close the {@code LineNumberReader}.
   */
  public static CueSheet parse(LineNumberReader reader) throws IOException {
    return parse(new ParseContext(reader));
  }

  /**
   * Parses a {@code CueSheet} from the given file.
   *
   * @throws IOException if the file is a directory, or some other I/O exception
   * is thrown.
   */
  public static CueSheet parseFile(File file) throws IOException {
    if (!file.exists())
      throw new FileNotFoundException(file.getPath());
    if (file.isDirectory())
      throw new IOException("file.isDirectory");
    Reader reader = new FileReader(file);
    Reader reader2 = new BufferedReader(new FileReader(file));
    try {
      TextFile textFile = new TextFile(file);
      textFile.setLineEnding(LineEnding.parse(reader2));
      reader2.close();
      CueSheet out = parse(reader);
      reader.close();
      out.setTextFile(textFile);
      return out;
    } catch (IOException e) {
      reader2.close();
      reader.close();
      throw new IOException(
          file.getPath() + ":" + e.getMessage(), e.getCause());
    }
  }

  /** Parses a {@code CueSheet} from the given string. */
  public static CueSheet parseString(String string) throws IOException {
    try (Reader reader = new StringReader(string)) {
      return parse(reader);
    }
  }

  /** Parses a {@code CueSheet} from the given context. */
  private static CueSheet parse(ParseContext context) throws IOException {
    ParseContext c = context;
    try {
      if (c.readAndSetNextLine().startsWith("\uFEFF"))
        c.line = c.line.substring(1);
      parseSession(c);
      return c.sheet;
    } catch (IOException e) {
      throw new IOException(
          c.getLineNumber() + ": " + e.getMessage(), e.getCause());
    }
  }

  /** Parses a {@code Session} from the given context. */
  private static Session parseSession(ParseContext context)
      throws IOException {
    ParseContext c = context;
    while (c.line != null) {
      c.trimThisLine();
      if (REGEX_TRACK.matcher(c.line).matches()) {
        parseTracks(c);
        break;
      } else if (REGEX_FILE.matcher(c.line).matches())
        c.parseAndSetNewFile();
      else if (REGEX_CATALOG.matcher(c.line).matches())
        c.session.setCatalog(c.line.substring(8));
      else if (REGEX_CDTEXTFILE.matcher(c.line).matches())
        c.session.setCdTextFile(c.line.substring(11));
      else if (REGEX_PERFORMER.matcher(c.line).matches())
        c.session.setPerformer(c.line.substring(10));
      else if (REGEX_SONGWRITER.matcher(c.line).matches())
        c.session.setSongwriter(c.line.substring(11));
      else if (REGEX_TITLE.matcher(c.line).matches())
        c.session.setTitle(c.line.substring(6));
      else if (REGEX_REM.matcher(c.line).matches())
        c.session.addRem(c.line.length() < 4 ? "" : c.line.substring(4));
      c.readAndSetNextLine();
    }
    return c.session;
  }

  /** Parses {@code Track}(s) to the given context's {@code Session}. */
  private static void parseTracks(ParseContext context) throws IOException {
    ParseContext c = context;
    assert (c.track == null);
    c.parseAndSetNewNumber();
    c.parseAndSetNewTrack();
    while (true) {
      if (c.readAndSetNextLine() == null) {
        c.addThisTrack();
        return;
      }
      c.trimThisLine();
      if (REGEX_FILE.matcher(c.line).matches()) {
        c.parseAndSetNewFile();
        continue;
      }
      if (REGEX_INDEX.matcher(c.line).matches()) {
        parseIndexes(c);
        if (c.line == null) {
          c.addThisTrack();
          return;
        }
      }
      if (REGEX_TRACK.matcher(c.line).matches()) {
        c.parseAndSetNewNumber();
        c.addThisTrack();
        c.parseAndSetNewTrack();
      } else if (REGEX_REM.matcher(c.line).matches())
        c.track.addRem(c.line.length() < 4 ? "" : c.line.substring(4));
      else
        parseHelper(c);
    }
  }

  /** Parses {@code Index}(es) to the given context's {@code Track}. */
  private static void parseIndexes(ParseContext context) throws IOException {
    ParseContext c = context;
    c.parseAndSetNewNumber();
    c.parseAndSetNewFrame();
    c.parseAndSetNewIndex();
    while (true) {
      if (c.readAndSetNextLine() == null) {
        c.addThisIndex();
        return;
      }
      c.trimThisLine();
      if (REGEX_FILE.matcher(c.line).matches())
        c.parseAndSetNewFile();
      else if (REGEX_INDEX.matcher(c.line).matches()) {
        c.parseAndSetNewNumber();
        c.parseAndSetNewFrame();
        c.addThisIndex();
        c.parseAndSetNewIndex();
      } else if (REGEX_TRACK.matcher(c.line).matches()) {
        c.addThisIndex();
        return;
      } else if (REGEX_REM.matcher(c.line).matches())
        c.index.addRem(c.line.length() < 4 ? "" : c.line.substring(4));
      else
        parseHelper(c);
    }
  }

  /** Parses commands that are placed higher in the structure. */
  private static void parseHelper(ParseContext context) {
    ParseContext c = context;
    if (REGEX_FLAGS.matcher(c.line).matches())
      c.track.setFlags(c.line.substring(6));
    else if (REGEX_ISRC.matcher(c.line).matches())
      c.track.setIsrc(c.line.substring(5));
    else if (REGEX_PERFORMER.matcher(c.line).matches())
      c.track.setPerformer(c.line.substring(10));
    else if (REGEX_SONGWRITER.matcher(c.line).matches())
      c.track.setSongwriter(c.line.substring(11));
    else if (REGEX_TITLE.matcher(c.line).matches())
      c.track.setTitle(c.line.substring(6));
    else if (REGEX_POSTGAP.matcher(c.line).matches())
      c.track.setPostgap(CDDAFrame.parse(c.line.substring(8)));
    else if (REGEX_PREGAP.matcher(c.line).matches())
      c.track.setPregap(CDDAFrame.parse(c.line.substring(7)));
    else if (REGEX_CATALOG.matcher(c.line).matches())
      c.session.setCatalog(c.line.substring(8));
    else if (REGEX_CDTEXTFILE.matcher(c.line).matches())
      c.session.setCatalog(c.line.substring(11));
  }

  /**
   * Shorthand for {@link #stringifyInt2Digits(int)}
   *
   * @see #stringifyInt2Digits(int)
   */
  public static String stringifyInt(int integer) {
    return stringifyInt2Digits(integer);
  }

  /**
   * Stringifies the given track number.
   *
   * @return "TRACK {@code track}" where {@code track} is formatted to two
   * digits.
   * @see #stringifyInt2Digits(int)
   */
  public static String stringifyTrack(int track) {
    return "TRACK " + stringifyInt(track);
  }

  /**
   * Stringifies the given index and track numbers.
   *
   * @return "TRACK {@code track} INDEX {@code index}" where each number is
   * formatted to two digits.
   * @see #stringifyInt2Digits(int)
   */
  public static String stringifyIndex(int track, int index) {
    return stringifyTrack(track) + " INDEX " + stringifyInt(index);
  }

  /** Writes the given {@code CueSheet} into its {@code TextFile}. */
  public static void write(CueSheet sheet) throws IOException {
    if (sheet.hasTextFile() || sheet.getTextFile().hasFile())
      return;
    Path path = Paths.get(sheet.getTextFile().getFile().toURI());
    if (Files.isDirectory(path))
      throw new IllegalArgumentException("sheet..file.isDirectory");
    try (Writer writer = Files.newBufferedWriter(path)) {
      write(sheet, writer);
    }
  }

  /**
   * Writes the given {@code CueSheet} with the given {@code Writer}. Does not
   * close the {@code Writer}.
   */
  public static void write(CueSheet sheet, Writer writer) throws IOException {
    if (sheet.hasTextFile() && sheet.getTextFile().hasLineEnding())
      writer.write(
          sheet.getSession().toString(sheet.getTextFile().getLineEnding()));
    else
      writer.write(sheet.getSession().toString());
    writer.flush();
  }

  /** Makes a BadCueSheetException about an invalid item number. */
  private static BadCueSheetException makeExcepInvalid(String item) {
    return new BadCueSheetException(item, "Invalid number");
  }

  /** Makes a BadCueSheetException about a missing item. */
  private static BadCueSheetException makeExcepMissing(String item) {
    return new BadCueSheetException(item, "Missing or inconsistent");
  }

  /** Makes a BadQuotationException. */
  private static BadQuotationException makeExcepQuotation(
      String item, String field) {
    return new BadQuotationException(item + SPACE + field);
  }

//~~STATIC CLASSES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Encapsulation to be passed around the CueSheet parser. Includes convenient
   * methods.
   */
  private static class ParseContext implements Nullifiable {

//~~~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final LineNumberReader reader;
    private final CueSheet sheet = new CueSheet();
    private final Session session;
    private Track track;
    private Index index;
    private String line;
    private String file;
    private int number;
    private int frame;

//~~~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ParseContext(LineNumberReader reader) {
      assert (reader != null);
      this.reader = reader;
      this.session = this.sheet.getSession();
    }

    private ParseContext() {
      this.reader = null;
      this.session = null;
    }

//~~~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void addThisIndex() {
      getIndexes().add(this.index);
    }

    private void addThisTrack() {
      getTracks().add(this.track);
    }

    private String parseAndSetNewFile() {
      this.file = this.line.substring(5);
      return this.file;
    }

    private int parseAndSetNewFrame() {
      this.frame = CDDAFrame.parse(this.line.substring(9));
      return this.frame;
    }

    private Index parseAndSetNewIndex() {
      if (this.file == null)
        this.index = new Index(this.number, this.frame, null, null);
      else {
        int limit = this.file.lastIndexOf(SPACE);
        this.index
            = new Index(this.number, this.frame,
                this.file.substring(0, limit), this.file.substring(limit + 1));
        this.file = null;
      }
      return this.index;
    }

    private int parseAndSetNewNumber() {
      this.number = parseInt(this.line.substring(6, 8));
      return this.number;
    }

    private Track parseAndSetNewTrack() {
      this.track = new Track(this.number, this.line.substring(9));
      return this.track;
    }

    private String readAndSetNextLine() throws IOException {
      this.line = this.reader.readLine();
      return this.line;
    }

    private String trimThisLine() {
      this.line = this.line.trim();
      return this.line;
    }

    private List<Track> getTracks() {
      return this.session.getTracks();
    }

    private Track getLastTrack() {
      return this.session.getLastTrack();
    }

    private List<Index> getIndexes() {
      return this.track.getIndexes();
    }

    private Index getLastIndex() {
      return getIndexes().isEmpty()
          ? this.session.getLastIndex() : this.track.getLastIndex();
    }

    private int getTrackNumber() {
      return this.track.getNumber();
    }

    private int getIndexNumber() {
      return this.index.getNumber();
    }

    private int getLineNumber() {
      return this.reader.getLineNumber();
    }

    /** @inheritDoc */
    @Override
    public void nullifyObject() {
      this.track = null;
      this.index = null;
      this.line = null;
      this.file = null;
      this.number = 0;
      this.frame = 0;
    }

    /** @inheritDoc */
    @Override
    public boolean isObjectNullified() {
      throw new UnsupportedOperationException();
    }
  }
}
