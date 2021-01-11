package eden.common.model.cd;

import eden.common.model.text.TextFile;
import eden.common.object.Nullifiable;

import java.util.List;

/**
 * A definition of a Compact Disc (CD) session layout. At the very least, it
 * defines the starting time of each track and its indexes. A {@code CueSheet}
 * is defined by a single {@code Session} and its accompanying {@code TextFile},
 * thus this class does not support multi-session layouts. Some applications got
 * over this limitation with REM statements.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.model.cd.Session
 * @see eden.common.model.text.TextFile
 */
public class CueSheet implements Nullifiable {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Session. */
  protected Session session;

  /** Text file from/to which the cuesheet is parsed/written. */
  protected TextFile file;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a blank {@code CueSheet} with a blank {@code Session}. */
  public CueSheet() {
    this(new Session(), null);
  }

  /** Makes a {@code CueSheet} with the given {@code Session}. */
  public CueSheet(Session session) {
    this(session, null);
  }

  /**
   * Makes a {@code CueSheet} with the given {@code Session} and {@code
   * TextFile}.
   */
  public CueSheet(Session session, TextFile file) {
    this.session = session;
    this.file = file;
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Returns the text file from/to which this {@code CueSheet} is
   * parsed/written.
   */
  public TextFile getTextFile() {
    return this.file;
  }

  /**
   * Sets the text file from/to which this {@code CueSheet} is parsed/written.
   */
  public void setTextFile(TextFile file) {
    this.file = file;
  }

  /** Returns the session. */
  public Session getSession() {
    return this.session;
  }

  /** Returns the {@code List} of tracks in the session. */
  public List<Track> getTracks() {
    return getSession().getTracks();
  }

  /** Returns the {@code Track} at the given index in the session. */
  public Track getTrack(int index) {
    return getSession().getTrack(index);
  }

  /** Returns whether this {@code CueSheet} has a text file. */
  public boolean hasTextFile() {
    return getTextFile() != null;
  }

  /** @inheritDoc */
  @Override
  public void nullifyObject() {
    this.file = null;
    this.session = null;
  }

  /** @inheritDoc */
  @Override
  public boolean isObjectNullified() {
    return getSession() == null;
  }
}
