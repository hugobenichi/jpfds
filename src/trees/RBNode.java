/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

public interface RBNode<X> extends Node<X> {
  boolean isBlack();
  default boolean isRed() { return !isBlack(); }

  default Node<X> insert(X x, Comparator<X> ord) {
    int comp = ord.compare(x, val());
    if (comp == 0) return this;
    if (comp < 0) {
      Node<X> l = left().insert(x, ord);
      if (l == left()) return this; else return make(val(), l, right());
    } else {
      Node<X> r = right().insert(x, ord);
      if (r == right()) return this; else return make(val(), left(), r);
    }
  }

  default Node<X> remove(X x, Comparator<X> ord) { return this; }

  public interface Leaf<X> extends RBNode<X> {
    default boolean isBlack() { return true; }
    default boolean isLeaf() { return true; }
    default Node<X> left() { throw new UnsupportedOperationException(); }
    default Node<X> right() { throw new UnsupportedOperationException(); }
    default boolean has(X x, Comparator<X> ord) { return false; }
    default Node<X> insert(X x, Comparator<X> ord) { return make(x,this,this); }
    default Node<X> remove(X x, Comparator<X> ord) { return this; }
  }
}
