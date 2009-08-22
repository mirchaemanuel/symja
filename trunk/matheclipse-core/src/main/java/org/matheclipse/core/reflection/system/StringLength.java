package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;

public class StringLength extends AbstractFunctionEvaluator {

	public StringLength() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() == 2 && lst.get(1) instanceof IStringX) {
			return F.integer(lst.get(1).toString().length());
		}
		return null;
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.LISTABLE);
	}
}