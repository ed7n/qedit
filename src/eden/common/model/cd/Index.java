package eden.common.model.cd;

import eden.common.util.CDDAFrame;
import java.util.LinkedList;
import java.util.List;

import static eden.common.util.CDDAFrame.stringifyInt2Digits;

/**
 * Defines a Compact Disc (CD) index.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class Index extends CDLayoutObject {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Maximum index number in a CD. */
  public static final int MAX_NUMBER = 99;

  /** Minimum index number in a CD. */
  public static final int MIN_NUMBER = 0;

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** FILE path. */
  protected String filePath;

  /** FILE type. */
  protected String fileType;

  /** Number arguments. */
  protected int frame, number;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes an {@code Index} with the given index and frame numbers. */
  public Index(int number, int frame) {
    this(number, frame, null, null);
  }

  /**
   * Makes an {@code Index} with the given index number, frame number, file
   * path, and file type.
   */
  public Index(int number, int frame, String filePath, String fileType) {
    this.filePath = filePath;
    this.fileType = fileType;
    this.frame = frame;
    this.number = number;
  }

  /** To prevent uninitialized instantiations of this class. */
  protected Index() {
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns whether the given frame number is valid. */
  public static boolean isValidFrame(int frame) {
    return frame >= 0;
  }

  /** Returns whether the given index number is valid. */
  public static boolean isValidIndexNumber(int number) {
    return number >= MIN_NUMBER && number <= MAX_NUMBER;
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns the FILE path. */
  public String getFilePath() {
    return this.filePath;
  }

  /** Sets the FILE path. */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /** Returns the FILE type. */
  public String getFileType() {
    return this.fileType;
  }

  /** Sets the FILE type. */
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  /** Returns the frame number. */
  public int getFrame() {
    return this.frame;
  }

  /** Sets the frame number. */
  public void setFrame(int frame) {
    this.frame = frame;
  }

  /** Returns the index number. */
  public int getNumber() {
    return this.number;
  }

  /** Sets the index number. */
  public void setNumber(int number) {
    this.number = number;
  }

  /** Returns whether the FILE path is set. */
  public boolean hasFilePath() {
    return getFilePath() != null;
  }

  /** Returns whether the FILE type is set. */
  public boolean hasFileType() {
    return getFilePath() != null;
  }

  /** @inheritDoc */
  @Override
  public List<CueSheetStatement> toStatements() {
    List<CueSheetStatement> out = new LinkedList<>();
    out.add(new CueSheetStatement(
        "    INDEX",
        stringifyInt2Digits(getNumber()),
        CDDAFrame.toTimeCode(getFrame())));
    return out;
  }

  /** @inheritDoc */
  @Override
  public void nullifyObject() {
    getRems().clear();
    this.remarks = null;
    this.filePath = null;
    this.frame = 0;
    this.number = 0;
  }

  /** @inheritDoc */
  @Override
  public boolean isObjectNullified() {
    return getRems() == null;
  }
}
