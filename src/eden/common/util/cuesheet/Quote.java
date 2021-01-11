package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

import java.util.regex.Pattern;

import static eden.common.shared.Constants.DOUBLE_QUOTE;
import static eden.common.shared.Constants.NUL_STRING;
import static eden.common.shared.Constants.SPACE;

/**
 * {@code CueSheetFilter}: Enclose fields in quotation marks. "auto" removes
 * them as allowed.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Quote implements CueSheetFilter {

//~~OBJECT CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private final Pattern PATTERN = Pattern.compile(DOUBLE_QUOTE);

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private boolean force;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code CueSheetFilter} with the given arguments. */
  public Quote(String... args) {
    setParams(args);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) {
    sheet.getTracks().forEach(track -> {
      track.getIndexes().stream().filter(index -> index.hasFilePath()).
          forEach(index -> {
            String string = removeQuotes(index.getFilePath());
            index.setFilePath(mustQuote(string)
                ? DOUBLE_QUOTE + string + DOUBLE_QUOTE : string);
          });
      track.forEachStringStatement((command, argument) -> {
        if (argument != null) {
          String string = removeQuotes(argument);
          return mustQuote(string)
              ? DOUBLE_QUOTE + string + DOUBLE_QUOTE : string;
        }
        return argument;
      });
    });
    sheet.getSession().forEachStringStatement((command, argument) -> {
      if (argument != null) {
        String string = removeQuotes(argument);
        return mustQuote(string)
            ? DOUBLE_QUOTE + string + DOUBLE_QUOTE : string;
      }
      return argument;
    });
    return true;
  }

  /** @inheritDoc */
  @Override
  public int getParamCount() {
    return 1;
  }

  /** @inheritDoc */
  @Override
  public int setParams(String... args) {
    if (args.length < getParamCount())
      throw new IllegalArgumentException();
    if (!args[0].equalsIgnoreCase("auto")) {
      if (!args[0].equalsIgnoreCase("force"))
        throw new IllegalArgumentException(args[0]);
      this.force = true;
    } else
      this.force = false;
    return getParamCount();
  }

  private boolean mustQuote(String string) {
    return this.force || string.contains(SPACE);
  }

  private String removeQuotes(String string) {
    return PATTERN.matcher(string).replaceAll(NUL_STRING);
  }
}
