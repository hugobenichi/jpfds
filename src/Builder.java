package jpfds;

/** A mutable object which knows how to assemble and produce an instance of a
 *  concrete Col type. Adding elements to a Builder object mutates its internal
 *  state. At any point of a Builder's life, make() can be called to publish an
 *  instance of the concrete collection type that this Builder is associated to.
 *  After publication, the Builder cannot be used again. It is not required
 *  that a concrete Builder implementation to be thread-safe.
 *  @param <C> the concrete collection type this Builder type can construct.
 *  @param <X> the type of elements this Builder type accepts. */
public interface Builder<X,C extends Col<X,C>> {

  /** Add an element to the underlying collection being built. This operation
   *  is illegal after publication and should throw an IllegalStateException.
   *  @param elem a element to add to the collection being built. Can be null.*/
  void add(X elem);

  /** Convenience for adding all elements from an Iterable to this builder.
   *  @param elems an Iterable collection of elements. Cannot be null. Can
   *  contain null references. */
  default void addAll(Iterable<X> elems) {
    for (X elem : elems) { this.add(elem); }
  }

  /** Convenience for chaining add() operations.
   *  @param elem an element to add to the collection being built.
   *  @return this builder instance. */
  default Builder<X,C> addThen(X elem) { this.add(elem); return this; }

  /** Convenience for chaining add() operations.
   *  @param elems an Iterable collection of elements. Cannot be null. Can
   *  contain null references.
   *  @return this builder instance. */
  default Builder<X,C> addAllThen(Iterable<X> elems) {
    this.addAll(elems);
    return this;
  }

  /** Publish the underlying collection being built. After publication, the
   *  builder cannot be used anymore and should throw unchecked IllegalState
   *  exceptions on any mutating operations.
   *  @return the underlying collection being built, in a valid state. */
  C make();

  /** Test if the underlying collection being built has been published.
   *  @return true if the underlying collection being built has been published,
   *  false otherwise. */
  boolean isConsumed();

  /** Add an element to a collection builder. Convenience function for giving
   *  as an argument to higher order functions such as Reducible#reduce().
   *  @param bld a collection builder. Cannot be null.
   *  @param elem an element. Can be null.
   *  @param <X> type of the elements of the collection being built.
   *  @param <C> concrete type of the collection being built.
   *  @return the same builder instance. */
  static <X,C extends Col<X,C>> Builder<X,C> addTo(Builder<X,C> bld, X elem) {
    bld.add(elem);
    return bld;
  }

}
