package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Returns the largest (closest to positive infinity)
 * <code>ISignedNumber</code> value that is not greater than
 * <code>this</code> and is equal to a mathematical integer.
 *
 * @return the largest (closest to positive infinity)
 *         <code>ISignedNumber</code> value that is not greater than
 *         <code>this</code> and is equal to a mathematical integer.
 */
public class Floor implements IFunctionEvaluator, INumeric {

	public Floor() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size()-1);
		}
		if (functionList.get(1) instanceof ISignedNumber) {
			return ((ISignedNumber) functionList.get(1)).floor();
		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.floor(stack[top]);
	}
}
