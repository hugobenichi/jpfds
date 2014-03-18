/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

import jpfds.Seq;
import jpfds.Seqs;
import jpfds.Set;
import jpfds.Size;

public class TreeSet<X> implements Set<X>, Size.Constant {

  private final Node<X> root;
  private final Comparator<X> ord;
  private final int size;

  private TreeSet(Comparator<X> ord, Node<X> node, int size) {
    this.ord = ord; this.root = node; this.size = size;
  }

  public Size sizeInfo() { return this; }

  public int size() { return size; }

  // assert root.isLeaf() == true if empty
  public boolean isEmpty() { return size == 0; }

  // back tracking iterator ! deleguate to node class
  public Seq<X> seq() { return Seqs.nil(); }

  public TreeSet<X> empty() { return empty(ord); }

  public boolean has(X x) { return Nodes.has(root, x, ord); }

  public TreeSet<X> add(X elem) {
    Node<X> newRoot = root.insert(elem, ord);
    return newRoot == root ? this : new TreeSet<X>(ord, newRoot, size+1);
  }

  public TreeSet<X> remove(X elem) {
    Node<X> newRoot = root.remove(elem, ord);
    return newRoot == root ? this : new TreeSet<X>(ord, newRoot, size-1);
  }

  public static <Y> TreeSet<Y> empty(Comparator<Y> ord) {
    return new TreeSet<Y>(ord, leaf(), 0);
  }

  public static <Y> Node<Y> leaf() { return new TreeLeaf<Y>(); }

  private static class TreeNode<X> implements Node<X> {
    private final X val;
    private final Node<X> left;
    private final Node<X> right;
    public TreeNode(X val, Node<X> l, Node<X> r) {
      this.val = val; this.left = l; this.right = r;
    }
    public X val() { return val; }
    public Node<X> left() { return left; }
    public Node<X> right() { return right; }
    public Node<X> make(Node<X> l, Node<X> r) {
      return new TreeNode<X>(val,l,r);
    }
    public Node<X> init(X val) {
      return new TreeNode<X>(val, new TreeLeaf<X>(), new TreeLeaf<X>());
    }
  }

  // share this as a singleton
  private static class TreeLeaf<X> implements Node.Leaf<X> {
    public Node<X> init(X val) {
      return new TreeNode<X>(val, new TreeLeaf<X>(), new TreeLeaf<X>());
    }
  }

}
