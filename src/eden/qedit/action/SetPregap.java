package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;

import eden.common.model.cd.CueSheet;
import eden.common.util.Numbers;

/**
 * Sets PREGAPs.
 *
 * @author Brendon
 */
public class SetPregap implements CueSheetAction {

  /** Key. */
  public static final String KEY = "set-pregap";

  /** New pregap in frames. */
  private final int pregap;

  /** Makes an instance with the given pregap in frames. */
  public SetPregap(int pregap) {
    this.pregap = Numbers.requireNonNegative(pregap);
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getTracks().forEach(track -> track.setPregap(this.pregap));
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY + SPACE + this.pregap;
  }
}
