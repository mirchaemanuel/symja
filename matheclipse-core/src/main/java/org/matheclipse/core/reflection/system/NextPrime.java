package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

import apache.harmony.math.BigInteger;

/**
 * Get the next prime number. See: <a
 * href="http://en.wikipedia.org/wiki/Prime_number">Wikipedia:Prime number</a>
 * 
 * @see org.matheclipse.core.reflection.system.PrimeQ
 */
public class NextPrime extends AbstractFunctionEvaluator {

	public NextPrime() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() == 2 && functionList.get(1) instanceof IInteger) {

			BigInteger primeBase = ((IntegerSym) functionList.get(1)).getBigNumerator();
			return F.integer(primeBase.nextProbablePrime());

		} else if (functionList.size() == 3 && functionList.get(1) instanceof IInteger && functionList.get(2) instanceof IInteger) {

			BigInteger primeBase = ((IntegerSym) functionList.get(1)).getBigNumerator();
			final int n = Validate.checkIntType(functionList, 2);
			return F.integer(primeBase.nextProbablePrime(n));

		}
		return null;
	}

}
