package jpfds.abs;

import java.util.Iterator;

public interface Col<X, C extends Col<X,C>> extends Iterable<X> {
  boolean isEmpty();
  default boolean nonEmpty() { return !isEmpty(); }
  Seq<X> seq();
  default Iterator<X> iterator() { return this.seq().iterator(); }
  C empty();
  C cons(X elem); // move somewhere else ? (Mergeable ? Expandable ?)
  C union(C col); // move somewhere else ? (Mergeable ? Expandable ?)
}
