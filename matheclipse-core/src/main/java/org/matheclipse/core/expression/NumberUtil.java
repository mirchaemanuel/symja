package org.matheclipse.core.expression;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INumber;

import apache.harmony.math.BigInteger;
import apache.harmony.math.Rational;

/**
 *
 */
public class NumberUtil {
//	public static final BigInteger B0 = BigInteger.ZERO;
//	public static final BigInteger B1 = BigInteger.ONE;
//	public static final BigInteger BN1 = BigInteger.MINUS_ONE;


	public static boolean isZero(final IExpr e) {
		if (e instanceof INumber) {
			return ((INumber)e).isZero() ;
		}
		return false;
	}

	public static boolean isZero(final Rational rationalNumber) {
		return rationalNumber.equals(Rational.ZERO);
	}

	public static boolean isOne(final IExpr i) {
		return (i instanceof IInteger)&&
						((IInteger)i).getBigNumerator().equals(BigInteger.ONE) ;
	}

	public static boolean isMinusOne(final IExpr i) {
		return (i instanceof IInteger)&&
						((IInteger)i).getBigNumerator().equals(BigInteger.MINUS_ONE) ;
	}

}
