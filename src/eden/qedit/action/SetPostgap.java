package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;

import eden.common.model.cd.CueSheet;
import eden.common.util.Numbers;

/**
 * Sets POSTGAPs.
 *
 * @author Brendon
 */
public class SetPostgap implements CueSheetAction {

  /** Key. */
  public static final String KEY = "set-postgap";
  /** New postgap in frames. */
  private final int postgap;

  /** Makes an instance with the given postgap in frames. */
  public SetPostgap(int postgap) {
    this.postgap = Numbers.requireNonNegative(postgap);
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getTracks().forEach(track -> track.setPostgap(this.postgap));
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY + SPACE + this.postgap;
  }
}
