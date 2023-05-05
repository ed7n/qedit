package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases PERFORMERs.
 *
 * @author Brendon
 */
public class NoPerformer implements CueSheetAction {

  /** Key: all. */
  public static final String KEY = "no-performer";
  /** Key: session. */
  public static final String KEY_SESSION = "no-session-performer";
  /** Key: track. */
  public static final String KEY_TRACK = "no-track-performer";
  /** Operation mode. */
  private final Mode mode;

  /** Makes an instance with the given operation mode. */
  public NoPerformer(Mode mode) {
    this.mode = mode;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    if (mode != Mode.TRACK) {
      sheet.getSession().unsetPerformer();
    }
    if (mode != Mode.SESSION) {
      sheet.getSession().getTracks().forEach(track -> track.unsetPerformer());
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
