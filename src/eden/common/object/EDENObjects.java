package eden.common.object;

/**
 * Consists of utility methods for operating on objects defined in this API.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public final class EDENObjects {

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** To prevent instantiations of this class. */
  private EDENObjects() {
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Ensures that the given {@code Dieable} is not dead.
   *
   * @see Dieable#requireNonDead()
   */
  public static Dieable requireNonDead(Dieable d) {
    return d.requireNonDead();
  }

  /**
   * Ensures that the given {@code Nullifiable} is not nullified.
   *
   * @see Nullifiable#requireNonNullified()
   */
  public static Nullifiable requireNonNullified(Nullifiable n) {
    return n.requireNonNullified();
  }
}
