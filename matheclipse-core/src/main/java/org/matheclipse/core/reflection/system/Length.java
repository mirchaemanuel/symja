package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Length extends AbstractFunctionEvaluator {

	public Length() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			return null;
		}
		int result = 0;
		if (functionList.get(1) instanceof IAST) {
			result = ((IAST) functionList.get(1)).size()-1;
		}
		return F.integer(result);
	}

}
