package org.matheclipse.core.expression;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;

import apache.harmony.math.BigInteger;
import apache.harmony.math.Rational;

/**
 * 
 */
public class FractionSym extends ExprImpl implements IFraction {
	// private static final ObjectFactory<FractionSym> FACTORY = new
	// ObjectFactory<FractionSym>() {
	// @Override
	// protected FractionSym create() {
	// if (Config.SERVER_MODE && currentQueue().getSize() >=
	// Config.FRACTION_MAX_POOL_SIZE) {
	// throw new PoolMemoryExceededException("FractionImpl",
	// currentQueue().getSize());
	// }
	// return new FractionSym();
	// }
	// };

	/**
	 * Be cautious with this method, no new internal rational is created
	 * 
	 * @param numerator
	 * @return
	 */
	protected static FractionSym newInstance(final Rational rational) {
		// FractionSym r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		// r = new FractionSym();
		// }
		FractionSym r = new FractionSym();
		r.fRational = rational;
		return r;
	}

	public static FractionSym valueOf(final BigInteger numerator) {
		// FractionSym r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		// r = new FractionSym();
		// }
		FractionSym r = new FractionSym();
		r.fRational = Rational.valueOf(numerator, BigInteger.ONE);
		return r;
	}

	public static FractionSym valueOf(final Rational rat) {
		// FractionSym r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		// r = new FractionSym();
		// }
		FractionSym r = new FractionSym();
		r.fRational = Rational.valueOf(rat);
		return r;
	}

	public static FractionSym valueOf(final BigInteger numerator, final BigInteger denominator) {
		// FractionSym r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		// r = new FractionSym();
		// }
		FractionSym r = new FractionSym();
		r.fRational = Rational.valueOf(numerator, denominator);
		return r;
	}

	public static FractionSym valueOf(final IInteger numerator, final IInteger denominator) {
		// FractionSym r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		// r = new FractionSym();
		// }
		FractionSym r = new FractionSym();
		r.fRational = Rational.valueOf(numerator.getBigNumerator(), denominator.getBigNumerator());
		return r;
	}

