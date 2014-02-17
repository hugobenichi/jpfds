/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import jpfds.Seq;
import jpfds.Size;

/** A lazy non thread-safe implementation of the Seq abstraction. This lazy Seq
 *  will lazily access new items from its source only as requested by callers.
 *  Concrete classes should implement in advance() how new elements are fetched
 *  from the underlying source or procedure. advance() is not protected against
 *  concurrent calls and it is therefore required that the implementation is
 *  free of side-effects. For synchronized "at-most-once" semantics in a
 *  multi-threaded situation, LazySeq.Synchronized should be used instead. */
public abstract class LazySeq<X> implements Seq<X> {

  private static final Object NotInit = new Object();
  private static final Object Empty = new Object();

  private Object head = NotInit;
  private Object tail = NotInit;

  abstract protected void advance();

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

  protected void setEmpty() { this.head = Empty; this.tail = Empty; }

  protected void setTo(X h, Seq<X> t) { this.head = h; this.tail = t; }

  protected boolean notInit() { return this.head == NotInit; }

  protected void ensureInit() { if (notInit()) advance(); }

  private void ensureNotEmptyOr(RuntimeException ex) {
    ensureInit();
    if (isEmpty()) throw ex;
  }

  /** A lazy thread-safe implementation of the Seq abstraction. This lazy Seq
   *  can handle infinite Iterator and sources and will lazily access new items
   *  from its source only as requested by callers. */
  public static abstract class Synchronized<X> extends LazySeq<X> {
    protected void ensureInit() { if (notInit()) tryAdvance(); }
    private synchronized void tryAdvance() { if (notInit()) advance(); }
  }

}
