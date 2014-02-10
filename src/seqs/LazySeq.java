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
    return (X) this.head;
  }

  public Seq<X> tail() {
    ensureNotEmptyOr(Seq.emptyTailException);
    return (Seq<X>) this.tail;
  }

  private void ensureInit() { if (notInit()) tryAdvanceIterator(); }

  private void ensureNotEmptyOr(RuntimeException ex) {
    ensureInit();
    if (isEmpty()) throw ex;
  }

  private boolean notInit() { return this.head == NotInit; }

  private synchronized void tryAdvanceIterator() {
    if (notInit()) advanceIterator((Iterator<X>)this.tail);
  }

  private void advanceIterator(Iterator<X> iter) {
    if (iter.hasNext()) {
      this.head = iter.next();
      this.tail = new LazySeq(iter);
    } else {
      this.head = Empty;
      this.tail = Empty;
    }
  }

  public static <X> Seq<X> from(Iterable<X> source) {
    return new LazySeq(source.iterator());
  }

  public static <X> Seq<X> from(Iterator<X> source) {
    return new LazySeq(source);
  }
}
