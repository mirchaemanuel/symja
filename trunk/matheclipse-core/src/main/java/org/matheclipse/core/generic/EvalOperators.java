package org.matheclipse.core.generic;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Contains function definitions for common operators.
 * 
 */
public final class EvalOperators {
//	public static final Finding<IExpr> FIND0 = new Finding<IExpr>(0);

//	public static final Finding<IExpr> FIND1 = new Finding<IExpr>(1);

	/**
	 * The default constructor is made private because instances of this class
	 * should not be created.
	 */
	private EvalOperators() {
	}

	/**
	 * Function that corresponds to the <code>+</code> operator.
	 */
	public static final BinaryFunctorImpl<IExpr> ADD = new BinaryEval(F.Plus);

	/**
	 * Function that corresponds to the <code>*</code> operator.
	 */
	public static final BinaryFunctorImpl<IExpr> MULTIPLY = new BinaryEval(F.Times);

	/**
	 * Function that corresponds to the <code>-</code> operator.
	 */
	public static final BinaryFunctorImpl<IExpr> SUBTRACT = new BinaryEval(F.Plus) {
		public IExpr apply(IExpr p_first, IExpr p_second) {
			return super.apply(p_first, F.Times(F.CN1, p_second));
		}
	};

	/**
	 * Function that corresponds to the <code>/</code> operator.
	 */
	public static final BinaryFunctorImpl<IExpr> DIVIDE = new BinaryEval(F.Times) {
		public IExpr apply(IExpr p_first, IExpr p_second) {
			return super.apply(p_first, F.Power(p_second, F.CN1));
		}
	};

	/**
	 * Function that corresponds to the <code>&amp;&amp;</code> operator.
	 */
	public static final BinaryFunctorImpl<IExpr> AND = new BinaryEval(F.And);

	/**
	 * Function that corresponds to the <code>||</code> operator.
	 */
	public static final BinaryFunctorImpl<IExpr> OR = new BinaryEval(F.Or);

	/**
	 * Function that corresponds to the boolean 'not' operator. This is
	 * represented by * the symbol '<code>!</code>'.
	 */
	public static final UnaryFunctorImpl<IExpr> NOT = new UnaryEval(F.Not);

}
