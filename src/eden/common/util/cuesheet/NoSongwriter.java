package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit SONGWRITERs.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoSongwriter implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getSession().getTracks().forEach(track -> {
      track.setSongwriter(null);
    });
    sheet.getSession().setSongwriter(null);
    return true;
  }
}
