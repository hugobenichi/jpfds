package jpfds.abs;

public interface ColBuilder<X,C> {
    C make();
    ColBuilder<X,C> cons(X elem);
    ColBuilder<X,C> add(X elem);
    default ColBuilder<X,C> addAll(Iterable<? extends X> elems) {
      ColBuilder<X,C> bld = this;
      for (X elem : elems) { bld = bld.add(elem); }
      return bld;
    }
    //does not work, need to try casting to own type or fallback to default
    // needs F-bounded polymorphism to be interesting
    // this function would be usefull for linear collections for rebuilding
    // segments efficiently (used by Folder / Merger for parallel cols)
    //ColBuilder<X,C> concat(ColBuilder<X,C> that);
}
