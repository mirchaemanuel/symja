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
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() >= 2) && (functionList.size() <= 3) && (functionList.get(1) instanceof IAST)) {
			final IAST shallowCopy = (IAST) ((IAST) functionList.get(1)).clone();
			if (shallowCopy.size() <= 2) {
				return shallowCopy;
			}
			if (functionList.size() == 2) {
				EvaluationSupport.sort(shallowCopy);
			} else if (functionList.get(2) instanceof ISymbol) {
				EvaluationSupport.sort(shallowCopy, new IsBinaryFalse<IExpr>(functionList.get(2)));
			}
			return shallowCopy;
		}

		return null;
	}
}
