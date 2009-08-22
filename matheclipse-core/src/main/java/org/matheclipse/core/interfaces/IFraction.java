package org.matheclipse.core.interfaces;


import apache.harmony.math.BigInteger;
import apache.harmony.math.Rational;
/**
 * interface for "fractional" numbers
 * 
 */
public interface IFraction extends IRational {

	 public IFraction add(IFraction parm1);
	//
	// public IFraction divide(IFraction parm1);

	public Rational getRational();

	/**
	 * Returns the denominator of this fraction.
	 * 
	 * @return denominator
	 */
	public BigInteger getBigDenominator();

	/**
	 * Returns the numerator of this fraction.
	 * 
	 * @return denominator
	 */
	public BigInteger getBigNumerator();

	 public IFraction multiply(IFraction parm1);
	//
	// public IFraction subtract(IFraction parm1);
	 
	 public IFraction pow(final int exp);
}
