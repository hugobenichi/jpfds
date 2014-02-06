package jpfds;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface Reducible<X> {

    <Y> Y reduce(Y seed, BiFunction<Y, ? super X,Y> f);

    default <C extends Col<X,C>> C into(C col) {
      return this.reduce(col, Col::addTo);
    }

    default <B extends Builder<X,B,C>,C extends Col<X,C>> C into(B builder) {
      return this.reduce(builder, Col::addTo).make();
    }

    default <Y> Reducible<Y> map(final Function<? super X,Y> f) {
      final Reducible<X> source = this;
      return new Reducible<Y>() {
        public <Z> Z reduce(Z seed, BiFunction<Z, ? super Y,Z> g) {
          return source.reduce(seed, (col,e) -> g.apply(col, f.apply(e)));
        }
      };
    }

    default <Y> Reducible<Y> flatMap(final Function<? super X,Reducible<Y>> f) {
      final Reducible<X> source = this;
      return new Reducible<Y>() {
        public <Z> Z reduce(Z seed, BiFunction<Z, ? super Y,Z> g) {
          return source.reduce(seed, (col,e) -> f.apply(e).reduce(col, g));
        }
      };
    }

    default Reducible<X> filter(Predicate<? super X> f) {
      final Reducible<X> source = this;
      return new Reducible<X>() {
        public <Y> Y reduce(Y seed, BiFunction<Y, ? super X,Y> g) {
          return source.reduce(seed,
            (col,e) -> {
              if (f.test(e)) return g.apply(col,e); else return col;
            }
          );
        }
      };
    }

    // skip(int n) //lazy, ignore n first elems
    // take(int n) //lazy, only considr n first elems
    // drop(int n) //lazy,
    // until(Predicate) //lazy, keep elements until the Predicate holds true
    // from(Predicate) //lazy, skip elements until the Predicate holds false

}
