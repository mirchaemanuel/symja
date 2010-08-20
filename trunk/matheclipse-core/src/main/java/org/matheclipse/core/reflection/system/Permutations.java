package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
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
			final IAST result = F.ast(f.head());
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
				k = Validate.checkIntType(functionList, 2);
				if (k > f.size() - 1) {
					return null;
				}
			}
			final KPermutationsList<IExpr, IAST> perm = new KPermutationsList<IExpr, IAST>(f, k, F.ast(f.head()), AST.COPY, 1);
			for (IAST temp : perm) {
				result.add(temp);
			}
			return result;

		}
		return null;
	}

}
