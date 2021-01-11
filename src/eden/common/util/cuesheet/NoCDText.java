package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit CD-TEXT commands.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoCDText implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getSession().getTracks().forEach(track -> {
      track.setIsrc(null);
      track.setPerformer(null);
      track.setSongwriter(null);
      track.setTitle(null);
    });
    sheet.getSession().setPerformer(null);
    sheet.getSession().setSongwriter(null);
    sheet.getSession().setTitle(null);
    return true;
  }
}
