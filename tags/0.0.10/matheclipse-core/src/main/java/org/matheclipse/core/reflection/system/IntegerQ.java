package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Predicate function
 *
 * Returns <code>True</code> if the 1st argument is an integer;
 * <code>False</code> otherwise
 */
public class IntegerQ extends AbstractFunctionEvaluator {

	public IntegerQ() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		return F.bool(functionList.get(1) instanceof IInteger);
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.LISTABLE);
	}

}
