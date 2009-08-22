package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Prepend extends AbstractFunctionEvaluator  {

	public Prepend() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if ((lst.size() == 3) && (lst.get(1) instanceof IAST)) {
			final IAST f0 = (IAST) ((IAST) lst.get(1)).clone();
			f0.add(1,lst.get(2));
			return f0;
		}

		return null;
	}

}