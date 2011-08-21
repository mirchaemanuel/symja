package org.matheclipse.core.interfaces;

/**
 * A numeric (double) number.
 * 
 */
public interface INum extends ISignedNumber {
	public double getRealPart();

	public INum add(INum val);

	public INum multiply(INum val);

	public INum pow(INum val);

	/** {@inheritDoc} */
	public boolean isNumIntValue();

	/**
	 * Returns the value of this number as an <code>int</code> (by simply casting
	 * to type <code>int</code>).
	 * 
	 * @return
	 */
	public int intValue();
}
