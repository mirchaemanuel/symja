package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

public class Level extends AbstractFunctionEvaluator {

	public Level() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() != 3) && (functionList.size() != 4)) {
			return null;
		}

		if (!functionList.get(1).isAtom()) {
			final IAST ast = (IAST) functionList.get(1);
			IAST resultList;
			if (functionList.size() != 4) {
				resultList = List();
			} else {
				resultList = F.ast(functionList.get(3));
			}
			final LevelSpec level = new LevelSpecification(functionList.get(2));
			// increment level becaus we select only subexpressions
			level.setCurrentLevel(1);
			for (int i = 1; i < ast.size(); i++) {
				AST.COPY.level(ast.get(i), level, resultList, 1);
			}
			return resultList;
		}
		return null;
	}

}
