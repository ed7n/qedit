package eden.qedit.model.application;

/**
 * The {@code Help} class consists of program help messages.
 *
 * @author Brendon
 * @version devE
 */
public final class Help {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Program usage syntax. */
  public static final String USAGE
      = "Usage: (<action> [<action-options>])... <input-path>";

  /** Explanation to syntax. */
  public static final String EXPLANATION
      = "Actions:\n"
      + "  --adj-time <offset>, --adjust-time <offset>\n"
      + "                Adjust INDEX time codes by a given offset in frames.\n"
      + "  --check       Check for errors and prints them to stderr.\n"
      + "  --index-to-pregap\n"
      + "                Convert INDEX 00 to PREGAP.\n"
      + "  --pregap-to-index\n"
      + "                Convert PREGAP to INDEX 00.\n"
      + "  --no-catalog  Omit CATALOG.\n"
      + "  --no-cdtext   Omit CD-TEXT commands.\n"
      + "  --no-isrc     Omit ISRCs.\n"
      + "  --no-performer\n"
      + "                Omit PERFORMERs.\n"
      + "  --no-postgap  Omit POSTGAPs.\n"
      + "  --no-pregap   Omit PREGAPs.\n"
      + "  --no-rems     Omit REMs.\n"
      + "  --no-songwriter\n"
      + "                Omit SONGWRITERs.\n"
      + "  --no-title    Omit TITLEs.\n"
      + "  --print       Print to stdout.\n"
      + "  --quote {auto|force}\n"
      + "                Enclose fields in quotation marks. \"auto\" removes them\n"
      + "                as allowed.\n"
      + "  --set-eol {CRLF|LF|CR}\n"
      + "                Set text document line ending.\n"
      + "  --swap-authors\n"
      + "                Swap SONGWRITERs and PERFORMERs.\n"
      + "  --write <path>\n"
      + "                Write to a given output path.\n"
      + "\n"
      + "Actions are case-insensitive. Any failed action halts the program.\n"
      + "\n"
      + "Program Arguments:\n"
      + "  -h, --help\n"
      + "                Display this help message.\n"
      + "  --\n"
      + "                Stop parsing arguments.\n";

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** To prevent instantiations of this class. */
  private Help() {
  }
}
