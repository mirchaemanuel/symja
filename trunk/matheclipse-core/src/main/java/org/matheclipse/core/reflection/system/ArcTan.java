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
 * Arctangent
 * 
 * See <a href="http://en.wikipedia.org/wiki/Inverse_trigonometric functions">Inverse_trigonometric functions</a>
 */
public class ArcTan extends AbstractTrigArg1 implements INumeric {
	// final static String[] RULES = { "ArcTan[0]=0", "ArcTan[0, 0]=0",
	// "ArcTan[1]=Pi/4", "ArcTan[3^(1/2)]=Pi/3",
	// "ArcTan[3^(1/2)/3]=Pi/6", "ArcTan[Infinity]=Pi/2",
	// "ArcTan[2^(1/2)-1]=1/8*Pi",
	// "ArcTan[1/5*5^(1/2)*(5-2*5^(1/2))^(1/2)]=1/10*Pi",
	// "ArcTan[2-3^(1/2)]=1/12*Pi",
	// "ArcTan[1/5*5^(1/2)*(5+2*5^(1/2))^(1/2)]=3/10*Pi",
	// "ArcTan[2+3^(1/2)]=5/12*Pi", "ArcTan[2^(1/2)+1]=3/8*Pi",
	// "ArcTan[(5+2*5^(1/2))^(1/2)]=2/5*Pi",
	// "ArcTan[(5-2*5^(1/2))^(1/2)]=1/5*Pi",
	// "ArcTan[x_NumberQ]:= -ArcTan[-x] /; SignCmp[x]<0",
	// "ArcTan[x_NumberQ*y_]:= -ArcTan[-x*y] /; SignCmp[x]<0" };
	//
	// @Override
	// public String[] getRules() {
	// return RULES;
	// }

	public ArcTan() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.atan(arg1.getRealPart()));
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return ComplexUtils.atan(arg1);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.atan(stack[top]);
	}
	
	@Override
  public void setUp(final ISymbol symbol) throws SyntaxError {
    symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
    super.setUp(symbol);
  }
}
