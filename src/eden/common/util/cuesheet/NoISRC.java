package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit ISRCs.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoISRC implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      track.setIsrc(null);
    });
    return true;
  }
}
