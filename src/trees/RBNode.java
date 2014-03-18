/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

// it is assumed that make returns a new Red node
public interface RBNode<X> extends Node<X> {
  RBNode<X> left();
  RBNode<X> right();
  boolean isBlack();
  default boolean isRed() { return !isBlack(); }

  RBNode<X> makeRed(X val, Node<X> left, Node<X> right);
  RBNode<X> makeBlack(X val, Node<X> left, Node<X> right);
  RBNode<X> toBlack();

  default RBNode<X> insert(X x, Comparator<X> ord) {
    int comp = ord.compare(x, val());
    if (comp == 0) return this;
    if (comp < 0) {
      Node<X> l = left().insert(x, ord);
      if (l == left()) return this; else return makeRed(val(), l, right());
    } else {
      Node<X> r = right().insert(x, ord);
      if (r == right()) return this; else return makeRed(val(), left(), r);
    }
  }

  default RBNode<X> remove(X x, Comparator<X> ord) { return this; }

  default RBNode<X> ensureBalance() {
    if (isBalanced()) return this;
    else {
      return this;
    }
  }

  default boolean isBalanced() {
    if (isRed()) return true;
    return true;
  }

  public interface Leaf<X> extends RBNode<X> {
    default boolean isBlack() { return true; }
    default boolean isLeaf() { return true; }
    default RBNode<X> left() { throw new UnsupportedOperationException(); }
    default RBNode<X> right() { throw new UnsupportedOperationException(); }
    default boolean has(X x, Comparator<X> ord) { return false; }
    default RBNode<X> insert(X x, Comparator<X> ord) {
      return makeRed(x,this,this);
    }
    default RBNode<X> remove(X x, Comparator<X> ord) { return this; }
  }
}
