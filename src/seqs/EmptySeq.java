package jpfds.seqs;

import jpfds.Seq;

public class EmptySeq implements Seq<Object> {
  private EmptySeq() {}

  private static final EmptySeq theEmptySeq = new EmptySeq();
  public static <X> Seq<X> get() { return (Seq<X>) theEmptySeq; }

  public boolean isEmpty() { return true; }
  public Object head() { throw headException; }
  public Seq<Object> tail() { throw tailException; }

  private static final RuntimeException headException =
    new RuntimeException("Empty list has no head");

  private static final RuntimeException tailException =
    new RuntimeException("Empty list has no tail");

}
