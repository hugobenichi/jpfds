package jpfds.col;

import jpfds.abs.Seq;

// todo: which interface for Reducible ? Transient
//        implements Iterable, Seq ?
public interface SeqBuilder<X> {
    SeqBuilder<X> cons(X elem);
    SeqBuilder<X> add(X elem);
    default SeqBuilder<X> addAll(Iterable<? extends X> elems) {
      SeqBuilder<X> bld = this;
      for (X elem : elems) { bld = bld.add(elem); }
      return bld;
    }
    SeqBuilder<X> concat(SeqBuilder<? extends X> that);
    Seq<X> toSeq();
}
