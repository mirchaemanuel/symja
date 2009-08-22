package org.matheclipse.core.combinatoric;

import static org.matheclipse.basic.Util.checkCanceled;

import java.util.List;

/**
 *
 */
public class KPermutationsIterator {

	final private int n;

	final private int k;

	final private int x[];

	final private int y[];

	private boolean first;

	private int h, i, m;
	
	/**
	 *
	 */
	public KPermutationsIterator(final int[] data, final int parts) {
		super();
		n = data.length;
		k = parts;
		// f = fun;
		x = new int[n];
		y = new int[n];
		for (int a = 0; a < n; a++) {
			checkCanceled();
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

	public KPermutationsIterator(final List fun, final int parts, final int headOffset) {
		n = fun.size() - headOffset;
		k = parts;
		// f = fun;
		x = new int[n];
		y = new int[n];
		x[0] = 0;
		y[0] = 0;
		for (int a = 1; a < n; a++) {
			checkCanceled();
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
	public int[] nextElement() {
		if (first) {
			first = false;
			return x;
		}
		do {
			checkCanceled();
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
				checkCanceled();
				h = x[i];
				x[i] = x[y[i]];
				x[y[i]] = h;
				y[i] = y[i] - 1;
			} while (y[i] > i);
			i--;
		} while (i != -1);
		return null;
	}

}
