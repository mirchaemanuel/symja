package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.generic.UnaryConstant;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

public class Apply extends AbstractFunctionEvaluator {

	public Apply() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() != 3) && (functionList.size() != 4)) {
			return null;
		}
		LevelSpec level = null;
		if (functionList.size() == 4) {
			level = new LevelSpecification(functionList.get(3));
		} else {
			level = new LevelSpec(0);
		}

		try {
			if (!functionList.get(2).isAtom()) {
				final IExpr result = (IExpr) AST.COPY.apply(functionList.get(2), new UnaryConstant(functionList.get(1)), level, 1);
				return result == null ? functionList.get(2) : result;
			} else if (functionList.size() == 3) {
				IAST fun = F.ast(functionList.get(1));
				fun.add(functionList.get(2));
				return fun;
			}
		} catch (final ArithmeticException e) {

		}
		return null;
	}

}
