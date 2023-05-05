package eden.qedit;

import static eden.common.shared.Constants.EOL;
import static eden.common.shared.Constants.EXIT_FAILURE;
import static eden.common.shared.Constants.EXIT_SUCCESS;
import static eden.common.shared.Constants.NUL_INT;
import static eden.common.shared.Constants.STDOUT;

import eden.common.excep.EDENException;
import eden.common.excep.EDENRuntimeException;
import eden.common.io.Modal;
import eden.common.model.cd.CueSheet;
import eden.common.model.plaintext.LineEnding;
import eden.common.util.CueSheets;
import eden.common.util.Strings;
import eden.qedit.action.Check;
import eden.qedit.action.CueSheetAction;
import eden.qedit.action.IndexToPregap;
import eden.qedit.action.NoCDText;
import eden.qedit.action.NoCatalog;
import eden.qedit.action.NoISRC;
import eden.qedit.action.NoPerformer;
import eden.qedit.action.NoPostgap;
import eden.qedit.action.NoPregap;
import eden.qedit.action.NoRem;
import eden.qedit.action.NoSongwriter;
import eden.qedit.action.NoTitle;
import eden.qedit.action.PregapToIndex;
import eden.qedit.action.Print;
import eden.qedit.action.SetEOL;
import eden.qedit.action.SetPerformer;
import eden.qedit.action.SetPostgap;
import eden.qedit.action.SetPregap;
import eden.qedit.action.SetSongwriter;
import eden.qedit.action.SetTitle;
import eden.qedit.action.ShiftTimes;
import eden.qedit.action.SwapAuthors;
import eden.qedit.action.Write;
import eden.qedit.model.application.Help;
import eden.qedit.model.application.Information;
import java.io.File;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 * This class serves as the entry point to this application. It consists of the
 * main method from which the application initializes into an instance.
 *
 * @author Brendon
 */
public class QEdit {

  /** Whether to print stack traces of caught exceptions. */
  public static final boolean DEBUG = false;
  private static final Pattern HELP = Pattern.compile(
    "^(-){1,2}[Hh]([Ee][Ll][Pp])?$"
  );

  /**
   * The main method is the entry point to this application.
   *
   * @param arguments Command-line arguments to be passed on execution.
   */
  public static void main(String[] arguments) {
    System.exit(new QEdit(arguments).run());
  }

  /** Program modal. */
  private final Modal modal = new Modal(Information.NAME);
  /** Program arguments. */
  private final Deque<String> arguments;
  /** Actions. */
  private final List<CueSheetAction> actions = new LinkedList<>();
  /** Working cuesheet. */
  private CueSheet sheet;
  /** Operation mode. */
  private Mode mode = Mode.PARSE;
  /** Whether there it has an output file to write to. */
  private boolean hasOutput = false;

  /** Makes an instance with the given arguments. */
  private QEdit(String[] args) {
    this.arguments = new LinkedList<>(Arrays.asList(args));
  }

  /** Runs itself. */
  private int run() {
    int out = NUL_INT;
    do {
      switch (this.mode) {
        case PARSE:
          out = parse();
          this.mode = Mode.READ;
          break;
        case READ:
          out = read();
          this.mode = Mode.ACT;
          break;
        case ACT:
          out = act();
          this.mode = Mode.DONE;
          break;
        case DONE:
          return out == hashCode() ? EXIT_SUCCESS : out;
      }
      if (out != EXIT_SUCCESS) {
        this.mode = Mode.DONE;
      }
    } while (true);
  }

  /** Prints its help message. */
  private int help() {
    STDOUT.println(
      Information.getHeader() + EOL + Help.USAGE + EOL + Help.EXPLANATION
    );
    return hashCode();
  }

  /** Prints the stack trace of the given exception. */
  private void printException(Exception exception) {
    printException(null, exception);
  }

