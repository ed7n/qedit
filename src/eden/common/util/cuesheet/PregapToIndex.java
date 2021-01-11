package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;

/**
 * {@code CueSheetFilter}: Convert PREGAP to INDEX 00.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class PregapToIndex implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      if (track.getIndex(0).getNumber() != 0 && track.hasPregap()) {
        int frame = track.getIndex(0).getFrame() - track.getPregap();
        if (frame >= 0) {
          track.getIndexes().add(0, new Index(0, frame));
          if (track.getIndex(1).hasFilePath()) {
            track.getIndex(0).setFilePath(track.getIndex(1).getFilePath());
            track.getIndex(1).setFilePath(null);
          }
          track.unsetPregap();
        }
      }
    });
    return true;
  }
}
