package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * 
 */
public class Divergence extends AbstractFunctionEvaluator {
	public Divergence() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if ((ast.size() == 3)
				&& (ast.get(1).isVector() == ast.get(2).isVector())
				&& (ast.get(1).isVector() >= 0)) {
			IAST vector = (IAST) ast.get(1);
			IAST variables = (IAST) ast.get(2);
			IAST divergenceValue = F.Plus();
			for (int i = 1; i < vector.size(); i++) {
				divergenceValue.add(F.D(vector.get(i), variables.get(i)));
			}
			return divergenceValue;
		}

		return null;
	}

}
