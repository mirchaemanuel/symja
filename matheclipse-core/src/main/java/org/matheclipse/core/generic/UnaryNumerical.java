package org.matheclipse.core.generic;

import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.matheclipse.basic.Alloc;
import org.matheclipse.core.eval.EvalDouble;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.ISymbol;

import com.google.common.base.Function;

/**
 * Unary numerical function for functions like Plot
 * 
 * @see org.matheclipse.core.reflection.system.Plot
 */
public class UnaryNumerical implements Function<IExpr, IExpr>, DifferentiableUnivariateRealFunction {
	IExpr fFunction;

	ISymbol fVariable;

	EvalEngine fEngine;

	public UnaryNumerical(final IExpr fn, final ISymbol v, final EvalEngine engine) {
		fVariable = v;
		fFunction = fn;
		fEngine = engine;
	}

	public IExpr apply(final IExpr firstArg) {
		return F.evaln(AST.COPY.substitute(fFunction, fVariable, firstArg, 1));
	}

	public double value(double x) {
		double result = 0.0;
		Alloc alloc = Alloc.get();
		final double[] stack = alloc.vector(10);
		try {
			fVariable.pushLocalVariable(Num.valueOf(x));
			result = EvalDouble.eval(stack, 0, fFunction);
		} finally {
			fVariable.popLocalVariable();
			alloc.freeVector(10);
		}
		return result;
	}

	/**
	 * First derivative of unary function
	 */
	public UnivariateRealFunction derivative() {
		IExpr expr = F.eval(F.D, fFunction, fVariable);
		return new UnaryNumerical(expr, fVariable, fEngine);
	}

	public ComplexNum value(final ComplexNum z) {
		final Object temp = apply(z);
		if (temp instanceof ComplexNum) {
			return (ComplexNum) temp;
		}
		if (temp instanceof INum) {
			return ComplexNum.valueOf((INum) temp);
		}
		throw new ArithmeticException("Expected numerical complex value object!");
	}

	public INum value(final INum z) {
		final Object temp = apply(z);
		if (temp instanceof INum) {
			return (INum) temp;
		}
		throw new ArithmeticException("Expected numerical double value object!");
	}
}
