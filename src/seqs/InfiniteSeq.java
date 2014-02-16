/*----------------------------------------------------------------------------*
 *              Copyright (c) Hugo Benichi. All right reserved.               *
 *----------------------------------------------------------------------------*/

package jpfds.seqs;

import java.util.function.Supplier;

import jpfds.Size;

class InfiniteSeq<X> extends BaseLazySeq<X> {
  public InfiniteSeq(Supplier<X> source) { super(NotInit, source); }
  public Size sizeInfo() { return Size.infinite; }
  protected void advance() {
    @SuppressWarnings("unchecked")
    Supplier<X> source = (Supplier<X>) this.tail;
    setTo(source.get(), new InfiniteSeq<X>(source));
  }
}