  /**
   * Prints the stack trace of the given exception headered by the given header.
   */
  private void printException(String header, Exception exception) {
    if (exception == null) {
      return;
    }
    if (!Strings.isNullOrEmpty(header)) {
      this.modal.print(header + ":" + EOL + "  ", Modal.ERROR);
    }
    if (exception instanceof EDENRuntimeException) {
      this.modal.println(exception.getMessage(), Modal.ERROR);
      this.modal.println(((EDENRuntimeException) exception).getRemedy());
    } else if (exception instanceof EDENException) {
      this.modal.println(exception.getMessage(), Modal.ERROR);
      this.modal.println(((EDENException) exception).getRemedy());
    } else {
      this.modal.println(exception.toString(), Modal.ERROR);
    }
    if (DEBUG) {
      exception.printStackTrace(this.modal.getPrintStream());
    }
  }

  private int parse() {
    if (this.arguments.isEmpty()) {
      STDOUT.println(
        Information.getHeader() +
        EOL +
        Help.USAGE +
        EOL +
        "`--help` for more information."
      );
      return hashCode();
    }
    CueSheetAction action;
    String argument = null;
    String option = null;
    try {
      while (this.arguments.size() > 1) {
        option = this.arguments.removeFirst();
        if (option.equalsIgnoreCase(Check.KEY)) {
          action = new Check();
        } else if (option.equalsIgnoreCase(IndexToPregap.KEY)) {
          action = new IndexToPregap();
        } else if (option.equalsIgnoreCase(NoCDText.KEY)) {
          action = new NoCDText();
        } else if (option.equalsIgnoreCase(NoCatalog.KEY)) {
          action = new NoCatalog();
        } else if (option.equalsIgnoreCase(NoISRC.KEY)) {
          action = new NoISRC();
        } else if (option.equalsIgnoreCase(NoPerformer.KEY)) {
          action = new NoPerformer(CueSheetAction.Mode.ALL);
        } else if (option.equalsIgnoreCase(NoPerformer.KEY_SESSION)) {
          action = new NoPerformer(CueSheetAction.Mode.SESSION);
        } else if (option.equalsIgnoreCase(NoPerformer.KEY_TRACK)) {
          action = new NoPerformer(CueSheetAction.Mode.TRACK);
        } else if (option.equalsIgnoreCase(NoPostgap.KEY)) {
          action = new NoPostgap();
        } else if (option.equalsIgnoreCase(NoPregap.KEY)) {
          action = new NoPregap();
        } else if (option.equalsIgnoreCase(NoRem.KEY)) {
          action = new NoRem();
        } else if (option.equalsIgnoreCase(NoSongwriter.KEY)) {
          action = new NoSongwriter(CueSheetAction.Mode.ALL);
        } else if (option.equalsIgnoreCase(NoSongwriter.KEY_SESSION)) {
          action = new NoSongwriter(CueSheetAction.Mode.SESSION);
        } else if (option.equalsIgnoreCase(NoSongwriter.KEY_TRACK)) {
          action = new NoSongwriter(CueSheetAction.Mode.TRACK);
        } else if (option.equalsIgnoreCase(NoTitle.KEY)) {
          action = new NoTitle(CueSheetAction.Mode.ALL);
        } else if (option.equalsIgnoreCase(NoTitle.KEY_SESSION)) {
          action = new NoTitle(CueSheetAction.Mode.SESSION);
        } else if (option.equalsIgnoreCase(NoTitle.KEY_TRACK)) {
          action = new NoTitle(CueSheetAction.Mode.TRACK);
        } else if (option.equalsIgnoreCase(PregapToIndex.KEY)) {
          action = new PregapToIndex();
        } else if (option.equalsIgnoreCase(Print.KEY)) {
          action = new Print();
          this.hasOutput = true;
        } else if (option.equalsIgnoreCase(SetEOL.KEY)) {
          argument = this.arguments.removeFirst();
          action = new SetEOL(LineEnding.parseName(argument));
        } else if (option.equalsIgnoreCase(SetPerformer.KEY)) {
          argument = this.arguments.removeFirst();
          action = new SetPerformer(CueSheetAction.Mode.ALL, argument);
        } else if (option.equalsIgnoreCase(SetPerformer.KEY_SESSION)) {
          argument = this.arguments.removeFirst();
          action = new SetPerformer(CueSheetAction.Mode.SESSION, argument);
        } else if (option.equalsIgnoreCase(SetPerformer.KEY_TRACK)) {
          argument = this.arguments.removeFirst();
          action = new SetPerformer(CueSheetAction.Mode.TRACK, argument);
        } else if (option.equalsIgnoreCase(SetPostgap.KEY)) {
          argument = this.arguments.removeFirst();
          action = new SetPostgap(Integer.parseInt(argument));
        } else if (option.equalsIgnoreCase(SetPregap.KEY)) {
          argument = this.arguments.removeFirst();
          action = new SetPregap(Integer.parseInt(argument));
        } else if (option.equalsIgnoreCase(SetSongwriter.KEY)) {
          argument = this.arguments.removeFirst();
          action = new SetSongwriter(CueSheetAction.Mode.ALL, argument);
        } else if (option.equalsIgnoreCase(SetSongwriter.KEY_SESSION)) {
          argument = this.arguments.removeFirst();
          action = new SetSongwriter(CueSheetAction.Mode.SESSION, argument);
        } else if (option.equalsIgnoreCase(SetSongwriter.KEY_TRACK)) {
          argument = this.arguments.removeFirst();
          action = new SetSongwriter(CueSheetAction.Mode.TRACK, argument);
        } else if (option.equalsIgnoreCase(SetTitle.KEY)) {
          argument = this.arguments.removeFirst();
          action = new SetTitle(CueSheetAction.Mode.ALL, argument);
        } else if (option.equalsIgnoreCase(SetTitle.KEY_SESSION)) {
          argument = this.arguments.removeFirst();
          action = new SetTitle(CueSheetAction.Mode.SESSION, argument);
        } else if (option.equalsIgnoreCase(SetTitle.KEY_TRACK)) {
          argument = this.arguments.removeFirst();
          action = new SetTitle(CueSheetAction.Mode.TRACK, argument);
        } else if (option.equalsIgnoreCase(ShiftTimes.KEY)) {
          argument = this.arguments.removeFirst();
          action = new ShiftTimes(Integer.parseInt(argument));
        } else if (option.equalsIgnoreCase(SwapAuthors.KEY)) {
          action = new SwapAuthors();
        } else if (option.equalsIgnoreCase(Write.KEY)) {
          argument = this.arguments.removeFirst();
          action = new Write(Paths.get(argument));
          this.hasOutput = true;
        } else if (HELP.matcher(option).matches()) {
          return help();
        } else if (option.equalsIgnoreCase("--")) {
          break;
        } else {
          this.modal.println("Invalid action: " + option, Modal.ERROR);
          return EXIT_FAILURE;
        }
        this.actions.add(action);
      }
      return HELP.matcher(this.arguments.getFirst()).matches()
        ? help()
        : EXIT_SUCCESS;
    } catch (NoSuchElementException exception) {
      this.modal.println(
          "Insufficient arguments for `" + option + "`.",
          Modal.ERROR
        );
      if (DEBUG) {
        printException(exception);
      }
    } catch (IllegalArgumentException | NullPointerException exception) {
      this.modal.println(
          "Invalid `" + option + "` argument `" + argument + "`.",
          Modal.ERROR
        );
      if (DEBUG) {
        printException(exception);
      }
    }
    return EXIT_FAILURE;
  }

  private int read() {
    if (this.arguments.isEmpty()) {
      this.modal.println("No input file.", Modal.ERROR);
      return EXIT_FAILURE;
    }
    if (!this.hasOutput) {
      this.modal.println("No output file.", Modal.ERROR);
      return EXIT_FAILURE;
    }
    String path = this.arguments.getLast();
    try {
      this.sheet = CueSheets.parse(new File(path));
      return EXIT_SUCCESS;
    } catch (AccessDeniedException exception) {
      this.modal.println(path + ": Access denied.", Modal.ERROR);
    } catch (NoSuchFileException exception) {
      this.modal.println(path + ": Not found.", Modal.ERROR);
    } catch (Exception exception) {
      printException("The cuesheet parser threw", exception);
    }
    return EXIT_FAILURE;
  }

  private int act() {
    for (CueSheetAction action : this.actions) try {
      if (!action.run(this.sheet)) {
        this.modal.println("`" + action.toString() + "` failed.", Modal.ERROR);
        return EXIT_FAILURE;
      }
    } catch (Exception exception) {
      printException("`" + action.toString() + "` threw", exception);
      return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
  }

  /** Operation modes. */
  private enum Mode {
    PARSE,
    READ,
    ACT,
    DONE,
  }
}
