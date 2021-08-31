package eden.qedit.action;

import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Track;

/**
 * Swaps SONGWRITERs and PERFORMERs.
 *
 * @author Brendon
 */
public class SwapAuthors implements CueSheetAction {

  /** Key. */
  public static final String KEY = "swap-authors";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
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

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
