package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Convert INDEX 00 to PREGAP.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class IndexToPregap implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      if (!track.hasPregap() && track.getIndex(0).getNumber() == 0) {
        int pregap
            = track.getIndex(1).getFrame() - track.getIndex(0).getFrame();
        if (pregap >= 0) {
          track.setPregap(pregap);
          if (!track.getIndex(1).hasFilePath())
            track.getIndex(1).setFilePath(track.getIndex(0).getFilePath());
          track.getIndexes().remove(0);
        }
      }
    });
    return true;
  }
}
