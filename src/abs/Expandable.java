package jpfds.abs;

public interface Expandable<X,E extends Expandable<X,E>> {
  E cons(X elem);
}
