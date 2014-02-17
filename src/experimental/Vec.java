package jpfds.experimental;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

// add standard java collection implementation
// add ordered index interface
// implement Seq via slices for tail
public interface Vec<X> extends Iterable<X>, IntFun<X> {

  default int size() { return 0; }

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
