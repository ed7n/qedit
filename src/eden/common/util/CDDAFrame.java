package eden.common.util;

import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * Consists of utility methods and constants for operating on CDDA (Compact Disc
 * Digital Audio) frames. One second is equal to 75 CDDA frames.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public final class CDDAFrame {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Number of frames per second. */
  public static final int FPS = 75;

  /** Number of frames per minute. */
  public static final int FPM = FPS * 60;

  /** Seconds per frame(s). */
  private static final float[] SPF = new float[FPS];

  static {
    for (int i = 0; i < FPS; i++)
      SPF[i] = (float) i / FPS;
  }

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** To prevent instantiations of this class. */
  private CDDAFrame() {
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Formats the given time code in MM:SS:FF format to a frame number. M:SS:FF
   * format is considered invalid unless it has a leading zero.
   *
   * @throws IllegalArgumentException if the given time code is invalid.
   */
  public static int parse(String timeCode) {
    Objects.requireNonNull(timeCode, "timeCode");
    if (timeCode.length() < 8)
      throw new IllegalArgumentException("timeCode.length < 8");
    try {
      return parseInt(timeCode.substring(6, timeCode.length()))
          + (FPS * parseInt(timeCode.substring(3, 5)))
          + (FPM * parseInt(timeCode.substring(0, 2)));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Formats the given frame number to a time code in MM:SS:FF format.
   *
   * @throws IllegalArgumentException if the given frame number is negative.
   */
  public static String toTimeCode(int frame) {
    if (frame < 0)
      throw new IllegalArgumentException("frame < 0");
    int[] fields = new int[3];
    fields[0] = Math.floorDiv(frame, FPM);
    frame -= fields[0] * FPM;
    fields[1] = Math.floorDiv(frame, FPS);
    frame -= fields[1] * FPS;
    fields[2] = frame;
    StringBuilder out = new StringBuilder(9);
    for (int i = 0;;) {
      out.append(stringifyInt2Digits(fields[i]));
      if (++i >= 3)
        break;
      out.append(':');
    }
    return out.toString();
  }

  /**
   * Converts the given frame number to time in seconds.
   *
   * @throws IllegalArgumentException if the given frame number is negative.
   */
  public static float toSecond(int frame) {
    if (frame < 0)
      throw new IllegalArgumentException("frame < 0");
    return Math.floorDiv(frame, FPS) + SPF[frame % FPS];
  }

  /** Formats the given number to at least a two-digit string. */
  public static String stringifyInt2Digits(int number) {
    if (number == 0)
      return "00";
    if (number > 0)
      return number >= 10 ? Integer.toString(number) : "0" + number;
    return number <= -10 ? Integer.toString(number) : "-0" + -number;
  }
}
