package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.eval.util.Options;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

import apache.harmony.math.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.poly.GenPolynomial;

/**
 * Greatest common divisor of two polynomials. See: <a href=
 * "http://en.wikipedia.org/wiki/Greatest_common_divisor_of_two_polynomials"
 * >Wikipedia:Greatest common divisor of two polynomials</a>
 */
public class PolynomialGCD extends AbstractFunctionEvaluator {

	public PolynomialGCD() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() < 3) {
			return null;
		}
		ExprVariables eVar = new ExprVariables(lst.get(1));
		if (!eVar.isSize(1)) {
			// gcd only possible for univariate polynomials
			return null;
		}
		ASTRange r = new ASTRange(eVar.getVarList(), 1);
		IExpr expr = F.eval(F.ExpandAll, lst.get(1));
		if (lst.size() > 3) {
			final Options options = new Options(lst.topHead(), lst, lst.size() - 1);
			IExpr option = options.getOption("Modulus");
			if (option != null && option instanceof IInteger) {
				try {
					// found "Modulus" option => use ModIntegerRing
					final BigInteger value = ((IInteger) option).getBigNumerator();
					int intValue = ((IInteger) option).toInt();
					ModIntegerRing modIntegerRing = new ModIntegerRing(intValue, value.isProbablePrime(32));
					JASConvert<ModInteger> jas = new JASConvert<ModInteger>(r.toList(), modIntegerRing);
					GenPolynomial<ModInteger> poly = jas.expr2Poly(expr);
					GenPolynomial<ModInteger> temp;
					for (int i = 2; i < lst.size() - 1; i++) {
						eVar = new ExprVariables(lst.get(i));
						if (!eVar.isSize(1)) {
							// gcd only possible for univariate polynomials
							return null;
						}
						expr = F.eval(F.ExpandAll, lst.get(i));
						temp = jas.expr2Poly(expr);
						poly = poly.gcd(temp);
					}
					return jas.modIntegerPoly2Expr(poly);
				} catch (ArithmeticException ae) {
					// toInt() conversion failed
					if (Config.DEBUG) {
						ae.printStackTrace();
					}
					return null; // no evaluation
				}
			}
		}
		try {
			JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList());
			GenPolynomial<BigRational> poly = jas.expr2Poly(expr);
			GenPolynomial<BigRational> temp;
			for (int i = 2; i < lst.size(); i++) {
				eVar = new ExprVariables(lst.get(i));
				if (!eVar.isSize(1)) {
					// gcd only possible for univariate polynomials
					return null;
				}
				expr = F.eval(F.ExpandAll, lst.get(i));
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
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}