/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package apache.harmony.math;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * This class represents the ratio of two {@link BigInteger} numbers.
 * </p>
 * 
 * <p>
 * Instances of this class are immutable and can be used to find exact solutions
 * to linear equations with the {@link org.jscience.mathematics.vectors.Matrix
 * Matrix} class.
 * </p>
 * 
 * <p>
 * All parts of this class depending on the <code>roundToBigInteger()</code>
 * method are based on Eric Laroche's <a
 * href="http://www.lrdev.com/lr/software.html">BigRational.java -- dynamically
 * sized big rational numbers</a> and are therefore &quot;Copyright (C)
 * 2002-2010 Eric Laroche. All rights reserved.&quot;
 * </p>
 * 
 * @author Eric Laroche <laroche@lrdev.com>
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Rational_numbers"> Wikipedia:
 *      Rational Numbers</a>
 * 
 */
public final class Rational implements Serializable {

	/**
	 * The {@link Rational} representing the additive identity.
	 */
	public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);

	/**
	 * The {@link Rational} representing the multiplicative identity.
	 */
	public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

	public static final Rational MINUS_ONE = new Rational(BigInteger.MINUS_ONE, BigInteger.ONE);

	/**
	 * Holds the dividend.
	 */
	private BigInteger fNumerator;

	/**
	 * Holds the divisor.
	 */
	private BigInteger fDenominator;

	/**
	 * Default constructor.
	 */
	private Rational() {
	}

	/**
	 * Creates a rational number for the specified integer dividend and divisor.
	 * 
	 * @param dividend
	 *          the dividend value.
	 * @param divisor
	 *          the divisor value.
	 * @throws ArithmeticException
	 *           if <code>divisor == 0</code>
	 */
	private Rational(BigInteger dividend, BigInteger divisor) {
		fNumerator = dividend;
		fDenominator = divisor;
	}

	/**
	 * Constructs an approximated rational for a given double number
	 */
	public static Rational valueOf(double x) {
		return valueOf(x, 1.0e-12);
	}

	/**
	 * Constructs an approximated rational for a given double number
	 */
	public static Rational valueOf(double x, double epsilon) {
		// from: http://www.math.uic.edu/~burgiel/Mtht420.99/5/Rational.java
		if (x == 0.0) {
			return Rational.ZERO;
		}
		if (Math.abs(x) > Long.MAX_VALUE || Math.abs(x) < 1 / (double) Long.MAX_VALUE) { // NaN
			return valueOf(0, 0); // undefined value
		}
		int sgn = 1;
		if (x < 0.0) {
			sgn = -1;
			x = -x;
		}
		long intPart = (long) x;
		double z = x - intPart;
		if (z != 0) {
			z = 1.0 / z;
			long a = (long) z;
			z = z - a;
			long prevNum = 0;
			long num = 1;
			long prevDen = 1;
			long den = a;
			long tmp;
			double approxAns = ((double) den * intPart + num) / den;
			while (Math.abs((x - approxAns) / x) >= epsilon) {
				z = 1.0 / z;
				a = (long) z;
				z = z - a;
				// deal with too-big numbers:
				if ((double) a * num + prevNum > Long.MAX_VALUE || (double) a * den + prevDen > Long.MAX_VALUE) {
					break;
				}
				tmp = a * num + prevNum;
				prevNum = num;
				num = tmp;
				tmp = a * den + prevDen;
				prevDen = den;
				den = tmp;
				approxAns = ((double) den * intPart + num) / den;
			}
			return valueOf(sgn * (den * intPart + num), den);
		} else { // is integer
			return valueOf(sgn * intPart, 1);
		}
	}

	/**
	 * Returns the rational number for the specified integer dividend and divisor.
	 * 
	 * @param dividend
	 *          the dividend value.
	 * @param divisor
	 *          the divisor value.
	 * @return <code>dividend / divisor</code>
	 * @throws ArithmeticException
	 *           if <code>divisor == 0</code>
	 */
	public static Rational valueOf(long dividend, long divisor) {
		Rational r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		r = new Rational();
		// }
		r.fNumerator = BigInteger.valueOf(dividend);
		r.fDenominator = BigInteger.valueOf(divisor);
		return r.normalize();
	}

	/**
	 * Returns the rational number for the specified large integer dividend and
	 * divisor.
	 * 
	 * @param dividend
	 *          the dividend value.
	 * @param divisor
	 *          the divisor value.
	 * @return <code>dividend / divisor</code>
	 * @throws ArithmeticException
	 *           if <code>divisor.isZero()</code>
	 */
	public static Rational valueOf(BigInteger dividend, BigInteger divisor) {
		Rational r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		r = new Rational();
		// }
		r.fNumerator = dividend;
		r.fDenominator = divisor;
		return r.normalize();
	}

	public static Rational valueOf(Rational rat) {
		Rational r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		r = new Rational();
		// }
		r.fNumerator = rat.fNumerator;
		r.fDenominator = rat.fDenominator;
		return r;
	}

	/**
	 * Returns the rational number for the specified character sequence.
	 * 
	 * @param chars
	 *          the character sequence.
	 * @return the corresponding rational number.
	 */
	public static Rational valueOf(String chars) {
		int sep = chars.indexOf("/");
		if (sep >= 0) {
			BigInteger dividend = BigInteger.valueOf(chars.substring(0, sep));
			BigInteger divisor = BigInteger.valueOf(chars.substring(sep + 1, chars.length()));
			return valueOf(dividend, divisor);
		} else { // No divisor.
			return valueOf(BigInteger.valueOf(chars), BigInteger.ONE);
		}
	}

	/**
	 * Returns the smallest numerator of the fraction representing this rational
	 * number.
	 * 
	 * @return this rational numerator.
	 */
	public BigInteger getNumerator() {
		return fNumerator;
	}

	/**
	 * Returns the smallest denominator of the fraction representing this rational
	 * (always positive).
	 * 
	 * @return this rational denominator.
	 */
	public BigInteger getDenominator() {
		return fDenominator;
	}

	/**
	 * Returns the closest integer to this rational number.
	 * 
	 * @return this rational rounded to the nearest integer.
	 */
	// public BigInteger round() {
	// return fNumerator.divide(fDenominator);
	// }

	/**
	 * Returns the opposite of this rational number.
	 * 
	 * @return <code>-this</code>.
	 */
	public Rational opposite() {
		return Rational.valueOf(fNumerator.opposite(), fDenominator);
	}

	/**
	 * Returns the sum of this rational number with the one specified.
	 * 
	 * @param that
	 *          the rational number to be added.
	 * @return <code>this + that</code>.
	 */
	public Rational plus(Rational that) {
		return Rational.valueOf(this.fNumerator.times(that.fDenominator).plus(this.fDenominator.times(that.fNumerator)),
				this.fDenominator.times(that.fDenominator)).normalize();
	}

	/**
	 * Returns the difference between this rational number and the one specified.
	 * 
	 * @param that
	 *          the rational number to be subtracted.
	 * @return <code>this - that</code>.
	 */
	public Rational minus(Rational that) {
		return Rational.valueOf(this.fNumerator.times(that.fDenominator).minus(this.fDenominator.times(that.fNumerator)),
				this.fDenominator.times(that.fDenominator)).normalize();
	}

	/**
	 * Returns the product of this rational number with the one specified.
	 * 
	 * @param that
	 *          the rational number multiplier.
	 * @return <code>this · that</code>.
	 */
	public Rational times(Rational that) {
		Rational r = Rational.valueOf(this.fNumerator.times(that.fNumerator), this.fDenominator.times(that.fDenominator)).normalize();
		return r;
	}

	/**
	 * Returns the inverse of this rational number.
	 * 
	 * @return <code>1 / this</code>.
	 * @throws ArithmeticException
	 *           if <code>dividend.isZero()</code>
	 */
	public Rational inverse() {
		if (fNumerator.isZero())
			throw new ArithmeticException("Dividend is zero");
		return fNumerator.isNegative() ? Rational.valueOf(fDenominator.opposite(), fNumerator.opposite()) : Rational.valueOf(
				fDenominator, fNumerator);
	}

	/**
	 * Returns this rational number divided by the one specified.
	 * 
	 * @param that
	 *          the rational number divisor.
	 * @return <code>this / that</code>.
	 * @throws ArithmeticException
	 *           if <code>that.equals(ZERO)</code>
	 */
	public Rational divide(Rational that) {
		return Rational.valueOf(this.fNumerator.times(that.fDenominator), this.fDenominator.times(that.fNumerator)).normalize();
	}

	/**
	 * Returns the absolute value of this rational number.
	 * 
	 * @return <code>|this|</code>.
	 */
	public Rational abs() {
		return Rational.valueOf(fNumerator.abs(), fDenominator);
	}

	/**
	 * Compares the absolute value of two rational numbers.
	 * 
	 * @param that
	 *          the rational number to be compared with.
	 * @return <code>|this| > |that|</code>
	 */
	public boolean isLargerThan(Rational that) {
		return this.fNumerator.times(that.fDenominator).isLargerThan(that.fNumerator.times(this.fDenominator));
	}

	public boolean isNegative() {
		return fNumerator.isNegative() != fDenominator.isNegative();
	}

	/**
	 * Returns the decimal text representation of this number.
	 * 
	 * @return the text representation of this number.
	 */
	// public Text toText() {
	// return
	// _dividend.toText().concat(Text.valueOf('/')).concat(_divisor.toText());
	// }

	/**
	 * Compares this rational number against the specified object.
	 * 
	 * @param that
	 *          the object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	public boolean equals(Object that) {
		if (that instanceof Rational) {
			return this.fNumerator.equals(((Rational) that).fNumerator) && this.fDenominator.equals(((Rational) that).fDenominator);
		} else {
			return false;
		}
	}

	/**
	 * Returns the hash code for this rational number.
	 * 
	 * @return the hash code value.
	 */
	public int hashCode() {
		return fNumerator.hashCode() - fDenominator.hashCode();
	}

	/**
	 * Returns the value of this rational number as a <code>long</code>.
	 * 
	 * @return the numeric value represented by this rational after conversion to
	 *         type <code>long</code>.
	 */
	public long longValue() {
		return (long) doubleValue();
	}

	/**
	 * Returns the value of this rational number as a <code>double</code>.
	 * 
	 * @return the numeric value represented by this rational after conversion to
	 *         type <code>double</code>.
	 */
	public double doubleValue() {
		// Normalize to 63 bits (minimum).
		int dividendBitLength = fNumerator.bitLength();
		int divisorBitLength = fDenominator.bitLength();
		if (dividendBitLength > divisorBitLength) {
			// Normalizes the divisor to 63 bits.
			int shift = divisorBitLength - 63;
			long divisor = fDenominator.shiftRight(shift).longValue();
			BigInteger dividend = fNumerator.shiftRight(shift);
			return dividend.doubleValue() / divisor;
		} else {
			// Normalizes the dividend to 63 bits.
			int shift = dividendBitLength - 63;
			long dividend = fNumerator.shiftRight(shift).longValue();
			BigInteger divisor = fDenominator.shiftRight(shift);
			return dividend / divisor.doubleValue();
		}
	}

	/**
	 * Compares two rational number numerically.
	 * 
	 * @param that
	 *          the rational number to compare with.
	 * @return -1, 0 or 1 as this rational number is numerically less than, equal
	 *         to, or greater than <code>that</code>.
	 */
	public int compareTo(Rational that) {
		return this.fNumerator.times(that.fDenominator).compareTo(that.fNumerator.times(this.fDenominator));
	}

	/**
	 * Returns the normalized form of this rational.
	 * 
	 * @return this rational after normalization.
	 * @throws ArithmeticException
	 *           if <code>divisor.isZero()</code>
	 */
	private Rational normalize() {
		if (!fDenominator.isZero()) {
			if (fDenominator.isPositive()) {
				BigInteger gcd = fNumerator.gcd(fDenominator);
				if (!gcd.equals(BigInteger.ONE)) {
					fNumerator = fNumerator.divide(gcd);
					fDenominator = fDenominator.divide(gcd);
				}
				return this;
			} else {
				fNumerator = fNumerator.opposite();
				fDenominator = fDenominator.opposite();
				return normalize();
			}
		} else {
			throw new ArithmeticException("Zero divisor");
		}
	}

	// @Override
	// public Rational copy() {
	// Rational r;
	// if (Config.SERVER_MODE) {
	// r = FACTORY.object();
	// } else {
	// r = new Rational();
	// }
	// r._dividend = _dividend.copy();
	// r._divisor = _divisor.copy();
	// return r;
	// }

	public Rational copyNew() {
		Rational r = new Rational();
		r.fNumerator = fNumerator.copyNew();
		r.fDenominator = fDenominator.copyNew();
		return r;
	}

	// public void recycle() {
	// FACTORY.recycle(this);
	// }

	private static final long serialVersionUID = 1L;

	/**
	 * Floor, round towards negative infinity.
	 * <P>
	 * Possible loss of precision.
	 */
	public BigInteger floor() {
		// [rounding step, possible loss of precision step]
		return round(BigDecimal.ROUND_FLOOR);
	}

	/**
	 * Ceiling, round towards positive infinity.
	 * <P>
	 * Possible loss of precision.
	 */
	public BigInteger ceiling() {
		// [rounding step, possible loss of precision step]
		return round(BigDecimal.ROUND_CEILING);
	}

	/**
	 * Truncate, round towards zero.
	 * <P>
	 * Possible loss of precision.
	 */
	public BigInteger trunc() {
		// [rounding step, possible loss of precision step]
		return round(BigDecimal.ROUND_DOWN);
	}

	/**
	 * Integer part.
	 * <P>
	 * Possible loss of precision.
	 */
	public BigInteger integerPart() {
		// [rounding step, possible loss of precision step]
		return round(BigDecimal.ROUND_DOWN);
	}

	/**
	 * Fractional part.
	 * <P>
	 * Possible loss of precision.
	 */
	public Rational fractionalPart() {
		// this==ip+fp; sign(fp)==sign(this)
		// [possible loss of precision step]
		return minus(valueOf(integerPart(), BigInteger.ONE));
	}

	/**
	 * Round.
	 * <P>
	 * Round mode is one of {
	 * <CODE>ROUND_UP, ROUND_DOWN, ROUND_CEILING, ROUND_FLOOR,
	 * ROUND_HALF_UP, ROUND_HALF_DOWN, ROUND_HALF_EVEN,
	 * ROUND_HALF_CEILING, ROUND_HALF_FLOOR, ROUND_HALF_ODD,
	 * ROUND_UNNECESSARY, DEFAULT_ROUND_MODE (==ROUND_HALF_UP)</CODE> .
	 * <P>
	 * If rounding isn't necessary, i.e. this BigRational is an integer, [as an
	 * optimization] this BigRational is returned.
	 * <P>
	 * Possible loss of precision.
	 */
	// @PrecisionLoss
	public BigInteger round(int roundMode) {
		// [rounding step, possible loss of precision step]
		return roundToBigInteger(roundMode);
	}

	/**
	 * Round to BigInteger helper function. Internally used.
	 * <P>
	 * Possible loss of precision.
	 */
	// @PrecisionLoss
	private BigInteger roundToBigInteger(int roundMode) {
		// note: remainder and its duplicate are calculated for all cases.

		BigInteger n = fNumerator;
		final BigInteger q = fDenominator;

		final int sgn = n.signum();

		// optimization
		if (sgn == 0) {
			// [typically not reached due to earlier test for integers]
			return BigInteger.ZERO;
		}

		// keep info on the sign
		final boolean pos = (sgn > 0);

		// operate on positive values
		if (!pos) {
			n = n.negate();
		}

		final BigInteger[] divrem = n.divideAndRemainder(q);
		BigInteger dv = divrem[0];
		final BigInteger r = divrem[1];

		// return if we don't need to round, independent of rounding mode
		if (r.isZero()) {
			// [typically not reached since remainder is not zero
			// with normalized that are not integerp]
			if (!pos) {
				dv = dv.negate();
			}

			return dv;
		}

		boolean up = false;
		final int comp = r.multiply(BigInteger.TWO).compareTo(q);

		switch (roundMode) {

		// Rounding mode to round away from zero.
		case BigDecimal.ROUND_UP:
			up = true;
			break;

		// Rounding mode to round towards zero.
		case BigDecimal.ROUND_DOWN:
			up = false;
			break;

		// Rounding mode to round towards positive infinity.
		case BigDecimal.ROUND_CEILING:
			up = pos;
			break;

		// Rounding mode to round towards negative infinity.
		case BigDecimal.ROUND_FLOOR:
			up = !pos;
			break;

		// Rounding mode to round towards "nearest neighbor" unless both
		// neighbors are equidistant, in which case round up.
		case BigDecimal.ROUND_HALF_UP:
			up = (comp >= 0);
			break;

		// Rounding mode to round towards "nearest neighbor" unless both
		// neighbors are equidistant, in which case round down.
		case BigDecimal.ROUND_HALF_DOWN:
			up = (comp > 0);
			break;

		// case BigDecimal.ROUND_HALF_CEILING:
		// up = (comp != 0 ? comp > 0 : pos);
		// break;
		//
		// case BigDecimal.ROUND_HALF_FLOOR:
		// up = (comp != 0 ? comp > 0 : !pos);
		// break;

		// Rounding mode to round towards the "nearest neighbor" unless both
		// neighbors are equidistant, in which case, round towards the even
		// neighbor.
		case BigDecimal.ROUND_HALF_EVEN:
			up = (comp != 0 ? comp > 0 : !dv.remainder(BigInteger.TWO).isZero());
			break;

		// case BigDecimal.ROUND_HALF_ODD:
		// up = (comp != 0 ? comp > 0 :
		// bigIntegerIsZero(dv.remainder(BIG_INTEGER_TWO)));
		// break;

		// Rounding mode to assert that the requested operation has an exact
		// result, hence no rounding is necessary. If this rounding mode is
		// specified on an operation that yields an inexact result, an
		// ArithmeticException is thrown.
		// case ROUND_UNNECESSARY:
		// if (!r.isZero()) {
		// throw new ArithmeticException("rounding necessary");
		// }
		// // [typically not reached due to earlier test for integerp]
		// up = false;
		// break;

		default:
			throw new IllegalArgumentException("unsupported rounding mode");
		}

		if (up) {
			dv = dv.add(BigInteger.ONE);
		}

		if (!pos) {
			dv = dv.negate();
		}

		// [rounding step, possible loss of precision step]
		return dv;
	}
}