package jpfds.abs;

// ColBuilder is implemented as ColBuilder<X,Seq<X>>
public interface ColBuilder<X,C> {
    ColBuilder<X,C> cons(X elem);
    ColBuilder<X,C> add(X elem);
    default ColBuilder<X,C> addAll(Iterable<? extends X> elems) {
      ColBuilder<X,C> bld = this;
      for (X elem : elems) { bld = bld.add(elem); }
      return bld;
    }
    ColBuilder<X,C> concat(ColBuilder<X,C> that);
    C make();
}
