package jpfds;

import java.util.Iterator;
import java.util.function.Supplier;

import jpfds.seqs.SeqBuilder;
import jpfds.seqs.LazySeq;

/** Work around Java interface wrapping of JRuby which does not know about
 *  static methods in Java 8 interfaces yet. */
public final class Seqs {
  private Seqs() {}

  public static <Y> Seq<Y> nil() { return Seq.nil(); }

  public static <Y> SeqBuilder<Y> builder() { return SeqBuilder.get(); }

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
}
