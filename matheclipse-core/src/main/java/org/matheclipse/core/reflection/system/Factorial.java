package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

import java.math.BigInteger;

/**
 * Returns the factorial of an integer n
 * 
 * See <a href="http://en.wikipedia.org/wiki/Factorial">Factorial</a>
 * 
 */
public class Factorial extends AbstractTrigArg1 {

	public Factorial() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		double d = org.apache.commons.math.special.Gamma.logGamma(arg1.doubleValue() + 1);
		return F.num(Math.exp(d));
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return null;
	}

	public static BigInteger factorial(final BigInteger biggi) {
		BigInteger result = BigInteger.ONE;
		if (biggi.compareTo(BigInteger.ZERO) == -1) {
			result = BigInteger.valueOf(-1);

			for (BigInteger i = BigInteger.valueOf(-2); i.compareTo(biggi) >= 0; i = i.add(BigInteger.valueOf(-1))) {
				result = result.multiply(i);
			}
		} else {
			for (BigInteger i = BigInteger.valueOf(2); i.compareTo(biggi) <= 0; i = i.add(BigInteger.ONE)) {
				result = result.multiply(i);
			}
		}
		return result;
	}

	@Override
	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1.isInteger()) {
			BigInteger fac = factorial(((IInteger) arg1).getBigNumerator());
			return F.integer(fac);
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
		super.setUp(symbol);
	}
}
