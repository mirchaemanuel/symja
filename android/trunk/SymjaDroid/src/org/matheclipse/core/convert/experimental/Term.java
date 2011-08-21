/**
 * 
 */
package org.matheclipse.core.convert.experimental;

import java.math.BigInteger;

public class Term {
	final BigInteger fCoefficient;

	final int fExponent;

	/**
	 * ZERO term constructor
	 */
	public Term() {
		this(0, BigInteger.ZERO);
	}

	public Term(int exponent, BigInteger coefficient) {
		fCoefficient = coefficient;
		fExponent = exponent;
	}

	public BigInteger getCoefficient() {
		return fCoefficient;
	}

	public int getExponent() {
		return fExponent;
	}

	@Override
	public String toString() {
		return "("+fCoefficient.toString()+"x^"+fExponent+")";
	}
}