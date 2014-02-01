package jpfds.abs;

public interface MonoidCol<M extends MonoidCol<M>> {
  M union(M that);
  M empty();
}
