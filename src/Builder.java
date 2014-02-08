package jpfds;

public interface Builder<X,C extends Col<X,C>> {

  // mutate the state of the builder (should it return itself ?)
  // throws IllegalStateException if called after make()
  void add(X elem);

  default void addAll(Iterable<X> elems) {
    for (X elem : elems) { this.add(elem); }
  }

  default Builder<X,C> addThen(X elem) {
    this.add(elem);
    return this;
  }

  default Builder<X,C> addAllThen(Iterable<X> elems) {
    this.addAll(elems);
    return this;
  }

  // how to protect this from building twice ?
  C make();

  // check if a builder has finished building
  boolean isConsumed();

  static <X,C extends Col<X,C>> Builder<X,C> addTo(Builder<X,C> bld, X elem) {
    bld.add(elem);
    return bld;
  }

}
