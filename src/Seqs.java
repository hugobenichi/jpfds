package jpfds;

import java.util.function.Supplier;

import jpfds.seqs.SeqBuilder;
import jpfds.seqs.LazySeq;

/** Work around Java interface wrapping of JRuby which does not know about
 *  static methods in Java 8 interfaces yet. */
public final class Seqs {
  private Seqs() {}
  public static <Y> Seq<Y> nil() { return Seq.nil(); }
  public static <Y> SeqBuilder<Y> builder() { return SeqBuilder.get(); }
  public static <Y> Seq<Y> lazy(Iterable<Y> source) {
    return LazySeq.from(source);
  }
  public static <Y> Seq<Y> infinite(Supplier<Y> source) {
    return LazySeq.from(source);
  }
}
