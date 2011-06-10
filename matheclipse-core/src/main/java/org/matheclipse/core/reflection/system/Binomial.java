package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractArg2;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

import java.math.BigInteger;

/**
 * Returns the binomial coefficient of 2 integers.
 * 
 * See <a href="http://en.wikipedia.org/wiki/Binomial_coefficient">Binomial
 * coefficient</a>
 */
public class Binomial extends AbstractArg2 {

	public Binomial() {
	}

	@Override
	public IExpr e2IntArg(final IInteger n0, final IInteger k0) {
		final BigInteger n = n0.getBigNumerator();
		final BigInteger k = k0.getBigNumerator();

		return F.integer(binomial(n, k));
	}

	private BigInteger binomial(final BigInteger n, final BigInteger k) {
		// k>n : by definition --> 0

		if (k.compareTo(n) > 0) {
			return BigInteger.ZERO;
		}
		if (k.equals(BigInteger.ZERO) || k.equals(n)) {
			return BigInteger.ONE;
		}

		BigInteger bin = BigInteger.ONE;
		BigInteger i = BigInteger.ONE;

		while (!(i.compareTo(k)>0)) {
			bin = bin.multiply(n.subtract(i).add(BigInteger.ONE)).divide(i);
			i = i.add(BigInteger.ONE);
		}
		return bin;
	}

}
