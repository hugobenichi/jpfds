package jpfds.abs;

import jpfds.Seq;

// TODO: better name for in{,Reverse}Order, move inOrder to Sequable interface
interface Indexed<X> extends IntFun<X> {

  // TOO refactor into IntBound interface
  int minEntry();
  int maxEntry();

  // to implement nicely, make a lazy seq of index,
  // and map it with #has() and #get()

  default Seq<X> range(int from, int to) {
    // iterate manually with index
    return null;
  }

  default Seq<X> inOrder() {
    // use min index to init Seq and iterate until max for tail()
    return null;
  }

  default Seq<X> inReverseOrder() {
    // same in reverse
    return null;
  }
}
