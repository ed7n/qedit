package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit CATALOG.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoCatalog implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getSession().setCatalog(null);
    return true;
  }
}
