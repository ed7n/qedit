package eden.common.model.text;

import eden.common.object.Nullifiable;

import java.io.File;
import java.nio.charset.Charset;

public class TextFile implements Nullifiable {

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private File file;
  private Charset charset;
  private LineEnding lineEnding;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  public TextFile(File file) {
    this(file, null, null);
  }

  public TextFile(Charset charset, LineEnding lineEnding) {
    this(null, charset, lineEnding);
  }

  public TextFile(File file, Charset charset, LineEnding lineEnding) {
    this.file = file;
    this.charset = charset;
    this.lineEnding = lineEnding;
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  public File getFile() {
    return this.file;
  }

  public Charset getCharset() {
    return this.charset;
  }

  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  public LineEnding getLineEnding() {
    return this.lineEnding;
  }

  public void setLineEnding(LineEnding lineEnding) {
    this.lineEnding = lineEnding;
  }

  public void setTextFile(File file) {
    this.file = file;
  }

  public boolean hasCharset() {
    return getCharset() != null;
  }

  public boolean hasFile() {
    return getFile() != null;
  }

  public boolean hasLineEnding() {
    return getLineEnding() != null;
  }

  /** @inheritDoc */
  @Override
  public void nullifyObject() {
    this.file = null;
    this.charset = null;
    this.lineEnding = null;
  }

  /** @inheritDoc */
  @Override
  public boolean isObjectNullified() {
    return !hasFile() && !hasCharset() && !hasLineEnding();
  }

  /** Returns the absolute pathname of this {@code TextFile}. */
  @Override
  public String toString() {
    return hasFile() ? getFile().getAbsolutePath() : "";
  }
}
