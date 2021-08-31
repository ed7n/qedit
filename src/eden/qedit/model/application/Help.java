package eden.qedit.model.application;

/**
 * Consists of program help messages.
 *
 * @author Brendon.
 */
public final class Help {

  /** Program usage syntax. */
  public static final String USAGE
      = "Usage: (<action> [<action-options>])... <file>";

  /** Explanation to syntax. */
  public static final String EXPLANATION
      = "Actions:\n"
      + "  check                 Check for errors and print them to the standard output.\n"
      + "  index-to-pregap       Move INDEX 00 to PREGAP.\n"
      + "  pregap-to-index       Move PREGAP to INDEX 00.\n"
      + "  no-catalog            Erase the session CATALOG.\n"
      + "  no-cdtext             Erase all CD-Text.\n"
      + "  no-isrc               Erase all ISRCs.\n"
      + "  no-performer          Erase all PERFORMERs.\n"
      + "  no-session-performer  Erase the session PERFORMER.\n"
      + "  no-track-performer    Erase track PERFORMERs.\n"
      + "  no-postgap            Erase all POSTGAPs.\n"
      + "  no-pregap             Erase all PREGAPs.\n"
      + "  no-rem                Erase all REMs.\n"
      + "  no-songwriter         Erase all SONGWRITERs.\n"
      + "  no-session-songwriter Erase the session SONGWRITER.\n"
      + "  no-track-songwriter   Erase track SONGWRITERs.\n"
      + "  no-title              Erase all TITLEs.\n"
      + "  no-session-title      Erase the session TITLE.\n"
      + "  no-track-title        Erase track TITLEs.\n"
      + "  print                 Print to the standard output.\n"
      + "  set-eol {CRLF|LF|CR}  Set line ending to the given.\n"
      + "  shift-times <offset>  Shift INDEX time codes by the given frame offset,\n"
      + "                        or `0` to use that of the absolute-first INDEX.\n"
      + "  swap-authors          Swap all SONGWRITERs and PERFORMERs.\n"
      + "  write <path>          Write to the given path.\n"
      + "\n"
      + "Actions are case-insensitive. Any failed action halts the program.\n"
      + "\n"
      + "Program Arguments:\n"
      + "  -h, --help            Display this help message.\n"
      + "  --                    Stop parsing arguments.\n";

  /** To prevent instantiations of this class. */
  private Help() {
  }
}
