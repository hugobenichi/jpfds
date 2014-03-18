/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

public interface Node<X> {
  X val();
  Node<X> left();
  Node<X> right();

  // make a new Node with the same node val but new children
  Node<X> make(Node<X> left, Node<X> right);
  Node<X> init(X val);

  default boolean isLeaf() { return false; }

  // if the element is already in the set, it is guaranteed that this function
  // returns the same Node reference.
  default Node<X> insert(X x, Comparator<X> ord) {
    int comp = ord.compare(x, val());
    if (comp == 0) return this;
    if (comp < 0) {
      Node<X> l = left().insert(x, ord);
      if (l == left()) return this; else return make(l, right());
    } else {
      Node<X> r = right().insert(x, ord);
      if (r == right()) return this; else return make(left(), r);
    }
  }

  default Node<X> remove(X x, Comparator<X> ord) {
    int comp = ord.compare(x, val());
    if (comp == 0) {
// merge both subtrees, return new root
    }
    if (comp < 0) {
      Node<X> l = left().remove(x, ord);
      if (l == left()) return this; else return make(l, right());
    } else {
      Node<X> r = right().remove(x, ord);
      if (r == right()) return this; else return make(left(), r);
    }
  }

  public interface Leaf<X> extends Node<X> {
    default boolean isLeaf() { return true; }
    default X val() { throw new UnsupportedOperationException("x_x"); }
    default Node<X> left() { throw new UnsupportedOperationException(">_<"); }
    default Node<X> right() { throw new UnsupportedOperationException(":-("); }
    default Node<X> remove(X x, Comparator<X> ord) { return this; }
    default Node<X> insert(X x, Comparator<X> ord) { return init(x); }
    default Node<X> make(Node<X> left, Node<X> right) {
      throw new UnsupportedOperationException(">_<");
    }
  }

}
