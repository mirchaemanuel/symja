package org.matheclipse.core.generic;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Retun the evaluation of an unary AST object
 * 
 */
public class UnaryCollect extends UnaryFunctorImpl<IExpr> {
	protected final EvalEngine fEngine;

	protected IAST fAST;

	/**
	 * Define an unary AST with the header <code>head</code>.
	 * 
	 * @param head
	 *          the AST's head expresion
	 */
	public UnaryCollect(final IExpr head) {
		fEngine = EvalEngine.get();
		fAST = F.ast(head, 1, false);
	}

	/**
	 * Add <code>firstArg</code> to this collection
	 * 
	 */
	public IExpr apply(final IExpr firstArg) {
		fAST.add(firstArg);
		return fAST;
	}

	public IAST getAST() {
		return fAST;
	}

}
