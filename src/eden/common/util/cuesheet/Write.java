package eden.common.util.cuesheet;

import eden.common.model.cd.CueSheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * {@code CueSheetFilter}: Write to a given output path.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Write implements CueSheetFilter {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private Path path;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code CueSheetFilter} with the given arguments. */
  public Write(String... args) {
    setParams(args);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) throws IOException {
    CueSheets.write(sheet, Files.newBufferedWriter(this.path));
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
    try {
      this.path = Paths.get(args[0]);
    } catch (InvalidPathException e) {
      throw new IllegalArgumentException(args[0]);
    }
    return getParamCount();
  }
}
