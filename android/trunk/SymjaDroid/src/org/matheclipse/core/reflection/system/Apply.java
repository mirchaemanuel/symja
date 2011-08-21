package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

public class Apply extends AbstractFunctionEvaluator {

	public Apply() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 4);

		LevelSpec level = null;
		if (ast.size() == 4) {
			level = new LevelSpecification(ast.get(3));
		} else {
			level = new LevelSpec(0);
		}

		try {
			
			if (!ast.get(2).isAtom()) {
				final IExpr result = AST.COPY.apply(ast.get(2), Functors.constant(ast.get(1)), level, 1);
				return result == null ? ast.get(2) : result;
			} else if (ast.size() == 3) {
//				if (!ast.get(1).isAtom()) {
//					IAST fun = F.ast(ast.get(1));
//					fun.add(ast.get(2));
//					return fun;
//				}
				if (ast.get(1).isFunction()) {
					IAST fun = F.ast(ast.get(1));
					fun.add(ast.get(2));
					return fun;
				}
				return ast.get(2);
			}
		} catch (final ArithmeticException e) {

		}
		return null;
	}

}
