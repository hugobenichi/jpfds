package jpfds.col;

import jpfds.abs.Seq;
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

  public static <X> ColBuilder<X,Seq<X>> builder() {
    return EmptySeqBuilder.get();
  }

  private static class EmptySeqBuilder<X> implements ColBuilder<X,Seq<X>> {
    private EmptySeqBuilder() {}
    private static final EmptySeqBuilder<Object> theEmptySeqBuilder =
      new EmptySeqBuilder<>();
    public static <X> EmptySeqBuilder<X> get() {
      return (EmptySeqBuilder<X>) theEmptySeqBuilder;
    }
    public Seq<X> make() { return EmptySeq.get(); }
    public SeqBuilder<X> cons(X elem) { return new SeqBuilder(elem); }
    public SeqBuilder<X> add(X elem) { return cons(elem); }
  }

  private static class SeqBuilder<X> implements ColBuilder<X,Seq<X>> {

    private List<X> head;
    private List<X> end;

    public SeqBuilder(X elem) {
      this.head = new List(elem);
      this.end = this.head;
    }

    public SeqBuilder<X> cons(X elem) {
      this.head = new List(elem, this.head);
      return this;
    }

    public SeqBuilder<X> add(X elem) {
      List<X> newTail = new List(elem);
      this.end.tail = newTail;
      this.end = newTail;
      return this;
    }

    public Seq<X> make() {
      // set end next to empty, unless it s empty
      // unset List to avoid double call to toSeq
      return this.head;
    }
    //public ListBuilder<X> concat(ColBuilder<? extends X> that) {
      //does not work, need to try casting to own type or fallback to default
      //this.end.tail = that.head;
      //this.end = that.end;
      //return this;
      // don't forget to kill the that builder;
    //}
  }

}
