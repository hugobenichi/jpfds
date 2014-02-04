package jpfds.abs;

import jpfds.col.List;

public abstract class SeqBuilder<X> implements Builder<X,SeqBuilder<X>,Seq<X>> {
  public abstract boolean isEmpty();
  public abstract SeqBuilder<X> cons(X elem);
  public abstract SeqBuilder<X> union(SeqBuilder<X> col);
  public abstract Seq<X> make();
  public Seq<X> seq() { return make(); }
  public SeqBuilder<X> empty() { return get(); }

  static <X> SeqBuilder<X> get() { return List.builder(); }
}
