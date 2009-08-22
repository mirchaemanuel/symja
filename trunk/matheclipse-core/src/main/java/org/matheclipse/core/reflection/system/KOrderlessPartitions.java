package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.basic.Util;
import org.matheclipse.core.combinatoric.KPartitionsIterator;
import org.matheclipse.core.combinatoric.KPermutationsIterator;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class KOrderlessPartitions extends AbstractFunctionEvaluator {

	public KOrderlessPartitions() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST) && (functionList.get(2) instanceof IInteger)) {
			final IAST listArg0 = (IAST) functionList.get(1);
			final ISymbol sym = listArg0.topHead();
			final int n = listArg0.size() - 1;
			final int k = ((IInteger) functionList.get(2)).getBigNumerator().intValue();
			final IAST result = F.function(F.List);
			final KPermutationsIterator permutationIterator = new KPermutationsIterator(listArg0, n, 1);
			final KPartitionsIterator partitionIterator = new KPartitionsIterator(n, k);
			int permutationsIndex[];
			int partitionsIndex[];
			IAST partition;

			// first generate all permutations:
			while ((permutationsIndex = permutationIterator.nextElement()) != null) {
				checkCanceled();
				// second generate all partitions:
				while ((partitionsIndex = partitionIterator.nextElement()) != null) {
					checkCanceled();
					partition = createSinglePartition(listArg0, sym, permutationsIndex, partitionsIndex);
					if (partition != null) {
						result.add(partition);
					}

					Util.checkCanceled();
				}
				partitionIterator.reset();
			}
			return result;
		}
		return null;
	}

	private IAST createSinglePartition(final IAST listArg0, final ISymbol sym, final int[] permutationsIndex,
			final int[] partitionsIndex) {
		IAST partition;
		IAST partitionElement;
		int partitionStartIndex;
		partition = F.function(F.List);

		final int n = listArg0.size() - 1;
		// 0 is always the first index of a partition
		partitionStartIndex = 0;
		for (int i = 1; i < partitionsIndex.length; i++) {
			checkCanceled();
			// System.out.println(partitionsIndex[i] + ",");
			partitionElement = F.function(sym);
			if (partitionStartIndex + 1 == partitionsIndex[i]) {
				// OneIdentity check here
				if ((sym.getAttributes() & ISymbol.ONEIDENTITY) == ISymbol.ONEIDENTITY) {
					partition.add(listArg0.get(permutationsIndex[partitionStartIndex]+1));
				} else {
					partitionElement.add(listArg0.get(permutationsIndex[partitionStartIndex]+1));
					partition.add(partitionElement);
				}
			} else {
				for (int m = partitionStartIndex; m < partitionsIndex[i]; m++) {
					checkCanceled();
					if (m + 1 < partitionsIndex[i]) {
						if ((listArg0.get(permutationsIndex[m + 1]+1)).isLTOrdered(listArg0.get(permutationsIndex[m]+1))) {
							return null;
						}
					}
					partitionElement.add(listArg0.get(permutationsIndex[m]+1));
				}
				partition.add(partitionElement);
			}
			partitionStartIndex = partitionsIndex[i];

		}
		// generate all elements for the last partitionElement of a partition:
		partitionElement = F.function(sym);
		if (partitionStartIndex + 1 == n) {
			// OneIdentity check here
			if ((sym.getAttributes() & ISymbol.ONEIDENTITY) == ISymbol.ONEIDENTITY) {
				partition.add(listArg0.get(permutationsIndex[partitionStartIndex]+1));
			} else {
				partitionElement.add(listArg0.get(permutationsIndex[partitionStartIndex]+1));
				partition.add(partitionElement);
			}
		} else {
			for (int m = partitionStartIndex; m < n; m++) {
				checkCanceled();
				if (m + 1 < n) {
					if ((listArg0.get(permutationsIndex[m + 1]+1)).isLTOrdered(listArg0.get(permutationsIndex[m]+1))) {
						return null;
					}
				}
				partitionElement.add(listArg0.get(permutationsIndex[m]+1));
			}
			partition.add(partitionElement);
		}

		return partition;
	}

}
