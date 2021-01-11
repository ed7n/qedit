package eden.common.model.text;

import java.io.IOException;
import java.io.Reader;

/**
 * Defines a line ending.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public final class LineEnding {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  public static final LineEnding CRLF
      = new LineEnding("\r\n", "CRLF", "Carriage Return Line Feed", "Windows");
  public static final LineEnding LF
      = new LineEnding("\n", "LF", "Line Feed", "Unix");
  public static final LineEnding CR
      = new LineEnding("\r", "CR", "Carriage Return", "Macintosh");
  public static final LineEnding SYSTEM = parse(System.lineSeparator());

//~~OBJECT CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private final String string;
  private final String name;
  private final String longName;
  private final String systemName;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private LineEnding(
      String string, String name, String longName, String systemName) {
    this.string = string;
    this.name = name;
    this.longName = longName;
    this.systemName = systemName;
  }

  private LineEnding() {
    this.string = null;
    this.name = null;
    this.longName = null;
    this.systemName = null;
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Parses a {@code LineEnding} from the given line. */
  public static LineEnding parse(String line) {
    if (line == null)
      return null;
    if (line.endsWith(CRLF.string))
      return CRLF;
    if (line.endsWith(LF.string))
      return LF;
    if (line.endsWith(CR.string))
      return CR;
    return null;
  }

  /** Parses a {@code LineEnding} from the given {@code Reader}. */
  public static LineEnding parse(Reader reader) throws IOException {
    int character;
    while (true) {
      character = reader.read();
      if (character == '\r') {
        character = reader.read();
        if (character == '\n')
          return CRLF;
        else
          return CR;
      }
      if (character == '\n')
        return LF;
    }
  }

  /** Parses the given name. */
  public static LineEnding parseName(String name) {
    if (name == null)
      return null;
    if (name.equalsIgnoreCase(CRLF.name)
        || name.equalsIgnoreCase(CRLF.longName)
        || name.equalsIgnoreCase(CRLF.systemName))
      return CRLF;
    if (name.equalsIgnoreCase(LF.name)
        || name.equalsIgnoreCase(LF.longName)
        || name.equalsIgnoreCase(LF.systemName))
      return LF;
    if (name.equalsIgnoreCase(CR.name)
        || name.equalsIgnoreCase(CR.longName)
        || name.equalsIgnoreCase(CR.systemName))
      return CR;
    return null;
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns the abbreviated name ("LF"). */
  public String getName() {
    return this.name;
  }

  /** Returns the long name ("Line Feed"). */
  public String getlongName() {
    return this.longName;
  }

  /** Returns the system name ("Unix"). */
  public String getSystemName() {
    return this.systemName;
  }

  /** Returns this {@code LineEnding} as a string ("\n"). */
  @Override
  public String toString() {
    return this.string;
  }
}
