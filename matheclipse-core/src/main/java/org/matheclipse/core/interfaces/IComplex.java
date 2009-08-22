package org.matheclipse.core.interfaces;

import apache.harmony.math.Rational;

/**
 * An expression representing a complex number
 * 
 */
public interface IComplex extends IBigNumber {
	public IComplex conjugate();

	public IComplex add(IComplex val);

	/**
	 * Returns the imaginary part of a complex number
	 * 
	 * @return imaginary part
	 */
	public Rational getImaginaryPart();

	public IFraction getIm();
	
	/**
	 * Returns the real part of a complex number
	 * 
	 * @return real part
	 */
	public Rational getRealPart();
	
	public IFraction getRe();
	
	public boolean isZero();

	public IComplex multiply(IComplex val);

	public IComplex pow(int parm1);

	public INumber normalize();

	public IExpr reciprocal();
}
