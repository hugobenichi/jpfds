/**
 * The jpfds package is a collection of immutable and functional data structure
 * abstractions and implementations for the Java language. These data structures
 * are trully immutable in use, and do not use locking or any other form of
 * synchronization. They are therefore safe to share and can be persisted
 * between several producers and consumers in multi-threaded contexts. Efficient
 * update operations are supported for creating new immutable collections from
 * existing ones. Mutable builders are also provided for optimized transient
 * operations. Lazy bulk operations are exposed through the Reducible interface.
 */
package jpfds;
