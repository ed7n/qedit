package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * Erases CATALOG.
 *
 * @author Brendon
 */
public class NoCatalog implements CueSheetAction {

  /** Key. */
  public static final String KEY = "no-catalog";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getSession().unsetCatalog();
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
