package eden.common.excep;

/**
 * Thrown when something within a {@code CueSheet} is malformed.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.model.cd.CueSheet
 */
public class BadCueSheetException extends EDENRuntimeException {

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code BadCueSheetException} with the given item and problem. */
  public BadCueSheetException(String item, String problem) {
    this(item, problem, null, null);
  }

  /**
   * Makes a {@code BadCueSheetException} with the given item, problem, and
   * remedy.
   */
  public BadCueSheetException(String item, String problem, String remedy) {
    this(item, problem, remedy, null);
  }

  /**
   * Makes a {@code BadCueSheetException} with the given item, problem, and
   * cause.
   */
  public BadCueSheetException(String item, String problem, Throwable cause) {
    this(item, problem, null, cause);
  }

  /**
   * Makes a {@code BadCueSheetException} with the given item, problem, remedy,
   * and cause.
   */
  public BadCueSheetException(
      String item, String problem, String remedy, Throwable cause) {
    super(item, problem, remedy, cause);
  }

  /** To prevent uninitialized instantiations of this class. */
  protected BadCueSheetException() {
  }
}
