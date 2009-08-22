package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;

/**
 * Greatest common divisor of two polynomials.
 * See: <a href="http://en.wikipedia.org/wiki/Greatest_common_divisor_of_two_polynomials">Wikipedia:Greatest common divisor of two polynomials</a>
 */
public class PolynomialGCD extends AbstractFunctionEvaluator {

	public PolynomialGCD() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() < 3) {
			return null;
		}
		IAST variableList = null;
		variableList = Variables.call(lst.get(1));
		if (variableList.size() != 2) {
			// factorization only possible for univariate polynomials
			return null;
		}
		// IExpr variable = variableList.get(1);
		try {
			IExpr expr = F.eval(F.ExpandAll, lst.get(1));
			ASTRange r = new ASTRange(variableList, 1);
			JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList());
			GenPolynomial<BigRational> poly = jas.expr2Poly(expr);
			GenPolynomial<BigRational> temp;
			for (int i = 2; i < lst.size(); i++) {
				variableList = Variables.call(lst.get(1));
				if (variableList.size() != 2) {
					// factorization only possible for univariate polynomials
					return null;
				}
				expr = F.eval(F.ExpandAll, lst.get(i));
				r = new ASTRange(variableList, 1);
				jas = new JASConvert<BigRational>(r.toList());
				temp = jas.expr2Poly(expr);
				poly = poly.gcd(temp);
			}
			return jas.rationalPoly2Expr(poly);
		} catch (Exception e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.FLAT);
	}
}