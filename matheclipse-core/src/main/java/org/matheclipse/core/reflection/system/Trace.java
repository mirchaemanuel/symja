package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Trace implements IFunctionEvaluator {

	public Trace() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		final IExpr temp = functionList.get(1);
		final IAST holdList = F.function(F.Hold);
		final EvalEngine engine = EvalEngine.get();
		try {
			engine.setTraceMode(true);
			engine.setTraceList(F.List());
			engine.evaluate(temp);
		} finally {
			engine.setTraceMode(false);
		}
		IAST traceList = engine.getTraceList();
		engine.setTraceList(null);
		if (traceList.size() == 2) {
			holdList.add(traceList.get(1));
		} else {
			// no trace available
			holdList.add(traceList);
		}
		return holdList;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
