package jpfds.abs;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

// add standard java collection implementation
// add stream implem ?
public interface Seq<X> extends Iterable<X> {

  Seq<X> tail();
  X head();

  boolean isEmpty();
  default boolean nonEmpty() { return !isEmpty(); }

  default Optional<X> headOpt() {
    if ( isEmpty() )
      return Optional.ofNullable(head());
    else
      return Optional.empty();
  }

  default X headOr(Supplier<X> prod) {
    if ( isEmpty() ) return head(); else return prod.get();
  }

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
      "Iterator over immutable Seq objects do not support #remove().");

}
