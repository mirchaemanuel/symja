package org.matheclipse.generic.combinatoric;

import java.util.Iterator;
import java.util.List;

import org.matheclipse.generic.nested.NestedAlgorithms;

/**
 * This class returns the indexes for partitioning a list of N elements. <br/>
 * <b>Note:</b> in contrast to <i>normal</i> Iterable behavior, the
 * {@link hasNext} method always returns true, and the {@link next} method
 * returns <code>null</code> if the iteration stops. Usage pattern:
 * 
 * <pre>
 *   ...
 * </pre>
 * 
 * Example: KPartitionsIterable(3,5) gives the following sequences [0, 1, 2],
 * [0, 1, 3], [0, 1, 4], [0, 2, 3], [0, 2, 4], [0, 3, 4] <br/> If you interpret
 * these integer lists as indexes for a list {a,b,c,d,e} which should be
 * partitioned into 3 parts the result is: <br/> {{{a},{b},{c,d,e}},
 * {{a},{b,c},{d,e}}, {{a},{b,c,d},{e}}, {{a,b},{c},{d,e}}, {{a,b},{c,d},{e}},
 * {{a,b,c},{d},{e}}}
 */
public class KPartitionsList<T, L extends List<T>> implements Iterator<L>,
		Iterable<L> {

	final private L fList;
	final private L fResultList;
	final private int fOffset;
	final private KPartitionsIterable fIterable;
	final private NestedAlgorithms<T, L> fCopier;

	public KPartitionsList(final L list, final int parts, L resultList,
			NestedAlgorithms<T, L> copier) {
		this(list, parts, resultList, copier, 0);
	}

	public KPartitionsList(final L list, final int parts, L resultList,
			NestedAlgorithms<T, L> copier, final int offset) {
		fIterable = new KPartitionsIterable(list.size() - offset, parts);
		fList = list;
		fResultList = resultList;
		fCopier = copier;
		fOffset = offset;
	}

	/**
	 * Get the index array for the next partition.
	 * 
	 * @return <code>null</code> if no further index array could be generated
	 */
	public L next() {
		int[] partitionsIndex = fIterable.next();
		if (partitionsIndex == null) {
			return null;
		}
		L part = fCopier.clone(fResultList);
		L temp;
		// System.out.println("Part:");
		int j = 0;
		for (int i = 1; i < partitionsIndex.length; i++) {
			// System.out.println(partitionsIndex[i] + ",");
			temp = fCopier.clone(fResultList);
			for (int m = j; m < partitionsIndex[i]; m++) {
				temp.add(fList.get(m + fOffset));
			}
			j = partitionsIndex[i];
			part.add(fCopier.castList(temp));
		}

		temp = fCopier.clone(fResultList);
		int n = fList.size() - fOffset;
		for (int m = j; m < n; m++) {
			temp.add(fList.get(m + fOffset));
		}
		part.add(fCopier.castList(temp));
		return part;
	}

	public boolean hasNext() {
		return fIterable.hasNext();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<L> iterator() {
		return this;
	}

}
