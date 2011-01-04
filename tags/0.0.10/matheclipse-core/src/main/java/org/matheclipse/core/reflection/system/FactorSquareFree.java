package org.matheclipse.core.reflection.system;

import java.util.List;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Factors out only multiple factors of a univariate polynomial
 * 
 */
public class FactorSquareFree extends Factor {

	public FactorSquareFree() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 2 && lst.size() != 3) {
			return null;
		}

		ExprVariables eVar = new ExprVariables(lst.get(1));
		if (!eVar.isSize(1)) {
			throw new WrongArgumentType(lst, lst.get(1), 1, "Factorization only implemented for univariate polynomials");
		}
		try {
			IExpr expr = F.evalExpandAll(lst.get(1));
			ASTRange r = new ASTRange(eVar.getVarList(), 1);
			List<IExpr> varList = r.toList();

			if (lst.size() == 3) {
				return factorWithOption(lst, expr, varList, true);
			}
			return factor(expr, varList, true);

		} catch (Exception e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

}