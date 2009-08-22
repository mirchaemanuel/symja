package org.matheclipse.core.reflection.system;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * 
 * See: <a href="http://en.wikipedia.org/wiki/Polynomial_long_division">Wikipedia:Polynomial long division</a>
 * 
 * @see org.matheclipse.core.reflection.system.PolynomialRemainder
 * @see org.matheclipse.core.reflection.system.PolynomialQuotientRemainder
 */
public class PolynomialQuotient extends PolynomialQuotientRemainder {

	public PolynomialQuotient() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 3) {
			return null;
		}
		IExpr[] result = quotientRemainder(lst);
		if (result == null) {
			return null;
		}
		return result[0];
	}

}