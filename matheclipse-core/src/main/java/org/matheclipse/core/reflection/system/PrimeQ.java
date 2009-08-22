package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

import apache.harmony.math.BigInteger;

import com.google.common.base.Predicate;

/**
 * Test if a number is prime.
 * See: <a href="http://en.wikipedia.org/wiki/Prime_number">Wikipedia:Prime number</a>
 * 
 * @see org.matheclipse.core.reflection.system.NextPrime
 */
public class PrimeQ extends AbstractFunctionEvaluator implements Predicate<IExpr> {

	public PrimeQ() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() != 2) || !(functionList.get(1) instanceof IInteger)) {
			return null;
		}
		return F.bool(apply(functionList.get(1)));
	}

	public boolean apply(final IExpr obj) {
		try {
			final BigInteger value = ((IInteger) obj).getBigNumerator();
			return value.isProbablePrime(32);
		} catch (final Exception e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
