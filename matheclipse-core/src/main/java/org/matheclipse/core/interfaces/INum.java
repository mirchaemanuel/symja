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
}
