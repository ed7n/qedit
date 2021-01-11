package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * A {@code CueSheetFilter} operates on {@code CueSheets}. It is designed to
 * work with command-line utilities.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.model.cd.CueSheet
 */
public interface CueSheetFilter {

//~~INTERFACE METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Operates this {@code CueSheetFilter} on the given {@code CueSheet}. */
  boolean filter(CueSheet sheet) throws Throwable;

  /**
   * Returns the number of parameters required for this {@code CueSheetFilter}.
   */
  default int getParamCount() {
    return 0;
  }

  /**
   * Sets the filter parameters to the given arguments then returns the number
   * of strings taken from them.
   */
  default int setParams(String... args) {
    return 0;
  }
}
