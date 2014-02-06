package jpfds.examples;

import jpfds.*;

public final class Seqs {
  private Seqs() { }

  private static final java.io.PrintStream cout = System.out;

  public static void example() {

    Seq<String> xs = Seq.nil();

    cout.println(xs.isEmpty());

    xs = xs.cons("foo").cons("bar").cons("paris").cons("tokyo").cons("java");
    cout.println(xs.isEmpty());

    xs.forEach( (s) -> cout.println(s) );

    int total = xs.map((s) -> s.length()).reduce(0, (t, s) -> t+s);
    cout.println(total);

    //Seq<Integer> is = Seq.of(1,2,3); // builder is broken
    Seq<Integer> is = Seq.nil();
    is = is.cons(1).cons(2).cons(3);

    is = xs.map((s) -> s.length()).into(is);

    is.forEach( (i) -> cout.println(i) );

  }

  public static void main(String[] args) { example(); }
}
