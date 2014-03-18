/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

public interface Node<X> {
  X val();
  Node<X> left();
  Node<X> right();
  Node<X> make(X val, Node<X> left, Node<X> right);

  default boolean isLeaf() { return false; }

// move to utility static function
  default boolean has(X x, Comparator<X> ord) {
    int comp = ord.compare(x, val());
    if (comp == 0) return true;
    if (comp < 0) return left().has(x, ord);
    return right().has(x, ord);
  }

  // if the element is already in the set, it is guaranteed that this function
  // returns the same Node reference.
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

  default Node<X> remove(X x, Comparator<X> ord) {
    int comp = ord.compare(x, val());
    if (comp == 0) {
// merge both subtrees, return new root
    }
    if (comp < 0) {
      Node<X> l = left().remove(x, ord);
      if (l == left()) return this; else return make(val(), l, right());
    } else {
      Node<X> r = right().remove(x, ord);
      if (r == right()) return this; else return make(val(), left(), r);
    }
  }

// move to utility static function and use a constructor
  default Node<X> rotateRight() {
    Node<X> y = left();
    if (y.isLeaf()) return this;
    Node<X> x = make(this.val(), y.right(), this.right());
    return make(y.val(), y.left(), x);
  }

// move to utility static funcion and use a constructor
  default Node<X> rotateLeft() {
    Node<X> x = right();
    if (x.isLeaf()) return this;
    Node<X> y = make(this.val(), this.left(), x.left());
    return make(x.val(), y, x.right());
  }

  public interface Leaf<X> extends Node<X> {
    default boolean isLeaf() { return true; }
    default Node<X> left() { throw new UnsupportedOperationException(">_<"); }
    default Node<X> right() { throw new UnsupportedOperationException(":-("); }
    default boolean has(X x, Comparator<X> ord) { return false; }
    default Node<X> insert(X x, Comparator<X> ord) { return make(x,this,this); }
    default Node<X> remove(X x, Comparator<X> ord) { return this; }
  }

}
