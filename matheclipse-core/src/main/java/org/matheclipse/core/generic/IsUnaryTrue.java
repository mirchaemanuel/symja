package org.matheclipse.core.generic;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

import com.google.common.base.Predicate;

/**
 * Check if the evaluation of an unary AST object gives <code>True</code>
 * 
 */
public class IsUnaryTrue<E extends IExpr> implements Predicate<E> {
	// UnaryEval implements IUnaryPredicate<IExpr> {
	protected final EvalEngine fEngine;

	protected final IAST fAST;

	/**
	 * Define an unary AST with the header <code>head</code>.
	 * 
	 * @param head
	 *            the AST's head expresion
	 */
	public IsUnaryTrue(final IExpr head) {
		this(EvalEngine.get(), head);
	}

	/**
	 * Define an unary AST with the header <code>head</code>.
	 * 
	 * @param engine
	 *            the evaluation engine
	 * @param head
	 *            the AST's head expresion
	 */
	public IsUnaryTrue(final EvalEngine engine, final IExpr head) {
		fEngine = EvalEngine.get();
		fAST = F.ast(head, 1, false);
	}

	/**
	 * Check if the evaluation of an unary AST object by settings it's first
	 * argument to <code>firstArg</code> gives <code>True</code>
	 * 
	 */
	public boolean apply(final IExpr firstArg) {
		final IAST ast = (IAST) fAST.clone();
		ast.add(firstArg);
		if (fEngine.evaluate(ast).equals(F.True)) {
			return true;
		}
		return false;
	}

}
