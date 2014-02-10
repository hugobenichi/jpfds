/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import jpfds.Seq;
import jpfds.Builder;

public interface SeqBuilder<X> extends Builder<X,Seq<X>> {
  void cons(X elem);
  default SeqBuilder<X> consThen(X elem) { this.cons(elem); return this; }
  Seq<X> concat(Seq<X> tail);
  static <Y> SeqBuilder<Y> get() { return List.builder(); }
}
