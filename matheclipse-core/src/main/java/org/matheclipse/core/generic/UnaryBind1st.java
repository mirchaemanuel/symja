package org.matheclipse.core.generic;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Clone a given AST and set the first argument of the new AST to {@code
 * firstArg} in the {@code apply} method.
 * 
 */
public class UnaryBind1st extends UnaryMap {

	/**
	 * The {@code constant} AST will be cloned in the {@code apply} method.
	 * 
	 * @param constant
	 *          a predefined &quot;template&quot; AST with all arguments set.
	 */
	public UnaryBind1st(final IAST constant) {
		super(constant);
	}

	/**
	 * Clone the given AST and set the first argument of the new AST to {@code
	 * firstArg}.
	 * 
	 * @param firstArg
	 *          the first argument in the new AST
	 */
	public IExpr apply(final IExpr firstArg) {
		final IAST ast = (IAST) fConstant.clone();
		ast.set(1, firstArg);
		return ast;
	}

}
