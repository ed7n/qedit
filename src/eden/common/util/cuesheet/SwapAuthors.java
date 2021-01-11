package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Track;

/**
 * {@code CueSheetFilter}: Swap SONGWRITERs and PERFORMERs.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class SwapAuthors implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    String performer;
    for (Track track : sheet.getTracks()) {
      performer = track.getPerformer();
      track.setPerformer(track.getSongwriter());
      track.setSongwriter(performer);
    }
    performer = sheet.getSession().getPerformer();
    sheet.getSession().setPerformer(sheet.getSession().getSongwriter());
    sheet.getSession().setSongwriter(performer);
    return true;
  }
}
