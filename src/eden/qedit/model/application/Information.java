package eden.qedit.model.application;

import static eden.common.shared.Constants.STRING_CAPACITY;

/**
 * The {@code Information} class consists of definitions that describe this
 * application.
 *
 * @author Brendon
 * @version devE
 */
public final class Information {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Application name. */
  public static final String NAME = "QEdit";

  /** Application version. */
  public static final String VER = "devE";

  /** Application version, long version. */
  public static final String VERSION = "Development E";

  /** Application release date. */
  public static final String DATE = "09/23/2020";

  /** Application description. */
  public static final String DESCRIPTION
      = "A cuesheet editor. Still under construction.";

  /** Application landing URL. */
  public static final String URL
      = "https://BrendonIrwan.github.com/QEdit";

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** To prevent instantiations of this class. */
  private Information() {
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns the header for this application. */
  public static String getHeader() {
    StringBuilder builder = new StringBuilder(STRING_CAPACITY);
    builder.append(NAME);
    builder.append(" ");
    builder.append(VER);
    builder.append(" by Brendon, ");
    builder.append(DATE);
    builder.append(".");
    builder.append(System.lineSeparator());
    builder.append("--");
    builder.append(DESCRIPTION);
    builder.append(System.lineSeparator());
    return builder.toString();
  }
}
