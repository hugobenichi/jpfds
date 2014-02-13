/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import jpfds.Seq;
import jpfds.Size;

public class List<X> implements Seq<X> {

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
    if (tail.isEmpty() || tail instanceof BoundedList)
      return new BoundedList<>(elem, tail);
    else
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
    public Size sizeInfo() { return Size.empty; }
    public Object head() { throw Seq.emptyHeadException; }
    public Seq<Object> tail() { throw Seq.emptyTailException; }
  }

  private static class BoundedList<X> extends List<X> implements Size.Linear {
    public BoundedList(X head, Seq<? extends X> tail) { super(head, tail); }
    public BoundedList(X head) { super(head); }
    public Size sizeInfo() { return this; }
    public int size() {
      int size = 0;
      Seq<X> seq = this;
      while (seq.nonEmpty()) {
        assert seq.isEmpty() || seq instanceof BoundedList;
        seq = seq.tail();
        size++;
      }
      return size;
    }
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
      this.end = new BoundedList<>(elem);
      this.head = this.end;
    }

    public void cons(X elem) {
      assertUnpublished();
      if (head.isEmpty()) {
        this.end = new BoundedList<>(elem);
        this.head = this.end;
      } else {
        this.head = new BoundedList<>(elem, this.head);
      }
    }

    public void add(X elem) {
      assertUnpublished();
      List<X> newEnd = new BoundedList<>(elem);
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

    public boolean isConsumed() { return published; }

    private void assertUnpublished() { if (published) throw consumedException; }

    private static final IllegalStateException consumedException =
      new IllegalStateException("Cannot use Builder after Buildee publication");
  }

}
