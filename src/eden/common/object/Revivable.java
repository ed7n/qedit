package eden.common.object;

/**
 * A {@code Revivable} can be revived from abnormalities.
 * <p>
 * Classes implementing this interface must be able to modify their object
 * fields to correct from abnormalities.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public interface Revivable {

//~~INTERFACE METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Corrects this {@code Revivable} from abnormalities and returns its success.
   */
  boolean reviveObject();

  /** Returns the number of successful revives. */
  int getObjectReviveCount();

  /** Returns whether it is possible to revive. */
  boolean isObjectRevivable();
}
