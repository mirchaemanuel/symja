package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Length extends AbstractFunctionEvaluator {

	public Length() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2) {
			return null;
		}
		int result = 0;
		if (ast.get(1) instanceof IAST) {
			result = ((IAST) ast.get(1)).size()-1;
		}
		return F.integer(result);
	}

}
