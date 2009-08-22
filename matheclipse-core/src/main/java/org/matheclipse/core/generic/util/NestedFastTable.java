package org.matheclipse.core.generic.util;

import static org.matheclipse.basic.Util.checkCanceled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.matheclipse.core.reflection.system.Head;

/**
 * Resizable-array implementation (a wrapper for
 * <code>java.util.ArrayList</code>).<br/> Adds a special
 * <code>head</code> object to the FastTable implementation
 * 
 */ 
public class NestedFastTable<E extends IRealtimeElement> extends ArrayList<E> implements INestedRealtimeList<E> {
	/**
	 * Holds the factory for this NestedList.
	 */
//	private static final ObjectFactory<NestedFastTable> FACTORY = new ObjectFactory<NestedFastTable>() {
//		@Override
//		public NestedFastTable create() {
//			return new NestedFastTable(5, 0);
//		}
//
//		@Override
//		public void cleanup(NestedFastTable obj) {
//			obj.reset();
//		}
//	};

	private static final long serialVersionUID = -8300867641898160542L;

	protected final static boolean DEBUG_HASH = true;

	protected int fHash = 0;

	/**
	 * Creates a table of specified initial capacity and adds <code>setLength</code> <code>null</code> elements 
	 * 
	 * @param initialCapacity
	 * @param setLength
	 */
	protected NestedFastTable(final int initialCapacity, final int setLength) {
		super(initialCapacity);
		for (int i = 0; i < setLength; i++) {
			add(null);
		}
	}

	/**
	 * Creates an empty NestedList with no head symbol
	 * 
	 */
	public NestedFastTable() {
		this((E) null);
	}

	/**
	 * Constructs an empty NestedList with an initial capacity of five.
	 */
	public NestedFastTable(final E head) {
		super(5);
		add(head);
	}

	/**
	 * Constructs an empty NestedList with the specified initial capacity and head
	 * symbol <code>null</code>
	 * 
	 * @param initialCapacity
	 *          the initial capacity of the list.
	 * @exception IllegalArgumentException
	 *              if the specified initial capacity is negative
	 */
	public NestedFastTable(final int initialCapacity) {
		super(initialCapacity);
		add(null);
	}

	/**
	 * Constructs a list containing the elements of the specified collection, in
	 * the order they are returned by the collection's iterator and assigns
	 * <code>null</code> to the header .
	 * 
	 * @param c
	 *          the collection whose elements are to be placed into this list.
	 * @throws NullPointerException
	 *           if the specified collection is null.
	 */
	public NestedFastTable(final Collection<E> c) {
		super(c.size() + 1);
		add(null);
		addAll(c);
	}

	/**
	 * Constructs a list containing the elements of the specified collection, in
	 * the order they are returned by the collection's iterator and sets the given
	 * header.
	 * 
	 * @param c
	 *          the collection whose elements are to be placed into this list.
	 * @throws NullPointerException
	 *           if the specified collection is null.
	 */
	public NestedFastTable(final Collection<E> c, final E head) {
		super(c.size() + 1);
		add(head);
		addAll(c);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof NestedFastTable)) {
			return false;
		}
		if (hashCode() != o.hashCode()) {
			return false;
		}
		if (size() != ((NestedFastTable) o).size()) {
			return false;
		}
		final NestedFastTable list = ((NestedFastTable) o);
		if (!head().equals(list.head())) {
			return false;
		}
		for (int i = 1; i < size(); i++) {
			checkCanceled();
			if (!get(i).equals(list.get(i))) {
				return false;
			}
		}
		return true;
	}

	// public int hashCode() {
	// return 31 * get(0).hashCode() + size();
	// }

	@Override
	public int hashCode() {
		if (fHash == 0) {
			if (size() >= 1) {
				if (size() == 1) {
					fHash = (17 * get(0).hashCode());
				} else {
					fHash = (31 * get(0).hashCode() + get(1).hashCode() + size());
				}
			} else {
				// this case shouldn't happen
				fHash = 41;
			}
		}
		// } else if (DEBUG_HASH) {
		// int dHash = 0;
		// if (size() >= 1) {
		// if (size() == 1) {
		// dHash = 17 * get(0).hashCode();
		// } else {
		// if (get(1) instanceof NestedList) {
		// dHash = 31 * get(0).hashCode() +
		// ((NestedList)get(1)).get(0).hashCode()
		// + size();
		// } else {
		// dHash = 37 * get(0).hashCode() + get(1).hashCode() + size();
		// }
		// }
		// }
		// if (dHash != fHash) {
		// throw new RuntimeException("Different hash values in AST class");
		// }
		return fHash;
	}

	/*
	 * Recalculate the internal cached hash value; this might be necessary if the
	 * order of the elements was rearranged in a special operation (for example in
	 * a "sort operation")
	 * 
	 */
	// public void rehashCode() {
	// if (size() >= 1) {
	// if (size() == 1) {
	// fHash = 17 * get(0).hashCode();
	// } else {
	// if (get(1) instanceof NestedList) {
	// fHash = 31 * get(0).hashCode() + ((NestedList)get(1)).get(0).hashCode() +
	// size();
	// } else {
	// fHash = 37 * get(0).hashCode() + get(1).hashCode() + size();
	// }
	// }
	// }
	// }
	@Override
	public Object clone() {
		final NestedFastTable<E> v = (NestedFastTable<E>) super.clone();// FACTORY.object();
		v.addAll(this);
		return v;
	}

	// @Override
	// public boolean move(final ObjectSpace os) {
	// if (super.move(os)) {
	// for (int i = 0; i < size(); i++) {
	// get(i).move(os);
	// }
	// return true;
	// }
	// return false;
	// }

	// @Override
	// public Object clone() {
	// final NestedList<E> v = (NestedList<E>) super.clone();
	// v.fHash = 0;
	// return v;
	// }

