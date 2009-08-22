package org.matheclipse.core.generic;


import org.matheclipse.core.interfaces.IExpr;

public class UnaryConstant extends UnaryFunctorImpl<IExpr> {
	final IExpr fConstant;

	public UnaryConstant(final IExpr constant) {
		fConstant = constant;
	}

	public IExpr apply(final IExpr firstArg) {
		return fConstant;
	}

}
