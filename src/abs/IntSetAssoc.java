package jpfds.abs;

public interface IntSetAssoc<S extends IntSetAssoc<S>> {
  S insert(int elem);
  S remove(int elem);
}
