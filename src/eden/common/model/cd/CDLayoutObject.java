package eden.common.model.cd;

import eden.common.model.text.LineEnding;
import eden.common.object.Nullifiable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Defines a Compact Disc (CD) layout object in terms of a cuesheet.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public abstract class CDLayoutObject implements Nullifiable {

//~~OBJECT CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** CATALOG command. */
  protected final String CATALOG = "CATALOG";

  /** CDTEXTFILE command. */
  protected final String CDTEXTFILE = "CDTEXTFILE";

  /** FILE command. */
  protected final String FILE = "FILE";

  /** FLAGS command. */
  protected final String FLAGS = "FLAGS";

  /** ISRC command. */
  protected final String ISRC = "ISRC";

  /** POSTGAP command. */
  protected final String POSTGAP = "POSTGAP";

  /** PREGAP command. */
  protected final String PREGAP = "PREGAP";

  /** REM command. */
  protected final String REM = "REM";

  /** PERFORMER command. */
  protected final String PERFORMER = "PERFORMER";

  /** SONGWRITER command. */
  protected final String SONGWRITER = "SONGWRITER";

  /** TITLE command. */
  protected final String TITLE = "TITLE";

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** String arguments. */
  protected Map<String, String> arguments;

  /** REM arguments. */
  protected List<String> remarks = new LinkedList<>();

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Returns a stringified representation of this {@code CDLayoutObject} as a
   * list of {@code CueSheetStatement}.
   *
   * @see #toString()
   */
  public abstract List<CueSheetStatement> toStatements();

  /** Calls the given {@code BiFunction} to each string argument. */
  public void forEachStringStatement(BiFunction<String, String, String> function) {
    this.arguments.forEach((key, value) -> {
      String newValue = function.apply(key, value);
      if (newValue != value)
        this.arguments.put(key, newValue);
    });
  }

  /** Returns the {@code List} of REM arguments. */
  public List<String> getRems() {
    return this.remarks;
  }

  /** Returns the last REM argument. */
  public String getLastRem() {
    return hasRems() ? getRems().get(getRems().size() - 1) : null;
  }

  /** Appends the given REM argument. */
  public boolean addRem(String statement) {
    return getRems().add(statement);
  }

  /** Removes then returns the last REM argument. */
  public String removeLastRem() {
    return hasRems() ? getRems().remove(getRems().size() - 1) : null;
  }

  /** Returns whether this {@code CDLayoutObject} has REM arguments. */
  public boolean hasRems() {
    return !getRems().isEmpty();
  }

  /** @inheritDoc */
  @Override
  public abstract void nullifyObject();

  /** @inheritDoc */
  @Override
  public abstract boolean isObjectNullified();

  /** @inheritDoc */
  @Override
  public String toString() {
    return toString(System.lineSeparator());
  }

  /**
   * Returns a stringified representation of this {@code CDLayoutObject} with
   * the given {@code LineEnding}.
   */
  public String toString(LineEnding lineEnding) {
    return toString(lineEnding.toString());
  }

  /**
   * Returns a stringified representation of this {@code CDLayoutObject} with
   * the given line ending.
   */
  public String toString(String lineEnding) {
    return appendString(new StringBuilder(CueSheetStatement.LINE_WIDTH),
        lineEnding).toString();
  }

  /**
   * Returns a stringified representation of this Session in the given
   * StringBuilder with the given line ending.
   */
  protected StringBuilder appendString(
      StringBuilder builder, String lineEnding) {
    if (lineEnding == null)
      lineEnding = System.lineSeparator();
    for (CueSheetStatement statement : toStatements())
      builder.append(statement.toString()).append(lineEnding);
    return builder;
  }
}
