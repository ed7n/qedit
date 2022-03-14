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

  private static final Pattern HELP
      = Pattern.compile("^(-){1,2}[Hh]([Ee][Ll][Pp])?$");

  /**
   * The main method is the entry point to this application.
   *
   * @param args Command-line arguments to be passed on execution.
   */
  public static void main(String[] args) {
    System.exit(new QEdit(args).run());
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

  /** Makes an instance with the given arguments. */
  private QEdit(String[] args) {
    this.arguments = new LinkedList<>(Arrays.asList(args));
  }

  /** Runs itself. */
  private int run() {
    int exitCode = NUL_INT;
    do
      switch (this.mode) {
        case PARSE:
          exitCode = parse();
          if (exitCode == hashCode())
            return EXIT_SUCCESS;
          this.mode = Mode.READ;
          break;
        case READ:
          exitCode = read();
          this.mode = Mode.ACT;
          break;
        case ACT:
          exitCode = act();
          this.mode = Mode.DONE;
          break;
        case DONE:
          return exitCode;
      }
    while (exitCode == EXIT_SUCCESS);
    return exitCode;
  }

  /** Prints its help message. */
  private int help() {
    STDOUT.println(
        Information.getHeader() + EOL + Help.USAGE + EOL + Help.EXPLANATION);
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
    if (!Strings.isNullOrEmpty(header))
      this.modal.print(header + ":\n  ", Modal.ERROR);
    if (exception instanceof EDENRuntimeException) {
      this.modal.println(exception.getMessage(), Modal.ERROR);
      this.modal.println(((EDENRuntimeException) exception).getRemedy());
    } else if (exception instanceof EDENException) {
      this.modal.println(exception.getMessage(), Modal.ERROR);
      this.modal.println(((EDENException) exception).getRemedy());
    } else
      this.modal.println(exception.toString(), Modal.ERROR);
    if (DEBUG)
      exception.printStackTrace(this.modal.getPrintStream());
  }

  private int parse() {
    if (this.arguments.isEmpty())
      return help();
    CueSheetAction action;
    String argument = null, option = null;
    try {
      while (this.arguments.size() > 1) {
        argument = this.arguments.removeFirst();
        if (argument.equalsIgnoreCase(Check.KEY))
          action = new Check();
        else if (argument.equalsIgnoreCase(IndexToPregap.KEY))
          action = new IndexToPregap();
        else if (argument.equalsIgnoreCase(NoCDText.KEY))
          action = new NoCDText();
        else if (argument.equalsIgnoreCase(NoCatalog.KEY))
          action = new NoCatalog();
        else if (argument.equalsIgnoreCase(NoISRC.KEY))
          action = new NoISRC();
        else if (argument.equalsIgnoreCase(NoPerformer.KEY))
          action = new NoPerformer(CueSheetAction.Mode.ALL);
        else if (argument.equalsIgnoreCase(NoPerformer.KEY_SESSION))
          action = new NoPerformer(CueSheetAction.Mode.SESSION);
        else if (argument.equalsIgnoreCase(NoPerformer.KEY_TRACK))
          action = new NoPerformer(CueSheetAction.Mode.TRACK);
        else if (argument.equalsIgnoreCase(NoPostgap.KEY))
          action = new NoPostgap();
        else if (argument.equalsIgnoreCase(NoPregap.KEY))
          action = new NoPregap();
        else if (argument.equalsIgnoreCase(NoRem.KEY))
          action = new NoRem();
        else if (argument.equalsIgnoreCase(NoSongwriter.KEY))
          action = new NoSongwriter(CueSheetAction.Mode.ALL);
        else if (argument.equalsIgnoreCase(NoSongwriter.KEY_SESSION))
          action = new NoSongwriter(CueSheetAction.Mode.SESSION);
        else if (argument.equalsIgnoreCase(NoSongwriter.KEY_TRACK))
          action = new NoSongwriter(CueSheetAction.Mode.TRACK);
        else if (argument.equalsIgnoreCase(NoTitle.KEY))
          action = new NoTitle(CueSheetAction.Mode.ALL);
        else if (argument.equalsIgnoreCase(NoTitle.KEY_SESSION))
          action = new NoTitle(CueSheetAction.Mode.SESSION);
        else if (argument.equalsIgnoreCase(NoTitle.KEY_TRACK))
          action = new NoTitle(CueSheetAction.Mode.TRACK);
        else if (argument.equalsIgnoreCase(PregapToIndex.KEY))
          action = new PregapToIndex();
        else if (argument.equalsIgnoreCase(Print.KEY))
          action = new Print();
        else if (argument.equalsIgnoreCase(SetEOL.KEY)) {
          option = this.arguments.removeFirst();
          action = new SetEOL(LineEnding.parseName(option));
        } else if (argument.equalsIgnoreCase(SetPerformer.KEY)) {
          option = this.arguments.removeFirst();
          action = new SetPerformer(CueSheetAction.Mode.ALL, option);
        } else if (argument.equalsIgnoreCase(SetPerformer.KEY_SESSION)) {
          option = this.arguments.removeFirst();
          action = new SetPerformer(CueSheetAction.Mode.SESSION, option);
        } else if (argument.equalsIgnoreCase(SetPerformer.KEY_TRACK)) {
          option = this.arguments.removeFirst();
          action = new SetPerformer(CueSheetAction.Mode.TRACK, option);
        } else if (argument.equalsIgnoreCase(SetPostgap.KEY)) {
          option = this.arguments.removeFirst();
          action = new SetPostgap(Integer.parseInt(option));
        } else if (argument.equalsIgnoreCase(SetPregap.KEY)) {
          option = this.arguments.removeFirst();
          action = new SetPregap(Integer.parseInt(option));
        } else if (argument.equalsIgnoreCase(SetSongwriter.KEY)) {
          option = this.arguments.removeFirst();
          action = new SetSongwriter(CueSheetAction.Mode.ALL, option);
        } else if (argument.equalsIgnoreCase(SetSongwriter.KEY_SESSION)) {
          option = this.arguments.removeFirst();
          action = new SetSongwriter(CueSheetAction.Mode.SESSION, option);
        } else if (argument.equalsIgnoreCase(SetSongwriter.KEY_TRACK)) {
          option = this.arguments.removeFirst();
          action = new SetSongwriter(CueSheetAction.Mode.TRACK, option);
        } else if (argument.equalsIgnoreCase(SetTitle.KEY)) {
          option = this.arguments.removeFirst();
          action = new SetTitle(CueSheetAction.Mode.ALL, option);
        } else if (argument.equalsIgnoreCase(SetTitle.KEY_SESSION)) {
          option = this.arguments.removeFirst();
          action = new SetTitle(CueSheetAction.Mode.SESSION, option);
        } else if (argument.equalsIgnoreCase(SetTitle.KEY_TRACK)) {
          option = this.arguments.removeFirst();
          action = new SetTitle(CueSheetAction.Mode.TRACK, option);
        } else if (argument.equalsIgnoreCase(ShiftTimes.KEY)) {
          option = this.arguments.removeFirst();
          action = new ShiftTimes(Integer.parseInt(option));
        } else if (argument.equalsIgnoreCase(SwapAuthors.KEY))
          action = new SwapAuthors();
        else if (argument.equalsIgnoreCase(Write.KEY)) {
          option = this.arguments.removeFirst();
          action = new Write(Paths.get(option));
        } else if (HELP.matcher(argument).matches())
          return help();
        else if (argument.equalsIgnoreCase("--"))
          break;
        else {
          this.modal.println("Invalid action: " + argument, Modal.ERROR);
          return EXIT_FAILURE;
        }
        this.actions.add(action);
      }
      if (this.arguments.isEmpty()) {
        this.modal.println("No input file.", Modal.ERROR);
        return EXIT_FAILURE;
      }
      return HELP.matcher(this.arguments.getFirst()).matches()
          ? help() : EXIT_SUCCESS;
    } catch (NoSuchElementException exception) {
      this.modal.println(
          "Insufficient options for `" + argument + "`.", Modal.ERROR);
      if (DEBUG)
        QEdit.this.printException(exception);
    } catch (IllegalArgumentException | NullPointerException exception) {
      this.modal.println(
          "Invalid option for `" + argument + "`: " + option, Modal.ERROR);
      if (DEBUG)
        QEdit.this.printException(exception);
    }
    return EXIT_FAILURE;
  }

  private int read() {
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
    for (CueSheetAction action : this.actions)
      try {
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
    PARSE, READ, ACT, DONE;
  }
}
