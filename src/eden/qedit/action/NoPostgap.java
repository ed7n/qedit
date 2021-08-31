package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases POSTGAPs.
 *
 * @author Brendon
 */
public class NoPostgap implements CueSheetAction {

  /** Key. */
  public static final String KEY = "no-postgap";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getTracks().forEach(track -> track.unsetPostgap());
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