//	public INestedRealtimeList<E> copyHead() {
//		final NestedFastTable<E> list = new NestedFastTable<E>(size());
//		list.set(0, get(0));
//		return list;
//	}

	public final E head() {
		return get(0);
	}

	public final void setHeader(final E head) {
		set(0, head);
	}

	/**
	 * Returns a String representation of this NestedList.
	 * 
	 * @return the string of this NestedList
	 */
	// @Override
	// public String toString() {
	// final E temp = getHeader();
	// final StringBuffer buf = new StringBuffer();
	// if (temp == null) {
	// buf.append("<null-tag>");
	// } else {
	// buf.append(temp.toString());
	// }
	// buf.append("[");
	// for (int i = 1; i < size(); i++) {
	// final E o = get(i);
	// buf.append(o == this ? "(this NestedList)" : String.valueOf(o));
	// if (i < size() - 1) {
	// buf.append(", ");
	// }
	// }
	// buf.append("]");
	// return buf.toString();
	// }
	
//	@Override
//	public Text toText() {
//		final Text sep = Text.valueOf(", ");
//		final E temp = getHeader();
//		Text text;
//		if (temp == null) {
//			text = Text.valueOf("<null-tag>");
//		} else {
//			text = temp.toText();
//		}
//		text = text.concat(Text.valueOf('['));
//		for (int i = 1; i < size(); i++) {
//			checkCanceled();
//			final E o = get(i);
//			text = text.concat(o == this ? Text.valueOf("(this NestedList)") : o.toText());
//			if (i < size() - 1) {
//				text = text.concat(sep);
//			}
//		}
//		return text.concat(Text.valueOf(']'));
//	}

	@Override
	public String toString() {
		final String sep = ", ";
		final E temp = head();
		StringBuffer text;
		if (temp == null) {
			text = new StringBuffer("<null-tag>");
		} else {
			text = new StringBuffer(temp.toString());
		}
		text.append('[');
		for (int i = 1; i < size(); i++) {
			checkCanceled();
			final E o = get(i);
			text = text.append(o == this ? "(this NestedList)" : o.toString());
			if (i < size() - 1) {
				text.append(sep);
			}
		}
		text.append(']');
		return text.toString();
	}

	protected static final class FastTableIterator implements ListIterator {

//		private static final ObjectFactory<FastTableIterator> FACTORY = new ObjectFactory<FastTableIterator>() {
//			protected FastTableIterator create() {
//				return new FastTableIterator();
//			}
//
//			protected void cleanup(FastTableIterator obj) {
//				FastTableIterator i = (FastTableIterator) obj;
//				i._table = null;
//			}
//		};

		private ArrayList _table;

		private int _currentIndex;

		private int _start; // Inclusive.

		private int _end; // Exclusive.

		private int _nextIndex;

		public boolean hasNext() {
			return (_nextIndex != _end);
		}

		public Object next() {
			if (_nextIndex == _end)
				throw new NoSuchElementException();
			return _table.get(_currentIndex = _nextIndex++);
		}

		public int nextIndex() {
			return _nextIndex;
		}

		public boolean hasPrevious() {
			return _nextIndex != _start;
		}

		public Object previous() {
			if (_nextIndex == _start)
				throw new NoSuchElementException();
			return _table.get(_currentIndex = --_nextIndex);
		}

		public int previousIndex() {
			return _nextIndex - 1;
		}

		public void add(Object o) {
			_table.add(_nextIndex++, o);
			_end++;
			_currentIndex = -1;
		}

		public void set(Object o) {
			if (_currentIndex >= 0) {
				_table.set(_currentIndex, o);
			} else {
				throw new IllegalStateException();
			}
		}

		public void remove() {
			if (_currentIndex >= 0) {
				_table.remove(_currentIndex);
				_end--;
				if (_currentIndex < _nextIndex) {
					_nextIndex--;
				}
				_currentIndex = -1;
			} else {
				throw new IllegalStateException();
			}
		}
	}

	/**
	 * Returns an iterator over the elements in this list starting with offset 1
	 * (allocated on the stack when executed in a
	 * 
	 * @return an iterator over this list values.
	 */
	@Override
	public Iterator<E> iterator() {
		FastTableIterator i = new FastTableIterator();//(FastTableIterator) FastTableIterator.FACTORY.object();
		i._table = this;
		i._start = 1;
		i._end = this.size();
		i._nextIndex = 1;
		i._currentIndex = 0;
		return i;
	}

	/**
	 * Returns an iterator over the elements in this list starting with offset 0
	 * (allocated on the stack when executed in a
	 * 
	 * @return an iterator over this list values.
	 */
	public Iterator<E> iterator0() {
		return super.iterator();
	}

//	@Override
//	public void reset() {
//		super.reset();
//		fHash = 0;
//	}

}
