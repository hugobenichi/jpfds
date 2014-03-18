/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

final class Nodes {
  private Nodes() {}

  public static <X> boolean has(Node<X> n, X x, Comparator<X> ord) {
    if (n.isLeaf()) return false;
    int comp = ord.compare(x, n.val());
    if (comp == 0) return true;
    if (comp < 0) return has(n.left(), x, ord);
    return has(n.right(), x, ord);
  }

  public static <X> Node<X> rotateRight(Node<X> x) {
    if (x.isLeaf()) return x;
    Node<X> y = x.left();
    if (y.isLeaf()) return x;
    return y.make(y.left(), x.make(y.right(), x.right()));
  }

  public static <X> Node<X> rotateLeft(Node<X> y) {
    if (y.isLeaf()) return y;
    Node<X> x = y.right();
    if (x.isLeaf()) return y;
    return x.make(y.make(y.left(), x.left()), x.right());
  }

}
