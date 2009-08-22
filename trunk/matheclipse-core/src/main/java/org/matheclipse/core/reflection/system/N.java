package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 */
public class N implements IFunctionEvaluator {

	public N() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		final EvalEngine engine = EvalEngine.get();
		final boolean numericMode = engine.isNumericMode();
		try {
			engine.setNumericMode(true);
			final IExpr temp = engine.evalLoop(functionList.get(1));
			if (temp == null) {
				return functionList.get(1);
			}
			return temp;
		} finally {
			engine.setNumericMode(numericMode);
		}
	}

	public IExpr numericEval(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		final IExpr temp = EvalEngine.get().evalLoop(functionList.get(1));
		if (temp == null) {
			return functionList.get(1);
		}
		return temp;
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
