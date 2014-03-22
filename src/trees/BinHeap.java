/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.trees;

import java.util.Comparator;

import jpfds.Col;
import jpfds.Heap;
import jpfds.Seq;
import jpfds.Size;

public class BinHeap<X> implements Heap<X,BinHeap<X>>, Size {
//public class BinHeap<X> implements Col<X,BinHeap<X>>, Heap<X>, Size {

  private final Comparator<X> ord;
  private final Node<X> root;
  private final int size;

  private BinHeap(Comparator<X> ord, Node<X> node, int size) {
    this.ord = ord; this.root = node; this.size = size;
  }

  public boolean isEmpty() { return size == 0; }

  public Size sizeInfo() { return this; }

  public int size() { return size; }

  public Seq<X> seq() { throw notYet; }

  public BinHeap<X> empty() { return BinHeap.empty(ord); }

  public X min() { return root.val(); }

  public BinHeap<X> add(X x) {
    int newSize = size + 1;
    Node<X> newRoot = addInLast(root, x, branchingPath(newSize), ord);
    return new BinHeap<X>(ord, newRoot, newSize);
  }

  public BinHeap<X> union(BinHeap<X> that) { return that.into(this); }

  public BinHeap<X> pop() { throw notYet; }

  public static <Y> BinHeap<Y> empty(Comparator<Y> ord) {
    return new BinHeap<Y>(ord, leaf(), 0);
  }

  private static <Y> Node<Y> leaf() { throw notYet; }

  private static int branchingPath(int size) {
    int rev = Integer.reverse(size);
    return rev >>> (1 + Integer.numberOfTrailingZeros(rev));
  }

  private static <Y> Node<Y> addInLast(Node<Y> n, Y elem, int branching,
                                       Comparator<Y> ord) {
    throw notYet;
  }

  private static final UnsupportedOperationException notYet =
    new UnsupportedOperationException("not yet implemented");
}
