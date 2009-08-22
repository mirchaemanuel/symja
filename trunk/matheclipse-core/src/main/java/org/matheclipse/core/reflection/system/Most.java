package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 */
public class Most extends AbstractFunctionEvaluator {

	public Most() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size()!=2) || !(functionList.get(1) instanceof IAST)) {
			return null;
		}
		final IAST sublist = (IAST) ((IAST)functionList.get(1)).clone();

		if (sublist.size() > 1) {
			sublist.remove(sublist.size()-1);
			return sublist;
		}

		return null;
	}

}
