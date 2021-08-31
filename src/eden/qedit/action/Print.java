package eden.qedit.action;

import static eden.common.shared.Constants.STDOUT;

import eden.common.model.cd.CueSheet;

/**
 * Prints to the standard output.
 *
 * @author Brendon
 */
public class Print implements CueSheetAction {

  /** Key. */
  public static final String KEY = "print";

  /** @inheritDoc */
  @Override
  public boolean run(CueSheet sheet) {
    STDOUT.print(sheet.getSession().toString(sheet.getFile().getLineEnding()));
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
