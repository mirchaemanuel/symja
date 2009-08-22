package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class CompoundExpression implements IFunctionEvaluator {

	public CompoundExpression() {
	}

	public IExpr evaluate(final IAST functionList) {
		final EvalEngine engine = EvalEngine.get();
		if (functionList.size() > 1) {
			for (int i = 1; i < functionList.size() - 1; i++) {
				checkCanceled();
				engine.evaluate(functionList.get(i));
			}
			return engine.evaluate(functionList.get(functionList.size() - 1));
		}
		return F.Null;
	}


	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.matheclipse.parser.interfaces.IEvaluator#setUp(org.matheclipse.parser.interfaces.ISymbol)
	 */
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
