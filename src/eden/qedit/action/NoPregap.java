package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases PREGAPs.
 *
 * @author Brendon
 */
public class NoPregap implements CueSheetAction {

  /** Key. */
  public static final String KEY = "no-pregap";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getTracks().forEach(track -> track.unsetPregap());
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
