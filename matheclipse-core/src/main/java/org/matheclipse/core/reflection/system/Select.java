package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.NonNegativeIntegerExpected;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.generic.IsUnaryTrue;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class Select implements IFunctionEvaluator {

	public Select() {
	}

	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST)) {
			return select((IAST) functionList.get(1), functionList.get(2));
		} else if ((functionList.size() == 4) && (functionList.get(1) instanceof IAST) && (functionList.get(3) instanceof IInteger)) {
			try {
				final int resultLimit = ((IInteger) functionList.get(3)).toInt();
				if (resultLimit<0) {
					throw new NonNegativeIntegerExpected(functionList, 3);
				}
				return select((IAST) functionList.get(1), functionList.get(2), resultLimit);
			} catch (final ArithmeticException e) {
				// wrong resultLimit value here (toInt())
				throw new NonNegativeIntegerExpected(functionList, 3);
			}

		}
		return null;
	}

	public static IAST select(final IAST list, final IExpr head) {
		final IsUnaryTrue<IExpr> matcher = new IsUnaryTrue<IExpr>(head);
		return list.args().select((IAST)list.copyHead(), matcher);
	}

	public static IAST select(final IAST list, final IExpr head, final int resultLimit) {
		final IsUnaryTrue<IExpr> matcher = new IsUnaryTrue<IExpr>(head);
//		final IAST resultList = (IAST)list.copyHead();
//		Finding.select(list, resultList, 1, list.size(), matcher, resultLimit);
//		return resultList;
		return list.args().select((IAST)list.copyHead(), matcher, resultLimit);
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}

}
