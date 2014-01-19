package jpfds.abs;

public interface Container {
  boolean isEmpty();
  default boolean nonEmpty() { return !isEmpty(); }
}
