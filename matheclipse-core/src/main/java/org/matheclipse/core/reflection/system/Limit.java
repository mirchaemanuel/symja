package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.RecursionLimitExceeded;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Limit of a function. See <a
 * href="http://en.wikipedia.org/wiki/List_of_limits">List of Limits</a>
 */
public class Limit extends AbstractFunctionEvaluator implements IConstantHeaders {

	public Limit() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 3 && ast.get(2).isAST(F.Rule, 3)) {
			return null;
		}
		IAST rule = (IAST) ast.get(2);
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
		return limit(ast.get(1), sym, lim, rule);
	}

	public static IExpr limit(final IExpr expr, ISymbol sym, IExpr lim, IAST rule) {
		if (expr.isFree(sym)) {
			// Limit[a_,sym->lim] -> a
			return expr;
		}
		if (expr.equals(sym)) {
			// Limit[x_,x_->lim] -> lim
			return lim;
		}
		if (expr instanceof IAST) {
			final IAST arg1 = (IAST) expr;
			final IExpr header = arg1.head();
			if (arg1.size() == 2) {
				if (header.equals(F.Sin) || header.equals(F.Cos)) {
					return F.$(header, F.Limit(arg1.get(1), rule));
				}
			}
			if (header == F.Plus) {
				// Limit[a_+b_+c_,sym->lim] ->
				// Limit[a,sym->lim]+Limit[b,sym->lim]+Limit[c,sym->lim]
				final IAST resultList = (IAST) arg1.clone();
				for (int i = 1; i < arg1.size(); i++) {
					resultList.set(i, F.Limit(arg1.get(i), rule));
				}
				return resultList;
			} else if (header == F.Times) {
				IExpr[] parts = Apart.getFractionalPartsTimes(arg1);
				IExpr numerator = parts[0];
				IExpr denominator = parts[1];
				return timesLimit(numerator, denominator, sym, lim, rule);
			} else if (arg1.isAST(F.Power, 3) && arg1.get(2) instanceof IInteger) {
				// Limit[a_^n_,sym->lim] -> Limit[a,sym->lim]^n
				IInteger n = (IInteger) arg1.get(2);
				if (n.isPositive()) {
					return F.Power(F.Limit(arg1.get(1), rule), n);
				}
			}

		}

		return null;
	}

	private static IExpr timesLimit(final IExpr numerator, final IExpr denominator, ISymbol sym, IExpr lim, IAST rule) {
		IExpr numValue;
		IExpr denValue;
		if (denominator.isOne() && numerator.isTimes()) {
			// Limit[a_*b_*c_,sym->lim] ->
			// Limit[a,sym->lim]*Limit[b,sym->lim]*Limit[c,sym->lim]
			final IAST resultList = (IAST) ((IAST) numerator).clone();
			for (int i = 1; i < resultList.size(); i++) {
				resultList.set(i, F.Limit(resultList.get(i), rule));
			}
			return resultList;
		}
		if (!denominator.isNumber() || denominator.isZero()) {
			denValue = F.evalBlock(denominator, sym, lim);
			if (denValue.isZero()) {
				numValue = F.evalBlock(numerator, sym, lim);
				if (numValue.isZero()) {
					return lHospitalesRule(numerator, denominator, sym, lim, rule);
				}
				return null;
			} else if (F.CInfinity.equals(denValue)) {
				numValue = F.evalBlock(numerator, sym, lim);
				if (F.CInfinity.equals(numValue)) {
					return lHospitalesRule(numerator, denominator, sym, lim, rule);
				}
				return null;
			} else if (F.CNInfinity.equals(denValue)) {
				numValue = F.evalBlock(numerator, sym, lim);
				if (F.CNInfinity.equals(numValue)) {
					return lHospitalesRule(numerator, denominator, sym, lim, rule);
				}
				return null;
			}
		}
		return F.Times(F.Limit(numerator, rule), F.Power(F.Limit(denominator, rule), F.CN1));
	}

	/**
	 * Try L'hospitales rule. See <a
	 * href="http://en.wikipedia.org/wiki/L%27H%C3%B4pital%27s_rule">Wikipedia
	 * L'Hôpital's rule</a>
	 * 
	 * @param numerator
	 * @param denominator
	 * @param sym
	 * @param rule
	 * @return
	 */
	private static IExpr lHospitalesRule(IExpr numerator, IExpr denominator, ISymbol sym, IExpr lim, IAST rule) {
		EvalEngine engine = EvalEngine.get();
		int limit = engine.getRecursionLimit();
		if (limit > 0) {
			IExpr expr = F.eval(F.Times(F.D(numerator, sym), F.Power(F.D(denominator, sym), F.CN1)));
			return limit(expr, sym, lim, rule);
		}
		try {
			if (limit <= 0) {
				// set recursion limit for using l'Hospitales rule
				engine.setRecursionLimit(128);
			}
			IExpr expr = F.eval(F.Times(F.D(numerator, sym), F.Power(F.D(denominator, sym), F.CN1)));
			return limit(expr, sym, lim, rule);
		} catch (RecursionLimitExceeded rle) {
			engine.setRecursionLimit(limit);
		} finally {
			engine.setRecursionLimit(limit);
		}
		return null;
	}

}