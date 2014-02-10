package jpfds;

import java.util.Iterator;

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

  public static <Y> Seq<Y> from(Iterable<Y> source) {
    return new LazySeq<Y>(source.iterator());
  }

  public static <Y> Seq<Y> from(Iterator<Y> source) {
    return new LazySeq<Y>(source);
  }
}
