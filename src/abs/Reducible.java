package jpfds.abs;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface Reducible<X> {

    <Y> Y reduce(Y seed, BiFunction<Y, ? super X,Y> f);

    default <C extends Col<X,C>> C into(C col) {
      return reduce(col, col.reducer());
    }

    default <B extends Builder<X,B,C>,C extends Col<X,C>> C into(B builder) {
      return reduce(builder, builder.reducer()).make();
    }

    default <C> C into(ColBuilder<X,C> builder) {
      BiFunction<ColBuilder<X,C>,X,ColBuilder<X,C>> reducer =
      new BiFunction<ColBuilder<X,C>,X,ColBuilder<X,C>>() {
        public ColBuilder<X,C> apply(ColBuilder<X,C> bld, X elem) {
          return bld.add(elem);
        }
      };
      return this.reduce(builder, reducer).make();
    }

    default <Y> Reducible<Y> map(final Function<? super X,Y> f) {
      final Reducible<X> source = this;
      return new Reducible<Y>() {
        public <Z> Z reduce(Z seed, BiFunction<Z, ? super Y,Z> g) {
          final BiFunction<Z,X,Z> gof = new BiFunction<Z,X,Z>() {
            public Z apply(Z z, X x) { return g.apply(z, f.apply(x)); }
          };
          return source.reduce(seed, gof);
        }
      };
    }

    //default Reducible<X> filter(Predicate<? super X> f);

    //default <Y> Reducible<Y> flatMap(Function<? super X,Reducible<? extends Y>> f);

    // skip(int n) //lazy, ignore n first elems
    // take(int n) //lazy, only considr n first elems
    // drop(int n) //lazy, 
    // until(Predicate) //lazy, keep elements until the Predicate holds true
    // from(Predicate) //lazy, skip elements until the Predicate holds false

}
