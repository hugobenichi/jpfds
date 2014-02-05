package jpfds.abs;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.BiFunction;

import jpfds.col.List;
import jpfds.col.EmptySeq;

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
  default Seq<X> cons(X elem) { return new List(elem, this); }
  default Seq<X> add(X elem) { return cons(elem); }
  default Seq<X> empty() { return EmptySeq.get(); }
  default Seq<X> union(Seq<X> that) {
    SeqBuilder<X> bld = builder();
    return bld.addAll(this).addAll(that).make(); // can't inline, bad inference
  }

  default Optional<X> headOpt() {
    if ( isEmpty() )
      return Optional.ofNullable(head());
    else
      return Optional.empty();
  }

  default X headOr(Supplier<X> prod) {
    if ( isEmpty() ) return head(); else return prod.get();
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

  static <Y> Seq<Y> cons(Y elem, Seq<? extends Y> seq) {
    return new List(elem, seq);
  }

  static <X> SeqBuilder<X> builder() { return SeqBuilder.get(); }

  static <X> Seq<X> of(Iterable<X> elems) {
    SeqBuilder<X> bld = builder();
    return bld.addAll(elems).make(); // can't inline -> bad inference ?
  }

  static <X> Seq<X> of(X... args) { return of(args); }

  final RuntimeException removeException =
    new UnsupportedOperationException(
      "Iterators over immutable Seq objects do not support #remove().");

}
