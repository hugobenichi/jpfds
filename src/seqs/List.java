package jpfds.seqs;

import jpfds.Seq;

public final class List<X> implements Seq<X> {

  private final X head;
  private Seq<X> tail;

  private List(X head, Seq<? extends X> tail) {
    @SuppressWarnings("unchecked") Seq<X> castedTail = (Seq<X>) tail;
    this.head = head;
    this.tail = castedTail;
  }

  private List(X head) {
    this(head, nil());
  }

  public boolean isEmpty() { return false; }
  public Seq<X> tail() { return tail; }
  public X head() { return head; }

  public static <Y> Seq<Y> cons(Y elem, Seq<? extends Y> tail) {
    return new List<>(elem, tail);
  }

  public static <Y> SeqBuilder<Y> builder() { return new ListBuilder<>(); }

  public static <Y> Seq<Y> nil() {
    @SuppressWarnings("unchecked") Seq<Y> castedNil = (Seq<Y>) Nil.nil;
    return castedNil;
  }

  private static class Nil implements Seq<Object> {
    private Nil() {}
    public static final Nil nil = new Nil();

    public boolean isEmpty() { return true; }
    public Object head() { throw Seq.emptyHeadException; }
    public Seq<Object> tail() { throw Seq.emptyTailException; }
  }

  private static class ListBuilder<X> implements SeqBuilder<X> {

    private Seq<X> head;
    private List<X> end;
    private boolean published = false;

    public ListBuilder() {
      this.head = List.nil();
      this.end = null;
    }

    public ListBuilder(X elem) {
      this.end = new List<>(elem);
      this.head = this.end;
    }

    public void cons(X elem) {
      assertUnpublished();
      if (head.isEmpty()) {
        this.end = new List<>(elem);
        this.head = this.end;
      } else {
        this.head = new List<>(elem, this.head);
      }
    }

    public void add(X elem) {
      assertUnpublished();
      List<X> newEnd = new List<>(elem);
      if (head.isEmpty()) {
        this.head = newEnd;
        this.end = newEnd;
      } else {
        this.end.tail = newEnd;
        this.end = newEnd;
      }
    }

    public Seq<X> make() {
      assertUnpublished();
      published = true;
      return this.head;
    }

    public Seq<X> concat(Seq<X> tail) {
      assertUnpublished();
      if (head.isEmpty()) {
        return tail;
      } else {
        this.end.tail = tail;
        return make();
      }
    }

    public boolean isConsumed() { return published; }

    private void assertUnpublished() { if (published) throw consumedException; }

    private static final IllegalStateException consumedException =
      new IllegalStateException("Cannot use Builder after Buildee publication");
  }

}
