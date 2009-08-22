package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IStringX;

public class StringJoin extends AbstractFunctionEvaluator {

	public StringJoin() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() > 2) {
			StringBuffer buf = new StringBuffer();
			for (int i = 1; i < lst.size(); i++) {
				checkCanceled();
				if (lst.get(i) instanceof IStringX) {
					buf.append(lst.get(i).toString());
				} else {
					return null;
				}
			}
			return F.stringx(buf.toString());
		}

		return null;
	}
}