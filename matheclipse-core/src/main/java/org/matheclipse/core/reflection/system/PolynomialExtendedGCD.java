package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
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
 * Greatest common divisor of two polynomials. See: <a href=
 * "http://en.wikipedia.org/wiki/Greatest_common_divisor_of_two_polynomials"
 * >Wikipedia:Greatest common divisor of two polynomials</a>
 */
public class PolynomialExtendedGCD extends AbstractFunctionEvaluator {

	public PolynomialExtendedGCD() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 4 && lst.size() != 5 && !(lst.get(3) instanceof ISymbol)) {
			return null;
		}
		ISymbol x = (ISymbol)lst.get(3);
		IExpr expr1 = F.eval(F.ExpandAll, lst.get(1));
		IExpr expr2 = F.eval(F.ExpandAll, lst.get(2));
		ExprVariables eVar = new ExprVariables(expr1);
		if (!eVar.isSize(1) || !eVar.contains(x)) {
			// egcd only possible for univariate polynomials
			return null;
		}
		eVar = new ExprVariables(expr2);
		if (!eVar.isSize(1) || !eVar.contains(x)) {
			// egcd only possible for univariate polynomials
			return null;
		}
		try {
			ASTRange r = new ASTRange(eVar.getVarList(), 1);
			JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList());
			GenPolynomial<BigRational> poly1 = jas.expr2Poly(expr1);
			GenPolynomial<BigRational> poly2 = jas.expr2Poly(expr2);
			GenPolynomial<BigRational>[] result = poly1.egcd(poly2);
			IAST list = F.List();
			for (int i = 0; i < result.length; i++) {
				list.add(jas.rationalPoly2Expr(result[i]));
			}
			return list;
		} catch (Exception e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}