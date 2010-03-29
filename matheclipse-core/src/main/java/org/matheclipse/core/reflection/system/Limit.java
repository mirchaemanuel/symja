package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Limit of a function. See <a
 * href="http://en.wikipedia.org/wiki/List_of_limits">List of Limits</a>
 */
public class Limit extends AbstractFunctionEvaluator implements IConstantHeaders {
	// String[] RULES = {
	//			
	//
	// };

	public Limit() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 3 && lst.get(2).isAST(F.Rule, 3)) {
			return null;
		}
		IAST rule = (IAST) lst.get(2);
		if (!(rule.get(1) instanceof ISymbol)) {
			return null;
		}
		ISymbol sym = (ISymbol) rule.get(1);
		IExpr lim = null;
		if (rule.get(2).isFree(sym)) {
			lim = rule.get(2);
		} else {
			return null;
		}
		if (lst.get(1).isFree(sym)) {
			// Limit[a_,sym->lim] -> a
			return lst.get(1);
		}
		if (lst.get(1).equals(sym)) {
			// Limit[x_,x_->lim] -> lim
			return lim;
		}
		if (lst.get(1) instanceof IAST) {
			final IAST list = (IAST) lst.get(1);
			final IExpr header = list.head();
			if (header == F.Plus || header == F.Times) {
				// Limit[a_+b_+c_,sym->lim] ->
				// Limit[a,sym->lim]+Limit[b,sym->lim]+Limit[c,sym->lim]
				final IAST resultList = (IAST) list.clone();
				for (int i = 1; i < list.size(); i++) {
					resultList.set(i, F.Limit(list.get(i), rule));
				}
				return resultList;
			}
			if (list.isAST(F.Power, 3) && list.get(2) instanceof IInteger) {
				// Limit[a_^n_,sym->lim] -> Limit[a,sym->lim]^n
				IInteger n = (IInteger) list.get(2);
				if (n.isPositive()) {
					return F.Power(F.Limit(list.get(1), rule), n);
				}
			}

		}

		return null;
	}

	@Override
	public String[] getRules() {
		return null;
		// return RULES;
	}

}