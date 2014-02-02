package jpfds.abs;

import jpfds.col.List;

public interface SeqBuilder<X> extends ColBuilder<X,Seq<X>> {
  Seq<X> concat(Seq<X> tail); // terminal op, consume Builder and return Seq
  static <X> SeqBuilder<X> get() { return List.builder(); }
}
