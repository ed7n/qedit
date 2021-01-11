package eden.common.model.cd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Compact Disc (CD) session layout in terms of a cuesheet. It
 * consists of special arguments used in audio CDs (CDDA) including CD-TEXT.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Session extends CDLayoutObject {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Session tracks. */
  protected List<Track> tracks;

  /** CATALOG argument. */
  protected String catalog;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a blank {@code Session}. */
  public Session() {
    this(1, null, null, null, null, null);
  }

  /** Makes a {@code Session} with the given number of tracks. */
  public Session(int trackCount) {
    this(trackCount, null, null, null, null, null);
  }

  /** Makes a {@code Session} with the given arguments. */
  public Session(
      int trackCount, String catalog, String cdTextFile,
      String performer, String songwriter, String title) {
    if (trackCount < 0)
      throw new IllegalArgumentException("trackCount < 0");
    this.arguments = new HashMap<>(4);
    this.tracks = new ArrayList<>(trackCount);
    this.catalog = catalog;
    this.arguments.put(CDTEXTFILE, cdTextFile);
    this.arguments.put(PERFORMER, performer);
    this.arguments.put(SONGWRITER, songwriter);
    this.arguments.put(TITLE, title);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns the {@code List} of {@code Tracks}. */
  public List<Track> getTracks() {
    return this.tracks;
  }

  /** Returns the last {@code Track}. */
  public Track getLastTrack() {
    return getTrack(getTracks().size() - 1);
  }

  /** Returns the {@code Track} at the given index. */
  public Track getTrack(int index) {
    return getTracks().size() > index ? getTracks().get(index) : null;
  }

  /** Returns the last {@code Index} of the last {@code Track}. */
  public Index getLastIndex() {
    Track last = getLastTrack();
    return last != null ? last.getLastIndex() : null;
  }

  /** Appends the given {@code Track}. */
  public boolean addTrack(Track index) {
    return getTracks().add(index);
  }

  /** Removes then returns the last {@code Track}. */
  public Track removeLastTrack() {
    return hasTracks() ? getTracks().remove(getTracks().size() - 1) : null;
  }

  /** Returns the CDTEXTFILE argument. */
  public String getCdTextFile() {
    return this.arguments.get(CDTEXTFILE);
  }

  /** Sets the CDTEXTFILE argument. */
  public void setCdTextFile(String cdTextFile) {
    this.arguments.put(CDTEXTFILE, cdTextFile);
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

  /** Returns the CATALOG argument. */
  public String getCatalog() {
    return this.catalog;
  }

  /** Sets the CATALOG argument. */
  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  /** Returns whether the CATALOG argument is set. */
  public boolean hasCatalog() {
    return getCatalog() != null;
  }

  /** Returns whether the CDTEXTFILE argument is set. */
  public boolean hasCdTextFile() {
    return getCdTextFile() != null;
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

  /** Returns whether this {@code Session} has {@code Tracks}. */
  public boolean hasTracks() {
    return !getTracks().isEmpty();
  }

  /** @inheritDoc */
  @Override
  public List<CueSheetStatement> toStatements() {
    List<CueSheetStatement> out = new LinkedList<>();
    if (hasRems())
      getRems().forEach(rem -> {
        out.add(new CueSheetStatement("REM", rem));
      });
    if (hasCatalog())
      out.add(new CueSheetStatement(CATALOG, getCatalog()));
    if (hasCdTextFile())
      out.add(new CueSheetStatement(CDTEXTFILE, getCdTextFile()));
    if (hasPerformer())
      out.add(new CueSheetStatement(PERFORMER, getPerformer()));
    if (hasSongwriter())
      out.add(new CueSheetStatement(SONGWRITER, getSongwriter()));
    if (hasTitle())
      out.add(new CueSheetStatement(TITLE, getTitle()));
    getTracks().forEach(track -> {
      out.addAll(track.toStatements());
    });
    return out;
  }

  /** @inheritDoc */
  @Override
  public void nullifyObject() {
    this.arguments.clear();
    getTracks().clear();
    getRems().clear();
    this.arguments = null;
    this.tracks = null;
    this.remarks = null;
  }

  /** @inheritDoc */
  @Override
  public boolean isObjectNullified() {
    return this.tracks == null;
  }
}
