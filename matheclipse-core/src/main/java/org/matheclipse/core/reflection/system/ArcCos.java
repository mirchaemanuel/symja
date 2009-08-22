package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexUtils;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Arccosine
 * 
 * See <a href="http://en.wikipedia.org/wiki/Inverse_trigonometric functions">Inverse_trigonometric functions</a>
 */
public class ArcCos extends AbstractTrigArg1 implements INumeric {
//	final static String[] RULES = {
//			"ArcCos[0]=Pi/2", "ArcCos[1]=0", "ArcCos[I]=Pi/2-I*ArcSinh[1]", "ArcCos[2^(1/2)/2]=Pi/4", "ArcCos[3^(1/2)/2]=Pi/6",
//			"ArcCos[DirectedInfinity[1]]=I*Infinity", "ArcCos[1/2*(2+2^(1/2))^(1/2)]=1/8*Pi", "ArcCos[1/2*(2-2^(1/2))^(1/2)]=3/8*Pi",
//			"ArcCos[1/4*5^(1/2)+1/4]=1/5*Pi", "ArcCos[1/4*6^(1/2)*(1+1/3*3^(1/2))]=1/12*Pi",
//			"ArcCos[1/4*2^(1/2)*(5-5^(1/2))^(1/2)]=3/10*Pi", "ArcCos[1/4*6^(1/2)*(1-1/3*3^(1/2))]=5/12*Pi", "ArcCos[Cosh[1]]=I",
//			"ArcCos[1/4*5^(1/2)-1/4]=2/5*Pi", "ArcCos[1/4*2^(1/2)*(5+5^(1/2))^(1/2)]=1/10*Pi", "ArcCos[1/2]=1/3*Pi",
//	// "ArcCos[x_NumberQ]:= Pi-ArcCos[-x] /; SignCmp[x]<0 ",
//	// "ArcCos[x_NumberQ*y_]:= Pi-ArcCos[-x*y] /; SignCmp[x]<0"
//	};

	public ArcCos() {
	}

//	@Override
//	public String[] getRules() {
//		return RULES;
//	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.acos(arg1.getRealPart()));
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return ComplexUtils.acos(arg1);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.acos(stack[top]);
	}
}
