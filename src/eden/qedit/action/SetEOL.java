package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;

import eden.common.model.cd.CueSheet;
import eden.common.model.plaintext.LineEnding;
import java.util.Objects;

/**
 * Sets line ending to the given.
 *
 * @author Brendon
 */
public class SetEOL implements CueSheetAction {

  /** Key. */
  public static final String KEY = "set-eol";

  /** Line ending. */
  private final LineEnding lineEnding;

  /** Makes an instance with the given line ending. */
  public SetEOL(LineEnding lineEnding) {
    this.lineEnding = Objects.requireNonNull(lineEnding, "lineEnding");
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    sheet.getFile().setLineEnding(this.lineEnding);
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY + SPACE + lineEnding.getName();
  }
}
