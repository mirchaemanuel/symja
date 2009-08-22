package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Join extends AbstractFunctionEvaluator {

	public Join() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() < 3) {
			return null;
		}
		if (functionList.args().any(AtomQ.CONST)) {
			return null;
		}
		final IAST result = F.List();
		for (int i = 1; i < functionList.size(); i++) {
			checkCanceled();
			final IAST subList = (IAST) functionList.get(i);
			for (int j = 1; j < subList.size(); j++) {
				checkCanceled();
				result.add(subList.get(j));
			}
		}
		return result;
	}
}
