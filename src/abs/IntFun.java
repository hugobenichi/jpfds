package jpfds.abs;

import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public interface IntFun<Y> extends IntSet, IntFunction<Y> {

  Y get(int index);

  default Y apply(int index) { return get(index); }

  default Optional<Y> getOp(int index) {
    if ( has(index) )
      return Optional.ofNullable(get(index));
    else
      return Optional.empty();
  }

  default Y getOr(int index, Supplier<Y> prod) {
    if ( has(index) ) return get(index); else return prod.get();
  }

}
