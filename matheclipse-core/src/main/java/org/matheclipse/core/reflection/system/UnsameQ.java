package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class UnsameQ extends AbstractFunctionEvaluator {

	public UnsameQ() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() > 1) {
			IAST result = lst.clone();
			int i = 2;
			int j;
			while (i < result.size()) {
				checkCanceled();
				j = i;
				while (j < result.size()) {
					checkCanceled();
					if (result.get(i - 1).equals(result.get(j++))) {
						return F.False;
					}
				}
				i++;
			}
			return F.True;

		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.FLAT);
	}
}