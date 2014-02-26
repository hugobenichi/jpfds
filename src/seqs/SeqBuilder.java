/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import jpfds.Seq;
import jpfds.Builder;

/** A Builder class for the Seq class. A SeqBuilder can efficiently add elements
 *  of the underlying buildee Seq both at the front with cons() or at the end
 *  with add(). */
public interface SeqBuilder<X> extends Builder<X,Seq<X>> {

  /** Add an element at the front of the Seq being built. This operation
   *  is illegal after publication and should throw an IllegalStateException.
   *  @param elem a element to add at the front of the Seq being built.*/
  void cons(X elem);

  /** Convenience operation for chaining cons() operations.
   *  @param elem an element to add to the front of the Seq being built.
   *  @return this builder instance. */
  default SeqBuilder<X> consThen(X elem) { this.cons(elem); return this; }
}
