package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.C0;
import static org.matheclipse.core.expression.F.C1;
import static org.matheclipse.core.expression.F.C1D2;
import static org.matheclipse.core.expression.F.C1D3;
import static org.matheclipse.core.expression.F.C2;
import static org.matheclipse.core.expression.F.C3;
import static org.matheclipse.core.expression.F.C4;
import static org.matheclipse.core.expression.F.CI;
import static org.matheclipse.core.expression.F.CN1D2;
import static org.matheclipse.core.expression.F.CN1D3;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Sqr;
import static org.matheclipse.core.expression.F.Sqrt;
import static org.matheclipse.core.expression.F.Subtract;
import static org.matheclipse.core.expression.F.Times;
import static org.matheclipse.core.expression.F.evalExpandAll;
import static org.matheclipse.core.expression.F.fraction;
import static org.matheclipse.core.expression.F.integer;

import java.util.List;
import java.util.SortedMap;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;

import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.Monomial;
import edu.jas.ufd.FactorAbstract;
import edu.jas.ufd.FactorFactory;

/**
 * Determine the roots of a univariate polynomial
 * 
 * See Wikipedia entries for: <a
 * href="http://en.wikipedia.org/wiki/Quadratic_equation">Quadratic equation
 * </a>, <a href="http://en.wikipedia.org/wiki/Cubic_function">Cubic
 * function</a> and <a
 * href="http://en.wikipedia.org/wiki/Quartic_function">Quartic function</a>
 */
public class Roots extends AbstractFunctionEvaluator {

	public Roots() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 2) {
			return null;
		}
		return roots(lst);
	}

	protected static IAST roots(final IAST ast) {
		try {
			ExprVariables eVar = new ExprVariables(ast.get(1));
			if (!eVar.isSize(1)) {
				// factor only possible for univariate polynomials
				return null;
			}
			IExpr expr = evalExpandAll(ast.get(1));
			ASTRange r = new ASTRange(eVar.getVarList(), 1);
			List<IExpr> varList = r.toList();

			JASConvert<BigRational> jas = new JASConvert<BigRational>(varList);
			GenPolynomial<BigRational> poly = jas.expr2Poly(expr);

			FactorAbstract<BigRational> factorAbstract = FactorFactory.getImplementation(BigRational.ONE);
			SortedMap<GenPolynomial<BigRational>, Long> map = factorAbstract.baseFactors(poly);
			IAST result = List();// function(Or);
			IInteger a;
			IInteger b;
			IInteger c;
			for (SortedMap.Entry<GenPolynomial<BigRational>, Long> entry : map.entrySet()) {
				GenPolynomial<BigRational> key = entry.getKey();
				GenPolynomial<edu.jas.arith.BigInteger> iPoly = (GenPolynomial<edu.jas.arith.BigInteger>) jas.factorTerms(key)[2];
				Long val = entry.getValue();
				long varDegree = iPoly.degree(0);
				if (iPoly.isONE()) {
					continue;
				}
				if (varDegree <= 2) {
					// solve Quadratic equation: a*x^2 + b*x + c = 0
					a = C0;
					b = C0;
					c = C0;
					for (Monomial<edu.jas.arith.BigInteger> monomial : iPoly) {
						edu.jas.arith.BigInteger coeff = monomial.coefficient();
						long lExp = monomial.exponent().getVal(0);
						if (lExp == 2) {
							a = integer(coeff.getVal());
						} else if (lExp == 1) {
							b = integer(coeff.getVal());
						} else if (lExp == 0) {
							c = integer(coeff.getVal());
						} else {
							throw new ArithmeticException("Roots::Unexpected exponent value: " + lExp);
						}
					}
					if (a.equals(C0)) {
						IFraction rat = fraction(c, b);
						result.add(rat.negate());
					} else {
						IAST sqrt = Sqrt(Plus(Sqr(b), Times(integer(-4), a, c)));
						IFraction rev2a = fraction(C1, a.multiply(C2));
						result.add(Times(rev2a, Plus(b.negate(), sqrt)));
						result.add(Times(rev2a, Plus(b.negate(), sqrt.negative())));
					}
				} else if (varDegree <= 3) {
					iPoly = iPoly.monic();
					// solve Cubic equation: x^3 + a*x^2 + b*x + c = 0
					a = C0;
					b = C0;
					c = C0;
					for (Monomial<edu.jas.arith.BigInteger> monomial : iPoly) {
						edu.jas.arith.BigInteger coeff = monomial.coefficient();
						long lExp = monomial.exponent().getVal(0);
						if (lExp == 2) {
							a = integer(coeff.getVal());
						} else if (lExp == 1) {
							b = integer(coeff.getVal());
						} else if (lExp == 0) {
							c = integer(coeff.getVal());
						} else if (lExp == 3) {
							if (!coeff.equals(edu.jas.arith.BigInteger.ONE)) {
								throw new ArithmeticException("Roots::Solution for cubic equation with leading coefficient: \"" + coeff.toString()
										+ "\" != 1 currently not implemented: ");
							}
						} else {
							throw new ArithmeticException("Roots::Unexpected exponent value: " + lExp);
						}
					}
					// m = 2*a^3 - 9*a*b + 27* c
					IInteger m = C2.multiply(a.pow(3)).subtract(a.multiply(b.multiply(integer(9)))).add(c.multiply(integer(27)));
					// k = a^2 - 3*b
					IInteger k = a.pow(2).subtract(C3.multiply(b));
					// n = m^2 - 4*k^3
					IInteger n = m.pow(2).subtract(C4.multiply(k.pow(3)));

					// omega1 = -(1/2) + 1/2 * Sqrt[3] * I
					IExpr omega1 = Plus(CN1D2, Times(C1D2, Sqrt(C3), CI));
					// omega2 = -(1/2) - 1/2 * Sqrt[3] * I
					IExpr omega2 = Plus(CN1D2, Times(CN1D2, Sqrt(C3), CI));

					// t1 = (1/2 * (m + n^(1/2))) ^ (1/3)
					IExpr t1 = Power(Times(C1D2, Plus(m, Sqrt(n))), C1D3);
					// t2 = (1/2 * (m - n^(1/2))) ^ (1/3)
					IExpr t2 = Power(Times(C1D2, Subtract(m, Sqrt(n))), C1D3);

					result.add(Times(CN1D3, Plus(a, t1, t2)));
					result.add(Times(CN1D3, Plus(a, Times(omega2, t1), Times(omega1, t2))));
					result.add(Times(CN1D3, Plus(a, Times(omega1, t1), Times(omega2, t2))));
				} else {
					result.add(Power(jas.integerPoly2Expr(iPoly), integer(val)));
				}
			}
			return result;

		} catch (Exception e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
		return null;
	}

}