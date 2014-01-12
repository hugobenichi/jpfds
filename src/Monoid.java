package jpfds;

public interface Monoid<M extends Monoid<M>> {
  public M union(M that);
  public M empty();
/*
  java 8
  public M concat(Iterable<M> ms);
  default public M concat(Iterable<M> ms) {
    M ret = empty();
    for (M m : ms) { ret = ret.union(m); }
    return ret;
  }
*/
}
