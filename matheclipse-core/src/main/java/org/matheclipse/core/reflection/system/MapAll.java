package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
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
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3);

		final LevelSpec level = new LevelSpec(0, Integer.MAX_VALUE);
		final IAST arg1AST = F.ast(ast.get(1));
		final IExpr result = Map.map(ast.get(2), Functors.append(arg1AST),
				level, 1);
		return result == null ? ast.get(2) : result;
	}

}
