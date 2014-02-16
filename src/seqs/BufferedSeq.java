/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import jpfds.Seq;

class BufferedSeq<X> extends BaseLazySeq<X> {
  public BufferedSeq(Seq<X> source) { super(LazySeq.NotInit, source); }
  protected void ensureInit() { if (notInit()) advance(); }
  protected void advance() {
    Object localTail = this.tail;
    if (!(localTail instanceof BufferedSeq)) {
      @SuppressWarnings("unchecked")
      Seq<X> source = (Seq<X>) localTail;
      this.head = source.head();
      this.tail = new BufferedSeq<X>(source.tail());
    }
  }

}
