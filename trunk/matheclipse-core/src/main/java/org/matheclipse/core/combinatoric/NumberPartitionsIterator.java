package org.matheclipse.core.combinatoric;


/**
 * Returns all partitions of a given int number (i.e. NumberPartitions(3) => 300
 * 210 111 )
 */
public class NumberPartitionsIterator {

	final private int n;

	final private int x[];

	private int i;

	private int k;

	/**
	 * 
	 */
	public NumberPartitionsIterator(final int num) {
		super();
		n = num;
		x = new int[num];
		for (int i = 0; i < num; i++) {
			x[i] = 0;
		}
	}

	/**
	 * 
	 */
	public int[] nextElement() {
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
				x[i] = x[i] - 1;
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

}
