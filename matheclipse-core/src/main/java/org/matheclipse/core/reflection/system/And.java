package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractArg2;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * 
 * 
 * See <a href="http://en.wikipedia.org/wiki/Logical_conjunction">Logical conjunction</a>
 *
 */
public class And extends AbstractArg2 {
	public And() {
	}

	@Override
	public IExpr e2ObjArg(final IExpr o0, final IExpr o1) {
		if (o0.equals(F.False) || o1.equals(F.False)) {
			return F.False;
		}

		if (o0.equals(F.True) && o1.equals(F.True)) {
			return F.True;
		}

		if (o0.equals(F.True)) {
			return o1;
		}

		if (o1.equals(F.True)) {
			return o0;
		}

		return null;
	}
}
