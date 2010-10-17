package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Expand;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Times;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.generic.combinatoric.KPermutationsIterable;
import org.matheclipse.generic.combinatoric.NumberPartitionsIterable;

public class Expand extends AbstractFunctionEvaluator implements IConstantHeaders {

	public Expand() {
		super();
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2) {
			return null;
		}

		if (ast.get(1) instanceof IAST) {
			final IAST arg1 = (IAST) ast.get(1);
			IExpr temp = expand(arg1);
			if (temp != null) {
				return temp;
			}
		}

		return ast.get(1);
	}

	public static IExpr expand(final IAST ast) {
		if (ast.isPower()) {
			// (a+b)^exp
			if ((ast.get(1) instanceof IAST) && (ast.get(2) instanceof IInteger)) {
				IExpr header = ((IAST) ast.get(1)).head();
				if (header == F.Plus) {
					int exp = Validate.checkIntType(ast, 2, Integer.MIN_VALUE);
					if (exp < 0) {
						exp *= (-1);
						return F.Power(expandPower((IAST) ast.get(1), exp), F.CN1);
					}
					return expandPower((IAST) ast.get(1), exp);
				}
			}
		} else if (ast.isTimes()) {
			// (a+b)*(c+d)...

			IExpr[] temp = Apart.getFractionalPartsTimes(ast);
			if (temp[0].equals(F.C1)) {
				if (temp[1].isTimes()) {
					return F.Power(expandTimes((IAST) temp[1]), F.CN1);
				}
				return null;
			}

			if (temp[1].equals(F.C1)) {
				return expandTimes(ast);
			}

			if (temp[0].isTimes()) {
				temp[0] = expandTimes((IAST) temp[0]);
			}
			if (temp[1].isTimes()) {
				temp[1] = expandTimes((IAST) temp[1]);
			}
			return F.Times(temp[0], F.Power(temp[1], F.CN1));
		} else if (ast.isASTSizeGE(F.Plus, 3)) {
			return ast.map(Functors.replace1st(Expand(F.Null)));
		}
		return null;
	}

	private static IExpr expandTimes(final IAST timesAST) {
		IExpr result = timesAST.get(1);
		for (int i = 2; i < timesAST.size(); i++) {
			result = expandTimesBinary(result, timesAST.get(i));
		}
		return result;
	}

	public static IExpr expandTimesBinary(final IExpr expr0, final IExpr expr1) {
		if (expr0.isNumber() && expr1.isPlus()) {
			return EvalEngine.eval(expandTimesPlus((INumber)expr0, (IAST)expr1));
		}
		final IAST ast0 = assurePlus(expr0);
		final IAST ast1 = assurePlus(expr1);
		return EvalEngine.eval(expandTimesPlus(ast0, ast1));
	}

	private static IAST assurePlus(final IExpr expr) {
		IAST astPlus = null;
		if (expr.isPlus()) {
			return (IAST)expr;
		}
		// if expr is not of the form Plus[...], generate Plus[expr]
		if (astPlus == null) {
			astPlus = Plus();
			astPlus.add(expr);
		}
		return astPlus;
	}

	public static IAST expandTimesPlus(final IAST expr0, final IAST expr1) {
		// (a+b)*(c+d) -> a*c+a*d+b*c+b*d
		final IAST pList = Plus();
		for (int i = 1; i < expr0.size(); i++) {
			expr1.args().map(pList, Functors.replace2nd(Times(expr0.get(i), F.Null)));
		}
		return pList;
	}

	public static IAST expandTimesPlus(final INumber expr1, final IAST ast) {
		// (a+b)*(c+d) -> a*c+a*d+b*c+b*d
		final IAST pList = Plus();
		for (int i = 1; i < ast.size(); i++) {
			pList.add(F.Times(expr1, ast.get(i)));
		} 
		return pList;
	}

	/**
	 * Expand a polynomial power with the multinomial theorem. See <a
	 * href="http://en.wikipedia.org/wiki/Multinomial_theorem">Wikipedia -
	 * Multinomial theorem</a>
	 * 
	 * @param expr0
	 * @param n
	 * @return
	 */
	public static IExpr expandPower(final IAST expr0, final int n) {
		if (n == 1) {
			return expr0;
		}
		if (n == 0) {
			return F.C0;
		}

		int m = expr0.size() - 1;
		final NumberPartitionsIterable comb = new NumberPartitionsIterable(n, m);
		IInteger multinomial;
		final IAST result = Plus();
		for (int j[] : comb) {
			if (m < n && j[m] != 0) {
				continue;
			}
			final KPermutationsIterable perm = new KPermutationsIterable(j, m, m);
			multinomial = F.integer(Multinomial.multinomial(j, n));
			for (int[] indices : perm) {
				final IAST tList = Times();
				tList.add(multinomial);
				for (int k = 0; k < m; k++) {
					if (indices[k] != 0) {
						tList.add(F.Power(expr0.get(k + 1), indices[k]));
					}
				}
				result.add(tList);
			}
		}
		return result;
	}
}