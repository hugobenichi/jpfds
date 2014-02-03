package jpfds.abs;

public interface Builder<X,B extends Builder<X,B,C>,C extends Col<X,C>>
    extends Col<X,B> {
  C make();
}
