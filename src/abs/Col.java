package jpfds.abs;

import java.util.Iterator;
import java.util.function.BiFunction;

public interface Col<X,C extends Col<X,C>> extends Iterable<X> {

  boolean isEmpty();
  default boolean nonEmpty() { return !isEmpty(); }

  Seq<X> seq();
  default Iterator<X> iterator() { return this.seq().iterator(); }

  C empty();
  C union(C col);

  C cons(X elem); // rename to add ?
  default C consAll(Iterable<X> elems) {
    Col<X,C> col = this;
    for (X elem : elems) { col = col.cons(elem); }
    return (C) col;
  }

  default BiFunction<C,X,C> reducer() { return (C c, X e) -> c.cons(e); }

}
