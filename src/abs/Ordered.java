package jpfds.abs;

// TODO: better nname for in{,Reverse}Order, move inOrder to Sequable interface
interface Ordered<X,Y> {
  Seq<Y> range(X from, X to);
  Seq<Y> inOrder();
  Seq<Y> inReverseOrder();
}

