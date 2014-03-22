/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds;

//public interface Heap<X,H extends Heap<X,H>> extends Col<X,H>, Seq<X> {
public interface Heap<X,H extends Heap<X,H>> extends Col<X,H> {
  X min();
  H pop(); // should I tie this to the specific type of the Heap implem ?
           // should I say that Heap extends Col ?
           // should I say that Heap extends Seq ?

  /*
  default Seq<X> seq() { return this; }

  default X head() { return min(); }

  default Seq<X> tail() { return pop(); }
  */

}
