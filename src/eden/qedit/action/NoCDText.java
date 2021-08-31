package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases CD-Text.
 *
 * @author Brendon
 */
public class NoCDText implements CueSheetAction {

  /** Key. */
  public static final String KEY = "no-cdtext";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getSession().getTracks().forEach(track -> track.clearCdText());
    sheet.getSession().clearCdText();
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
