package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

import apache.harmony.math.BigInteger;
 
/**
 * 
 * See <a href="http://en.wikipedia.org/wiki/Catalan_number">Wikipedia:Catalan number</a>
 * 
 */
public class CatalanNumber extends AbstractTrigArg1 {
	public CatalanNumber() {
	}

	@Override
	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1 instanceof IInteger) {
			return F.integer(catalanNumber(((IInteger) arg1).getBigNumerator()));
		}

		return null;
	}

	public static BigInteger catalanNumber(BigInteger n) {
		n = n.plus(1);
		if (!n.isLargerThan(BigInteger.ZERO)) {
			return BigInteger.ZERO;
		}
		BigInteger i = BigInteger.ONE;
		BigInteger c = BigInteger.ONE;
		final BigInteger temp1 = n.shiftLeft(1).minus(BigInteger.ONE);
		while (i.isLessThan(n)) {
			c = c.times(temp1.minus(i)).divide(i);
			i = i.plus(1);
		}
		return c.divide(n);
	}

	@Override
  public void setUp(final ISymbol symbol) throws SyntaxError {
    symbol.setAttributes(ISymbol.LISTABLE );
    super.setUp(symbol);
  }
}
