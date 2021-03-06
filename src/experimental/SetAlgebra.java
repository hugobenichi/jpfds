package jpfds.experimental;

public interface SetAlgebra<S extends SetAlgebra<S>> {
  S empty();
  S union(S that);
  S inter(S that);
  S excl(S that);
  S conj(S total);
  /*
    defaut: conj(S total) { return S.excl(this); }
  */
}
