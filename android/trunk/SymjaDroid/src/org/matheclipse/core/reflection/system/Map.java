package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

import com.google.common.base.Function;

/**
 * 
 * @see Scan
 */
public class Map extends AbstractFunctionEvaluator {

	public Map() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 4);

		final IAST arg1 = F.ast(ast.get(1));
		if (ast.size() == 4) {
			LevelSpec level = new LevelSpecification(ast.get(3));
			final IExpr result = map(ast.get(2), Functors.append(arg1), level, 1);
			return result == null ? ast.get(2) : result;
		} else {
			if (ast.get(2).isAST()) {
				return ((IAST) ast.get(2)).map(Functors.append(arg1));
			}
			return ast.get(2);
		}
	}

	/**
	 * Maps a function to the parameters of a nested list, by applying in turn
	 * each element of the list as a parameter to the function and calling it, and
	 * returning a list of the results.
	 * 
	 * @param expr
	 * @param function
	 * @param level
	 * @param headOffset
	 * @return
	 */
	public static IExpr map(final IExpr expr, final Function<IExpr, ? extends IExpr> function, final LevelSpec level, final int headOffset) {
		IAST result = null;
		int minDepth = 0;
		level.incCurrentLevel();

		IExpr temp;
		IAST ast;
		if (expr.isAST()) {
			ast = (IAST) expr;
			for (int i = headOffset; i < ast.size(); i++) {

				temp = map(ast.get(i), function, level, headOffset);
				if (temp != null) {
					if (result == null) {
						result = ast.clone();
					}
					result.set(i, temp);
				}
				if (level.getCurrentDepth() < minDepth) {
					minDepth = level.getCurrentDepth();
				}
			}
		}
		level.setCurrentDepth(--minDepth);
		level.decCurrentLevel();
		if (level.isInRange()) {
			if (result == null) {
				return function.apply(expr);
			}
			return function.apply(result);
		}
		return result;
	}
}
