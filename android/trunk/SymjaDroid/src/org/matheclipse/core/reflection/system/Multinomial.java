package org.matheclipse.core.reflection.system;

import java.math.BigInteger;
import java.util.List;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Returns the multinomial coefficient.
 * 
 * See <a
 * href="http://en.wikipedia.org/wiki/Multinomial_coefficient">Multinomial
 * coefficient</a>
 */
public class Multinomial extends AbstractFunctionEvaluator {
	public Multinomial() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() >= 3) {
			for (int i = 1; i < ast.size(); i++) {
				if (!(ast.get(i) instanceof IInteger)) {
					return null;
				}
				if (((IInteger) ast.get(i)).isNegative()) {
					return null;
				}
			}

			return F.integer(multinomial(ast));
		}
		return null;
	}

	public static BigInteger multinomial(final List<IExpr> ast) {
		BigInteger k;
		BigInteger n = BigInteger.ZERO;
		BigInteger denom = BigInteger.ONE;
		for (int i = 1; i < ast.size(); i++) {
			k = ((IInteger) ast.get(i)).getBigNumerator();
			n = n.add(k);
			denom = denom.multiply(Factorial.factorial(k));
		}
		return Factorial.factorial(n).divide(denom);
	}

	public static BigInteger multinomial(final int[] indices, final int n) {
		BigInteger bn = BigInteger.valueOf(n);
		BigInteger denom = BigInteger.ONE;
		for (int i = 0; i < indices.length; i++) {
			if (indices[i] != 0) {
				denom = denom.multiply(Factorial.factorial(BigInteger.valueOf(indices[i])));
			}
		}
		return Factorial.factorial(bn).divide(denom);
	}

	@Override
	public void setUp(ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
