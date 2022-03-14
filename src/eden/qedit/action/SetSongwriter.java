package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;

import eden.common.model.cd.CueSheet;

/**
 * Sets SONGWRITERs.
 *
 * @author Brendon
 */
public class SetSongwriter implements CueSheetAction {

  /** Key: all. */
  public static final String KEY = "set-songwriter";

  /** Key: session. */
  public static final String KEY_SESSION = "set-session-songwriter";

  /** Key: track. */
  public static final String KEY_TRACK = "set-track-songwriter";

  /** Operation mode. */
  private final Mode mode;

  /** New songwriter. */
  private final String songwriter;

  /** Makes an instance with the given operation mode and new songwriter. */
  public SetSongwriter(Mode mode, String songwriter) {
    this.mode = mode;
    this.songwriter = songwriter;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    if (this.mode != Mode.TRACK)
      sheet.getSession().setSongwriter(this.songwriter);
    if (this.mode != Mode.SESSION)
      sheet.getSession().getTracks().forEach(
          track -> track.setSongwriter(this.songwriter));
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    switch (this.mode) {
      case SESSION:
        return KEY_SESSION + SPACE + this.songwriter;
      case TRACK:
        return KEY_TRACK + SPACE + this.songwriter;
      default:
        return KEY + SPACE + this.songwriter;
    }
  }
}
