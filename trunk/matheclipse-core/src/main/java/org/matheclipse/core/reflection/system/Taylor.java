package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

/**
 * Power series expansion with Taylor formula
 */
public class Taylor extends AbstractFunctionEvaluator {
	public Taylor() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() == 3 && (ast.get(2).isVector() == 3)) {
			try {
				IAST list = (IAST) ast.get(2);
				int lowerLimit = ((IInteger) list.get(2)).toInt();
				if (lowerLimit != 0) {
					// TODO support other cases than 0
					return null;
				}
				int upperLimit = ((IInteger) list.get(3)).toInt();
				if (upperLimit < 0) {
					return null;
				}
				IAST fadd = F.Plus();
				fadd.add(F.ReplaceAll(ast.get(1), F.Rule(list.get(1), list.get(2))));
				IExpr temp = ast.get(1);
				IExpr factor = null;
				for (int i = 1; i <= upperLimit; i++) {
					temp = F.D(temp, list.get(1));
					factor = F.Times(F.Power(F.Factorial(F.integer(i)), F.CN1), F.Power(F
							.Plus(list.get(1), F.Times(F.CN1, list.get(2))), F.integer(i)));
					fadd.add(F.Times(F.ReplaceAll(temp, F.Rule(list.get(1), list.get(2))), factor));
				}
				return fadd;
			} catch (Exception e) {
				if (Config.SHOW_STACKTRACE) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
