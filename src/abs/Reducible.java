package jpfds.abs;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface Reducible<X> {
    <Y> Y reduce(Y seed, BiFunction<Y, ? extends X,Y> f);

    //<C extends Col<X>> C into(Col<? extends X,C> receiver);

    //<C extends Col<X>> C into(ColBuilder<X,C> builder);

    //<C> C into(Col<X,C> receiver); // this function is broken

    <C> C into(ColBuilder<X,C> builder);

    <Y> Reducible<Y> map(Function<? super X,Y> f);

    Reducible<X> filter(Predicate<? super X> f);

    <Y> Reducible<Y> flatMap(Function<? super X,Reducible<? extends Y>> f);
    // skip(int n) //lazy, ignore n first elems
    // take(int n) //lazy, only considr n first elems
    // drop(int n) //lazy, 
    // until(Predicate) //lazy, keep elements until the Predicate holds true
    // from(Predicate) //lazy, skip elements until the Predicate holds false
}
