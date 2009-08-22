package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.IStringX;

public class StringTake extends AbstractFunctionEvaluator {

	public StringTake() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() == 3) {
			if (lst.get(1) instanceof IStringX && lst.get(2) instanceof IInteger) {
				String s = lst.get(1).toString();
				int n = ((IInteger)lst.get(2)).toInt();
				if(n >= 0) {
					return F.stringx(s.substring(0, n));
				} else {
					return F.stringx(s.substring(s.length() + n, s.length()));
				}
			}
		}

		return null;
	}
}