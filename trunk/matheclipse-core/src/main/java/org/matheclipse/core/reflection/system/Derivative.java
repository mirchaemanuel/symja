package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.*; 

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 */
public class Derivative extends AbstractFunctionEvaluator {

	/**
	 * <pre>
	 * Derivative[ArcCot]=(-1)*(1+#^2)^(-1)&,
  Derivative[Sinh]=Cosh[#]&,
  Derivative[Coth]=(-1)*Sinh[#]^(-2)&,
  Derivative[Log]=#^(-1)&,
  Derivative[ArcCos]=(-1)*(1-#^2)^(-1/2)&,
  Derivative[ArcSinh]=(1+#^2)^(-1/2)&,
  Derivative[Tanh]=Cosh[#]^(-2)&,
  Derivative[Cot]=(-1)*Sin[#]^(-2)&,
  Derivative[Cos]=(-1)*Sin[#]&,
  Derivative[ArcSin]=(1-#^2)^(-1/2)&,
  Derivative[Sin]=Cos[#]&,
  Derivative[Tan]=Cos[#]^(-2)&,
  Derivative[ArcCosh]=(#^2-1)^(-1/2)&,
  Derivative[ArcCoth]=(1-#^2)^(-1)&,
  Derivative[ArcTan]=(1+#^2)^(-1)&,
  Derivative[ArcTanh]=(1-#^2)^(-1)&,
  Derivative[Cosh]=(-1)*Sinh[#]&,
	 </pre>
	 */
	final static IAST RULES = List(
			Set(Derivative(symbol("ArcTanh")),Function(Power(Plus(Times(CN1,Power(Slot(C1),C2)),C1),CN1))),
			Set(Derivative(symbol("ArcSin")),Function(Power(Plus(Times(CN1,Power(Slot(C1),C2)),C1),CN1D2))),
			Set(Derivative(symbol("Sin")),Function(Cos(Slot(C1)))), 
			Set(Derivative(symbol("Tanh")),Function(Power(Cosh(Slot(C1)),integer(-2L)))),
			Set(Derivative(symbol("Log")),Function(Power(Slot(C1),CN1))),
			Set(Derivative(symbol("ArcCoth")),Function(Power(Plus(Times(CN1,Power(Slot(C1),C2)),C1),CN1))),
			Set(Derivative(symbol("ArcCosh")),Function(Power(Plus(CN1,Power(Slot(C1),C2)),CN1D2))),
			Set(Derivative(symbol("ArcCot")),Times(CN1,Function(Power(Plus(Power(Slot(C1),C2),C1),CN1)))),
			Set(Derivative(symbol("ArcCos")),Times(CN1,Function(Power(Plus(Times(CN1,Power(Slot(C1),C2)),C1),CN1D2)))),
			Set(Derivative(symbol("Cos")),Function(Times(CN1,Sin(Slot(C1))))),
			Set(Derivative(symbol("ArcTan")),Function(Power(Plus(Power(Slot(C1),C2),C1),CN1))),
			Set(Derivative(symbol("ArcSinh")),Function(Power(Plus(Power(Slot(C1),C2),C1),CN1D2))),
			Set(Derivative(symbol("Cot")),Times(CN1,Function(Power(Sin(Slot(C1)),integer(-2L))))),
			Set(Derivative(symbol("Tan")),Function(Power(Cos(Slot(C1)),integer(-2L)))),
			Set(Derivative(symbol("Sinh")),Function(Cosh(Slot(C1)))),
			Set(Derivative(symbol("Cosh")),Function(Times(CN1,Sinh(Slot(C1))))),
			Set(Derivative(symbol("Coth")),Times(CN1,Function(Power(Sinh(Slot(C1)),integer(-2L)))))
			);

	@Override
	public IAST getRuleAST() {
		return RULES;
	}

	public Derivative() {
	}

	@Override
	public IExpr evaluate(IAST ast) {
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
		super.setUp(symbol);
	}

}
