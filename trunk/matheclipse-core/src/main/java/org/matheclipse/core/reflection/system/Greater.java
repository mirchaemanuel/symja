package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.ITernaryComparator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;

public class Greater extends AbstractFunctionEvaluator implements
		ITernaryComparator<IExpr> {
	public final static Greater CONST = new Greater();
	public Greater() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() > 1) {
			if (lst.size() == 2) {
				return F.True;
			}
			int b = 0;
			boolean evaled = false;
			IAST result = (IAST) lst.clone();
			int[] cResult = new int[lst.size()];
			cResult[0] = 1;
			for (int i = 1; i < lst.size() - 1; i++) {
				checkCanceled();
				b = compare(result.get(i), result.get(i + 1));
				if (b == (-1)) {
					return F.False;
				}
				if (b == 1) {
					evaled = true;
				}
				cResult[i] = b;
			}
			cResult[lst.size() - 1] = 1;
			if (!evaled) {
				// expression doesn't change
				return null;
			}
			int i = 2;
			evaled = false;
			for (int j = 1; j < lst.size(); j++) {
				checkCanceled();
				if (cResult[j - 1] == 1 && cResult[j] == 1) {
					evaled = true;
					result.remove(i - 1);
				} else {
					i++;
				}
			}

			if (evaled) {
				if (result.size() <= 2) {
					return F.True;
				}
				return result;
			}

		}
		return null;
	}

	public int compare(final IExpr o0, final IExpr o1) {
		if ((o0 instanceof ISignedNumber) && (o1 instanceof ISignedNumber)) {
			if (o1.isLTOrdered(o0)) {
				return 1;
			}
			return -1;
		}

		// don't compare strings here
		// if ((o0 instanceof StringImpl) && (o1 instanceof StringImpl)) {
		// return (((StringImpl) o0).compareTo((StringImpl) o1) > 0 ? 1 : -1);
		// }

		return 0;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.FLAT);
	}
}
