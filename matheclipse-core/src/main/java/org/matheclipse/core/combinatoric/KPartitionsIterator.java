package org.matheclipse.core.combinatoric;

import static org.matheclipse.basic.Util.checkCanceled;

/**
 *
 */
public class KPartitionsIterator { 

	final private int fLength;

	final private int fNumberOfParts;

	final private int fPartitionsIndex[];

	public KPartitionsIterator(final int length, final int parts) {
		super();
		fLength = length;
		fNumberOfParts = parts;
		fPartitionsIndex = new int[fNumberOfParts];
		fPartitionsIndex[0] = -1;
	}

	public final void reset() {
		for (int i = 1; i < fNumberOfParts; i++) {
			checkCanceled();
			fPartitionsIndex[i] = 0;
		}
		fPartitionsIndex[0] = -1;
	}

	public int[] nextElement() {
		int i;
		if (fPartitionsIndex[0] < 0) {
			for (i = 0; i < fNumberOfParts; ++i) {
				checkCanceled();
				fPartitionsIndex[i] = i;
			}

			return fPartitionsIndex;
		} else {

			for (i = fNumberOfParts - 1; (i >= 0) && (fPartitionsIndex[i] >= fLength - fNumberOfParts + i); --i) {
				checkCanceled();
			}

			if (i >= 0) {
				// --- begin: insert
				if (i == 0) {
					return null;
				}
				// --- end
				fPartitionsIndex[i]++;

				for (int m = i + 1; m < fNumberOfParts; ++m) {
					checkCanceled();
					fPartitionsIndex[m] = fPartitionsIndex[m - 1] + 1;
				}

				return fPartitionsIndex;
			} else {
				return null;
			}
		}
	}

}
