package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.combinatoric.KSubsetsList;
import org.matheclipse.generic.nested.LevelSpec;

/**
 * Generate a list of all k-combinations from a given list
 * 
 * See <a href=" http://en.wikipedia.org/wiki/Combination">Combination</a>
 */
public class Subsets extends AbstractFunctionEvaluator {

	public Subsets() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {

		if ((functionList.size() >= 2) && (functionList.size() <= 3) && (functionList.get(1) instanceof IAST)) {
			final IAST f = (IAST) functionList.get(1);
			final int n = f.size() - 1;
			final LevelSpecification level;
			if (functionList.size() == 3) {
				level = new LevelSpecification(functionList.get(2));
			} else {
				level = new LevelSpecification(1, n);
			}

			// final int k = ((IInteger)
			// functionList.get(2)).getBigNumerator().intValue();
			// if (k > n) {
			// return null;
			// }
			int k;
			final IAST result = F.ast(f.head());
			level.setFromLevelAsCurrent();
			while (level.isInRange()) {
				k = level.getCurrentLevel();
				final KSubsetsList<IExpr, IAST> iter = KSubsetsList.createKSubsets(f, k, F.function(F.List), AST.COPY, 1);
				int i = 0;
				for (IAST part : iter) {
					if (part == null) {
						break;
					}
					result.add(part);
					i++;
				}
				level.incCurrentLevel();
			}
			// IAST temp;
			// final KSubsetsIterator comb = new KSubsetsIterator(n, k);
			// int j[];
			// while ((j = comb.nextElement()) != null) {
			// checkCanceled();
			// temp = F.ast(f.getHeader());
			// for (int i = 0; i < k; i++) {
			// checkCanceled();
			// temp.add(f.get(j[i]+1));
			// }
			// result.add(temp);
			//
			// Util.checkCanceled();
			// }
			return result;
		}
		return null;
	}

}
