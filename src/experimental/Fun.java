package jpfds.experimental;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Fun<X,Y> extends Set<X>, Function<X,Y> {

  Y get(X key);

  default Y apply(X key) { return get(key); }

  default Optional<Y> getOp(X key) {
    if ( has(key) )
      return Optional.ofNullable(get(key));
    else
      return Optional.empty();
  }

  default Y getOr(X key, Supplier<Y> prod) {
    if ( has(key) ) return get(key); else return prod.get();
  }

}
