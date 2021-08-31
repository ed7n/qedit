package eden.qedit.model.application;

import static eden.common.shared.Constants.EOL;
import static eden.common.shared.Constants.SPACE;

/**
 * Consists of application descriptions.
 *
 * @author Brendon
 */
public final class Information {

  /** Application name. */
  public static final String NAME = "QEdit";

  /** Application version. */
  public static final String VER = "devF";

  /** Application version, long version. */
  public static final String VERSION = "Development F";

  /** Application release date. */
  public static final String DATE = "08/31/2021";

  /** Application description. */
  public static final String DESCRIPTION
      = "A cuesheet editor.";

  /** Application landing URL. */
  public static final String URL
      = "https://ed7n.github.com/qedit";

  /** To prevent instantiations of this class. */
  private Information() {
  }

  /** Returns the header for this application. */
  public static String getHeader() {
    return NAME + SPACE + VER + SPACE
        + "by Brendon," + SPACE + DATE + "." + EOL + "——" + DESCRIPTION + EOL;
  }
}
