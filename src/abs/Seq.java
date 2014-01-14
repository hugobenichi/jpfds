package jpfds.abs;

import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// add standard java collection implementation
public interface Seq<E> extends Stream<E>, Iterable<E> {

  Seq<E> tail();
  E head();

  boolean isEmpty();
  default boolean nonEmpty() { return !isEmpty(): }

  default Optional<E> headOpt() {
    if (isEmpty())
      return Optional.ofNullable(head());
    else
      return Optional.empty();
  }

  default E headOr(Supplier<E> prod) {
    if (isEmpty()) return head(); else return prod.get();
  }

  default Iterator<E> iterator() {
    return new Iterator<>() {
      Seq<E> seq = this;
      public void remove() { throw removeException; }
      public boolean hasNext() { return seq.nonEmpty(); }
      public E next() {
        E nextValue = seq.head();
        seq = seq.tail();
        return nextValue;
      }
  }

  default Stream<E> stream() {
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
      "Immutable Seq objects do not support remove().");

}
