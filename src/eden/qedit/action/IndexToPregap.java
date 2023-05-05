package eden.qedit.action;

import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;

/**
 * Moves INDEX 00 to PREGAP. For each track:
 *
 * - The duration between INDEXes 00 and 01 is added to its PREGAP.
 *
 * - The FILE for INDEX 01 is set to that of INDEX 00.
 *
 * - INDEX 00 is removed.
 *
 * If INDEX 01 has a file, then it needs to calculate the duration between INDEX
 * 00 and the end of its file. But as calculations on files are not yet
 * supported, it does nothing.
 *
 * @author Brendon
 */
public class IndexToPregap implements CueSheetAction {

  /** Key. */
  public static final String KEY = "index-to-pregap";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet
      .getTracks()
      .forEach(track -> {
        if (
          track.getIndexes().size() > 1 &&
          track.getIndex(0).getNumber() == Index.MIN_NUMBER &&
          track.getIndex(1).getNumber() == Index.MIN_NUMBER + 1 &&
          !track.getIndex(1).hasFilePath()
        ) {
          int frames =
            track.getIndex(1).getFrame() - track.getIndex(0).getFrame();
          if (frames > 0) {
            track.setPregap(
              track.hasPregap() ? track.getPregap() + frames : frames
            );
            track
              .getIndex(1)
              .setFile(
                track.getIndex(0).getFilePath(),
                track.getIndex(0).getFileType()
              );
            track.getIndexes().remove(0);
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
