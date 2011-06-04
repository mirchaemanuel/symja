package org.matheclipse.core.expression;

import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;

import apache.harmony.math.BigInteger;
import apache.harmony.math.Rational;

/**
 * A concrete complex implementation
 * 
 */
public class ComplexSym extends ExprImpl implements IComplex {
	//
	// protected static final XmlFormat FRACTION_XML = new
	// XmlFormat(ComplexImpl.class) {
	// public void format(Object obj, XmlElement xml) {
	// ComplexImpl expr = (ComplexImpl) obj;
	// FastList list = xml.getContent();
	// list.add(expr._real);
	// list.add(expr._imaginary);
	// }
	//
	// public Object parse(XmlElement xml) {
	// ComplexImpl expr = (ComplexImpl) xml.object();
	// FastList list = xml.getContent();
	// expr._real = (IFraction) list.get(0);
	// expr._imaginary = (IFraction) list.get(1);
	// return expr;
	// }
	// };

	/**
	 * 
	 */
	private static final long serialVersionUID = 1489050560741527824L;

	private Rational _real;

	private Rational _imaginary;

	/**
	 * Holds the factory constructing complex instances.
	 */
	// private static final ObjectFactory<ComplexSym> FACTORY = new
	// ObjectFactory<ComplexSym>() {
	// @Override
	// public ComplexSym create() {
	// if (Config.SERVER_MODE && currentQueue().getSize() >=
	// Config.COMPLEX_MAX_POOL_SIZE) {
	// throw new PoolMemoryExceededException("ComplexImpl",
	// currentQueue().getSize());
	// }
	// return new ComplexSym();
	// }
	// };
	// write this object after the FACTORY initialization
	public final static ComplexSym ZERO = ComplexSym.valueOf(Rational.ZERO);

	private ComplexSym() {
	}

