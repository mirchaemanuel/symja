package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Times;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISignedNumber;

import apache.harmony.math.Rational;

public class Arg extends AbstractTrigArg1 implements INumeric {

//	@Override
//	public String[] getRules() {
//		return null;
//	}

	public Arg() {
	}

	@Override
	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1 instanceof ISignedNumber) {
			final ISignedNumber in = (ISignedNumber) arg1;
			if (in.isNegative()) {
				return F.Pi;
			} else if (!in.equals(F.C0)) {
				return F.C0;
			}
		} else if (arg1 instanceof IComplex) {
			final IComplex ic = (IComplex) arg1;
			if (ic.getRealPart().equals(Rational.ZERO)) {
				final Rational imaginaryPart = ic.getImaginaryPart();
				if (Rational.ZERO.isLargerThan(imaginaryPart)) {
					return Times(F.CN1D2, F.Pi);
				} else if (imaginaryPart.isLargerThan(Rational.ZERO)) {
					return Times(F.C1D2, F.Pi);
				}
			}
		}
		return null;
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		final double d = arg1.getRealPart();
		if (d < 0) {
			return F.num(Math.PI);
		} else if (d > 0) {
			return F.CD0;
		}
		return null;
	}

//	public IExpr numericEvalDC1(AbstractExpressionFactory f, DoubleComplexImpl arg1) {
//		return f.createDouble(arg1.argument());
//	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		if (stack[top] < 0) {
			return Math.PI;
		} else if (stack[top] > 0) {
			return 0;
		}
		throw new UnsupportedOperationException();
	}

}
