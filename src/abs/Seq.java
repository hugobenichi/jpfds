package jpfds.abs;

import java.util.stream.Stream;

public interface Seq<E> extends Stream<E>, Iterable<E> {

  boolean isEmpty();
  default: boolean nonEmpty() { return !isEmpty(): }

  Seq<E> tail();
  E head();
  default: Optional<E> headOpt() {
    if (isEmpty())
      return new Optional(head());
    else
      return None;
  }
  default E headOr(Producer<E> prod) {
    if (isEmpty())
      return head();
    else
      return prod();
  }

  default: Iterator<E> iterator() {
    return new Iterator<E> {
      Seq<E> seq = this;
      public boolean hasNext() { return seq.nonEmpty(); }
      public E next() {
        E nextValue = seq.head();
        seq = seq.tail();
        return nextValue;
      }
      public void remove() {
        // cache this in constant object
        throw new UnsupportedException(
          "Immutable sequences do not support remove().");
      }
  }

  default: Stream<E> stream();

}
