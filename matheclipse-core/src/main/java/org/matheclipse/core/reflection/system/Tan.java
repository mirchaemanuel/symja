package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexUtils;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Tan
 * 
 * See <a href="http://en.wikipedia.org/wiki/Trigonometric_functions">Trigonometric functions</a>
 */
public class Tan extends AbstractTrigArg1 implements INumeric {
	// final static String[] RULES = { "Tan[Pi]=0", "Tan[I]=I*Tanh[1]",
	// "Tan[0]=0", "Tan[Pi/6]=3^(1/2)/3", "Tan[Pi/4]=1",
	// "Tan[Pi/3]=3^(1/2)",
	// // "Tan[Pi/2]:=Throw(SingularityError,Tan)",
	// "Tan[5/12*Pi]=2+3^(1/2)", "Tan[1/5*Pi]=(5-2*5^(1/2))^(1/2)",
	// "Tan[1/12*Pi]=2-3^(1/2)",
	// "Tan[1/10*Pi]=1/5*5^(1/2)*(5-2*5^(1/2))^(1/2)",
	// "Tan[2/5*Pi]=(5+2*5^(1/2))^(1/2)",
	// "Tan[3/10*Pi]=1/5*5^(1/2)*(5+2*5^(1/2))^(1/2)", "Tan[1/3*Pi]=3^(1/2)",
	// "Tan[3/8*Pi]=2^(1/2)+1", "Tan[1/8*Pi]=2^(1/2)-1",
	// "Tan[ArcSin[x_]]:=x/(1-x^2)^(1/2)", "Tan[ArcCos[x_]]:=(1-x^2)^(1/2)/x",
	// "Tan[ArcTan[x_]]:=x", "Tan[ACot[x_]]:=1/x",
	// "Tan[x_NumberQ]:= -Tan[-x] /; SignCmp[x]<0", "Tan[x_NumberQ*y_]:=
	// -Tan[-x*y] /; SignCmp[x]<0",
	// "Tan[x_NumberQ*Pi]:=If[x<1,-Tan[(1-x)*Pi],If[x<2,Tan[(x-1)*Pi],Tan[(x-2*Quotient[Trunc[x],2])*Pi]]]
	// /; x>1/2"
	//
	// };
	//
	// @Override
	// public String[] getRules() {
	// return RULES;
	// }

	public Tan() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.tan(arg1.getRealPart()));
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return ComplexUtils.tan(arg1);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.tan(stack[top]);
	}
}
