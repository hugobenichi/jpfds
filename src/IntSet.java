package jpfds;

// move to native
// abstract has modifiable collection

public interface IntSet<S extends IntSet<S>> extends IntContainer {
  public S insert(int elem);
  public S remove(int elem);
}
