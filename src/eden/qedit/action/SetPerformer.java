package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;

import eden.common.model.cd.CueSheet;

/**
 * Sets PERFORMERs.
 *
 * @author Brendon
 */
public class SetPerformer implements CueSheetAction {

  /** Key: all. */
  public static final String KEY = "set-performer";

  /** Key: session. */
  public static final String KEY_SESSION = "set-session-performer";

  /** Key: track. */
  public static final String KEY_TRACK = "set-track-performer";

  /** Operation mode. */
  private final CueSheetAction.Mode mode;

  /** New performer. */
  private final String performer;

  /** Makes an instance with the given operation mode and new performer. */
  public SetPerformer(CueSheetAction.Mode mode, String performer) {
    this.mode = mode;
    this.performer = performer;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    if (this.mode != CueSheetAction.Mode.TRACK)
      sheet.getSession().setPerformer(this.performer);
    if (this.mode != CueSheetAction.Mode.SESSION)
      sheet.getSession().getTracks().forEach(
          track -> track.setPerformer(this.performer));
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    switch (this.mode) {
      case SESSION:
        return KEY_SESSION + SPACE + this.performer;
      case TRACK:
        return KEY_TRACK + SPACE + this.performer;
      default:
        return KEY + SPACE + this.performer;
    }
  }
}
