package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Head extends AbstractFunctionEvaluator {

	public Head() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			return null;
		}
		return functionList.get(1).getHeader();
	}

	@Override
	public void setUp(final ISymbol symbol) {
	}

}
