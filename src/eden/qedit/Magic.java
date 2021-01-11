package eden.qedit;

import eden.common.model.cd.CueSheet;
import eden.common.model.cd.Index;
import eden.common.model.cd.Track;
import eden.common.util.cuesheet.CueSheetFilter;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import static eden.common.shared.Constants.DOUBLE_QUOTE;
import static eden.common.shared.Constants.NUL_STRING;
import static eden.common.shared.Constants.SPACE;
import static eden.common.shared.Constants.STDIN;
import static eden.common.shared.Constants.STDOUT;
import static eden.common.util.CDDAFrame.stringifyInt2Digits;
import static eden.common.util.CDDAFrame.toSecond;
import static eden.common.util.CDDAFrame.toTimeCode;

/**
 * {@code CueSheetFilter}: Performs some magic.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Magic implements CueSheetFilter, Runnable {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private MediaPlayer player;
  private String string;
  private String prefix;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code CueSheetFilter} with the given arguments. */
  public Magic(String... args) {
    setParams(args);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** @inheritDoc */
  @Override
  public boolean filter(CueSheet sheet) throws InterruptedException {
    STDOUT.println("  wird geladen...");
    List<Track> tracks = sheet.getTracks();
    Track track = tracks.get(0);
    if (!track.hasIndexes() || !track.getIndex(0).hasFilePath())
      return false;
    Index index = track.getIndex(0);
    int i = 0;
    this.string = index.getFilePath();
    this.string = this.string.replaceAll(DOUBLE_QUOTE, NUL_STRING);
    this.string = new File(this.prefix + this.string).toURI().toString();
    Scanner in = new Scanner(STDIN);
    Platform.startup(this);
    STDOUT.println(
        "  — " + stringifyInt2Digits(tracks.size()) + "          — ");
    STDOUT.print("> ");
    while (true) {
      STDOUT.println(
          "｢ " + stringifyInt2Digits(track.getNumber())
          + SPACE + toTimeCode(index.getFrame()) + " ｣");
      this.string = in.nextLine();
      STDOUT.print("» ");
      i = (i + 1) % sheet.getTracks().size();
      track = tracks.get(i);
      if (!track.hasIndexes())
        return false;
      index = track.getIndex(0).getNumber() == 0
        ? track.getIndex(1) : track.getIndex(0);
      this.player.seek(Duration.valueOf(toSecond(index.getFrame()) + "s"));
    }
  }

  /** @inheritDoc */
  @Override
  public void run() {
    this.player = new MediaPlayer(new Media(this.string));
    this.player.play();
  }

  /** @inheritDoc */
  @Override
  public int setParams(String... args) {
    this.prefix = args[0];
    if (this.prefix.contains(File.separator))
      this.prefix = this.prefix.substring(
          0, this.prefix.lastIndexOf(File.separator) + 1);
    else
      this.prefix = "";
    return 0;
  }
}
