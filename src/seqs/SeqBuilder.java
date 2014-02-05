package jpfds.seqs;

import jpfds.Seq;
import jpfds.Builder;

public abstract class SeqBuilder<X> implements Builder<X,SeqBuilder<X>,Seq<X>> {
  public abstract boolean isEmpty();
  public abstract SeqBuilder<X> cons(X elem);
  public abstract SeqBuilder<X> add(X elem);
  public abstract SeqBuilder<X> union(SeqBuilder<X> col);
  public abstract Seq<X> make();
  public abstract Seq<X> concat(Seq<X> tail);
  public Seq<X> seq() { return make(); }
  public SeqBuilder<X> empty() { return get(); }

  static <X> SeqBuilder<X> get() { return List.builder(); }
}
