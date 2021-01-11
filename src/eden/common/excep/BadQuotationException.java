package eden.common.excep;

/**
 * Thrown when a string is not properly enclosed in quotation marks.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class BadQuotationException extends EDENRuntimeException {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Description of the problem. */
  protected static final String PROBLEM
      = "The string is not properly enclosed in quotation marks (\").";

  /** Remedy to the problem. */
  protected static final String REMEDY
      = "Enclose it in quotation marks (\"), or check for \"open quotes that "
      + "must be closed.\"";

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code BadQuotationException} with the given string. */
  public BadQuotationException(String string) {
    super(string, PROBLEM, REMEDY);
  }

  /** To prevent uninitialized instantiations of this class. */
  protected BadQuotationException() {
  }
}
