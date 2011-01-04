package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.generic.IsBinaryFalse;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.list.algorithms.EvaluationSupport;

public class Sort extends AbstractFunctionEvaluator {

	public Sort() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if ((ast.size() >= 2) && (ast.size() <= 3) && (ast.get(1) instanceof IAST)) {
			final IAST shallowCopy = ((IAST) ast.get(1)).clone();
			if (shallowCopy.size() <= 2) {
				return shallowCopy;
			}
			if (ast.size() == 2) {
				EvaluationSupport.sort(shallowCopy);
			} else if (ast.get(2) instanceof ISymbol) {
				EvaluationSupport.sort(shallowCopy, new IsBinaryFalse<IExpr>(ast.get(2)));
			}
			return shallowCopy;
		}

		return null;
	}
}
