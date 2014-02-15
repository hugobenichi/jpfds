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
public class LazySeq<X> implements Seq<X> {

  private static final Object NotInit = new Object();
  private static final Object Empty = new Object();

  protected Object head;
  protected Object tail;

  protected LazySeq(Object h, Object t) { this.head = h; this.tail = t; }
  private LazySeq(Iterator<X> iter) { this(NotInit, iter); }

  public boolean isEmpty() { ensureInit(); return this.head == Empty; }

  public X head() {
    ensureNotEmptyOr(Seq.emptyHeadException);
    @SuppressWarnings("unchecked") X castedHead = (X) this.head;
    return castedHead;
  }

  public Seq<X> tail() {
    ensureNotEmptyOr(Seq.emptyTailException);
    @SuppressWarnings("unchecked") Seq<X> castedTail = (Seq<X>) this.tail;
    return castedTail;
  }

  private void ensureNotEmptyOr(RuntimeException ex) {
    ensureInit();
    if (isEmpty()) throw ex;
  }

  protected void ensureInit() { if (notInit()) tryAdvance(); }

  protected boolean notInit() { return this.head == NotInit; }

  private synchronized void tryAdvance() { if (notInit()) advance(); }

  protected void advance() {
    @SuppressWarnings("unchecked") Iterator<X> iter = (Iterator<X>) this.tail;
    if (iter.hasNext()) {
      this.head = iter.next();
      this.tail = new LazySeq<X>(iter);
    } else {
      this.head = Empty;
      this.tail = Empty;
    }
  }

  private static class InfiniteSeq<X> extends LazySeq<X> {
    public InfiniteSeq(Supplier<X> source) { super(NotInit, source); }
    public Size sizeInfo() { return Size.infinite; }
    protected void advance() {
      @SuppressWarnings("unchecked")
      Supplier<X> source = (Supplier<X>) this.tail;
      this.head = source.get();
      this.tail = new InfiniteSeq<X>(source);
    }
  }

  private static class BufferedSeq<X> extends LazySeq<X> {
    public BufferedSeq(Seq<X> source) { super(NotInit, source); }
    protected void ensureInit() { if (notInit()) advance(); }
    protected void advance() {
      Object localTail = this.tail;
      if (!(localTail instanceof BufferedSeq)) {
        @SuppressWarnings("unchecked")
        Seq<X> source = (Seq<X>) localTail;
        this.head = source.head();
        this.tail = new BufferedSeq<X>(source.tail());
      }
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
