package jpfds.col;

import jpfds.abs.Seq;
import jpfds.abs.ColBuilder;

// how to return a tail type as List which can be built upon
//    a) only use cons() as a static method onto Seq as tail (can mix Seq types)
//    b) self-type Seq to allow tail() to keep the type
//    c) introduce ESeq that wraps Seq to have tail keep type ?
//
// how to share Empty list with generic typing
//    a) implements head / tail / cons as static method with casting
//    b) use base abstract class ?
//
// should I have pure List (tail is always a List), or unpure List ?
//    maybe add a static cons() function in Seq interface to build mixed List
//
// maybe offer both an instance method cons() that does not allow covariance
//    and a static constructor method for covariance
//
// if allow mixed List, I can add a specialized ArrayList with fast access
// what about mixe ArrayList / LinkedList
//
// look at covariant return type for tail() and see if I can return a List
//    see if it generates brigdes ?
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
