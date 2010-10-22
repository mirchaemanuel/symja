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
	public IExpr evaluate(final IAST ast) {
		if ((ast.size()!=2) || !(ast.get(1) instanceof IAST)) {
			return null;
		}
		final IAST sublist = ((IAST)ast.get(1)).clone();

		if (sublist.size() > 1) {
			sublist.remove(sublist.size()-1);
			return sublist;
		}

		return null;
	}

}
