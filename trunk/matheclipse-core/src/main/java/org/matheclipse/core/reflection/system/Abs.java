package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.*;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * Absolute value of a number.
 * See <a href="http://en.wikipedia.org/wiki/Absolute_value">Wikipedia:Absolute value</a>
 */
public class Abs extends AbstractTrigArg1 implements INumeric {
//	final static String[] RULES = { "Abs[Pi]=Pi", "Abs[E]=E", "Abs[x_NumberQ*y_]:=Abs[x]*Abs[y]" };

	/**
	 <pre>Abs[Pi]=Pi, 
  Abs[E]=E, 
  Abs[x_NumberQ*y_]:=Abs[x]*Abs[y]
  </pre>
	 */
	final static IAST RULES = List( 
			Set(Abs(Pi),Pi),
			Set(Abs(E),E),
			SetDelayed(Abs(Times(pattern("x",symbol("NumberQ")),pattern("y"))),Times(Abs(symbol("x")),Abs(symbol("y"))))
			);
	
	@Override
	public IAST getRuleAST() {
		return RULES;
	}
	
	public Abs() {
	}

	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1 instanceof INumber) {
			return ((INumber) arg1).eabs();
		}
		return null;
	}

	public IExpr numericEvalD1(final Num arg1) {
		return F.num(Math.abs(arg1.getRealPart()));
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 1) {
			throw new UnsupportedOperationException();
		}
		return Math.abs(stack[top]);
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return F.num(arg1.dabs());
	}

	@Override
  public void setUp(final ISymbol symbol) throws SyntaxError {
    symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
    super.setUp(symbol);
  }
}
