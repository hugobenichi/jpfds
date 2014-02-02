package jpfds.col;

import jpfds.abs.Seq;
import jpfds.abs.SeqBuilder;
import jpfds.abs.ColBuilder;

// if allow mixed List, I can add a specialized ArrayList with fast access
// what about mixe ArrayList / LinkedList
//
// check how safe the casts here are
public class List<X> implements Seq<X> {

  private final X head;
  private Seq<X> tail;

  public List(X head, Seq<? extends X> tail) {
    this.head = head;
    this.tail = (Seq<X>) tail;
  }

  public List(X head) {
    this(head, EmptySeq.get());
  }

  public boolean isEmpty() { return false; }
  public Seq<X> tail() { return tail; }
  public X head() {return head; }

  public static <X> SeqBuilder<X> builder() {
    return EmptySeqBuilder.get();
  }

  private static class EmptySeqBuilder<X> implements SeqBuilder<X> {
    private EmptySeqBuilder() {}
    private static final EmptySeqBuilder<Object> theEmptySeqBuilder =
      new EmptySeqBuilder<>();
    public static <X> EmptySeqBuilder<X> get() {
      return (EmptySeqBuilder<X>) theEmptySeqBuilder;
    }
    public Seq<X> make() { return EmptySeq.get(); }
    public Seq<X> concat(Seq<X> tail) { return tail; }
    public SeqBuilder<X> cons(X elem) { return new ListBuilder(elem); }
    public SeqBuilder<X> add(X elem) { return cons(elem); }
  }

  private static class ListBuilder<X> implements SeqBuilder<X> {

    private List<X> head;
    private List<X> end;

    public ListBuilder(X elem) {
      this.head = new List(elem);
      this.end = this.head;
    }

    public ListBuilder<X> cons(X elem) {
      this.head = new List(elem, this.head);
      return this;
    }

    public ListBuilder<X> add(X elem) {
      List<X> newTail = new List(elem);
      this.end.tail = newTail;
      this.end = newTail;
      return this;
    }

    public Seq<X> make() {
      // unsafe !! set end next to empty, unless it s empty
      // unset List to avoid double call to toSeq and avoid mutating List
      return this.head;
    }

    public Seq<X> concat(Seq<X> tail) {
      this.end.tail = tail;
      return make();
    }
  }

}
