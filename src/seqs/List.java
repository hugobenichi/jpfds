/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import jpfds.Seq;
import jpfds.Size;

/** The canonical implementation of a Seq. List is an immutable singly linked
 *  list that knows it is bounded and knows how to compute its size. It also
 *  provides a Builder implementation that can efficiently append at the end of
 *  a List being built, until it is published. List instances can be created
 *  on top of any type of Seq, which allows to mix lazy and strict List
 *  instances. */
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

  /** A covariant constructor for adding an element at the front of a Seq.
   *  @param elem an element reference. Can be null.
   *  @param tail any type of Seq. Cannot be null.
   *  @param <Y> type of the element added at the front of the given Seq.
   *  @return a new Seq made of the given arguments using the List class. */
  public static <Y> Seq<Y> cons(Y elem, Seq<? extends Y> tail) {
    if (tail.isEmpty() || tail instanceof BoundedList)
      return new BoundedList<>(elem, tail);
    else
      return new List<>(elem, tail);
  }

  /** Creates a new empty SeqBuilder object for creating a List instance.
   *  @param <Y> type of the elements this Builder should accept.
   *  @return an empty SeqBuilder. */
  public static <Y> SeqBuilder<Y> builder() { return new ListBuilder<>(); }

  /** Return a reference to the empty List. The underlying object representing
   *  the empty List is a singleton shared accross all types.
   *  @param <Y> type of the elements this reference to the empty List should
   *  be for.
   *  @return the empty List. */
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
