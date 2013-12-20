//package jpfds;

// move to native
// abstract has modifiable collection

public interface IntSet<Self> extends IntContainer {
  public IntSet<Self> insert(int elem);
  public IntSet<Self> remove(int elem);
  public IntSet<Self> union(IntSet<Self> that);
  public IntSet<Self> empty();
}
