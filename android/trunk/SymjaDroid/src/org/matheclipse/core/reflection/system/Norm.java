package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Norm of a given argument
 */
public class Norm implements IFunctionEvaluator {

	public Norm() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		IExpr arg = functionList.get(1);
		if (arg instanceof INumber) {
			// absolute Value of a number
			return ((INumber) arg).eabs();
		}
		int dim = arg.isVector();
		if (dim > (-1)) {
			// norm of a vector
			IAST plusAST = F.Plus();
			for (IExpr expr : ((IAST) arg).range(1)) {
				plusAST.add(F.Sqr(F.Abs(expr)));
			}
			return F.Sqrt(plusAST);
		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(ISymbol symbol) {

	}

}
