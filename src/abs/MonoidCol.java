package jpfds.abs;

public interface MonoidCol<M extends MonoidCol<M>> {
  M union(M that);
  M empty();
/*
  java 8
  default: M unionFrom(Iterable<M> ms) {
    M ret = empty();
    for (M m : ms) { ret = ret.union(m); }
    return ret;
  }
*/
}
