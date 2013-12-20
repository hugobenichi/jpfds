//package jpfds.impl;

//import jpfds.IntSet;

public abstract class IntBST implements IntSet<IntBST> {

  abstract public IntSet<IntBST> insert(int elem);
  abstract public IntSet<IntBST> remove(int elem);
  abstract public IntSet<IntBST> union(IntSet<IntBST> that);
           public IntSet<IntBST> empty() { return empty; }
  abstract public boolean has(int elem);
           public boolean isEmpty() { return false; }
           public boolean nonEmpty() { return !isEmpty(); }

  public static final IntBST empty = new Empty();

  private static class Empty extends IntBST {
    public IntSet<IntBST> insert(int elem) { return new IntBSTNode(elem); }
    public IntSet<IntBST> remove(int elem) { return this; }
    public IntSet<IntBST> union(IntSet<IntBST> that) { return that; }
    public boolean has(int elem) { return false; }
    public boolean isEmpty() { return true; }
    public String toString() { return "E"; }
  }

  private static class IntBSTNode extends IntBST {

    public final IntSet<IntBST> left;
    public final IntSet<IntBST> right;
    public final int elem;

    public IntBSTNode(int elem) {
      this.left = empty;
      this.right = empty;
      this.elem = elem;
    }

    public IntBSTNode(int elem, IntSet<IntBST> _left, IntSet<IntBST> _right) {
      this.left = _left;
      this.right = _right;
      this.elem = elem;
    }

    public String toString() {
      return "{ " + left + " | " + elem + " | " + right + "}";
    }

    public IntSet<IntBST> insert(int elem) {
      if (this.elem == elem)
        return this;

      if (this.elem > elem)
        return copyRight(this.left.insert(elem));

      return copyLeft(this.right.insert(elem));
    }

    public IntSet<IntBST> remove(int elem) {
      if (this.elem == elem)
        return this.left.union(this.right);

      if (this.elem > elem)
        return copyRight(this.left.remove(elem));

      return copyLeft(this.right.remove(elem));
    }

    // ahhhhh I need to do self recursive typing if I use oo !
    // how to do this cleanly ?
    public IntSet<IntBST> union(IntSet<IntBST> that) {
      if (that.isEmpty())
        return this;
      else
        return this.selfUnion((IntBSTNode)that);
    }

    public IntSet<IntBST> selfUnion(IntBSTNode that) {
      if (this.elem == that.elem) {
        IntSet<IntBST> newLeft = this.left.union(that.left);
        IntSet<IntBST> newRight = this.right.union(that.right);
        return new IntBSTNode(this.elem, newLeft, newRight);
      }

      if (this.elem > that.elem)
        return new IntBSTNode(that.elem, that.left, that.right.union(this));

      return new IntBSTNode(that.elem, that.left.union(this), that.right);
    }

    public boolean has(int elem) {
      return (this.elem == elem)
          || (this.elem > elem && this.left.has(elem))
          || (this.right.has(elem));
    }

    public boolean isEmpty() { return false; }

    private IntSet<IntBST> copyLeft(IntSet<IntBST> _right) {
      return new IntBSTNode(this.elem, this.left, _right);
    }

    private IntSet<IntBST> copyRight(IntSet<IntBST> _left) {
      return new IntBSTNode(this.elem, _left, this.right);
    }

  }

  public static void main(String[] argv) {
    System.out.println("hello world");
    test();
  }

  public static void test() {

    int[] nums = {0, 4, 3, 6, 3, 4, 8, 9, 6, 5, 1, 4, 6, 2};

    IntSet<IntBST> set = IntBST.empty;

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
