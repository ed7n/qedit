package eden.common.excep;

/**
 * Thrown when a malformed International Standard Recording Code (ISRC) is
 * found.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see <a href="https://isrc.ifpi.org/en/isrc-standard/structure">Structure</a>
 */
public class BadISRCException extends EDENRuntimeException {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Description of the problem. */
  protected static final String PROBLEM = "The ISRC format is invalid.";

  /** Remedy to the problem. */
  protected static final String REMEDY
      = "Correct it such that the first five characters are alphanumeric, and "
      + "the last seven are numeric only.";

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code BadISRCException} with the given ISRC. */
  public BadISRCException(String isrc) {
    super(isrc, PROBLEM, REMEDY);
  }

  /** To prevent uninitialized instantiations of this class. */
  protected BadISRCException() {
  }
}
