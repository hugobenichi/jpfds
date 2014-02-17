package jpfds.experimental;

import java.util.function.IntPredicate;

public interface IntSet extends IntPredicate {
  boolean has(int value);
  default boolean test(int value) { return has(value); }
}
