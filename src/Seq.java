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
  default Seq<X> cons(X elem) { return List.cons(elem, this); }
  default Seq<X> add(X elem) { return this.cons(elem); }
  default Seq<X> empty() { return EmptySeq.get(); }
  default Seq<X> union(Seq<X> that) {
    SeqBuilder<X> bld = List.builder();
    return bld.addAll(this).addAll(that).make(); // can't inline, bad inference
  }

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

  static <X> Seq<X> of(Iterable<X> elems) {
    SeqBuilder<X> bld = List.builder();
    return bld.addAll(elems).make(); // can't inline -> bad inference ?
  }

  static <X> Seq<X> of(X... args) { return of(args); }

  static <X> Seq<X> nil() { return EmptySeq.get(); }

  final RuntimeException removeException =
    new UnsupportedOperationException(
      "Iterators over immutable Seq objects do not support #remove().");

}
