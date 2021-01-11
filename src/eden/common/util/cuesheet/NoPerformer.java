package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit PERFORMERs.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoPerformer implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getSession().getTracks().forEach(track -> {
      track.setPerformer(null);
    });
    sheet.getSession().setPerformer(null);
    return true;
  }
}
