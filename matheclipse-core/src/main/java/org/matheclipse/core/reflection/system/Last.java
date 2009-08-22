package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 */
public class Last extends AbstractFunctionEvaluator {

	public Last() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() != 2) || !(functionList.get(1) instanceof IAST)) {
			return null;
		}
		final IAST sublist = (IAST) functionList.get(1);

		if (sublist.size() > 1) {
			return sublist.get(sublist.size() - 1);
		}

		return null;
	}
}
