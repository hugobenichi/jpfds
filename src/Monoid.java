//package jpfds;

public interface Monoid<M extends Monoid<M>> {
  public M union(M that);
  public M empty();
}
