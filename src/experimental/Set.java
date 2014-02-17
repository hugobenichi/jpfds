package jpfds.experimental;

import java.util.function.Predicate;

public interface Set<X> extends Predicate<X> {
  boolean has(X elem);
  default boolean test(X elem) { return has(elem); }
}
