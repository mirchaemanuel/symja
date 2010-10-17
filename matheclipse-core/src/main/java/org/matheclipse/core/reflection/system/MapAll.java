package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

/**
 */
public class MapAll extends AbstractFunctionEvaluator {

	public MapAll() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 3) {
			return null;
		}
		final LevelSpec level = new LevelSpec(0, Integer.MAX_VALUE);
		final IAST ast = F.ast(functionList.get(1));
		final IExpr result = (IExpr) AST.COPY.map(functionList.get(2), Functors.append(ast), level, 1);
		return result == null ? functionList.get(2) : result;
	}

}
