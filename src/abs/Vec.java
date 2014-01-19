package jpfds.abs;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

// add standard java collection implementation
// add stream implem ?
// add ordered index interface
// implemnent Seq via slices for tail
public interface Vec<X>
    extends Container, Countable, Iterable<X>, IntFunction<X>, IntPredicate {


  /* base methods of the Vec api */

  boolean has(int index);
  X get(int index);

  default Optional<X> getOp(int i) {
    if ( has(i) )
      return Optional.ofNullable(get(i));
    else
      return Optional.empty();
  }

  default X getOr(int i, Supplier<X> prod) {
    if ( has(i) ) return get(i); else return prod.get();
  }


  /* Vec reprensentation as functions */

  /* as an IntFunction */
  // TODO: put default implementation into the int -> X mapping api
  default X apply(int value) { return get(value); }

  /* as an IntPredicate */
  // TODO: put default implementation into the int set api (that int -> X implements)
  default boolean test(int value) { return has(value); }


  /* Java standard utilities */

  default Iterator<X> iterator() {
    final Vec<X> vec = this;
    return new Iterator<X>() {
      int left = vec.size();
      int index = 0;
      public void remove() { throw removeException; }
      public boolean hasNext() { return left != 0; }
      public X next() {
        if ( left == 0 ) throw depletedIteratorException;
        while ( !vec.has(index) ) { index++; }
        left--;
        return vec.get(index);
      }
    };
  }

  default Stream<X> stream() {
    // to implement properly, use StreamSupport.stream(spliterator, false);
    // where the spliterator arg implements  java.util.Spliterator
    /*
    return StreamSupport.stream(
      new Spliterator<E>() {

      },
      false);
    */
    return null;
  }

  final RuntimeException removeException =
    new UnsupportedOperationException(
      "Iterators over immutable Vec objects do not support #remove().");

  final RuntimeException depletedIteratorException =
    new NoSuchElementException(
      "Iterator over immutable Vec has no more element left.");

}
