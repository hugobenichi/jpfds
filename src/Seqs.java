package jpfds;

/** Work around Java interface wrapping of JRuby which does not know about
 *  static methods in Java 8 interfaces yet. */
public final class Seqs {
  private Seqs() {}
  public static <Y> Seq<Y> nil() { return Seq.nil(); }
}
