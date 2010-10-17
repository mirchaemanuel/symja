package org.matheclipse.core.generic;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

import com.google.common.base.Predicate;

public class Predicates {
	private Predicates() {

	}

	/**
	 * Check if the evaluation of an unary AST object gives <code>True</code>.
	 * 
	 */
	private static class IsUnaryTrue<E extends IExpr> implements Predicate<E> {
		// UnaryEval implements IUnaryPredicate<IExpr> {
		protected final EvalEngine fEngine;

		protected final IAST fAST;

		/**
		 * Define an unary AST with the header <code>head</code>. The
		 * <code>apply()</code> method evaluates the cretaed AST with the given
		 * expression and checks if the result equals <code>True</code>.
		 * 
		 * @param head
		 *          the AST's head expresion
		 */
		public IsUnaryTrue(final IExpr head) {
			this(EvalEngine.get(), head);
		}

		/**
		 * Define an unary AST with the header <code>head</code>.
		 * 
		 * @param engine
		 *          the evaluation engine
		 * @param head
		 *          the AST's head expresion
		 */
		public IsUnaryTrue(final EvalEngine engine, final IExpr head) {
			fEngine = EvalEngine.get();
			fAST = F.ast(head, 1, false);
		}

		/**
		 * Check if the evaluation of an unary AST object gives <code>True</code>,
		 * by setting the first argument of the AST to <code>arg</code>.
		 * 
		 */
		public boolean apply(final IExpr arg) {
			final IAST ast = (IAST) fAST.clone();
			ast.add(arg); 
			if (fEngine.evaluate(ast).equals(F.True)) {
				return true;
			}
			return false;
		}

	}

	/**
	 * Check if the evaluation of an unary AST object gives <code>True</code>.
	 * 
	 * @param head
	 * @return
	 */
	public static Predicate<IExpr> isTrue(IExpr head) {
		return new IsUnaryTrue<IExpr>(head);
	}

	/**
	 * Check if the evaluation of an unary AST object gives <code>True</code>.
	 * 
	 * @param engine
	 * @param head
	 * @return
	 */
	public static Predicate<IExpr> isTrue(final EvalEngine engine, final IExpr head) {
		return new IsUnaryTrue<IExpr>(engine, head);
	}
}
