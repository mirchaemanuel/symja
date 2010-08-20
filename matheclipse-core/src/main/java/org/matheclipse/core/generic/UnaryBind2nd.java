package org.matheclipse.core.generic;


import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class UnaryBind2nd extends UnaryMap {

	public UnaryBind2nd(final IAST constant) {
		super(constant);
	}

	public IExpr apply(final IExpr secondArg) {
		final IAST ast = (IAST)fConstant.clone();
		ast.set(2, secondArg);
		return ast;
	}

}
