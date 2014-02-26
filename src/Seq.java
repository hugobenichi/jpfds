/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds;

import java.util.Objects;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jpfds.seqs.LazySeq;

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
  default Seq<X> cons(X elem) { return Seqs.cons(elem, this); }
  default Seq<X> add(X elem) { return this.cons(elem); }
  default Seq<X> empty() { return Seqs.nil(); }
  default Seq<X> union(Seq<X> that) {
    return Seqs.<X>builder().addAllThen(this).addAllThen(that).make();
  }

  default boolean eq(Seq<X> that) {
    if (this == that) return true;
    if (that.isEmpty()) return this.isEmpty();
    if (this.isEmpty()) return false;
    return Objects.equals(this.head(), that.head())
      && this.tail().eq(that.tail());
  }

  default Seq<X> reverse() { return this.reduce(empty(), (s,x) -> s.cons(x)); }

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

  default Seq<X> lcat(final Seq<X> right) {
    final Seq<X> left = this;
    return new LazySeq<X>() {
      protected void advance() {
        if (left.nonEmpty())
          setTo(left.head(), left.tail().lcat(right));
        else if (right.nonEmpty())
          setTo(right.head(), right.tail());
        else
          setEmpty();
      }
    };
  }

  default <Y> Seq<Y> lmap(final Function<X,Y> f) {
    final Seq<X> source = this;
    return new LazySeq<Y>() {
      protected void advance() {
        if (source.isEmpty())
          setEmpty();
        else
          setTo(f.apply(source.head()), source.tail().lmap(f));
      }
    };
  }

  default <Y> Seq<Y> lflatMap(final Function<X,Seq<Y>> f) {
    final Seq<X> source = this;
    return new LazySeq<Y>() {
      protected void advance() {
        for (Seq<X> s = source; s.nonEmpty(); s = s.tail()) {
          Seq<Y> front = f.apply(s.head());
          if (front.nonEmpty()) {
            setTo(front.head(), front.tail().lcat(s.tail().lflatMap(f)));
            return;
          }
        }
        setEmpty();
      }
    };
  }

  default Seq<X> lfilter(final Predicate<X> f) {
    final Seq<X> source = this;
    return new LazySeq<X>() {
      protected void advance() {
        for (Seq<X> s = source; s.nonEmpty(); s = s.tail()) {
          if (f.test(s.head())) {
            setTo(s.head(), s.tail().lfilter(f));
            return;
          }
        }
        setEmpty();
      }
    };
  }

  /** Keep the first n elements of this Seq.
   *  @param n the number of initial elements to keep.
   *  @return a lazy Seq that stops after producing the n first initial
   *  elements of this Seq. */
  default Seq<X> take(final int n) {
    if (n <= 0) return empty();
    final Seq<X> source = this;
    return new LazySeq<X>() {
      protected void advance() {
        if (source.nonEmpty())
          setTo(source.head(), source.tail().take(n-1));
        else
          setEmpty();
      }
    };
  }

  /** Skips the n first elements of this Seq.
   *  @param n the number of initial elements to skip.
   *  @return a lazy Seq that skips the n first initial elements of this Seq. */
  default Seq<X> skip(final int n) {
    if (n <= 0) return this;
    final Seq<X> source = this;
    return new LazySeq<X>() {
      protected void advance() {
        Seq<X> s = source;
        for (int c = n; c > 0 && s.nonEmpty(); c--) { s = s.tail(); }
        if (s.nonEmpty()) setTo(s.head(), s.tail()); else setEmpty();
      }
    };
  }

  /** Keep elements from this Seq until the predicate holds true.
   *  @param f a boolean predicate. Cannot be null.
   *  @return a lazy Seq that stops at the first failure of the predicate. */
  default Seq<X> until(final Predicate<X> f) {
    final Seq<X> source = this;
    return new LazySeq<X>() {
      protected void advance() {
        if (source.nonEmpty() && f.test(source.head()))
          setTo(source.head(), source.tail().until(f));
        else
          setEmpty();
      }
    };
  }

  RuntimeException removeException =
    new UnsupportedOperationException("Seq Iterators do not support remove().");

  RuntimeException emptyHeadException =
    new UnsupportedOperationException("An empty seq has no head.");

  RuntimeException emptyTailException =
    new UnsupportedOperationException("An empty seq has no tail.");

}
