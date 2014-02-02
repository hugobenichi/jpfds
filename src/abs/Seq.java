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
  default Seq<X> union(Seq<X> col) { return this; } // dummy

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

  static <X> Seq<X> of(Iterable<X> elems) {
    Seq<X> ls = EmptySeq.get();
    for (X elem : elems) { ls = cons(elem, ls); }
    return ls;
  }

  static <X> Seq<X> of(X... args) { return of(args); }

  static <X> ColBuilder<X,Seq<X>> builder() {
    return List.builder();
  }

  final RuntimeException removeException =
    new UnsupportedOperationException(
      "Iterators over immutable Seq objects do not support #remove().");

}
