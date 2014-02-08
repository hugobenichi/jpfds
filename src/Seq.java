package jpfds;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.BiFunction;

import jpfds.seqs.EmptySeq;
import jpfds.seqs.List;
import jpfds.seqs.SeqBuilder;

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
  default Seq<X> empty() { return EmptySeq.get(); }
  default Seq<X> union(Seq<X> that) {
    SeqBuilder<X> bld = SeqBuilder.get();
    return bld.addAllThen(this).addAllThen(that).make();
  }

  default Seq<X> reverse() { return this.reduce(empty(), Seq::cons); }

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

  static <Y> Seq<Y> cons(Y elem, Seq<? extends Y> seq) {
    return List.cons(elem, seq);
  }

  static <Y> Seq<Y> of(Iterable<Y> elems) {
    SeqBuilder<Y> bld = SeqBuilder.get();
    return bld.addAllThen(elems).make();
  }

  static <Y> Seq<Y> of(Y... args) {
    SeqBuilder<Y> bld = SeqBuilder.get();
    for (Y elem : args) { bld.add(elem); }
    return bld.make();
  }

  static <Y> Seq<Y> nil() { return List.nil(); }

  final RuntimeException removeException =
    new UnsupportedOperationException(
      "Iterators over immutable Seq objects do not support #remove().");

}
