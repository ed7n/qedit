package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases TITLEs.
 *
 * @author Brendon
 */
public class NoTitle implements CueSheetAction {

  /** Key: all. */
  public static final String KEY = "no-title";
  /** Key: session. */
  public static final String KEY_SESSION = "no-session-title";
  /** Key: track. */
  public static final String KEY_TRACK = "no-track-title";
  /** Operation mode. */
  private final Mode mode;

  /** Makes an instance with the given operation mode. */
  public NoTitle(Mode mode) {
    this.mode = mode;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    if (this.mode != Mode.TRACK) {
      sheet.getSession().unsetTitle();
    }
    if (this.mode != Mode.SESSION) {
      sheet.getSession().getTracks().forEach(track -> track.unsetTitle());
    }
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
