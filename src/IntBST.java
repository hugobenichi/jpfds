//package jpfds.impl;

//import jpfds.IntSet;

public abstract class IntBST implements IntSet<IntBST>, Monoid<IntBST> {

  abstract public IntBST insert(int elem);
  abstract public IntBST remove(int elem);
  abstract public IntBST union(IntBST that);
           public IntBST empty() { return empty; }
  abstract public boolean has(int elem);
           public boolean isEmpty() { return false; }
           public boolean nonEmpty() { return !isEmpty(); }

  public static final IntBST empty = new Empty();

  private static class Empty extends IntBST {
    public IntBST insert(int elem) { return new IntBSTNode(elem); }
    public IntBST remove(int elem) { return this; }
    public IntBST union(IntBST that) { return that; }
    public boolean has(int elem) { return false; }
    public boolean isEmpty() { return true; }
    public String toString() { return "E"; }
  }

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
