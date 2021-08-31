package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases SONGWRITERs.
 *
 * @author Brendon
 */
public class NoSongwriter implements CueSheetAction {

  /** Key: all. */
  public static final String KEY = "no-songwriter";

  /** Key: session. */
  public static final String KEY_SESSION = "no-session-songwriter";

  /** Key: track. */
  public static final String KEY_TRACK = "no-track-songwriter";

  /** Operation mode. */
  private final Mode mode;

  /** Makes an instance with the given operation mode. */
  public NoSongwriter(Mode mode) {
    this.mode = mode;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    if (this.mode != Mode.TRACK)
      sheet.getSession().unsetSongwriter();
    if (this.mode != Mode.SESSION)
      sheet.getSession().getTracks().forEach(track -> track.unsetSongwriter());
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    switch (this.mode) {
      case SESSION:
        return KEY_SESSION;
      case TRACK:
        return KEY_TRACK;
      default:
        return KEY;
    }
  }
}
