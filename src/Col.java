package jpfds;

import java.util.Iterator;
import java.util.function.BiFunction;

public interface Col<X,C extends Col<X,C>> extends Iterable<X>, Reducible<X> {

  boolean isEmpty();
  default boolean nonEmpty() { return !this.isEmpty(); }

  Seq<X> seq();
  default Iterator<X> iterator() { return this.seq().iterator(); }

  C empty();
  C union(C col);

  C add(X elem);
  default C addAll(Iterable<X> elems) {
    Col<X,C> col = this;
    for (X elem : elems) { col = col.add(elem); }
    return (C) col;
  }

  default BiFunction<C,X,C> reducer() { return (c,e) -> c.add(e); }

  default <Y> Y reduce(Y seed, BiFunction<Y, ? super X,Y> f) {
    for (X elem : this) { seed = f.apply(seed, elem); }
    return seed;
  }

}
