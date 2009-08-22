package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;
import static org.matheclipse.core.expression.F.Expand;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Times;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.NonNegativeIntegerExpected;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Expand extends AbstractFunctionEvaluator implements
		IConstantHeaders {
	public final static Expand CONST = new Expand();

	public Expand() {
		super();
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2) {
			return null;
		}

		if (ast.get(1) instanceof IAST) {
			final IAST list = (IAST) ast.get(1);
			IExpr temp = expand(list);
			if (temp != null) {
   			return temp;
			}
		}

		return ast.get(1);
	}

	public IExpr expand(final IAST ast) {
		IExpr header = ast.head();
		if ((header == F.Power) && (ast.size() == 3)) {
			// (a+b)^exp
			if ((ast.get(1) instanceof IAST)
					&& (ast.get(2) instanceof IntegerSym)) {
				header = ((IAST) ast.get(1)).head();
				if (header == F.Plus) {
					final IntegerSym exponent = (IntegerSym) ast.get(2);
					if (!exponent.isPositive()) {
						return null;
					}
					try {
						final int exp = exponent.toInt();
						return expandPower((IAST) ast.get(1), exp);
					} catch (final ArithmeticException e) {
						if (Config.SHOW_STACKTRACE) {
							e.printStackTrace();
						}
						throw new NonNegativeIntegerExpected(ast, 2);
					}
				}
			}
		}

		if ((header == F.Times) && (ast.size() >= 3)) {
			// (a+b)*(c+d)...
			IExpr result = ast.get(1);
			for (int i = 2; i < ast.size(); i++) {
				checkCanceled();
				result = expandTimes(result, ast.get(i));
			}
			return result;
		}

		if ((header == F.Plus) && (ast.size() >= 3)) {
			final IAST result = Plus();
			for (int i = 1; i < ast.size(); i++) {
				checkCanceled();
				result.add(Expand(ast.get(i)));
			}
			return result;
		}
		return null;
	}

	public static IExpr expandTimes(final IExpr expr0, final IExpr expr1) {
		final IAST ast0 = assurePlus(expr0);
		final IAST ast1 = assurePlus(expr1);
		return EvalEngine.eval(expandTimesPlus(ast0, ast1));
	}

	private static IAST assurePlus(final IExpr expr) {
		IAST astPlus = null;
		if (expr instanceof IAST) {
			final IAST ast = (IAST) expr;
			final IExpr header = ast.head();
			if (header == F.Plus) {
				astPlus = ast;
				return astPlus;
			}
		}
		// if expr is not of the form Plus[...], generate Plus[expr]
		if (astPlus == null) {
			astPlus = Plus();
			astPlus.add(expr);
		}
		return astPlus;
	}

	public static IAST expandTimesPlus(final IAST expr0, final IAST expr1) {
		final IAST pList = Plus();
		for (int i = 1; i < expr0.size(); i++) {
			for (int j = 1; j < expr1.size(); j++) {
				checkCanceled();
				pList.add(Times(expr0.get(i), expr1.get(j)));
			}
		}
		return pList;
	}

	public static IExpr expandPower(final IAST expr0,
			final int exponent) {
		int exp = exponent;
		if (exp==1) {
			return expr0;
		}
		if (exp==0) {
			return F.C0;
		}
		IExpr pow2 = expr0;
		IExpr result = null;
		while (exp >= 1) { // Iteration.
			checkCanceled();
			if ((exp & 1) == 1) {
				result = (result == null) ? pow2 : expandTimes(result, pow2);
			}
			pow2 = expandTimes(pow2, pow2);
			exp >>>= 1;
		}
		return result;
	}

}