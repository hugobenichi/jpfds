/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import java.util.Iterator;
import java.util.function.Supplier;

import jpfds.Seq;

/** A lazy thread-safe implementation of the Seq abstraction. This lazy Seq
 *  can handle infinite Iterator and sources and will lazily access new items
 *  from its source only as requested by callers. */
public final class LazySeq<X> implements Seq<X> {

  private static final Object NotInit = new Object();
  private static final Object Empty = new Object();

  private Object head;
  private Object tail;

  private LazySeq(Iterator<X> iter) { this.head = NotInit; this.tail = iter; }

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

  private void ensureInit() { if (notInit()) tryAdvanceIterator(); }

  private void ensureNotEmptyOr(RuntimeException ex) {
    ensureInit();
    if (isEmpty()) throw ex;
  }

  private boolean notInit() { return this.head == NotInit; }

  private synchronized void tryAdvanceIterator() {
    if (notInit()) advanceIterator();
  }

  private void advanceIterator() {
    @SuppressWarnings("unchecked") Iterator<X> iter = (Iterator<X>) this.tail;
    if (iter.hasNext()) {
      this.head = iter.next();
      this.tail = new LazySeq<X>(iter);
    } else {
      this.head = Empty;
      this.tail = Empty;
    }
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
  public static <Y> Seq<Y> from(Iterator<Y> source) {
    return new LazySeq<Y>(source);
  }

  /** Creates a new lazy infinite Seq wrapping over the given source of object.
   *  Callers should be careful not to retain the head of this Seq and prevent
   *  GC for long running iterations.
   *  @param source a Supplier of objects. Cannot be null.
   *  @return an infinite lazy Seq. */
  public static <Y> Seq<Y> from(final Supplier<Y> source) {
    Iterator<Y> iter = new Iterator<Y>() {
      public Y next() { return source.get(); }
      public boolean hasNext() { return true; }
      public void remove() { }
    };
    return new LazySeq<Y>(iter);
  }
}

/*
  Notes:
    - call to next() should be protected in case of failure, fallback on Empty
    - from(Supplier) should returne a specialized subclass that return infinite
      for sizeInfo and that share the code with LazySeq, but avoid creating an
      internal Iterator.
    - there should be a non-thread safe subclass that can only be generated from
      known immutable sources (other sequences). Call it BufferedSeq
*/
