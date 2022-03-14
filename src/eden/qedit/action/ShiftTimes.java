package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;

import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;

/**
 * Shifts INDEX time codes by the given frame offset.
 *
 * @author Brendon
 */
public class ShiftTimes implements CueSheetAction {

  /** Key. */
  public static final String KEY = "shift-times";

  /** Frame offset. */
  private int offset;

  /** Makes an instance with the given frame offset. */
  public ShiftTimes(int offset) {
    this.offset = offset;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    if (this.offset == 0
        && sheet.getSession().hasTracks()
        && sheet.getSession().getTrack(0).hasIndexes())
      this.offset = -sheet.getTrack(0).getIndex(0).getFrame();
    sheet.getTracks().forEach(track -> {
      track.getIndexes().forEach(index -> {
        index.setFrame(Math.max(
            Index.MIN_FRAME, index.getFrame() + this.offset));
      });
    });
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY + SPACE + this.offset;
  }
}
