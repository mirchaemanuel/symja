package org.matheclipse.core.reflection.system;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISignedNumber;

public class Less extends Greater {
	public final static Less CONST = new Less();
	public Less() {
	}

	@Override
	public int compare(final IExpr o0, final IExpr o1) {
		if ((o0 instanceof ISignedNumber) && (o1 instanceof ISignedNumber)) {
			if (o0.isLTOrdered(o1)) {
				return 1;
			}

			return -1;
		}
		// IExpr result[] = new IExpr[2];
		// if (compareAddFunction(result, o0, o1)) {
		// return result[0].less(result[1]) ? ft.True : ft.False;
		// }

		// don't compare strings here
		// if ((o0 instanceof StringImpl) && (o1 instanceof StringImpl)) {
		// return (((StringImpl) o0).compareTo((StringImpl) o1) < 0 ? 1 : -1);
		// }

		return 0;
	}
}
