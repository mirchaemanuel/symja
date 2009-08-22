package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

import apache.harmony.math.BigInteger;
import apache.harmony.math.Rational;

/**
 * Harmonic number of a given integer value
 * 
 * See: <a href="http://en.wikipedia.org/wiki/Harmonic_number">Harmonic number</a>
 */
public class HarmonicNumber implements IFunctionEvaluator {

	public HarmonicNumber() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		IExpr arg1 = functionList.get(1);
		if (arg1 instanceof IInteger) {
			try {
				int n = ((IInteger) arg1).toInt();
				if (n < 0) {
					return null;
				}
				if (n == 0) {
					return F.C0;
				}
				if (n == 1) {
					return F.C1;
				}
				n--;
				Rational sum = Rational.ONE;
				BigInteger counter = BigInteger.ONE;
				for (int i = 0; i < n; i++) {
					counter = counter.add(BigInteger.ONE);
					sum = sum.plus(Rational.valueOf(BigInteger.ONE, counter));
				}
				return F.fraction(sum);
			} catch (ArithmeticException ae) {

			}
		}

		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(ISymbol symbol) {

	}

}
