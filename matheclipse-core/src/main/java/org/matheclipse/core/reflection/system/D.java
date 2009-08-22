package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.BinaryMap;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Differentiation of a function.
 * See <a href="http://en.wikipedia.org/wiki/Derivative">Wikipedia:Derivative</a>
 */
public class D extends AbstractFunctionEvaluator {
	// String[] RULES = {
	// "D[Sin[x_],y_]:= Cos[x]*D[x,y]",
	// "D[Cos[x_],y_]:= -Sin[x]*D[x,y]",
	// "D[Tan[x_],y_]:= 1/(Cos[x]^2)*D[x,y]",
	// "D[Cot[x_],y_]:= (-1)/(Sin[x]^2)*D[x,y]",
	// "D[ArcSin[x_],y_]:= (1-x^2)^(-1/2)*D[x,y]",
	// "D[ArcCos[x_],y_]:= -(1-x^2)^(-1/2)*D[x,y]",
	// "D[ArcTan[x_],y_]:= (1+x^2)^(-1)*D[x,y]",
	// "D[ArcCot[x_],y_]:= -(1+x^2)^(-1)*D[x,y]",
	// "D[Sinh[x_],y_]:= Cosh[x]*D[x,y]",
	// "D[Cosh[x_],y_]:= -Sinh[x]*D[x,y]",
	// "D[Tanh[x_],y_]:= (Cosh[x]^(-2))*D[x,y]",
	// "D[Coth[x_],y_]:= -(Sinh[x]^(-2))*D[x,y]",
	// "D[ArcSinh[x_],y_]:= (1+x^2)^(-1/2)*D[x,y]",
	// "D[ArcCosh[x_],y_]:= (x^2-1)^(-1/2)*D[x,y]",
	// "D[ArcTanh[x_],y_]:= (1-x^2)^(-1)*D[x,y]",
	// "D[ArcCoth[x_],y_]:= (1-x^2)^(-1)*D[x,y]",
	// "D[Log[x_],y_]:= x^(-1)*D[x,y]"
	// };

	public D() {
	}

	@Override
	public IExpr evaluate(final IAST dList) {
		if (dList.size() != 3) {
			return null;
		}
		if (FreeQ.freeQ(dList.get(1), dList.get(2))) {
			return F.C0;
		}
		if (dList.get(1) instanceof INumber) {
			// D[x_NumberQ,y_] -> 0
			return F.C0;
		}
		if (dList.get(1).equals(dList.get(2))) {
			// D[x_,x_] -> 1
			return F.C1;
		}
		if (!(dList.getHeader() instanceof ISymbol)) {
			return null;
		}
		final ISymbol symbolD = dList.topHead();
		if (dList.get(1) instanceof IAST) {
			final IAST listArg1 = (IAST) dList.get(1);
			final IExpr header = listArg1.getHeader();
			if (header == F.Plus) {
				// D[a_+b_+c_,x_] -> D[a,x]+D[b,x]+D[c,x]
				return listArg1.args().map(F.Plus(), new BinaryMap(F.D()).bind2(dList.get(2)));
			}
			if (header == F.Times) {
				final IAST resultList = F.Plus();
				IAST argList;
				for (int i = 1; i < listArg1.size(); i++) {
					checkCanceled();
					argList = (IAST) listArg1.clone();
					argList.set(i, F.D(listArg1.get(i), dList.get(2)));
					resultList.add(argList);
				}
				return resultList;
			}
			if ((header == F.Power) && (listArg1.size() == 3)) {
				if (listArg1.get(2) instanceof INumber) {
					// D[x_^i_NumberQ, z_]:= i*x^(i-1)*D[x,z];
					final IAST timesList = F.Times();
					timesList.add(listArg1.get(2));
					final IAST powerList = F.Power();
					powerList.add(listArg1.get(1));
					final IAST plusList = F.Plus();
					plusList.add(listArg1.get(2));
					plusList.add(F.CN1);
					powerList.add(plusList);
					timesList.add(powerList);
					timesList.add(F.D(listArg1.get(1), dList.get(2)));
					return timesList;
				} else {
					// D[f_^g_,y_]:= f^g*(((g*D[f,y])/f)+Log[f]*D[g,y])
					final IAST resultList = F.Times();
					final IExpr f = listArg1.get(1);
					final IExpr g = listArg1.get(2);
					final IExpr y = dList.get(2);

					IAST powerList = F.Power();
					powerList.add(f);
					powerList.add(g);
					resultList.add(powerList);

					final IAST plusList = F.Plus();
					IAST timesList = F.Times();
					timesList.add(g);
					timesList.add(F.function(symbolD, f, y));
					timesList.add(F.Power(f, F.CN1));
					plusList.add(timesList);

					timesList = F.Times(F.Log(f), F.D(g, y));
					plusList.add(timesList);

					resultList.add(plusList);
					return resultList;
				}
			}
			return null;
		}

		return null;
	}

	// @Override
	// public String[] getRules() {
	// return RULES;
	// }

}