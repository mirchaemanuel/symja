package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

import com.google.common.base.Function;

/**
 * @see Map
 */
public class Scan extends Map {

	public Scan() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 4);
		try {
			final IAST arg1 = F.ast(ast.get(1));
			if (ast.size() == 4) {
				LevelSpec level = new LevelSpecification(ast.get(3));
				IAST result = F.List();
				scan(ast.get(2), result, Functors.append(arg1), level, 1);
				for (int i = 1; i < result.size(); i++) {
					F.eval(result.get(i));
				}
			} else {
				if (ast.get(2).isAST()) {
					F.eval(((IAST) ast.get(2)).map(Functors.append(arg1)));
				} else {
					F.eval(ast.get(2));
				}
			}
			return F.Null;
		} catch (final ReturnException e) {
			return e.getValue();
			// don't catch Throw[] here !
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
	public static void scan(final IExpr expr, IAST result, final Function<IExpr, ? extends IExpr> function, final LevelSpec level,
			final int headOffset) {
		int minDepth = 0;
		level.incCurrentLevel();

		IAST ast;
		if (expr.isAST()) {
			ast = (IAST) expr;
			for (int i = headOffset; i < ast.size(); i++) {
				scan(ast.get(i), result, function, level, headOffset);
				if (level.getCurrentDepth() < minDepth) {
					minDepth = level.getCurrentDepth();
				}
			}
		}
		level.setCurrentDepth(--minDepth);
		level.decCurrentLevel();
		if (level.isInRange()) {
			IExpr temp = function.apply(expr);
			if (temp == null) {
				result.add(expr);
			} else {
				result.add(temp);
			}
		}
	}
}
