package org.matheclipse.generic.combinatoric;

import java.util.Iterator;
import java.util.List;

/**
 * Generate an Iterable for permutations
 *
 * See <a href=" http://en.wikipedia.org/wiki/Permutation">Permutation</a>
 */
public class KPermutationsIterable implements Iterator<int[]>, Iterable<int[]> {

	final private int n;

	final private int k;

	final private int x[];

	final private int y[];

	private boolean first;

	private int h, i, m;

	/**
	 *
	 */
	public KPermutationsIterable(final int[] data, final int parts) {
		super();
		n = data.length;
		k = parts;
		// f = fun;
		x = new int[n];
		y = new int[n];
		for (int a = 0; a < n; a++) {
			x[a] = data[a];
			y[a] = a;
		}
		if (k == n) {
			m = k - 1;
		} else {
			m = k;
		}
		first = true;
		i = m - 1;
	}

	public <T> KPermutationsIterable(final List<T> fun, final int parts, final int headOffset) {
		n = fun.size() - headOffset;
		k = parts;
	 
		x = new int[n];
		y = new int[n];
		x[0] = 0;
		y[0] = 0;
		for (int a = 1; a < n; a++) {
			if (fun.get(a + headOffset).equals(fun.get(a + headOffset - 1))) {
				x[a] = x[a - 1];
			} else {
				x[a] = a;
			}
			y[a] = a;
		}
		if (k == n) {
			m = k - 1;
		} else {
			m = k;
		}
		first = true;
		i = m - 1;
	}

	/**
	 *
	 */
	public int[] next() {
		if (first) {
			first = false;
			return x;
		}
		do {
			if (y[i] < (n - 1)) {
				y[i] = y[i] + 1;
				if (x[i] != x[y[i]]) {
					// check fixpoint
					h = x[i];
					x[i] = x[y[i]];
					x[y[i]] = h;
					i = m - 1;
					return x;
				}
				continue;
			}
			do {
				h = x[i];
				x[i] = x[y[i]];
				x[y[i]] = h;
				y[i] = y[i] - 1;
			} while (y[i] > i);
			i--;
		} while (i != -1);
		return null;
	}

	public boolean hasNext() {
		return true;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<int[]> iterator() {
		return this;
	}
}
