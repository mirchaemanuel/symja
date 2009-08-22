package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.nested.Generating;

public class Outer extends AbstractFunctionEvaluator {

	public Outer() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 4) {
			return null;
		}
		if (functionList.get(1) instanceof ISymbol && functionList.get(2) instanceof IAST && functionList.get(3) instanceof IAST) {
			Generating<IExpr, IAST> gen = new Generating<IExpr, IAST>(F.List(), F.ast(functionList.get(1)), 1, AST.COPY);

			return (IExpr) gen.outer((IAST) functionList.get(2), (IAST) functionList.get(3));
		}
		return null;
	}

}
