package eden.qedit.action;

import eden.common.model.cd.CueSheet;

/**
 * A CueSheetAction operates on cuesheets.
 *
 * @author Brendon
 * @see eden.common.model.cd.CueSheet
 */
public interface CueSheetAction {

  /** Runs its operation on the given cuesheet and returns its success. */
  boolean run(CueSheet sheet) throws Exception;

  /** {@inheritDoc} */
  @Override
  String toString();

  /** Operation modes. */
  public enum Mode {
    ALL, TRACK, SESSION;
  }
}
