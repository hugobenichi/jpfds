package jpfds;

import java.util.function.*;

// test: Reducible implementation with monoid typeclass
//  1) code looks nice but type safety is broken because the adapted monoids
//  can take in collections built with the old monoid
//  2) basing Reducer on into() requires to adapt the monoid for map, filter
//  and flatmap. Preferable to touch the Reducer and base on reduce.
//
//  Possible solution: rework Col and ColBuilder to provide the Reducing
//    function that allow to easily write into() from reduce()
interface Monoid<X,M> {

  M empty();
  M union(M a, M b);
  M add(X elem, M col);

  default M addAll(Iterable<X> elems, M col) {
    for (X elem : elems) { col = add(elem, col); }
    return col;
  }

  // can this be put into the Col interface ?
  default <W> Monoid<W,M> adapt(Function<W,X> f) {
    Monoid<X,M> original = this;
    return new Monoid<W,M>() {
      public M empty() { return original.empty(); }
      public M union(M a, M b) { return original.union(a, b); }
      public M add(W elem, M col) { return original.add(f.apply(elem), col); }
    };
  }

  default <W> Monoid<W,M> expand(Function<W,Reducer<X>> f) {
    Monoid<X,M> original = this;
    return new Monoid<W,M>() {
      public M empty() { return original.empty(); }
      public M union(M a, M b) { return original.union(a, b); }
      public M add(W elem, M col) {
        return union(col, f.apply(elem).into(original));
      }
    };
  }

  default Monoid<X,M> filtered(Predicate<X> f) {
    Monoid<X,M> original = this;
    return new Monoid<X,M>() {
      public M empty() { return original.empty(); }
      public M union(M a, M b) { return original.union(a, b); }
      public M add(X elem, M col) {
        if (f.test(elem)) return original.add(elem, col); else return col;
      }
    };
  }

  interface Reducer<X> {

    <M> M into(Monoid<X,M> monoid);

    default <Y> Reducer<Y> map(Function<X,Y> f) {
      Reducer<X> source = this;
      return new Reducer<Y>() {
        public <M> M into(Monoid<Y,M> monoid) {
          return source.into(monoid.adapt(f));
        }
      };
    }

    default <Y> Reducer<Y> flatMap(Function<X,Reducer<Y>> f) {
      Reducer<X> source = this;
      return new Reducer<Y>() {
        public <M> M into(Monoid<Y,M> monoid) {
          return source.into(monoid.expand(f));
        }
      };
    }

    default Reducer<X> filter(Predicate<X> f) {
      Reducer<X> source = this;
      return new Reducer<X>() {
        public <M> M into(Monoid<X,M> monoid) {
          return source.into(monoid.filtered(f));
        }
      };
    }

  }



}
