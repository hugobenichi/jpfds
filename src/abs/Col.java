package jpfds.abs;

public interface Col<X, C extends Col<X,C>> {
  boolean isEmpty();
  default boolean nonEmpty() { return !isEmpty(); }
  Seq<X> seq();
  C cons(X elem);
  C empty();
  C union(C col);
}
