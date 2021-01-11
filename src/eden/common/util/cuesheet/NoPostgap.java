package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit CD-TEXT fields.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoPostgap implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      track.unsetPostgap();
    });
    return true;
  }
}
