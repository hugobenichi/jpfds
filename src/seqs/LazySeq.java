/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import java.util.Iterator;
import java.util.function.Supplier;

import jpfds.Seq;
import jpfds.Size;

/** A lazy thread-safe implementation of the Seq abstraction. This lazy Seq
 *  can handle infinite Iterator and sources and will lazily access new items
 *  from its source only as requested by callers. */
public class LazySeq<X> extends BaseLazySeq<X> {

  private LazySeq(Iterator<X> iter) { super(NotInit, iter); }

  protected void advance() {
    @SuppressWarnings("unchecked") Iterator<X> iter = (Iterator<X>) this.tail;
    if (iter.hasNext()) {
      this.head = iter.next();
      this.tail = new LazySeq<X>(iter);
    } else {
      setEmpty();
    }
  }

  /** Creates a new lazy infinite Seq wrapping over the given source of object.
   *  Callers should be careful not to retain the head of this Seq and prevent
   *  GC for long running iterations.
   *  @param source a Supplier of objects. Cannot be null.
   *  @return an infinite lazy Seq. */
  public static <Y> Seq<Y> from(Supplier<Y> source) {
    return new InfiniteSeq<Y>(source);
  }

  /** Creates a new lazy Seq wrapping over the given iterable object. Callers
   *  should be careful not to retain the head of this Seq and prevent GC for
   *  long running iterations.
   *  @param source a object implementing Iterable. Cannot be null.
   *  @return a lazy Seq. */
  public static <Y> Seq<Y> from(Iterable<Y> source) {
    return new LazySeq<Y>(source.iterator());
  }

  /** Creates a new lazy Seq wrapping over the given itetator object. Callers
   *  should be careful not to retain the head of this Seq and prevent GC for
   *  long running iterations.
   *  @param source a object implementing Iterable. Cannot be null.
   *  @return a lazy Seq. */
  public static <Y> Seq<Y> of(Iterator<Y> source) {
    return new LazySeq<Y>(source);
  }

  public static <Y> Seq<Y> from(Seq<Y> source) {
    if (source instanceof BufferedSeq)
        return source;
    else
        return new BufferedSeq<Y>(source);
  }
}
