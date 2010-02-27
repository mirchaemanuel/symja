package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexUtils;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * Arcsine
 * 
 * See <a href="http://en.wikipedia.org/wiki/Inverse_trigonometric functions">Inverse_trigonometric functions</a>
 */
public class ArcSin extends AbstractTrigArg1 implements INumeric {
//	final static String[] RULES = { "ArcSin[0]=0", "ArcSin[1/2]=Pi/6", "ArcSin[2^(1/2)/2]=Pi/4", "ArcSin[3^(1/2)/2]=Pi/3",
//			"ArcSin[1]=Pi/2", "ArcSin[I]=I*ArcSinh[1]", "ArcSin[Infinity]=(-I)*Infinity", "ArcSin[1/2*3^(1/2)]=1/3*Pi",
//			"ArcSin[1/2*(2+2^(1/2))^(1/2)]=3/8*Pi", "ArcSin[1/2*(2-2^(1/2))^(1/2)]=1/8*Pi", "ArcSin[1/2*2^(1/2)]=1/4*Pi",
//			"ArcSin[1/4*5^(1/2)+1/4]=3/10*Pi", "ArcSin[1/4*6^(1/2)*(1+1/3*3^(1/2))]=5/12*Pi",
//			"ArcSin[1/4*2^(1/2)*(5-5^(1/2))^(1/2)]=1/5*Pi", "ArcSin[1/4*6^(1/2)*(1-1/3*3^(1/2))]=1/12*Pi",
//			"ArcSin[1/4*5^(1/2)-1/4]=1/10*Pi", "ArcSin[1/4*2^(1/2)*(5+5^(1/2))^(1/2)]=2/5*Pi",
//			"ArcSin[x_NumberQ]:= -ArcSin[-x] /; SignCmp[x]<0", "ArcSin[x_NumberQ*y_]:= -ArcSin[-x*y] /; SignCmp[x]<0" };
//
//	@Override
//	public String[] getRules() {
//		return RULES;
//	}
	public ArcSin() { 
	} 

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.asin(arg1.getRealPart()));
	}
 
	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return ComplexUtils.asin(arg1);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.asin(stack[top]);
	}
	
	@Override
  public void setUp(final ISymbol symbol) throws SyntaxError {
    symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
    super.setUp(symbol);
  }
}
