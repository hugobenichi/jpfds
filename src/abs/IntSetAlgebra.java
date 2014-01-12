package jpfds.abs;

public interface IntSetAlgebra<S extends IntSetAlgebra<S>> {
  S empty();
  S union(S that);
  S inter(S that);
  S excl(S that);
  S conj(S total);
  /*
    defaut: conj(S total) { return S.excl(this); }
  */
}
