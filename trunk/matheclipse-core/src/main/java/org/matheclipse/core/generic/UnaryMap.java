package org.matheclipse.core.generic;


import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class UnaryMap extends UnaryFunctorImpl<IExpr> {
	IAST fConstant;

	public UnaryMap(final IAST constant) {
		fConstant = constant;
	}

	public IExpr apply(final IExpr firstArg) {
		final IAST ast = (IAST)fConstant.clone();
		ast.add(firstArg);
		return ast;
	}

}
