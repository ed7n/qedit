package eden.common.model.cd;

import eden.common.object.Nullifiable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Consists of a {@code List} of argument strings and a method that turn it into
 * a space-delimited string.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class CueSheetStatement implements Nullifiable {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Length of the longest fix-width {@code CueSheetStatement}. It is the
   * SONGWRITER command, up to 80 characters plus quotation marks and a space
   * delimiter. Note that FILE and REM statement can be longer than this.
   */
  public static final int LINE_WIDTH = 10 + 80 + 2 + 1;

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** List of arguments. */
  protected List<String> arguments;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code CueSheetStatement} with the given arguments. */
  public CueSheetStatement(String... arguments) {
    this.arguments = arguments != null
        ? new LinkedList<String>(Arrays.asList(arguments))
        : new LinkedList<String>();
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns the {@code List} of arguments. */
  public List<String> getArguments() {
    return this.arguments;
  }

  /** Returns the last argument. */
  public String getLastArgument() {
    return hasArguments()
        ? getArguments().get(getArguments().size() - 1) : null;
  }

  /** Appends the given argument. */
  public boolean addArgument(String argument) {
    return getArguments().add(argument);
  }

  /** Removes then returns the last argument. */
  public String removeLastArgument() {
    return hasArguments()
        ? getArguments().remove(getArguments().size() - 1) : null;
  }

  /** Returns whether this {@code CueSheetStatement} has arguments. */
  public boolean hasArguments() {
    return !getArguments().isEmpty();
  }

  /** @inheritDoc */
  @Override
  public String toString() {
    if (!hasArguments())
      return "";
    StringBuilder builder = new StringBuilder(LINE_WIDTH);
    Iterator<String> iterator = getArguments().iterator();
    while (true) {
      String argument = iterator.next();
      builder.append(argument);
      if (!iterator.hasNext())
        break;
      builder.append(" ");
    }
    return builder.toString();
  }

  /** @inheritDoc */
  @Override
  public void nullifyObject() {
    getArguments().clear();
    this.arguments = null;
  }

  /** @inheritDoc */
  @Override
  public boolean isObjectNullified() {
    return getArguments() == null;
  }
}
