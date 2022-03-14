package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;

import eden.common.model.cd.CueSheet;

/**
 * Sets TITLEs.
 *
 * @author Brendon
 */
public class SetTitle implements CueSheetAction {

  /** Key: all. */
  public static final String KEY = "set-title";

  /** Key: session. */
  public static final String KEY_SESSION = "set-session-title";

  /** Key: track. */
  public static final String KEY_TRACK = "set-track-title";

  /** Operation mode. */
  private final CueSheetAction.Mode mode;

  /** New title. */
  private final String title;

  /** Makes an instance with the given operation mode and new title. */
  public SetTitle(CueSheetAction.Mode mode, String title) {
    this.mode = mode;
    this.title = title;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    if (this.mode != CueSheetAction.Mode.TRACK)
      sheet.getSession().setTitle(this.title);
    if (this.mode != CueSheetAction.Mode.SESSION)
      sheet.getSession().getTracks().forEach(
          track -> track.setTitle(this.title));
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    switch (this.mode) {
      case SESSION:
        return KEY_SESSION + SPACE + this.title;
      case TRACK:
        return KEY_TRACK + SPACE + this.title;
      default:
        return KEY + SPACE + this.title;
    }
  }
}