	public static FractionSym valueOf(final long numerator, final long denominator) {
		// FractionSym r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		// r = new FractionSym();
		// }
		FractionSym r = new FractionSym();
		r.fRational = Rational.valueOf(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
		return r;
	}

	public static FractionSym valueOf(final double value) {
		// FractionSym r;
		// if (Config.SERVER_MODE) {
		// r = FACTORY.object();
		// } else {
		// r = new FractionSym();
		// }
		FractionSym r = new FractionSym();
		r.fRational = Rational.valueOf(value);
		return r;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2396715994276842438L;

	/* package private */Rational fRational;

	private FractionSym() {
		fRational = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.matheclipse.parser.interfaces.INumber#isZero()
	 */
	public boolean isZero() {
		return fRational.getNumerator().equals(BigInteger.ZERO);
	}

	public boolean equalsInt(final int i) {
		return fRational.getNumerator().equals(BigInteger.valueOf(i)) && fRational.getDenominator().equals(BigInteger.ONE);
	}

	/**
	 * Returns the denominator of this fraction.
	 * 
	 * @return denominator
	 */
	public BigInteger getBigDenominator() {
		return fRational.getDenominator();
	}

	/**
	 * Returns the numerator of this Rational.
	 * 
	 * @return numerator
	 */
	public BigInteger getBigNumerator() {
		return fRational.getNumerator();
	}

	/**
	 * Returns the denominator of this fraction.
	 * 
	 * @return denominator
	 */
	public IInteger getDenominator() {
		return IntegerSym.valueOf(fRational.getDenominator());
	}

	/**
	 * Returns the numerator of this Rational.
	 * 
	 * @return numerator
	 */
	public IInteger getNumerator() {
		return IntegerSym.valueOf(fRational.getNumerator());
	}

	public int hierarchy() {
		return FRACTIONID;
	}

	public IFraction add(final IFraction parm1) {
		return newInstance(fRational.plus(((FractionSym) parm1).fRational));
	}

	public IFraction multiply(final IFraction parm1) {
		return newInstance(fRational.times(((FractionSym) parm1).fRational));
	}

	public boolean isNegative() {
		return (fRational.getNumerator().compareTo(BigInteger.ZERO) == -1);
	}

	public boolean isPositive() {
		return (fRational.getNumerator().compareTo(BigInteger.ZERO) == 1);
	}

	/**
	 * @return
	 */
	public FractionSym absNumber() {
		return newInstance(fRational.abs());
	}

	/**
	 * @param that
	 * @return
	 */
	public Rational add(final Rational that) {
		return fRational.plus(that);
	}

	/**
	 * @return
	 */
	// public byte byteValue() {
	// return fRational.byteValue();
	// }

	/**
	 * @param that
	 * @return
	 */
	public Rational divide(final Rational that) {
		return fRational.divide(that);
	}

	/**
	 * @return
	 */
	public double doubleValue() {
		return fRational.doubleValue();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof FractionSym) {
			return fRational.equals(((FractionSym) obj).fRational);
		}
		return false;
	}

	/**
	 * @return
	 */
	// public float floatValue() {
	// return fRational.floatValue();
	// }

	/**
	 * @return
	 */
	public BigInteger getDividend() {
		return fRational.getNumerator();
	}

	/**
	 * @return
	 */
	public BigInteger getDivisor() {
		return fRational.getDenominator();
	}

	@Override
	public int hashCode() {
		return fRational.hashCode();
	}

	/**
	 * @return
	 */
	// public int intValue() {
	// return fRational.intValue();
	// }

	/**
	 * @return
	 */
	public long longValue() {
		return fRational.longValue();
	}

	/**
	 * @param cs
	 */
	// @Override
	// public boolean move(final ObjectSpace os) {
	// if (super.move(os)) {
	// fRational.move(os);
	// return true;
	// }
	// return false;
	// }
	// public FractionSym copy() {
	// // FractionSym r;
	// // if (Config.SERVER_MODE) {
	// // r = FACTORY.object();
	// // } else {
	// // r = new FractionSym();
	// // }
	// FractionSym r = new FractionSym();
	// r.fRational = fRational.copy();
	// return r;
	// }
	//
	// public FractionSym copyNew() {
	// FractionSym r = new FractionSym();
	// r.fRational = fRational.copyNew();
	// return r;
	// }

	// @Override
	// public void recycle() {
	// fRational.recycle();
	// FACTORY.recycle(this);
	// }

	/**
	 * @param that
	 * @return
	 */
	public Rational multiply(final Rational that) {
		return fRational.times(that);
	}

	/**
	 * @return
	 */
	public ISignedNumber negate() {
		return newInstance(fRational.opposite());
	}

	/**
	 * @return
	 */
	@Override
	public IExpr opposite() {
		return newInstance(fRational.opposite());
	}

	/**
	 * @param that
	 * @return
	 */
	@Override
	public IExpr plus(final IExpr that) {
		if (that instanceof FractionSym) {
			return this.add((FractionSym) that);
		}
		if (that instanceof IntegerSym) {
			return this.add(valueOf(((IntegerSym) that).fInteger));
		}
		return super.plus(that);
	}

	public ISignedNumber minus(ISignedNumber that) {
		if (that instanceof FractionSym) {
			return this.add((FractionSym) that.negate());
		}
		if (isZero()) {
			return that.negate();
		}
		if (that instanceof IntegerSym) {
			return this.minus(valueOf(((IntegerSym) that).fInteger));
		}
		return Num.valueOf(doubleValue() - that.doubleValue());
	}

	/**
	 * @param exp
	 * @return
	 */
	// public IFraction pow(final int exp) {
	// return newInstance(fRational.pow(exp));
	// }

	/**
	 * Returns this number raised at the specified positive exponent.
	 * 
	 * @param exp
	 *          the positive exponent.
	 * @return <code>this<sup>exp</sup></code>
	 * @throws IllegalArgumentException
	 *           if <code>exp &lt;= 0</code>
	 */
	public IFraction pow(int exp) {
		if (exp <= 0)
			throw new IllegalArgumentException("exp: " + exp + " should be a positive number");
		IFraction powSqr = this;
		IFraction result = null;
		while (exp >= 1) { // Iteration.
			if ((exp & 1) == 1) {
				result = (result == null) ? powSqr : result.multiply(powSqr);
			}
			powSqr = powSqr.multiply(powSqr);
			exp >>>= 1;
		}
		return result;
	}

	/**
	 * @return
	 */
	@Override
	public IExpr inverse() {
		return newInstance(fRational.inverse());
	}

	/**
	 * @return
	 */
	public BigInteger round() {
		return fRational.round();
	}

	/**
	 * @return
	 */
	// public short shortValue() {
	// return fRational.shortValue();
	// }

	/**
	 * @param that
	 * @return
	 */
	public Rational subtract(final Rational that) {
		return fRational.minus(that);
	}

	/**
	 * @param that
	 * @return
	 */
	@Override
	public IExpr times(final IExpr that) {
		if (that instanceof FractionSym) {
			return this.multiply((FractionSym) that);
		}
		if (that instanceof IntegerSym) {
			return this.multiply(valueOf(((IntegerSym) that).fInteger));
		}
		return super.times(that);
	}

	public String internalFormString(boolean callSymbolFactory) {
		int numerator = fRational.getNumerator().intValue();
		int denominator = fRational.getDenominator().intValue();
		if (numerator == 1) {
			switch (denominator) {
			case 2:
				return "C1D2";
			case 3:
				return "C1D3";
			case 4:
				return "C1D4";
			}
		}
		if (numerator == -1) {
			switch (denominator) {
			case 2:
				return "CN1D2";
			case 3:
				return "CN1D3";
			case 4:
				return "CN1D4";
			}
		}
		return "fraction(" + numerator + "L," + denominator + "L)";
	}

	/**
	 * @return
	 */
	// public Text toText() {
	// return
	// fRational.getDividend().toText().concat(Text.valueOf('/')).concat(fRational
	// .getDivisor().toText());
	// }
	@Override
	public String toString() {
		return fRational.getNumerator().toString() + "/" + fRational.getDenominator().toString();
		// return toText().toString();
	}

	@Override
	public String fullFormString() {
		StringBuffer buf = new StringBuffer("Rational[");
		buf.append(fRational.getNumerator().toString().toString());
		buf.append(',');
		buf.append(fRational.getDenominator().toString().toString());
		buf.append(']');
		return buf.toString();
	}

	/**
	 * @param radix
	 * @return
	 */
	// public Text toText(final int radix) {
	// return fRational.toText(radix);
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.matheclipse.parser.interfaces.IFraction#getRational()
	 */
	public Rational getRational() {
		return fRational;
	}

	public int sign() {
		return fRational.getNumerator().signum();
	}

	public int complexSign() {
		return sign();
	}

	public ISignedNumber ceil() {

		BigInteger[] result = fRational.getNumerator().divideAndRemainder(fRational.getDenominator());
		// final BigInteger[] result = new BigInteger[2];
		// result[0] = fRational.getDividend().divide(fRational.getDivisor());
		// result[1] = result[0].getRemainder();

		if (!result[1].isPositive()) {
			return IntegerSym.valueOf(result[0]);
		}

		return IntegerSym.valueOf(result[0].plus(BigInteger.ONE));
	}

	public ISignedNumber floor() {
		BigInteger[] result = fRational.getNumerator().divideAndRemainder(fRational.getDenominator());
		// final BigInteger[] result = new BigInteger[2];
		// result[0] = fRational.getDividend().divide(fRational.getDivisor());
		// result[1] = result[0].getRemainder();

		if (!result[1].isNegative()) {
			return IntegerSym.valueOf(result[0]);
		}

		return IntegerSym.valueOf(result[0].minus(BigInteger.ONE));

	}

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(final IExpr obj) {
		if (obj instanceof FractionSym) {
			return fRational.compareTo(((FractionSym) obj).fRational);
		}
		if (obj instanceof IntegerSym) {
			return fRational.compareTo(Rational.valueOf(((IntegerSym) obj).fInteger, BigInteger.ONE));
		}
		return (hierarchy() - (obj).hierarchy());
	}

	public boolean isLessThan(ISignedNumber obj) {
		if (obj instanceof FractionSym) {
			return fRational.compareTo(((FractionSym) obj).fRational) < 0;
		}
		if (obj instanceof IntegerSym) {
			return fRational.compareTo(Rational.valueOf(((IntegerSym) obj).fInteger, BigInteger.ONE)) < 0;
		}
		return fRational.doubleValue() < obj.doubleValue();
	}

	public boolean isGreaterThan(ISignedNumber obj) {
		if (obj instanceof FractionSym) {
			return fRational.compareTo(((FractionSym) obj).fRational) > 0;
		}
		if (obj instanceof IntegerSym) {
			return fRational.compareTo(Rational.valueOf(((IntegerSym) obj).fInteger, BigInteger.ONE)) > 0;
		}
		return fRational.doubleValue() < obj.doubleValue();
	}

	public ISymbol head() {
		return F.RationalHead;
	}

	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public boolean accept(IVisitorBoolean visitor) {
		return visitor.visit(this);
	}

	public int accept(IVisitorInt visitor) {
		return visitor.visit(this);
	}
}