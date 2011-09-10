package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

import com.google.common.base.Function;

/**
 */
public class MapThread extends AbstractFunctionEvaluator {

	private static class UnaryMapThread implements Function<IExpr, IExpr> {
		final IExpr fConstant;

		public UnaryMapThread(final IExpr constant) {
			fConstant = constant;
		}

		public IExpr apply(final IExpr firstArg) {
			if (firstArg.isAST()) {
				IExpr result = Thread.threadList((IAST) firstArg, F.List, fConstant, 1);
				if (result == null) {
					return firstArg;
				}
				return result;
			}
			return firstArg;
		}

	}

	public MapThread() {
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
		final IExpr result = Map.map(ast.get(2), new UnaryMapThread(ast.get(1)), level, 1);
		return result == null ? ast.get(2) : result;
	}

}
