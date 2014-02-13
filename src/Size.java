/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds;

/** Represents what is known about the size of a collection. The size can be
 *  either unknown or infinite, or known to be computable for collections which
 *  are known to be bounded. Computing the size can be either a constant time
 *  operation, or takes time proportional to the size of the collection. */
public interface Size {

  /** Singleton representing no information on size. */
  Size unknown = new Size() { };

  /** Singleton representing infinite size of a collection. */
  Size infinite = new Size() { };

  /** Represents the possibility of computing the size of a collection in
   *  bounded time. */
  public interface Bounded extends Size { int size(); }

  /** Represents the possibility of computing the size of a collection in time
   *  proportional to its size. */
  public interface Linear extends Bounded { }

  /** Represents the possibility of getting the size of a collection in constant
   *  time. */
  public interface Constant extends Bounded { }

  /** Singleton representing the size of an empty collection. */
  Size empty = new Constant() { public int size() { return 0; } };

}
