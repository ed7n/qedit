package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit TITLEs.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoTitle implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getSession().getTracks().forEach(track -> {
      track.setTitle(null);
    });
    sheet.getSession().setTitle(null);
    return true;
  }
}
