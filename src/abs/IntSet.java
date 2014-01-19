package jpfds.abs;

import java.util.function.IntPredicate;

public interface IntSet extends IntPredicate {
  boolean has(int elem);
  default boolean test(int value) { return has(value); }
}
