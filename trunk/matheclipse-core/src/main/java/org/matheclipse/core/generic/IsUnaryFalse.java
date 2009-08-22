package org.matheclipse.core.generic;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Check if the evaluation of an unary AST object gives <code>False</code>
 *
 */
public class IsUnaryFalse<E extends IExpr> extends IsUnaryTrue<E> {
	/**
	 * Define an unary AST with the header <code>head</code>.
	 *
	 * @param head the AST's head expresion
	 */
	public IsUnaryFalse(final IExpr head) {
		super(head);
	}

	/**
	 * Check if the evaluation of an unary AST object by settings it's frist argument
	 * to <code>firstArg</code> gives  <code>False</code>
	 *
	 */
	@Override
	public boolean apply(final IExpr firstArg) {
		final IAST ast = (IAST) fAST.clone();
		ast.add(firstArg);
		if (fEngine.evaluate(ast).equals(F.False)) {
			return true;
		}
		return false;
	}

}
