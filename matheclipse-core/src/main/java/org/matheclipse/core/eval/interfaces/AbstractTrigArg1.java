package org.matheclipse.core.eval.interfaces;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;

/**
 * Base class for functions with 1 argument (i.e. Sin, Cos...) with Attributes
 * <i>Listable</i> and <i>NumericFunction</i>
 * 
 */
public abstract class AbstractTrigArg1 extends AbstractFunctionEvaluator {

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 2);

		return evaluateArg1(ast.get(1));
	}

	public IExpr evaluateArg1(final IExpr arg1) {
		return null;
	}

	@Override
	public IExpr numericEval(final IAST functionList) {
		Validate.checkSize(functionList, 2);
		if (functionList.get(1) instanceof Num) {
			return numericEvalD1((Num) functionList.get(1));
		}
		if (functionList.get(1) instanceof ComplexNum) {
			return numericEvalDC1((ComplexNum) functionList.get(1));
		}
		return numericEvalArg1(functionList.get(1));
	}

	/**
	 * Evaluate this function for one double argument
	 * 
	 * @param arg1
	 *          a double number
	 * 
	 * @return
	 */
	public IExpr numericEvalD1(final Num arg1) {
		return null;
	}

	/**
	 * Evaluate this function for one double complex argument
	 * 
	 * @param arg1
	 *          a double complex number
	 * 
	 * @return
	 */
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return null;
	}

	public IExpr numericEvalArg1(final IExpr arg1) {
		return null;
	}

	/**
	 * Check if the given expression is canonical negative.
	 * 
	 * @return
	 */
	public static boolean isNegativeExpression(final IExpr expr) {
		if (expr instanceof INumber) {
			return (((INumber) expr).complexSign() == (-1));
		} else if (expr instanceof AST) {
			final AST exprAST = (AST) expr;
			if ((exprAST.head() == F.Times) && (exprAST.size() > 1)) {
				if (exprAST.get(1) instanceof INumber) {
					return (((INumber) exprAST.get(1)).complexSign() == (-1));
				}
			} else if ((exprAST.head() == F.Plus) && (exprAST.size() > 2)) {
				if (exprAST.get(1) instanceof INumber) {
					return (((INumber) exprAST.get(1)).complexSign() == (-1));
					// } else if (exprAST.get(0) instanceof AST) {
					// AST subAST = (AST) exprAST.get(0);
					// if (subAST.getHeader() == ft.Times && subAST.size() > 1)
					// {
					// if (subAST.get(0) instanceof INumber) {
					// return (((INumber) subAST.get(0)).complexSign() == (-1));
					// }
					// }
				}
			}
		}
		return false;
	}

	/**
	 * Check if the first argument of the given AST is canonical negative. If yes,
	 * return the number and the rest expression in an array with size 2.
	 * 
	 * @return <code>null</code> if the first argument isn't canonical negative
	 */
	public static IExpr[] isNegativeExpr(final IExpr expr) {
		if (expr.isTimes()) {
			IAST times = (IAST) expr;
			if (times.get(1).isNumber()) {
				int signum = ((INumber) times.get(1)).complexSign();
				if (signum < 0) {
					IAST clon = times.clone();
					clon.remove(1);
					return new IExpr[] { times.get(1), clon };
				}
			}
			return null;
		}
		if (expr.isNumber()) {
			int signum = ((INumber) expr).complexSign();
			if (signum < 0) {
				return new IExpr[] { expr, F.C1 };
			}
		}
		return null;
	}
}
