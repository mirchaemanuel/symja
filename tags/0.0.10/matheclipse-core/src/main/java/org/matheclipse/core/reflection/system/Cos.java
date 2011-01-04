package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.ComplexUtils;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * Cosine function
 * 
 * See <a href="http://en.wikipedia.org/wiki/Trigonometric_functions">Trigonometric functions</a>
 */
public class Cos extends AbstractTrigArg1 implements INumeric {
//	final static String[] RULES = {
//	 
//			"Cos[Pi/6]=3^(1/2)/2",
//			"Cos[Pi/4]=2^(1/2)/2",
//			"Cos[Pi/3]=1/2",
//			"Cos[Pi/2]=0",
//			"Cos[Pi]=-1",
//			"Cos[I]=Cosh[1]",
//			"Cos[5/12*Pi]=1/4*6^(1/2)*(1-1/3*3^(1/2))",
//			"Cos[1/5*Pi]=1/4*5^(1/2)+1/4",
//			"Cos[1/12*Pi]=1/4*6^(1/2)*(1+1/3*3^(1/2))",
//			"Cos[1/10*Pi]=1/4*2^(1/2)*(5+5^(1/2))^(1/2)",
//			"Cos[2/5*Pi]=1/4*5^(1/2)-1/4",
//			"Cos[3/10*Pi]=1/4*2^(1/2)*(5-5^(1/2))^(1/2)",
//			"Cos[3/8*Pi]=1/2*(2-2^(1/2))^(1/2)",
//			"Cos[1/8*Pi]=1/2*(2+2^(1/2))^(1/2)",
//			"Cos[ArcCos[x_]]:=x",
//			"Cos[ArcSin[x_]]:=(1-x^2)^(1/2)",
//			"Cos[ArcTan[x_]]:=1/(1+x^2)^(1/2)",
//			"Cos[ACot[x_]]:=x /(1+x**2)**1/2",
//			"Cos[x_NumberQ]:= Cos[-x]  /; SignCmp[x]<0",
//			"Cos[x_NumberQ*y_]:= Cos[-x*y]  /; SignCmp[x]<0",
//			"Cos[x_NumberQ*Pi]:=If[x<1,-Cos[(1-x)*Pi],If[x<2,Cos[(2-x)*Pi],Cos[(x-2*Quotient[Trunc[x],2])*Pi]]] /; x>=1/2" };
//
//	@Override
//	public String[] getRules() {
//		return RULES;
//	}

	public Cos() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.cos(arg1.getRealPart()));
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return ComplexUtils.cos(arg1);
	}
 
	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.cos(stack[top]);
	}

//	public IExpr evalInteger(IInteger i) {
//		if (i.equals(F.C0)) {
//			return F.C1;
//		}
//		return null;
//	}

//	public void setUp(ISymbol symbol) throws SyntaxError {
//		super.setUp(symbol);
//		createRuleFromMethodName(symbol, "Cos[x_Integer]", "evalInteger");
//	}
	@Override
  public void setUp(final ISymbol symbol) throws SyntaxError {
    symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
    super.setUp(symbol);
  }
}
