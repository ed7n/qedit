package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases ISRCs.
 *
 * @author Brendon
 */
public class NoISRC implements CueSheetAction {

  /** Key. */
  public static final String KEY = "no-isrc";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getTracks().forEach(track -> track.unsetIsrc());
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
