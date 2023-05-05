package eden.qedit.action;

import eden.common.model.cd.CueSheet;
import eden.common.util.CueSheets;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes to the given path.
 *
 * @author Brendon
 */
public class Write implements CueSheetAction {

  /** Key. */
  public static final String KEY = "write";
  /** Output path. */
  private final Path path;

  /** Makes an instance with the given path. */
  public Write(Path path) {
    this.path = path;
  }

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) throws IOException {
    try (Writer writer = Files.newBufferedWriter(this.path)) {
      CueSheets.write(sheet, writer);
    }
    return true;
  }
}
