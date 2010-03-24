package org.matheclipse.generic.combinatoric;

import java.util.Iterator;

/**
 * Returns all partitions of a given int number (i.e. NumberPartitions(3) =>
 * [3,0,0] [2,1,0] [1,1,1] ).
 * 
 * See <a href="http://en.wikipedia.org/wiki/Integer_partition">Wikipedia -
 * Integer partition</a>
 */
public class NumberPartitionsIterable implements Iterator<int[]>, Iterable<int[]> {

	final private int n;

	final private int len;

	final private int x[];

	private int i;

	private int k;

	/**
	 * 
	 */
	public NumberPartitionsIterable(final int num) {
		this(num, num);
	}

	public NumberPartitionsIterable(final int num, final int l) {
		super();
		n = num;
		len = l;
		int size = n;
		if (len > n) {
			size = len;
		}
		x = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = 0;
		}
	}

	/**
	 * 
	 */
	public int[] next() {
		int l;
		int k1;
		if (i == -1) {
			return null;
		}
		if (x[0] != 0) {
			k1 = k;
			while (x[k1] == 1) {
				x[k1--] = 0;
			}
			while (true) {
				l = k - i;
				k = i;
				// if (i != len - 1) {
				x[i] -= 1;
				// }
				while (x[k] <= l) {
					l = l - x[k++];
					x[k] = x[k - 1];
				}
				if (k != n - 1) {
					x[++k] = l + 1;
					if (x[i] != 1) {
						i = k;
					}
					if (x[i] == 1) {
						i--;
					}

				} else {
					k++;
					// if (i == len - 1) {
					// l += 1;
					// i--;
					// continue;
					// }
					if (x[i] != 1) {
						i = k;
					}
					if (x[i] == 1) {
						i--;
					}
					continue;
				}

				return x;
			}
		} else {
			x[0] = n;

			k = 0;
			i = 0;
		}
		return x;
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
