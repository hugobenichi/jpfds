/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds;

import java.util.Iterator;
import java.util.function.BiFunction;

/** A collection abstraction common to all concrete collection types. The Col
 *  interface represents abstract collection types with an underlying monoid
 *  or group algebraic structure.
 *  @param <X> the type of the elements this collection can hold.
 *  @param <C> the concrete type of the collection. */
public interface Col<X,C extends Col<X,C>> extends Iterable<X>, Reducible<X> {

  /** Test for emptiness.
   *  @return true if the collection is empty, false otherwise. */
  boolean isEmpty();

  /** Test for non-emptiness.
   *  @return true if the collection is not empty, false otherwise. */
  default boolean nonEmpty() { return !this.isEmpty(); }

  /** Queries information about the size of this collection. If the collection
   *  allows it, the Size value will be an instance of Size.Bounded. Collections
   *  that know how to compute their own size should directly implement this
   *  interface and use themselves as return values for sizeInfo().
   *  @return knowledge of the size the size of this collection. */
  Size sizeInfo();

  /** A persistent linear view of this collection. Implementation can be lazy.
   *  @return a sequence traversing the collection in the most canonical way. */
  Seq<X> seq();

  /** A mutable one-use only iterator over this collection.
   *  @return an iterator traversing the collection in the most canonical way.*/
  default Iterator<X> iterator() { return this.seq().iterator(); }

  /** The empty collection of the same concrete type. This empty collection
   *  represents the neutral element of the underlying monoid structure of the
   *  concrete collection type C. Therefore for any collection instance c, the
   *  following relation must hold: c = c.union(c.empty()) = c.empty().union(c).
   *  @return an empty collection of the same type. */
  C empty();

  /** Merge the given collection into this collection. It is not required that
   *  this operation be commutative. For example, unions of linear collections
   *  such as Seqs could be concatenation operations.
   *  @param col another collection of the same type. Cannot be null.
   *  @return a collection that represents the union of both collections in the
   *  most canonical way for the type of given collections. */
  C union(C col);

  /** Add an element to this collection. Depending on the concrete collection
   *  type, this operation may be a no op (example: Set). The add operation
   *  must be equivalent to merging a collection instance with a singleton
   *  instance containing the input argument. In other word, for any collection
   *  instance c and any element x, the following relation must hold:
   *  c.add(x) = c.union(c.empty().add(x)).
   *  @param elem a element reference. Can be null.
   *  @return a new collection that contains all the elements of this collection
   *  and the additional element. */
  C add(X elem);

  /** Add all elements in the given Iterable to this collection. Depending on
   *  the concrete collection type, this operation may be a no op for some of
   *  the given elements (example: Set).
   *  @param elems an Iterable collection of elements. Cannot be null. Can
   *  contain null references.
   *  @return a new collection that contains all the elements of both this
   *  collection and of the given Iterable collection. */
  default C addAll(Iterable<X> elems) {
    @SuppressWarnings("unchecked") C col = (C) this;
    for (X elem : elems) { col = col.add(elem); }
    return col;
  }

  /** Default implementation of the Reducible interface. Concrete collection
   *  types should override this default with specialized implementations.
   *  @param seed the initial value of the accumulator.
   *  @param f a reducing function that updates the accumulator given an
   *  element of this collection.
   *  @return the result of applying the reducing function onto the initial seed
   *  value for all elements contained in this collection. */
  default <Y> Y reduce(Y seed, BiFunction<Y, ? super X,Y> f) {
    for (X elem : this) { seed = f.apply(seed, elem); }
    return seed;
  }

  /** Add an element to a collection. Convenience function for giving as
   *  an argument to higher order functions such as Reducible#reduce().
   *  @param col a collection. Cannot be null.
   *  @param elem an element. Can be null.
   *  @param <X> type of the elements of the collection.
   *  @param <C> concrete type of the collection.
   *  @return the result of adding the given element to the given collection. */
  static <X,C extends Col<X,C>> C addTo(C col, X elem) { return col.add(elem); }

}
