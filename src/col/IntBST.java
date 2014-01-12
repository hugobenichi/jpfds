package jpfds.col;

import jpfds.abs.*;

public abstract class IntBST
    implements Container, IntSet, IntSetAssoc<IntBST>, SetAlgebra<IntBST> {

  /* IntSet */
  abstract public boolean has(int elem);

  /* IntSetAssoc */
  abstract public IntBST insert(int elem);
  abstract public IntBST remove(int elem);

  /* IntSetAlgebra */
           public IntBST empty() { return empty; }
  abstract public IntBST union(IntBST that);
  abstract public IntBST inter(IntBST that);
  abstract public IntBST excl(IntBST that);
           public IntBST conj(IntBST total) { return total.excl(this); }

  /* Container */
           public boolean isEmpty() { return false; }
           public boolean nonEmpty() { return !isEmpty(); }

  public static final IntBST empty = new Empty();

  /** Empty Tree class which populates the leaves of the binary tree set.
   *  This clas should be used as a singleton and instantiated exactly once. */
  private static class Empty extends IntBST {
    public IntBST insert(int elem) { return new IntBSTNode(elem); }
    public IntBST remove(int elem) { return this; }
    public IntBST union(IntBST that) { return that; }
    public IntBST inter(IntBST that) { return this; }
    public IntBST excl(IntBST that) { return this; }
    public boolean has(int elem) { return false; }
    public boolean isEmpty() { return true; }
    public String toString() { return "E"; }
  }

  /** Node class which populates the internal nodes of the binary tree set. */
  private static class IntBSTNode extends IntBST {

    public final IntBST left;
    public final IntBST right;
    public final int elem;

    public IntBSTNode(int elem) {
      this.left = empty;
      this.right = empty;
      this.elem = elem;
    }

    public IntBSTNode(int elem, IntBST _left, IntBST _right) {
      this.left = _left;
      this.right = _right;
      this.elem = elem;
    }

    public String toString() { return "{ "+left+" | "+elem+" | "+right+" }"; }

    public boolean isEmpty() { return false; }

    public boolean has(int elem) {
      return (this.elem == elem)
          || (this.elem > elem && this.left.has(elem))
          || (this.right.has(elem));
    }

    public IntBST insert(int elem) {
      if (this.elem == elem)
        return this;

      if (this.elem > elem)
        return copyRight(this.left.insert(elem));

      return copyLeft(this.right.insert(elem));
    }

    public IntBST remove(int elem) {
      if (this.elem == elem)
        return this.left.union(this.right);

      if (this.elem > elem)
        return copyRight(this.left.remove(elem));

      return copyLeft(this.right.remove(elem));
    }

    public IntBST union(IntBST that) {
      if (that.isEmpty())
        return this;
      else
        return this.selfUnion((IntBSTNode)that);
    }

    private IntBST selfUnion(IntBSTNode that) {
      if (this.elem == that.elem) {
        IntBST newLeft = this.left.union(that.left);
        IntBST newRight = this.right.union(that.right);
        return new IntBSTNode(this.elem, newLeft, newRight);
      }

      if (this.elem > that.elem)
        return new IntBSTNode(that.elem, that.left, that.right.union(this));

      return new IntBSTNode(that.elem, that.left.union(this), that.right);
    }

    public IntBST inter(IntBST that) {
      if (that.isEmpty())
        return empty;
      else
        /* copy self with query of elements in other set. Can only remove
           elements, therefore should be straightforward. */
        return this;
    }

    public IntBST excl(IntBST that) {
      if (that.isEmpty())
        return this;
      else
        /* cf above comments. */
        return this;
    }

    private IntBST copyLeft(IntBST _right) {
      return new IntBSTNode(this.elem, this.left, _right);
    }

    private IntBST copyRight(IntBST _left) {
      return new IntBSTNode(this.elem, _left, this.right);
    }

  }

  public static void main(String[] argv) {
    System.out.println("hello world");
    test();
  }

  public static void test() {

    int[] nums = {0, 4, 3, 6, 3, 4, 8, 9, 6, 5, 1, 4, 6, 2};

    IntBST set = IntBST.empty;

    for (int i : nums) {
      set = set.insert(i);
      System.out.println(set);
      if (set.isEmpty()) throw new IllegalStateException();
    }

    for (int i : nums) {
      System.out.println("checking " + i);
      if (!set.has(i)) throw new IllegalStateException();
    }

    for (int i : nums) {
      set = set.remove(i);
      System.out.println(set);
    }

    for (int i : nums) {
      System.out.println("checking " + i);
      if (set.has(i)) throw new IllegalStateException();
    }

  }

}
