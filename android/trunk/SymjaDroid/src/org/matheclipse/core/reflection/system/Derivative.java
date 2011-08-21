package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.$s;
import static org.matheclipse.core.expression.F.C1;
import static org.matheclipse.core.expression.F.C2;
import static org.matheclipse.core.expression.F.CN1;
import static org.matheclipse.core.expression.F.CN1D2;
import static org.matheclipse.core.expression.F.Cos;
import static org.matheclipse.core.expression.F.Cosh;
import static org.matheclipse.core.expression.F.Cot;
import static org.matheclipse.core.expression.F.Csc;
import static org.matheclipse.core.expression.F.Derivative;
import static org.matheclipse.core.expression.F.Function;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Sec;
import static org.matheclipse.core.expression.F.Set;
import static org.matheclipse.core.expression.F.Sin;
import static org.matheclipse.core.expression.F.Sinh;
import static org.matheclipse.core.expression.F.Slot1;
import static org.matheclipse.core.expression.F.Tan;
import static org.matheclipse.core.expression.F.Times;
import static org.matheclipse.core.expression.F.integer;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
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
  Derivative[Sec]=Sec[#]*Tan[#]&,
  Derivative[Csc]=(-1)*Cot[#]*Csc[#]&,
	 </pre>
	 */
	final static IAST RULES = List(
			Set(Derivative($s("ArcTanh")),Function(Power(Plus(C1,Times(CN1,Power(Slot1,C2))),CN1))),
			Set(Derivative($s("ArcSin")),Function(Power(Plus(C1,Times(CN1,Power(Slot1,C2))),CN1D2))),
			Set(Derivative($s("Sin")),Function(Cos(Slot1))),
			Set(Derivative($s("Tanh")),Function(Power(Cosh(Slot1),integer(-2L)))),
			Set(Derivative($s("Log")),Function(Power(Slot1,CN1))),
			Set(Derivative($s("ArcCoth")),Function(Power(Plus(C1,Times(CN1,Power(Slot1,C2))),CN1))),
			Set(Derivative($s("ArcCosh")),Function(Power(Plus(Power(Slot1,C2),Times(CN1,C1)),CN1D2))),
			Set(Derivative($s("ArcCot")),Times(CN1,Function(Power(Plus(C1,Power(Slot1,C2)),CN1)))),
			Set(Derivative($s("ArcCos")),Times(CN1,Function(Power(Plus(C1,Times(CN1,Power(Slot1,C2))),CN1D2)))),
			Set(Derivative($s("Cos")),Function(Times(CN1,Sin(Slot1)))),
			Set(Derivative($s("ArcTan")),Function(Power(Plus(C1,Power(Slot1,C2)),CN1))),
			Set(Derivative($s("ArcSinh")),Function(Power(Plus(C1,Power(Slot1,C2)),CN1D2))),
			Set(Derivative($s("Cot")),Times(CN1,Function(Power(Sin(Slot1),integer(-2L))))),
			Set(Derivative($s("Tan")),Function(Power(Cos(Slot1),integer(-2L)))),
			Set(Derivative($s("Sinh")),Function(Cosh(Slot1))),
			Set(Derivative($s("Cosh")),Function(Times(CN1,Sinh(Slot1)))),
			Set(Derivative($s("Coth")),Times(CN1,Function(Power(Sinh(Slot1),integer(-2L))))),
			Set(Derivative($s("Sec")),Function(Times(Sec(Slot1),Tan(Slot1)))),
			Set(Derivative($s("Csc")),Function(Times(Times(CN1,Cot(Slot1)),Csc(Slot1))))
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
		symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NHOLDALL);
		super.setUp(symbol);
	}

}
