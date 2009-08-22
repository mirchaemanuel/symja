package org.matheclipse.generic.interfaces;

import java.util.Iterator;

/**
 * Interface for an iterator with additional reset() method, to run the iterator
 * again
 */
public interface IIterator<E> extends Iterator<E> {
	boolean setUp();

	void tearDown();
}
