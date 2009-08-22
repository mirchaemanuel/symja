package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexUtils;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Sine
 * 
 * See <a href="http://en.wikipedia.org/wiki/Trigonometric_functions">Trigonometric functions</a>
 */
public class Sin extends AbstractTrigArg1 implements INumeric {

	// final static String[] RULES = {
	// "Sin[0]=0",
	// "Sin[1/6*Pi]=1/2",
	// "Sin[1/4*Pi]=2^(1/2)/2",
	// "Sin[1/3*Pi]=3^(1/2)/2",
	// "Sin[1/2*Pi]=1",
	// "Sin[Pi]=0",
	// "Sin[5/12*Pi]=1/4*6^(1/2)*(1+1/3*3^(1/2))",
	// "Sin[Pi/5]=1/4*2^(1/2)*(5-5^(1/2))^(1/2)",
	// "Sin[Pi/12]=1/4*6^(1/2)*(1-1/3*3^(1/2))",
	// "Sin[Pi/10]=1/4*5^(1/2)-1/4",
	// "Sin[2/5*Pi]=1/4*2^(1/2)*(5+5^(1/2))^(1/2)",
	// "Sin[3/10*Pi]=1/4*5^(1/2)+1/4",
	// "Sin[3/8*Pi]=1/2*(2+2^(1/2))^(1/2)",
	// "Sin[1/8*Pi]=1/2*(2-2^(1/2))^(1/2)",
	// "Sin[I]=I*Sinh[1]",
	// "Sin[ArcSin[x_]]:=x",
	// "Sin[ArcCos[x_]]:=(1-x^2)^(1/2)",
	// "Sin[ArcTan[x_]]:=x/(1+x^2)^(1/2)",
	// "Sin[x_NumberQ]:= -Sin[-x] /; SignCmp[x]<0",
	// "Sin[x_NumberQ*y_]:=-Sin[-x*y] /; SignCmp[x]<0",
	// "Sin[x_NumberQ*Pi]:=If[x<1, Sin[(1-x)*Pi],If[x<2,-Sin[(2-x)*Pi],
	// Sin[(x-2*Quotient[Trunc[x],2])*Pi] ] ] /; x>=1/2"
	// };

	public Sin() {
	}

	// @Override
	// public String[] getRules() {
	// return RULES;
	// }

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.sin(arg1.getRealPart()));
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return ComplexUtils.sin(arg1);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.sin(stack[top]);
	}

}
