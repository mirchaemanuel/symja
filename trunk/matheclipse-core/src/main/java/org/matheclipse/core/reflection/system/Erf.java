package org.matheclipse.core.reflection.system;

import org.apache.commons.math.MathException;
import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IExpr;

public class Erf extends AbstractTrigArg1 implements INumeric {
	public Erf() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		try {
			return Num.valueOf(org.apache.commons.math.special.Erf.erf(arg1.getRealPart()));
		} catch (final MathException e) {
		}
		return null;
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return null;
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		try {
			return org.apache.commons.math.special.Erf.erf(stack[top]);
		} catch (final MathException e) {
		}
		throw new UnsupportedOperationException();
	}

}
