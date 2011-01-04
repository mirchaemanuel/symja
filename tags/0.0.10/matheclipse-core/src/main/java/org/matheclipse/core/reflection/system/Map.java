package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

/**
 */
public class Map extends AbstractFunctionEvaluator {

	public Map() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if ((ast.size() != 3) && (ast.size() != 4)) {
			return null;
		}
		LevelSpec level = null;
		final IAST arg1 = F.ast(ast.get(1));
		if (ast.size() == 4) {
			level = new LevelSpecification(ast.get(3));
			final IExpr result = AST.COPY.map(ast.get(2), Functors.append(arg1), level, 1);
			return result == null ? ast.get(2) : result;
		} else {
			level = new LevelSpec(1);
			if (ast.get(2).isAST()) {
				return ((IAST) ast.get(2)).map(Functors.append(arg1));
			}
			return ast.get(2);
		}
	}

}
