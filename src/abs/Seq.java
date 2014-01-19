package jpfds.abs;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

// add standard java collection implementation
// add stream implem ?
public interface Seq<X> extends Container, Iterable<X> {


  /* base methods of the Seq api */

  Seq<X> tail();
  X head();

  default Optional<X> headOpt() {
    if ( isEmpty() )
      return Optional.ofNullable(head());
    else
      return Optional.empty();
  }

  default X headOr(Supplier<X> prod) {
    if ( isEmpty() ) return head(); else return prod.get();
  }


  /* Java standard utilities */

  default Iterator<X> iterator() {
    final Seq<X> self = this;
    return new Iterator<X>() {
      Seq<X> seq = self;
      public void remove() { throw removeException; }
      public boolean hasNext() { return seq.nonEmpty(); }
      public X next() {
        X nextValue = seq.head();
        seq = seq.tail();
        return nextValue;
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
      "Iterators over immutable Seq objects do not support #remove().");

}
