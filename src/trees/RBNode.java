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

  // make new Node with same val and same color
  RBNode<X> make(Node<X> left, Node<X> right);

  // make a new Red node with leaf children
  RBNode<X> init(X val);

  // turn this node into a Black node
  RBNode<X> toBlack();

  default RBNode<X> insert(X x, Comparator<X> ord) {
    int comp = ord.compare(x, val());
    if (comp == 0) return this;
    if (comp < 0) return insertLeft(x, ord); else return insertRight(x, ord);
  }

  default RBNode<X> insertLeft(X x, Comparator<X> ord) {
    Node<X> l = left().insert(x, ord);
    if (l == left())
      return this;
    else
      // make will return a new node with same color, but the new left child
      // could violate the RB invariant. We ensure the invariant is preserved.
      return make(l, right()).ensureBalanceAfterLeftInsert();
  }

  default RBNode<X> insertRight(X x, Comparator<X> ord) {
    Node<X> r = right().insert(x, ord);
    if (r == right())
      return this;
    else
      return make(left(), r).ensureBalanceAfterRightInsert();
  }

  // we now that this node and this node right child have the same colors, so
  // we only need to probe on the left to see if the RB tree invariant holds.
  // Since we did an insert on the left, we are guaranteed that the left node
  // is not a leaf.
  //
  // if we are red, we propagate up one level and let the
  // caller restore balance because we would not be able to
  // do the correct tree rotation here.
  //
  // So now we know we are black. If we detect two reds, we restore balance
  // there are two violation patterns which requires two different tree
  // rotation sequences
  default RBNode<X> ensureBalanceAfterLeftInsert() {
    RBNode<X> l = left();
    if (isBlack() && l.isRed()) {
      RBNode<X> ll = l.left();
      RBNode<X> lr = l.right();
      if (ll.isRed()) {
        // simple tree rotation + coloring
        RBNode<X> newL = ll.toBlack();
        RBNode<X> newR = make(lr, right());
        return l.make(newL, newR);
      }
      if (lr.isRed()) {
        // we know that l.right() is not a leaf because it s red
        RBNode<X> newL = l.make(ll, lr.left()).toBlack();
        RBNode<X> newR = make(lr.right(), right());
        return lr.make(newL, newR);
      }
    }
    return this;
  }

  default RBNode<X> ensureBalanceAfterRightInsert() {
    RBNode<X> r = right();
    if (isBlack() && r.isRed()) {
      RBNode<X> rr = r.right();
      RBNode<X> rl = r.left();
      if (rr.isRed()) {
        RBNode<X> newL = make(left(), rl);
        RBNode<X> newR = rr.toBlack();
        return r.make(newL, newR);
      }
      if (rl.isRed()) {
        RBNode<X> newL = make(left(), rl.left());
        RBNode<X> newR = r.make(rl.right(), r.right()).toBlack();
        return right().make(newL, newR);
      }
    }
    return this;
  }

  default RBNode<X> remove(X x, Comparator<X> ord) { return this; }

  public interface Leaf<X> extends RBNode<X> {
    default RBNode<X> toBlack() { return this; }
    default boolean isBlack() { return true; }
    default boolean isLeaf() { return true; }
    default X val() { throw new UnsupportedOperationException("x_x"); }
    default RBNode<X> left() { throw new UnsupportedOperationException(">_<"); }
    default RBNode<X> right() { throw new UnsupportedOperationException(":-("); }
    default RBNode<X> insert(X x, Comparator<X> ord) { return init(x); }
    default RBNode<X> remove(X x, Comparator<X> ord) { return this; }
    default RBNode<X> make(RBNode<X> left, RBNode<X> right) {
      throw new UnsupportedOperationException(">_<");
    }
  }
}
