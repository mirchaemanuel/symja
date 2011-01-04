package org.matheclipse.core.reflection.system;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISignedNumber;

public class GreaterEqual extends Greater {
	public final static GreaterEqual CONST = new GreaterEqual();
	public GreaterEqual() {
	}

	@Override
	public int compare(final IExpr o0, final IExpr o1) {
		if (o0.equals(o1)) {
			return 1;
		}

		if ((o0 instanceof ISignedNumber) && (o1 instanceof ISignedNumber)) {
			if (o1.isLTOrdered(o0)) {
				return 1;
			}

			return -1;
		}
		
		// don't compare strings here
		// if ((o0 instanceof StringImpl) && (o1 instanceof StringImpl)) {
		// return (((StringImpl) o0).compareTo((StringImpl) o1) >= 0 ? 1 : -1);
		// }

		return 0;
	}
}
