package eden.qedit;

import eden.common.io.Modal;
import eden.common.model.cd.CueSheet;
import eden.common.util.cuesheet.AdjustTime;
import eden.common.util.cuesheet.Check;
import eden.common.util.cuesheet.CueSheetFilter;
import eden.common.util.cuesheet.CueSheets;
import eden.common.util.cuesheet.IndexToPregap;
import eden.common.util.cuesheet.NoCDText;
import eden.common.util.cuesheet.NoCatalog;
import eden.common.util.cuesheet.NoISRC;
import eden.common.util.cuesheet.NoPerformer;
import eden.common.util.cuesheet.NoPostgap;
import eden.common.util.cuesheet.NoPregap;
import eden.common.util.cuesheet.NoRems;
import eden.common.util.cuesheet.NoSongwriter;
import eden.common.util.cuesheet.NoTitle;
import eden.common.util.cuesheet.PregapToIndex;
import eden.common.util.cuesheet.Print;
import eden.common.util.cuesheet.Quote;
import eden.common.util.cuesheet.SetEOL;
import eden.common.util.cuesheet.SwapAuthors;
import eden.common.util.cuesheet.Write;
import eden.qedit.model.application.Help;
import eden.qedit.model.application.Information;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static eden.common.shared.Constants.EXIT_FAILURE;
import static eden.common.shared.Constants.EXIT_SUCCESS;
import static eden.common.shared.Constants.STDOUT;

/**
 * This class serves as the entry point to this application. It consists of the
 * main method from which the application initializes into an instance.
 *
 * @author Brendon
 * @version devD
 */
public class QEdit {

//~~OBJECT CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private final Modal out = new Modal("QEdit");
  private final Modal err = new Modal("QEdit");
  private final String[] args;
  private final List<CueSheetFilter> filters = new LinkedList<>();

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private CueSheet sheet;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private QEdit(String[] args) {
    this.args = args;
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private int run() {
    int exitCode = parseArguments();
    if (exitCode != EXIT_SUCCESS)
      return exitCode;
    if (exitCode == Integer.MIN_VALUE)
      return EXIT_SUCCESS;
    try {
      this.sheet
          = CueSheets.parseFile(new File(this.args[this.args.length - 1]));
    } catch (IOException e) {
      this.err.println(e.getMessage(), Modal.ERROR);
      return EXIT_FAILURE;
    }
    return operateFilters();
  }

  private int parseArguments() {
    if (this.args.length == 0)
      return help();
    CueSheetFilter filter;
    for (int i = 0; i < this.args.length; i += filter.getParamCount()) {
      String arg = this.args[i++];
      try {
        if (arg.equalsIgnoreCase("--adjust-time")
            || arg.equalsIgnoreCase("--adj-time"))
          filter = new AdjustTime(this.args[i]);
        else if (arg.equalsIgnoreCase("--check"))
          filter = new Check(this.args[this.args.length - 1]);
        else if (arg.equalsIgnoreCase("--index-to-pregap"))
          filter = new IndexToPregap();
        else if (arg.equalsIgnoreCase("--pregap-to-index"))
          filter = new PregapToIndex();
        /*
        else if (arg.equalsIgnoreCase("--magic"))
          filter = new Magic(this.args[this.args.length - 1]);
        */
        else if (arg.equalsIgnoreCase("--no-catalog"))
          filter = new NoCatalog();
        else if (arg.equalsIgnoreCase("--no-cdtext"))
          filter = new NoCDText();
        else if (arg.equalsIgnoreCase("--no-isrc"))
          filter = new NoISRC();
        else if (arg.equalsIgnoreCase("--no-performer"))
          filter = new NoPerformer();
        else if (arg.equalsIgnoreCase("--no-postgap"))
          filter = new NoPostgap();
        else if (arg.equalsIgnoreCase("--no-pregap"))
          filter = new NoPregap();
        else if (arg.equalsIgnoreCase("--no-rems"))
          filter = new NoRems();
        else if (arg.equalsIgnoreCase("--no-songwriter"))
          filter = new NoSongwriter();
        else if (arg.equalsIgnoreCase("--no-title"))
          filter = new NoTitle();
        else if (arg.equalsIgnoreCase("--print"))
          filter = new Print();
        else if (arg.equalsIgnoreCase("--quote"))
          filter = new Quote(this.args[i]);
        else if (arg.equalsIgnoreCase("--set-eol"))
          filter = new SetEOL(this.args[i]);
        else if (arg.equalsIgnoreCase("--swap-authors"))
          filter = new SwapAuthors();
        else if (arg.equalsIgnoreCase("--write"))
          filter = new Write(this.args[i]);
        else if (arg.equalsIgnoreCase("--help") || arg.equalsIgnoreCase("-h"))
          return help();
        else if (arg.equals("--") || i == this.args.length)
          return EXIT_SUCCESS;
        else {
          this.err.println("Malformed action: " + arg, Modal.ERROR);
          return EXIT_FAILURE;
        }
      } catch (IndexOutOfBoundsException e) {
        this.err.println("Insufficient options for action " + arg, Modal.ERROR);
        return EXIT_FAILURE;
      } catch (IllegalArgumentException e) {
        this.err.println(
            "Malformed option for action " + arg + ": " + e.getMessage(),
            Modal.ERROR);
        return EXIT_FAILURE;
      }
      this.filters.add(filter);
    }
    this.err.println("No input file specified.", Modal.ERROR);
    return EXIT_FAILURE;
  }

  private int operateFilters() {
    for (CueSheetFilter filter : this.filters)
      try {
      if (!filter.filter(this.sheet)) {
        this.err.println(
            "Action " + filter.getClass().getSimpleName() + " failed.",
            Modal.ERROR);
        return EXIT_FAILURE;
      }
    } catch (Throwable e) {
      this.err.println(
          "Error in action " + filter.getClass().getSimpleName() + ": "
          + e.getMessage(),
          Modal.ERROR);
      return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
  }

  private int help() {
    STDOUT.println(
        Information.getHeader()
        + System.lineSeparator() + Help.USAGE
        + System.lineSeparator() + Help.EXPLANATION);
    return Integer.MIN_VALUE;
  }

//~~APPLICATION ENTRY POINT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * The main method is the entry point to this application.
   *
   * @param args command-line arguments to be passed on execution.
   */
  public static void main(String[] args) {
    System.exit(new QEdit(args).run());
  }
}
