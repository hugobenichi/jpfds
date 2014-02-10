/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

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

    Seq<Integer> is = Seq.of(1,2,3);
    //Seq<Integer> is = Seq.nil();
    is = is.cons(10).cons(20).cons(30);

    //is = xs.map((s) -> s.length()).into(is);

    is.forEach( (i) -> cout.println(i) );
    Seq<Integer> si = is.reverse();
    si.forEach( (i) -> cout.println(i) );

    cout.println(is.size());
    cout.println(si.size());

  }

  public static void main(String[] args) { example(); }
}
