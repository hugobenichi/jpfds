package jpfds.abs;

import java.util.Iterator;

public interface Col<X, C extends Col<X,C>> extends Iterable<X> {
  boolean isEmpty();
  default boolean nonEmpty() { return !isEmpty(); }
  Seq<X> seq();
  C cons(X elem);
  C empty();
  C union(C col); // move somewhere else ? (Mergeable)
  default Iterator<X> iterator() { return this.seq().iterator(); }
}
