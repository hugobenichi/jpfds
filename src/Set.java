/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds;

public interface Set<X> extends Col<X,Set<X>> {
  boolean has(X x);
  Set<X> remove(X elem);
  default Set<X> union(Set<X> that) { return that.into(this); }
}

