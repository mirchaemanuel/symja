package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

import apache.harmony.math.BigInteger;

/**
 * Returns the Gamma function value.
 * 
 * See <a href="http://en.wikipedia.org/wiki/Gamma_function">Gamma functio</a>
 * 
 */
public class Gamma extends AbstractTrigArg1 {

  public Gamma() {
  }

  @Override
  public IExpr numericEvalD1(final Num arg1) {
    double d = org.apache.commons.math.special.Gamma.logGamma(arg1.doubleValue());
    return F.num(Math.exp(d));
  }

  @Override
  public IExpr numericEvalDC1(final ComplexNum arg1) {
    return null;
  }

  public static BigInteger gamma(final BigInteger biggi) {
    return Factorial.factorial(biggi.minus(BigInteger.ONE));
  }

  @Override
  public IExpr evaluateArg1(final IExpr arg1) {
    if (arg1 instanceof IInteger) {
      BigInteger fac = gamma(((IInteger) arg1).getBigNumerator());
      return F.integer(fac);
    }
    return null;
  }

}
