package eden.qedit.action;

import static eden.common.shared.Constants.SPACE;
import static eden.common.shared.Constants.STDOUT;

import eden.common.excep.EDENException;
import eden.common.excep.EDENRuntimeException;
import eden.common.excep.io.FileAbsentException;
import eden.common.excep.io.FileDirectoryException;
import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;
import eden.common.model.cd.Track;
import eden.common.util.CueSheets;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Checks for errors and prints them to the standard output.
 *
 * @author Brendon
 */
public class Check implements CueSheetAction {

  /** Key. */
  public static final String KEY = "check";

  /** {@inheritDoc} */
  @Override
  public boolean run(CueSheet sheet) {
    List<Exception> exceptions = new LinkedList<>();
    Index lastIndex = null;
    Path path;
    String directory = sheet.getFile().getFile().getParent(), file;
    exceptions.addAll(CueSheets.checkSyntax(sheet.getSession()));
    if (sheet.getSession().hasCdTextFile()) {
      file = sheet.getSession().getCdTextFile();
      path = Paths.get(directory + File.separator + file);
      if (Files.notExists(path)) {
        exceptions.add(new FileAbsentException(file));
      } else if (Files.isDirectory(path)) {
        exceptions.add(new FileDirectoryException(file));
      }
    }
    exceptions.addAll(CueSheets.checkSyntaxTrack(sheet.getSession()));
    for (Track track : sheet.getTracks()) {
      exceptions.addAll(CueSheets.checkSyntax(track, lastIndex));
      if (track.hasIndexes()) {
        for (Index index : track.getIndexes()) {
          if (index.hasFile()) {
            file = index.getFilePath();
            path = Paths.get(directory + File.separator + file);
            if (Files.notExists(path)) {
              exceptions.add(new FileAbsentException(file));
            } else if (Files.isDirectory(path)) {
              exceptions.add(new FileDirectoryException(file));
            }
          }
        }
        lastIndex = track.getLastIndex();
      }
    }
    exceptions.forEach(exception -> {
      STDOUT.println(exception.getMessage());
      if (exception instanceof EDENRuntimeException) {
        STDOUT.println(
          SPACE + SPACE + ((EDENRuntimeException) exception).getRemedy()
        );
      } else if (exception instanceof EDENException) {
        STDOUT.println(SPACE + SPACE + ((EDENException) exception).getRemedy());
      }
    });
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return KEY;
  }
}
