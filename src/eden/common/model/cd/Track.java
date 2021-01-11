package eden.common.model.cd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static eden.common.util.CDDAFrame.toTimeCode;
import static eden.common.util.CDDAFrame.stringifyInt2Digits;

/**
 * Represents a Compact Disc (CD) track. It consists of special arguments used
 * in audio CDs (CDDA) including CD-TEXT, but it can also be used to represent a
 * binary track.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Track extends CDLayoutObject {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Maximum track number in a CD. */
  public static final int MAX_NUMBER = 99;

  /** Minimum track number in a CD. */
  public static final int MIN_NUMBER = 1;

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Value to use for an unset int. */
  protected static final int NOT_SET = Integer.MIN_VALUE;

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Track indexes. */
  protected List<Index> indexes;

  /** Track type. */
  protected String type;

  /** FLAGS argument. */
  protected String flags;

  /** ISRC argument. */
  protected String isrc;

  /** Number arguments. */
  protected int number, postgap = NOT_SET, pregap = NOT_SET;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes an empty {@code Track} with the given track number. */
  public Track(int number) {
    this(number, 1, NOT_SET, NOT_SET, null, null, null, null, null, null);
  }

  /** Makes a {@code Track} with the given track number and type. */
  public Track(int number, String type) {
    this(number, 1, NOT_SET, NOT_SET, null, null, null, null, null, type);
  }

  /** Makes a {@code Track} with the given arguments. */
  public Track(
      int number, int indexCount, int postgap, int pregap,
      String flags, String isrc, String performer,
      String songwriter, String title, String type) {
    if (indexCount < 0)
      throw new IllegalArgumentException("indexCount < 0");
    this.arguments = new HashMap<>(3);
    this.indexes = new ArrayList<>(indexCount);
    this.flags = flags;
    this.isrc = isrc;
    this.type = type;
    this.number = number;
    this.postgap = postgap;
    this.pregap = pregap;
    this.arguments.put(PERFORMER, performer);
    this.arguments.put(SONGWRITER, songwriter);
    this.arguments.put(TITLE, title);
  }

  /** To prevent uninitialized instantiations of this class. */
  private Track() {
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns whether the given track number is valid. */
  public static boolean isValidTrackNumber(int number) {
    return number >= MIN_NUMBER && number <= MAX_NUMBER;
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns the {@code List} of {@code Indexes}. */
  public List<Index> getIndexes() {
    return this.indexes;
  }

  /** Returns the last {@code Index}. */
  public Index getLastIndex() {
    return getIndex(getIndexes().size() - 1);
  }

  /** Returns the {@code Index} at the given index. */
  public Index getIndex(int index) {
    return getIndexes().size() > index ? getIndexes().get(index) : null;
  }

  /** Appends the given {@code Index}. */
  public boolean addIndex(Index index) {
    return getIndexes().add(index);
  }

  /** Removes then returns the last {@code Index}. */
  public Index removeLastIndex() {
    return hasIndexes() ? getIndexes().remove(getIndexes().size() - 1) : null;
  }

  /** Returns the PERFORMER argument. */
  public String getPerformer() {
    return this.arguments.get(PERFORMER);
  }

  /** Sets the PERFORMER argument. */
  public void setPerformer(String performer) {
    this.arguments.put(PERFORMER, performer);
  }

  /** Returns the SONGWRITER argument. */
  public String getSongwriter() {
    return this.arguments.get(SONGWRITER);
  }

  /** Sets the SONGWRITER argument. */
  public void setSongwriter(String songwriter) {
    this.arguments.put(SONGWRITER, songwriter);
  }

  /** Returns the TITLE argument. */
  public String getTitle() {
    return this.arguments.get(TITLE);
  }

  /** Sets the TITLE argument. */
  public void setTitle(String title) {
    this.arguments.put(TITLE, title);
  }

  /** Returns the FLAGS argument. */
  public String getFlags() {
    return this.flags;
  }

  /** Sets the FLAGS argument. */
  public void setFlags(String flags) {
    this.flags = flags;
  }

  /** Returns the ISRC argument. */
  public String getIsrc() {
    return this.isrc;
  }

  /** Sets the ISRC argument. */
  public void setIsrc(String isrc) {
    this.isrc = isrc;
  }

  /** Returns the track type argument. */
  public String getType() {
    return this.type;
  }

  /** Sets the track type argument. */
  public void setType(String type) {
    this.type = type;
  }

  /** Returns the track number. */
  public int getNumber() {
    return this.number;
  }

  /** Sets the track number. */
  public void setNumber(int number) {
    this.number = number;
  }

  /** Returns the track PREGAP. */
  public int getPregap() {
    return this.pregap;
  }

  /** Sets the track PREGAP. */
  public void setPregap(int pregap) {
    this.pregap = pregap;
  }

  /** Unsets the track PREGAP. */
  public void unsetPregap() {
    this.pregap = NOT_SET;
  }

  /** Returns the track POSTGAP. */
  public int getPostgap() {
    return this.postgap;
  }

  /** Sets the track POSTGAP. */
  public void setPostgap(int postgap) {
    this.postgap = postgap;
  }

  /** Unsets the track POSTGAP. */
  public void unsetPostgap() {
    this.postgap = NOT_SET;
  }

  /** Returns whether the FLAGS argument is set. */
  public boolean hasFlags() {
    return getFlags() != null;
  }

  /** Returns whether this {@code Track} has {@code Indexes}. */
  public boolean hasIndexes() {
    return !getIndexes().isEmpty();
  }

  /** Returns whether the ISRC argument is set. */
  public boolean hasIsrc() {
    return getIsrc() != null;
  }

  /** Returns whether the PERFORMER argument is set. */
  public boolean hasPerformer() {
    return getPerformer() != null;
  }

  /** Returns whether the SONGWRITER argument is set. */
  public boolean hasSongwriter() {
    return getSongwriter() != null;
  }

  /** Returns whether the TITLE argument is set. */
  public boolean hasTitle() {
    return getTitle() != null;
  }

  /** Returns whether the track PREGAP is set. */
  public boolean hasPregap() {
    return getPregap() != NOT_SET;
  }

  /** Returns whether the track POSTGAP is set. */
  public boolean hasPostgap() {
    return getPostgap() != NOT_SET;
  }

  /** @inheritDoc */
  @Override
  public List<CueSheetStatement> toStatements() {
    List<CueSheetStatement> out = new LinkedList<>();
    if (hasIndexes() && getIndex(0).hasFilePath())
      out.add(new CueSheetStatement(
          "FILE", getIndex(0).getFilePath(), getIndex(0).getFileType()));
    out.add(new CueSheetStatement(
        "  TRACK", stringifyInt2Digits(getNumber()), getType()));
    if (hasFlags())
      out.add(new CueSheetStatement("    FLAGS", getFlags()));
    if (hasRems())
      getRems().forEach(rem -> {
        out.add(new CueSheetStatement("    REM", rem));
      });
    if (hasIsrc())
      out.add(new CueSheetStatement("    ISRC", getIsrc()));
    if (hasTitle())
      out.add(new CueSheetStatement("    TITLE", getTitle()));
    if (hasPerformer())
      out.add(new CueSheetStatement("    PERFORMER", getPerformer()));
    if (hasSongwriter())
      out.add(new CueSheetStatement("    SONGWRITER", getSongwriter()));
    if (hasPregap())
      out.add(new CueSheetStatement("    PREGAP", toTimeCode(getPregap())));
    for (int i = 0; i < getIndexes().size(); i++) {
      if (i > 0 && getIndex(i).hasFilePath())
        out.add(new CueSheetStatement("FILE", getIndex(i).getFilePath()));
      out.addAll(getIndex(i).toStatements());
      if (i < getIndexes().size() - 1 && getIndex(i).hasRems())
        getIndex(i).getRems().forEach(rem -> {
          out.add(new CueSheetStatement("    REM", rem));
        });
    }
    if (hasPostgap())
      out.add(new CueSheetStatement("    POSTGAP", toTimeCode(getPregap())));
    if (getLastIndex().hasRems())
      getLastIndex().getRems().forEach(rem -> {
        out.add(new CueSheetStatement("  REM", rem));
      });
    return out;
  }

  /** @inheritDoc */
  @Override
  public void nullifyObject() {
    this.arguments.clear();
    getIndexes().clear();
    getRems().clear();
    this.arguments = null;
    this.indexes = null;
    this.remarks = null;
    this.number = 0;
    this.pregap = 0;
    this.postgap = 0;
  }

  /** @inheritDoc */
  @Override
  public boolean isObjectNullified() {
    return getIndexes() == null;
  }
}
