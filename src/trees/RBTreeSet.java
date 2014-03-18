/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

import jpfds.Seq;
import jpfds.Seqs;
import jpfds.Set;
import jpfds.Size;

public class RBTreeSet<X> implements Set<X>, Size.Constant {

  private final Node<X> root;
  private final Comparator<X> ord;
  private final int size;

  private RBTreeSet(Comparator<X> ord, Node<X> node, int size) {
    this.ord = ord; this.root = node; this.size = size;
  }

  public Size sizeInfo() { return this; }

  public int size() { return size; }

  // assert root.isLeaf() == true if empty
  public boolean isEmpty() { return size == 0; }

  // back tracking iterator ! deleguate to node class
  public Seq<X> seq() { return Seqs.nil(); }

  public RBTreeSet<X> empty() { return empty(ord); }

  public boolean has(X x) { return root.has(x, ord); }

  public RBTreeSet<X> add(X elem) {
    Node<X> newRoot = root.insert(elem, ord);
    return newRoot == root ? this : new RBTreeSet<X>(ord, newRoot, size+1);
  }

  public RBTreeSet<X> remove(X elem) {
    Node<X> newRoot = root.remove(elem, ord);
    return newRoot == root ? this : new RBTreeSet<X>(ord, newRoot, size-1);
  }

  public static <Y> RBTreeSet<Y> empty(Comparator<Y> ord) {
    return new RBTreeSet<Y>(ord, leaf(), 0);
  }

  // need a concrete class
  public static <Y> Node<Y> leaf() { return null; }

}
