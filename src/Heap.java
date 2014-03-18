/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds;

public interface Heap<X> extends Col<X,Heap<X>> {
  X min();
  Heap<X> pop();
  default Heap<X> union(Heap<X> that) { return that.into(this); }
}
