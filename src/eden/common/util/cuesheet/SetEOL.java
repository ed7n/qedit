package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;
import eden.common.model.text.LineEnding;

/**
 * {@code CueSheetFilter}: Set text document line ending.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class SetEOL implements CueSheetFilter {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private LineEnding lineEnding;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code CueSheetFilter} with the given arguments. */
  public SetEOL(String... args) {
    setParams(args);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTextFile().setLineEnding(this.lineEnding);
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
    if (!args[0].equalsIgnoreCase("CRLF")
        && !args[0].equalsIgnoreCase("LF")
        && !args[0].equalsIgnoreCase("CR"))
      throw new IllegalArgumentException(args[0]);
    this.lineEnding = LineEnding.parseName(args[0]);
    return getParamCount();
  }
}
