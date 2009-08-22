package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.generic.combinatoric.KPermutationsList;

/**
 * Generate a list of permutations
 * 
 * See <a href=" http://en.wikipedia.org/wiki/Permutation">Permutation</a>
 */
public class Permutations extends AbstractFunctionEvaluator {

	public Permutations() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() >= 2) && (functionList.size() <= 3) && (functionList.get(1) instanceof IAST)) {
			final IAST f = (IAST) functionList.get(1);
			final IAST result = F.ast(f.getHeader());
			if (f.size() <= 2) {
				if (f.size() == 2) {
					result.add(f);
				}
				return result;
			}
			int k = f.size() - 1;
			if (functionList.size() == 3) {
				if (!(functionList.get(2) instanceof IInteger)) {
					return null;
				}
				k = ((IInteger) functionList.get(2)).getBigNumerator().intValue();
				if (k > f.size() - 1) {
					return null;
				}
			}
			final KPermutationsList<IExpr, IAST> perm = new KPermutationsList<IExpr, IAST>(f, k, F.ast(f.getHeader()), AST.COPY, 1);
			// int j[];
			IAST temp;
			while ((temp = perm.next()) != null) {
				checkCanceled();
				// temp = F.ast(f.getHeader());
				// for (int i = 0; i < k; i++) {
				// checkCanceled();
				// temp.add(f.get(j[i]+1));
				// }
				result.add(temp);
			}
			return result;
			// final KPermutationsIterator perm = new KPermutationsIterator(f, k, 1);
			// // while (perm.hasNext()) {
			// int j[];
			// IAST temp;
			// while ((j = perm.nextElement()) != null) {
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
			// return result;
		}
		return null;
	}

}
