package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

import static java.lang.Integer.parseInt;

/**
 * {@code CueSheetFilter}: Adjust INDEX time codes by a given offset in frames.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class AdjustTime implements CueSheetFilter {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private int offset = 0;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code CueSheetFilter} with the given arguments. */
  public AdjustTime(String... args) {
    setParams(args);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      track.getIndexes().forEach(index -> {
        index.setFrame(index.getFrame() + this.offset < 0
            ? 0 : index.getFrame() + this.offset);
      });
    });
    return true;
  }

  /** @inheritDoc */
  @Override
  public int getParamCount() {
    return 1;
  }

  /** @inheritDoc */
  @Override
  public int setParams(String... args) {
    if (args.length < getParamCount())
      throw new IllegalArgumentException();
    try {
      this.offset = parseInt(args[0]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(args[0]);
    }
    return getParamCount();
  }
}
