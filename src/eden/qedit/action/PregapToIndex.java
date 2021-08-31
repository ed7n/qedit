package eden.qedit.action;

import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;

/**
 * Moves PREGAP to INDEX 00. For each track:
 *
 * - The positive difference between its first INDEX and PREGAP is set to INDEX
 * 00.
 *
 * - If INDEX 00 was absent, the FILE for INDEX 01 is moved to INDEX 00.
 *
 * - If there is no remaining duration for PREGAP, it is unset.
 *
 * @author Brendon
 */
public class PregapToIndex implements CueSheetAction {

  /** Key. */
  public static final String KEY = "pregap-to-index";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      if (track.hasPregap() && track.hasIndexes()) {
        int frame = track.getIndex(0).getFrame() - track.getPregap();
        switch (track.getIndex(0).getNumber()) {
          case Index.MIN_NUMBER:
            if (frame == 0)
              return;
            if (frame < 0) {
              track.getIndex(0).setFrame(Index.MIN_FRAME);
              track.setPregap(-frame);
            } else {
              track.getIndex(0).setFrame(frame);
              track.unsetPregap();
            }
            break;
          case Index.MIN_NUMBER + 1:
            if (frame > 0) {
              track.getIndexes().add(
                  Index.MIN_NUMBER, new Index(Index.MIN_FRAME, frame));
              track.getIndex(0).setFile(
                  track.getIndex(1).getFilePath(),
                  track.getIndex(1).getFileType());
              track.getIndex(1).unsetFile();
              track.unsetPregap();
            }
            break;
          default:
            return;
        }
      }
    });
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
