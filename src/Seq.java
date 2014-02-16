/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jpfds.seqs.List;
import jpfds.seqs.SeqBuilder;
import jpfds.seqs.BaseLazySeq;

public interface Seq<X> extends Iterable<X>, Col<X,Seq<X>> {

  Seq<X> tail();
  X head();

  default <Y> Y reduce(Y seed, BiFunction<Y, ? super X,Y> f) {
    Seq<X> seq = this;
    while (seq.nonEmpty()) {
      seed = f.apply(seed, seq.head());
      seq = seq.tail();
    }
    return seed;
  }

  default Seq<X> seq() { return this; }
  default Seq<X> cons(X elem) { return Seq.cons(elem, this); }
  default Seq<X> add(X elem) { return this.cons(elem); }
  default Seq<X> empty() { return List.nil(); }
  default Seq<X> union(Seq<X> that) {
    SeqBuilder<X> bld = SeqBuilder.get();
    return bld.addAllThen(this).addAllThen(that).make();
  }

  default boolean eq(Seq<X> that) {
    if (this == that) return true;
    if (that.isEmpty()) return this.isEmpty();
    if (this.isEmpty()) return false;
    return eqHead(that.head()) & this.tail().eq(that.tail());
  }

  default boolean eqHead(X thatHead) {
    X thisHead = this.head();
    if (thisHead == null) return thatHead == null;
    if (thatHead == null) return false;
    return thisHead.equals(thatHead);
  }

  default Seq<X> reverse() { return this.reduce(empty(), Seq::cons); }

  default Size sizeInfo() { return Size.unknown; }

  default int size() { return this.reduce(0, (l,x) -> l + 1); }

  default Optional<X> headOpt() {
    if (isEmpty())
      return Optional.ofNullable(head());
    else
      return Optional.empty();
  }

  default X headOr(Supplier<X> prod) {
    if (isEmpty()) return this.head(); else return prod.get();
  }

  default Iterator<X> iterator() {
    final Seq<X> self = this;
    return new Iterator<X>() {
      Seq<X> seq = self;
      public void remove() { throw removeException; }
      public boolean hasNext() { return seq.nonEmpty(); }
      public X next() {
        X nextValue = seq.head();
        seq = seq.tail();
        return nextValue;
      }
    };
  }

  default <Y> Seq<Y> lmap(final Function<X,Y> f) {
    final Seq<X> source = this;
    return new BaseLazySeq<Y>() {
      protected void ensureInit() { if (notInit()) advance(); }
      protected void advance() {
        if (source.isEmpty()) {
          setEmpty();
        } else {
          this.head = f.apply(source.head());
          this.tail = source.tail().lmap(f);
        }
      }
    };
  }

  default Seq<X> lfilter(final Predicate<X> f) {
    final Seq<X> source = this;
    return new BaseLazySeq<X>() {
      protected void ensureInit() { if (notInit()) advance(); }
      protected void advance() {
        Seq<X> s = source;
        while (s.nonEmpty()) {
          if (f.test(s.head())) {
            this.head = s.head();
            this.tail = s.tail().lfilter(f);
            return;
          }
          s = s.tail();
        }
        setEmpty();
      }
    };
  }

  static <Y> Seq<Y> cons(Y elem, Seq<? extends Y> seq) {
    return List.cons(elem, seq);
  }

  static <Y> Seq<Y> of(Iterable<Y> elems) {
    SeqBuilder<Y> bld = SeqBuilder.get();
    return bld.addAllThen(elems).make();
  }

  static <Y> Seq<Y> of(Y y1) {
    return SeqBuilder.<Y>get().addThen(y1).make();
  }

  static <Y> Seq<Y> of(Y y1, Y y2) {
    return SeqBuilder.<Y>get().addThen(y1).addThen(y2).make();
  }

  static <Y> Seq<Y> of(Y y1, Y y2, Y y3) {
    return SeqBuilder.<Y>get().addThen(y1).addThen(y2).addThen(y3).make();
  }

  static <Y> Seq<Y> of(Y y1, Y y2, Y y3, Y y4) {
    SeqBuilder<Y> bld = SeqBuilder.get();
    return bld.addThen(y1).addThen(y2).addThen(y3).addThen(y4).make();
  }

  static <Y> Seq<Y> nil() { return List.nil(); }

  RuntimeException removeException =
    new UnsupportedOperationException("Seq Iterators do not support remove().");

  RuntimeException emptyHeadException =
    new UnsupportedOperationException("An empty seq has no head.");

  RuntimeException emptyTailException =
    new UnsupportedOperationException("An empty seq has no tail.");

}
