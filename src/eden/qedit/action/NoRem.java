package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases REMs.
 *
 * @author Brendon
 */
public class NoRem implements CueSheetAction {

  /** Key. */
  public static final String KEY = "no-rem";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      track.getIndexes().forEach(index -> index.getRems().clear());
      track.getRems().clear();
    });
    sheet.getSession().getRems().clear();
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
