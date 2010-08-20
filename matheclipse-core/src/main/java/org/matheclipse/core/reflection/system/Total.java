package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.generic.UnaryCollect;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

public class Total extends AbstractFunctionEvaluator {

	public Total() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() != 2) && (functionList.size() != 3)) {
			return null;
		}

		if (!functionList.get(1).isAtom()) {
			final IAST ast = (IAST) functionList.get(1);
			final UnaryCollect uCollect = new UnaryCollect(F.Plus);
			if (functionList.size() == 2) {
				for (int i = 1; i < ast.size(); i++) {
					uCollect.apply(ast.get(i));
				}
			} else {

				final LevelSpec level = new LevelSpecification(functionList.get(2));
				// increment level because we select only subexpressions
				level.setCurrentLevel(1);
				for (int i = 1; i < ast.size(); i++) {
					AST.COPY.total(ast.get(i), level, uCollect, 1);
				}
			}
			return uCollect.getCollectedAST();
		}
		return null;
	}

}
