package jpfds.abs;

import java.util.*;
import java.util.function.Supplier;

import jpfds.col.List;
import jpfds.col.EmptySeq;

public interface Seq<X> extends Iterable<X>, Col<X,Seq<X>> {

  Seq<X> tail();
  X head();

  default Seq<X> seq() { return this; }
  default Seq<X> cons(X elem) { return new List(elem, this); }
  default Seq<X> empty() { return EmptySeq.get(); }
  default Seq<X> union(Seq<X> col) {
    SeqBuilder<X> bld = builder();
    //bld = bld.addAll(this);
    for (X elem : this) { bld = (SeqBuilder<X>) bld.add(elem); } //super ugly
    return bld.concat(col);
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
    ColBuilder<X,Seq<X>> bld = builder();
    for (X elem : elems) { bld = bld.add(elem); }
    return bld.make();
  }

  static <X> Seq<X> of(X... args) { return of(args); }

  final RuntimeException removeException =
    new UnsupportedOperationException(
      "Iterators over immutable Seq objects do not support #remove().");

}
