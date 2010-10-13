package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Condition implements IFunctionEvaluator {

	public Condition() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 3) {
			throw new WrongNumberOfArguments(functionList, 2, functionList.size() - 1);
		}
//		throw new RuntimeException("Condition is only allowed in Set, SetDelayed expressions");
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
