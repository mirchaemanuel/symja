package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Times;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Integration of a function. 
 * See <a href="http://en.wikipedia.org/wiki/Integral">Integral</a>
 */
public class Integrate extends AbstractFunctionEvaluator implements IConstantHeaders {
	String[] RULES = {
			"Integrate[y_ * x_,z_]:= y*Integrate[x,z] /; FreeQ[y,z]",
			"Integrate[y_,x_]:=y*x /; FreeQ[y,x]",
			"Integrate[x_,x_]:= x^2/2",
			"Integrate[x_^n_NumberQ, x_]:=x^(n+1)/(n+1) /; n!=(-1)",
			"Integrate[x_^(-1), x_]:=Log[x]",
			"Integrate[(a_+x_)^(-1), x_]:=Log[x+a] /; FreeQ[a,x]",
			"Integrate[(x_+b_)^n_NumberQ,x_]:= (x+b)^(n+1)/(n+1) /; n!=(-1)&&FreeQ[a,x]",
			"Integrate[(a_*x_+b_)^n_NumberQ,x_]:= (a*x+b)^(n+1)/(a*(n+1)) /; (n!=(-1))&&FreeQ[a,x]&&FreeQ[b,x]",
			"Integrate[E_^x_, x_]:=E^x",
			"Integrate[E_^(a_*x_), x_]:=a^(-1)*E^(a*x) /; FreeQ[a,x]",
			"Integrate[Log[x_], x_]:=x*Log[x]-x",
			"Integrate[Log[a_*x_], x_]:=Log[a*x]*x-x",
			"Integrate[Sinh[x_], x_]:=Cosh[x]",
			"Integrate[Cosh[x_], x_]:=Sinh[x]",
			"Integrate[ArcSinh[x_], x_]:=x*ArcSinh[x]-Sqrt[x^2+1]",
			"Integrate[ArcCosh[x_], x_]:=x*ArcCosh[x]-Sqrt[x^2-1]",
			"Integrate[ArcTanh[x_], x_]:=x*ArcTanh[x]+1/2*Log[1-x^2]",
			"Integrate[Sin[x_], x_]:= -Cos[x]",
			"Integrate[Sin[a_*x_], x_]:= -Cos[a*x]/a /; FreeQ[a,x]",
			"Integrate[Cos[x_], x_]:= Sin[x]",
			"Integrate[Cos[a_*x_], x_]:= Sin[a*x]/a /; FreeQ[a,x]",
			"Integrate[Sin[x_]^n_IntegerQ, x_]:= (n-1)/n*Integrate[Sin[x]^(n-2),x]-Sin[x]^(n-1)*Cos[x]/n /; Positive[n]",
			"Integrate[Sin[a_*x_]^n_IntegerQ, x_]:= -Sin[a*x]^(n-1)*Cos[a*x]/(n*a)+(n-1)/n*Integrate[Sin[a*x]^(n-2),x] /; Positive[n]&&FreeQ[a,x]",
			"Integrate[Cos[x_]^n_IntegerQ, x_]:= Cos[x]^(n-1)*Sin[x]/n+(n-1)/n*Integrate[Cos[x]^(n-2),x] /; Positive[n]",
			"Integrate[Cos[a_*x_]^n_IntegerQ, x_]:= Cos[a*x]^(n-1)*Sin[a*x]/(n*a)+(n-1)/n*Integrate[Cos[a*x]^(n-2),x] /; Positive[n]&&FreeQ[a,x]",
			"Integrate[Tan[x_], x_]:= -Log[Cos[x]]",
			"Integrate[Tan[a_*x_], x_]:= -Log[Cos[a*x]]/a /; FreeQ[a,x]",
			"Integrate[Tan[x_]^n_IntegerQ, x_]:= 1/(n-1)*Tan[x]^(n-1)-Integrate[Tan[x]^(n-2),x] /; Positive[n]",
			"Integrate[Tan[a_*x_]^n_IntegerQ, x_]:= 1/(a*(n-1))*Tan[a*x]^(n-1)-Integrate[Tan[a*x]^(n-2),x] /; Positive[n]&&FreeQ[a,x]",
			"Integrate[ArcSin[x_], x_]:=x*ArcSin[x]+Sqrt[1-x^2]",
			"Integrate[ArcSin[a_*x_], x_]:= x*ArcSin[a*x]+Sqrt[1-a^2*x^2]/a /; FreeQ[a,x]",
			"Integrate[ArcCos[x_], x_]:= x*ArcCos[x]-Sqrt[1-x^2]",
			"Integrate[ArcCos[a_*x_], x_]:= x*ArcCos[a*x]-Sqrt[1-a^2*x^2]/a  /; FreeQ[a,x]",
			"Integrate[ArcTan[x_], x_]:= x*ArcTan[x]-1/2*Log[1+x^2]",
			"Integrate[ArcTan[a_*x_], x_]:= x*ArcTan[a*x]-1/2*Log[1+a^2*x^2]/a  /; FreeQ[a,x]"

	};

	public Integrate() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 3) {
			return null;
		}
		// PatternMatcher matcher = new PatternMatcher((IExpr) lst.get(1));
		// if (FreeQ.freeQ(matcher, (IExpr) lst.get(0))) {
		// return C0;
		// }
		if (lst.get(1) instanceof INumber) {
			// Integrate[x_NumberQ,y_] -> x*y
			return Times(lst.get(1), lst.get(2));
		}
		if (lst.get(1).equals(lst.get(2))) {
			// Integrate[x_,x_] -> x^2 / 2
			return Times(F.C1D2, Power(lst.get(1), F.C2));
		}
		final ISymbol symbolIntegrate = (ISymbol) lst.head();
		if (lst.get(1) instanceof IAST) {
			final IAST list = (IAST) lst.get(1);
			final IExpr header = list.head();
			if (header == F.Plus) {
				// Integrate[a_+b_+c_,x_] ->
				// Integrate[a,x]+Integrate[b,x]+Integrate[c,x]
				final IAST resultList = (IAST) list.clone();
				for (int i = 1; i < list.size(); i++) {
					checkCanceled();
					resultList.set(i, F.function(symbolIntegrate, list.get(i), lst.get(2)));
				}
				return resultList;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator#getRules()
	 */
	@Override
	public String[] getRules() {
		return RULES;
	}

}