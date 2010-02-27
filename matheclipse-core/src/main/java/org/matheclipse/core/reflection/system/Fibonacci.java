package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

import apache.harmony.math.BigInteger;

public class Fibonacci extends AbstractTrigArg1 {

	public Fibonacci() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return null;
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return null;
	}

	public static IInteger fibonacci(final IInteger iArg) {
		BigInteger a = BigInteger.ONE;
		BigInteger b = BigInteger.ZERO;
		BigInteger c = BigInteger.ONE;
		BigInteger d = BigInteger.ZERO;
		BigInteger f = BigInteger.ZERO;
		final BigInteger c2 = BigInteger.valueOf(2);
		BigInteger temp = iArg.getBigNumerator();

		while (!temp.isZero()) {
			checkCanceled();
			if (temp.isOdd()) {
				d = f.times(c);
				f = a.times(c).plus(f.times(b).plus(d));
				a = a.times(b).plus(d);
			}

			d = c.times(c);
			c = b.times(c).times(c2).plus(d);
			b = b.times(b).plus(d);
			temp = temp.shiftRight(1);
		}

		final IInteger i = F.integer(f);

		return i;

	}

	@Override
	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1 instanceof IInteger) {
			return fibonacci((IInteger) arg1);
		}
		return null;
	}

	@Override
  public void setUp(final ISymbol symbol) throws SyntaxError {
    symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
    super.setUp(symbol);
  }
}