	public static ComplexSym valueOf(final BigInteger real, final BigInteger imaginary) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = Rational.valueOf(real, BigInteger.ONE);
		c._imaginary = Rational.valueOf(imaginary, BigInteger.ONE);
		return c;
	}

	public static ComplexSym valueOf(final BigInteger real) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = Rational.valueOf(real, BigInteger.ONE);
		c._imaginary = Rational.ZERO;
		return c;
	}

	public static ComplexSym valueOf(final IInteger real, final IInteger imaginary) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = Rational.valueOf(real.getBigNumerator(), BigInteger.ONE);
		c._imaginary = Rational.valueOf(imaginary.getBigNumerator(), BigInteger.ONE);
		return c;
	}

	public static ComplexSym valueOf(final IInteger real) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = Rational.valueOf(real.getBigNumerator(), BigInteger.ONE);
		c._imaginary = Rational.ZERO;
		return c;
	}

	public static ComplexSym valueOf(final Rational real) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = real;
		c._imaginary = Rational.ZERO;
		return c;
	}

	public static ComplexSym valueOf(final Rational real, final Rational imaginary) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = real;
		c._imaginary = imaginary;
		return c;
	}

	public static ComplexSym valueOf(final long real_numerator, final long real_denominator, final long imag_numerator,
			final long imag_denominator) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = Rational.valueOf(real_numerator, real_denominator);
		c._imaginary = Rational.valueOf(imag_numerator, imag_denominator);
		return c;
	}

	public static ComplexSym valueOf(final IFraction real) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = real.getRational();
		c._imaginary = Rational.ZERO;
		return c;
	}

	public static ComplexSym valueOf(final IFraction real, final IFraction imaginary) {
		final ComplexSym c = new ComplexSym();// FACTORY.object();
		c._real = real.getRational();
		c._imaginary = imaginary.getRational();
		return c;
	}

	public IComplex conjugate() {
		return ComplexSym.valueOf(_real, _imaginary.opposite());
	}

	public IExpr eabs() {
		return F.Sqrt(FractionSym.valueOf(_real.times(_real).plus(_imaginary.times(_imaginary))));
	}

	public ComplexSym add(final ComplexSym parm1) throws java.lang.ArithmeticException {
		return ComplexSym.valueOf(_real.plus(parm1._real), _imaginary.plus(parm1._imaginary));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.matheclipse.parser.interfaces.IComplex#add(org.matheclipse.parser.
	 * interfaces.IComplex)
	 */
	public IComplex add(final IComplex parm1) {
		return ComplexSym.valueOf(_real.plus(parm1.getRealPart()), _imaginary.plus(parm1.getImaginaryPart()));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ComplexSym) {
			return _real.equals(((ComplexSym) obj)._real) && _imaginary.equals(((ComplexSym) obj)._imaginary);
		}
		return false;
	}

	/**
	 * Returns the imaginary part of a complex number
	 * 
	 * @return imaginary part
	 */
	public Rational getImaginaryPart() {
		return _imaginary;
	}

	public IFraction getIm() {
		return FractionSym.valueOf(_imaginary);
	}

	/**
	 * Returns the real part of a complex number
	 * 
	 * @return real part
	 */
	public Rational getRealPart() {
		return _real;
	}

	public IFraction getRe() {
		return FractionSym.valueOf(_real);
	}

	@Override
	public int hashCode() {
		return _real.hashCode() ^ _imaginary.hashCode();
	}

	public int hierarchy() {
		return COMPLEXID;
	}

	@Override
	public boolean isZero() {
		return NumberUtil.isZero(_real) && NumberUtil.isZero(_imaginary);
	}

	public IComplex multiply(final IComplex parm1) {
		return ComplexSym.valueOf(_real.times(parm1.getRealPart()).minus(_imaginary.times(parm1.getImaginaryPart())), _real.times(
				parm1.getImaginaryPart()).plus(parm1.getRealPart().times(_imaginary)));
	}

	public IComplex pow(final int parm1) {
		int temp = parm1;

		if ((parm1 == 0) && _real.equals(Rational.ZERO) && _imaginary.equals(Rational.ZERO)) {
			throw new java.lang.ArithmeticException();
		}

		if (parm1 == 1) {
			return this;
		}

		IComplex res = ComplexSym.valueOf(Rational.ONE, Rational.ZERO);

		if (parm1 < 0) {
			temp *= -1;
		}

		for (int i = 0; i < temp; i++) {
			res = res.multiply(this);
		}

		if (parm1 < 0) {
			final Rational d = res.getRealPart().times(res.getRealPart()).plus(res.getImaginaryPart().times(res.getImaginaryPart()));

			return ComplexSym.valueOf(res.getRealPart().divide(d), res.getImaginaryPart().opposite().divide(d));
		}

		return res;
	}

	@Override
	public IExpr plus(final IExpr that) {
		if (that instanceof ComplexSym) {
			return this.add((ComplexSym) that);
		}
		return super.plus(that);
	}

	@Override
	public IExpr opposite() {
		return ComplexSym.valueOf(_real.opposite(), _imaginary.opposite());
	}

	@Override
	public IExpr times(final IExpr that) {
		if (that instanceof ComplexSym) {
			return multiply((ComplexSym) that);
		}
		return super.times(that);
	}

	@Override
	public IExpr inverse() {
		final Rational tmp = (_real.times(_real)).plus((_imaginary.times(_imaginary)));
		return ComplexSym.valueOf(_real.divide(tmp), _imaginary.opposite().divide(tmp));
	}

	public ComplexSym copyNew() {
		ComplexSym r = new ComplexSym();
		r._real = _real.copyNew();
		r._imaginary = _imaginary.copyNew();
		return r;
	}

	@Override
	public String toString() {
		final StringBuilder tb = new StringBuilder();
		tb.append('(');
		tb.append(_real.toString());
		tb.append(")+I*(");
		tb.append(_imaginary.toString());
		tb.append(')');
		return tb.toString();
	}

	@Override
	public String fullFormString() {
		StringBuffer buf = new StringBuffer("Complex[");
		if (_real.getDenominator().equals(BigInteger.ONE)) {
			buf.append(_real.getNumerator().toString());
		} else {
			buf.append("Rational[");
			buf.append(_real.getNumerator().toString().toString());
			buf.append(',');
			buf.append(_real.getDenominator().toString().toString());
			buf.append(']');
		}
		buf.append(',');

		if (_imaginary.getDenominator().equals(BigInteger.ONE)) {
			buf.append(_imaginary.getNumerator().toString());
		} else {
			buf.append("Rational[");
			buf.append(_imaginary.getNumerator().toString().toString());
			buf.append(',');
			buf.append(_imaginary.getDenominator().toString().toString());
			buf.append(']');
		}
		buf.append(']');
		return buf.toString();
	}

	@Override
	public String internalFormString(boolean symbolsAsFactoryMethod, int depth) {
		int real_numerator = _real.getNumerator().toInt();
		int real_denominator = _real.getDenominator().toInt();
		int imag_numerator = _imaginary.getNumerator().toInt();
		int imag_denominator = _imaginary.getDenominator().toInt();
		if (_real.equals(Rational.ZERO)) {
			if (_imaginary.equals(Rational.ONE)) {
				return "CI";
			}
			if (_imaginary.equals(Rational.MINUS_ONE)) {
				return "CNI";
			}
		}
		return "complex(" + real_numerator + "L," + real_denominator + "L," + imag_numerator + "L," + imag_denominator + "L)";
	}

	public INumber normalize() {
		if (_imaginary.equals(Rational.ZERO)) {
			if (_real.getDenominator().equals(BigInteger.ZERO)) {
				return IntegerSym.valueOf(_real.getNumerator());
			}
			return FractionSym.newInstance(_real);
		}
		return this;
	}

	public int complexSign() {
		final int i = _real.getNumerator().signum();

		if (i == 0) {
			return _imaginary.getNumerator().signum();
		}

		return i;
	}

	public static void main(final String[] args) {
		final ComplexSym c = ComplexSym.valueOf(BigInteger.ZERO, BigInteger.ONE);
		final IExpr e = c.times(c);
		System.out.println(e);
	}

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(final IExpr obj) {
		if (obj instanceof ComplexSym) {
			final int cp = _real.compareTo(((ComplexSym) obj)._real);
			if (cp != 0) {
				return cp;
			}
			return _imaginary.compareTo(((ComplexSym) obj)._imaginary);
		}
		return (hierarchy() - (obj).hierarchy());
	}

	@Override
	public ISymbol head() {
		return F.ComplexHead;
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

	@Override
	public boolean equalsInt(int i) {
		return false;
	}
}