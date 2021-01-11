package eden.common.util.cuesheet;

import eden.common.excep.BadCueSheetException;
import eden.common.excep.EDENRuntimeException;
import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;
import eden.common.model.cd.Session;
import eden.common.model.cd.Track;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static eden.common.shared.Constants.DOUBLE_QUOTE;
import static eden.common.shared.Constants.NUL_STRING;
import static eden.common.shared.Constants.SPACE;
import static eden.common.shared.Constants.STDERR;
import static eden.common.util.cuesheet.CueSheets.stringifyIndex;

/**
 * {@code CueSheetFilter}: Check for errors and prints them to stdout.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Check implements CueSheetFilter {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private String prefix;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code CueSheetFilter} with the given arguments. */
  public Check(String... args) {
    setParams(args);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    List<EDENRuntimeException> errors = new LinkedList<>();
    Session session = sheet.getSession();
    Index lastIndex = null;
    errors.addAll(CueSheets.checkSession(session));
    if (session.hasCdTextFile()) {
      String file
          = session.getCdTextFile().replaceAll(DOUBLE_QUOTE, NUL_STRING);
      if (Files.notExists(Paths.get(prefix + file)))
        errors.add(makeExcepFile(
            "Session", DOUBLE_QUOTE + file + DOUBLE_QUOTE, "does not exist"));
      else if (Files.isDirectory(Paths.get(prefix + file)))
        errors.add(makeExcepFile(
            "Session", DOUBLE_QUOTE + file + DOUBLE_QUOTE, "is a directory"));
    }
    errors.addAll(CueSheets.checkTracks(session));
    for (Track track : sheet.getTracks()) {
      errors.addAll(CueSheets.checkIndexes(track, lastIndex));
      track.getIndexes().forEach(index -> {
        if (!index.hasFilePath())
          return;
        String file
            = index.getFilePath().replaceAll(DOUBLE_QUOTE, NUL_STRING);
        if (Files.notExists(Paths.get(prefix + file)))
          errors.add(makeExcepFile(
              stringifyIndex(track.getNumber(), index.getNumber()),
              DOUBLE_QUOTE + file + DOUBLE_QUOTE, "does not exist"));
        else if (Files.isDirectory(Paths.get(prefix + file)))
          errors.add(makeExcepFile(
              stringifyIndex(track.getNumber(), index.getNumber()),
              DOUBLE_QUOTE + file + DOUBLE_QUOTE, "is a directory"));
      });
      lastIndex = track.getLastIndex();
    }
    errors.forEach(error -> {
      STDERR.println(error.getMessage());
    });
    return true;
  }

  /** @inheritDoc */
  @Override
  public int getParamCount() {
    return 0;
  }

  /** @inheritDoc */
  @Override
  public int setParams(String... args) {
    if (args.length < getParamCount())
      throw new IllegalArgumentException();
    this.prefix = args[0];
    if (this.prefix.contains(File.separator))
      this.prefix = this.prefix.substring(
          0, this.prefix.lastIndexOf(File.separator) + 1);
    else
      this.prefix = "";
    return getParamCount();
  }

  /** Makes a BadCueSheetException about a CDTEXTFILE-related error. */
  private static BadCueSheetException makeExcepCdTextFile(
      String item, String file, String description) {
    return new BadCueSheetException(
        item, "CDTEXTFILE " + file + SPACE + description);
  }

  /** Makes a BadCueSheetException about a FILE-related error. */
  private static BadCueSheetException makeExcepFile(
      String item, String file, String description) {
    return new BadCueSheetException(item, "FILE " + file + SPACE + description);
  }
}
