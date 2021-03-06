package jpfds;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.function.Function;

import jpfds.seqs.SeqBuilder;
import jpfds.seqs.LazySeq;
import jpfds.seqs.List;

/** Work around Java interface wrapping of JRuby which does not know about
 *  static methods in Java 8 interfaces yet. */
public final class Seqs {
  private Seqs() {}

  public static <Y> Seq<Y> nil() { return List.nil(); }

  public static <Y> Seq<Y> cons(Y elem, Seq<? extends Y> seq) {
    return List.cons(elem, seq);
  }

  public static <Y> SeqBuilder<Y> builder() { return List.builder(); }

  public static <Y> Seq<Y> of(Iterable<Y> elems) {
    SeqBuilder<Y> bld = builder();
    return bld.addAllThen(elems).make();
  }

  public static <Y> Seq<Y> of(Y... args) {
    SeqBuilder<Y> bld = builder();
    for (Y y : args) { bld.add(y); }
    return bld.make();
  }

  public static <Y> Seq<Y> of(Y y1) {
    SeqBuilder<Y> bld = builder();
    return bld.addThen(y1).make();
  }

  public static <Y> Seq<Y> of(Y y1, Y y2) {
    SeqBuilder<Y> bld = builder();
    return bld.addThen(y1).addThen(y2).make();
  }

  public static <Y> Seq<Y> of(Y y1, Y y2, Y y3) {
    SeqBuilder<Y> bld = builder();
    return bld.addThen(y1).addThen(y2).addThen(y3).make();
  }

  public static <Y> Seq<Y> of(Y y1, Y y2, Y y3, Y y4) {
    SeqBuilder<Y> bld = builder();
    return bld.addThen(y1).addThen(y2).addThen(y3).addThen(y4).make();
  }

  /** Creates a new lazy Seq wrapping over the given iterable object. Callers
   *  should be careful not to retain the head of this Seq and prevent GC for
   *  long running iterations.
   *  @param <Y> type of the elements in the given Iterable source.
   *  @param source a object implementing Iterable. Cannot be null.
   *  @return a lazy Seq. */
  public static <Y> Seq<Y> lazy(Iterable<Y> source) {
    return buffer(source.iterator());
  }

  /** Creates a new lazy Seq wrapping over the given itetator object. Callers
   *  should be careful not to retain the head of this Seq and prevent GC for
   *  long running iterations.
   *  @param <Y> type of the elements in the given Iterator.
   *  @param iter a object implementing Iterable. Cannot be null.
   *  @return a lazy Seq. */
  public static <Y> Seq<Y> buffer(final Iterator<Y> iter) {
    return new LazySeq.Synchronized<Y>() {
      protected void advance() {
        if (iter.hasNext())
          setTo(iter.next(), buffer(iter));
        else
          setEmpty();
      }
    };
  }

  /** Creates a new lazy infinite Seq wrapping over the given source of object.
   *  Callers should be careful not to retain the head of this Seq and prevent
   *  GC for long running iterations.
   *  @param <Y> type of the elements produced by the given source.
   *  @param source a Supplier of objects. Cannot be null.
   *  @return an infinite lazy Seq. */
  public static <Y> Seq<Y> infinite(final Supplier<Y> source) {
    return new LazySeq.Synchronized<Y>() {
      public Size sizeInfo() { return Size.infinite; }
      protected void advance() {
        setTo(source.get(), infinite(source));
      }
    };
  }

  public static <Y> Seq<Y> constant(Y value) {
    return new LazySeq<Y>() {
      public Size sizeInfo() { return Size.infinite; }
      protected void advance() { setTo(value, this); }
    };
  }

  /** Creates an infinite Seq from a seed and an induction rule.
   *  Callers should be careful not to retain the head of this Seq and prevent
   *  GC for long running iterations.
   *  @param <Y> type of the elements of this sequence.
   *  @param init the first value of this sequence.
   *  @param rule an induction rule for computing the next value.
   *  @return a lazy infinite sequence. */
  public static <Y> Seq<Y> induce(Y init, Function<Y,Y> rule) {
    return new LazySeq<Y>() {
      public Size sizeInfo() { return Size.infinite; }
      protected void advance() {
        Y next = rule.apply(init);
        setTo(init, induce(next, rule));
      }
    };
  }

  /** Creates an infinite cyclic Seq repeating the elements in the given Seq.
   *  Callers should be careful not to retain the head of this Seq and prevent
   *  GC for long running iterations.
   *  @param <Y> type of the elements produced by the given source.
   *  @param source any sequence. Cannot be null.
   *  @return a sequence that repeats itself. */
  public static <Y> Seq<Y> cycle(final Seq<Y> source) {
    return constant(null).lflatMap(any -> source);
  }

}
