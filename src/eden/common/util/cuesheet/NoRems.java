package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

/**
 * {@code CueSheetFilter}: Omit REM statements.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class NoRems implements CueSheetFilter {

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      track.getIndexes().forEach(index -> {
        index.getRems().clear();
      });
      track.getRems().clear();
    });
    sheet.getSession().getRems().clear();
    return true;
  }
}
