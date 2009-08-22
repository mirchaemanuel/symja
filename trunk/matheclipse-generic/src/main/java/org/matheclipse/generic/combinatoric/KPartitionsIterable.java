package org.matheclipse.generic.combinatoric;

import java.util.Iterator;

/**
 * This class returns the indexes for partitioning a list of N elements.
 * <br/> <b>Note:</b> in contrast to <i>normal</i> Iterable behavior, the
 * {@link hasNext} method always returns true, and the {@link next} method
 * returns <code>null</code> if the iteration stops. Usage pattern:
 * 
 * <pre>
 * final KPartitionsIterable iter = new KPartitionsIterable(n, k);
 * for (int[] partitionsIndex : iter) {
 *   if (partitionsIndex==null) {
 *     break;
 *   }
 *   ...
 * </pre>
 * 
 * Example: KPartitionsIterable(3,5) gives the following sequences [0, 1,
 * 2], [0, 1, 3], [0, 1, 4], [0, 2, 3], [0, 2, 4], [0, 3, 4] <br/> If you
 * interpret these integer lists as indexes for a list {a,b,c,d,e} which should
 * be partitioned into 3 parts the result is: <br/> {{{a},{b},{c,d,e}},
 * {{a},{b,c},{d,e}}, {{a},{b,c,d},{e}}, {{a,b},{c},{d,e}}, {{a,b},{c,d},{e}},
 * {{a,b,c},{d},{e}}}
 */
public class KPartitionsIterable implements Iterator<int[]>, Iterable<int[]> {

	final private int fLength;

	final private int fNumberOfParts;

	final private int fPartitionsIndex[];

	public KPartitionsIterable(final int length, final int parts) {
		super();
		fLength = length;
		fNumberOfParts = parts;
		fPartitionsIndex = new int[fNumberOfParts];
		fPartitionsIndex[0] = -1;
	}

	public final void reset() {
		for (int i = 1; i < fNumberOfParts; i++) {
			fPartitionsIndex[i] = 0;
		}
		fPartitionsIndex[0] = -1;
	}

	/**
	 * Get the index array for the next partition.
	 * 
	 * @return <code>null</code> if no further index array could be generated
	 */
	public int[] next() {
		int i;
		if (fPartitionsIndex[0] < 0) {
			for (i = 0; i < fNumberOfParts; ++i) {
				fPartitionsIndex[i] = i;
			}
			return fPartitionsIndex;
		} else {
			i = 0;
			for (i = fNumberOfParts - 1; (i >= 0)
					&& (fPartitionsIndex[i] >= fLength - fNumberOfParts + i); --i) {
			}
			if (i <= 0) {
				return null;
			}
			fPartitionsIndex[i]++;
			for (int m = i + 1; m < fNumberOfParts; ++m) {
				fPartitionsIndex[m] = fPartitionsIndex[m - 1] + 1;
			}
			return fPartitionsIndex;
		}
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
