package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Join extends AbstractFunctionEvaluator {

	public Join() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() < 3) {
			return null;
		}
		if (ast.args().any(AtomQ.CONST)) {
			return null;
		}
		final IAST result = F.List();
		for (int i = 1; i < ast.size(); i++) {
			final IAST subList = (IAST) ast.get(i);
			for (int j = 1; j < subList.size(); j++) {
				result.add(subList.get(j));
			}
		}
		return result;
	}
}
