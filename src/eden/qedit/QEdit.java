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
import eden.qedit.action.ShiftTimes;
import eden.qedit.action.SwapAuthors;
import eden.qedit.action.Write;
import eden.qedit.model.application.Help;
import eden.qedit.model.application.Information;
import java.io.File;
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

  /** Whether to print stack traces of caught throwables. */
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

  /** Prints the stack trace of the given throwable. */
  private void printThrowable(Throwable throwable) {
    printThrowable(null, throwable);
  }

  /**
   * Prints the stack trace of the given throwable headered by the given header.
   */
  private void printThrowable(String header, Throwable throwable) {
    if (!Strings.isNullOrEmpty(header))
      this.modal.print(header + ":\n  ", Modal.ERROR);
    if (DEBUG)
      throwable.printStackTrace(this.modal.getPrintStream());
    else
      this.modal.println(throwable.toString(), Modal.ERROR);
    if (throwable instanceof EDENRuntimeException)
      this.modal.println(((EDENRuntimeException) throwable).getRemedy());
    else if (throwable instanceof EDENException)
      this.modal.println(((EDENException) throwable).getRemedy());
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
        else if (argument.equalsIgnoreCase(NoCatalog.KEY))
          action = new NoCatalog();
        else if (argument.equalsIgnoreCase(NoCDText.KEY))
          action = new NoCDText();
        else if (argument.equalsIgnoreCase(NoISRC.KEY))
          action = new NoISRC();
        else if (argument.equalsIgnoreCase(NoPerformer.KEY))
          action = new NoPerformer(CueSheetAction.Mode.ALL);
        else if (argument.equalsIgnoreCase(NoPostgap.KEY))
          action = new NoPostgap();
        else if (argument.equalsIgnoreCase(NoPregap.KEY))
          action = new NoPregap();
        else if (argument.equalsIgnoreCase(NoRem.KEY))
          action = new NoRem();
        else if (argument.equalsIgnoreCase(NoPerformer.KEY_SESSION))
          action = new NoPerformer(CueSheetAction.Mode.SESSION);
        else if (argument.equalsIgnoreCase(NoSongwriter.KEY_SESSION))
          action = new NoSongwriter(CueSheetAction.Mode.SESSION);
        else if (argument.equalsIgnoreCase(NoTitle.KEY_SESSION))
          action = new NoTitle(CueSheetAction.Mode.SESSION);
        else if (argument.equalsIgnoreCase(NoSongwriter.KEY))
          action = new NoSongwriter(CueSheetAction.Mode.ALL);
        else if (argument.equalsIgnoreCase(NoTitle.KEY))
          action = new NoTitle(CueSheetAction.Mode.ALL);
        else if (argument.equalsIgnoreCase(NoPerformer.KEY_TRACK))
          action = new NoPerformer(CueSheetAction.Mode.TRACK);
        else if (argument.equalsIgnoreCase(NoSongwriter.KEY_TRACK))
          action = new NoSongwriter(CueSheetAction.Mode.TRACK);
        else if (argument.equalsIgnoreCase(NoTitle.KEY_TRACK))
          action = new NoTitle(CueSheetAction.Mode.TRACK);
        else if (argument.equalsIgnoreCase(PregapToIndex.KEY))
          action = new PregapToIndex();
        else if (argument.equalsIgnoreCase(Print.KEY))
          action = new Print();
        else if (argument.equalsIgnoreCase(SetEOL.KEY)) {
          option = this.arguments.removeFirst();
          action = new SetEOL(LineEnding.parseName(option));
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
        printThrowable(exception);
    } catch (IllegalArgumentException | NullPointerException exception) {
      this.modal.println(
          "Invalid option for `" + argument + "`: " + option, Modal.ERROR);
      if (DEBUG)
        printThrowable(exception);
    }
    return EXIT_FAILURE;
  }

  private int read() {
    try {
      this.sheet = CueSheets.parse(new File(this.arguments.getLast()));
    } catch (Throwable throwable) {
      printThrowable("CueSheetParser threw", throwable);
      return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
  }

  private int act() {
    for (CueSheetAction action : this.actions)
      try {
      if (!action.run(this.sheet)) {
        this.modal.println(
            "`" + action.toString() + "` returned failure.", Modal.ERROR);
        return EXIT_FAILURE;
      }
    } catch (Throwable throwable) {
      printThrowable("`" + action.toString() + "` threw", throwable);
      return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
  }

  /** Operation modes. */
  private enum Mode {
    PARSE, READ, ACT, DONE;
  }
}
