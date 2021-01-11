package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

import static eden.common.shared.Constants.STDOUT;

/**
 * {@code CueSheetFilter}: Print to stdout.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Print implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    STDOUT.print(sheet.getSession().toString());
    return true;
  }
}
