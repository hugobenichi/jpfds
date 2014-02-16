/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import jpfds.Seq;
import jpfds.Size;

/** A lazy thread-safe implementation of the Seq abstraction. This lazy Seq
 *  can handle infinite Iterator and sources and will lazily access new items
 *  from its source only as requested by callers. */
public abstract class BaseLazySeq<X> implements Seq<X> {

  private static final Object NotInit = new Object();
  private static final Object Empty = new Object();

  private Object head = NotInit;
  private Object tail = NotInit;

  protected void setEmpty() { this.head = Empty; this.tail = Empty; }

  protected void setTo(Object h, Object t) { this.head = h; this.tail = t; }

  public boolean isEmpty() { ensureInit(); return this.head == Empty; }

  protected boolean notInit() { return this.head == NotInit; }

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

  private synchronized void tryAdvance() { if (notInit()) advance(); }

  abstract protected void advance();

}
