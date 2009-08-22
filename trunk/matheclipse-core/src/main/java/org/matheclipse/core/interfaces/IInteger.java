package org.matheclipse.core.interfaces;

import apache.harmony.math.BigInteger;
/**
 * An expression representing a big integer number
 *
 */
public interface IInteger extends IRational {

	public IInteger add(IInteger val);

	/**
	 * Returns the numerator of this Rational.
	 *
	 * @return numerator
	 */
	public BigInteger getBigNumerator(); 

	/**
	 * Multiply this integer with value
	 *
	 * @param value
	 * @return
	 */
	public IInteger multiply(IInteger value);

	public IInteger pow(int exponent);

	/**
     * Converts this large integer to <code>int</code>; unlike
     * {@link #toInt} this method raises no exception, if this
     * integer cannot be represented by an <code>int</code> type.
     *
     * @return the numeric value represented by this integer after conversion
     *         to type <code>int</code>.
     */
	public int intValue();

	/**
     * Converts this large integer to <code>int</code>; unlike
     * {@link #fIntValue} this method raises {@link ArithmeticException} if this
     * integer cannot be represented by an <code>int</code> type.
     *
     * @return the numeric value represented by this integer after conversion
     *         to type <code>int</code>.
     * @throws ArithmeticException if conversion to <code>int</code> is not
     *         possible.
     */
	public int toInt() throws ArithmeticException;

	public long longValue();

	/**
   * Returns the greatest common divisor of this large integer and
   * the one specified.
   *
   */
	public IInteger gcd(IInteger val);
	
	/**
   * Returns the least common multiple of this large integer and
   * the one specified.
   *
   */
	public IInteger lcm(IInteger val);
	
	/**
	 * Returns the n-th integer root
	 * 
	 * @param n
	 * @return
	 * @throws ArithmeticException
	 */
	public IInteger nthRoot(int n) throws ArithmeticException;
}
