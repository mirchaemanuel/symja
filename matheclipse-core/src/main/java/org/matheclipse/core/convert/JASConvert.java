package org.matheclipse.core.convert;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

import com.google.common.base.Predicates;

import edu.jas.arith.BigRational;
import edu.jas.arith.ModInteger;
import edu.jas.integrate.Integral;
import edu.jas.integrate.LogIntegral;
import edu.jas.integrate.QuotIntegral;
import edu.jas.poly.AlgebraicNumber;
import edu.jas.poly.AlgebraicNumberRing;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.Monomial;
import edu.jas.poly.PolyUtil;
import edu.jas.poly.TermOrder;
import edu.jas.structure.RingElem;
import edu.jas.structure.RingFactory;
import edu.jas.ufd.Quotient;

/**
 * Convert <a href="http://krum.rz.uni-mannheim.de/jas/">JAS</a> objects from
 * and to MathEclipse objects
 * 
 * 
 * @param <C>
 */
public class JASConvert<C extends RingElem<C>> {
	private final RingFactory<C> fRingFactory;
	private final TermOrder fTermOrder;
	private final GenPolynomialRing<C> fPolyFactory;

	private final GenPolynomialRing<edu.jas.arith.BigInteger> fBigIntegerPolyFactory;

	private final List<? extends IExpr> fVariables;

	/**
	 * Constructor which uses edu.jas.arith.BigRational as ring factory.
	 * 
	 * @param variablesList
	 */
	// public JASConvert(final List<? extends IExpr> variablesList) {
	// this(variablesList, (RingFactory<BigRational>)BigRational.ZERO, new
	// TermOrder(TermOrder.INVLEX));
	// };

	public JASConvert(final List<? extends IExpr> variablesList, RingFactory<C> ringFactory) {
		this(variablesList, ringFactory, new TermOrder(TermOrder.INVLEX));
	}

	public JASConvert(final List<? extends IExpr> variablesList, RingFactory<C> ringFactory, TermOrder termOrder) {
		this.fRingFactory = ringFactory;
		this.fVariables = variablesList;
		String[] vars = new String[fVariables.size()];
		for (int i = 0; i < fVariables.size(); i++) {
			vars[i] = fVariables.get(i).toString();
		}
		this.fTermOrder = termOrder;
		this.fPolyFactory = new GenPolynomialRing<C>(fRingFactory, fVariables.size(), fTermOrder, vars);
		this.fBigIntegerPolyFactory = new GenPolynomialRing<edu.jas.arith.BigInteger>(edu.jas.arith.BigInteger.ZERO, fVariables.size(),
				fTermOrder, vars);
	}

	/**
	 * Convert the given expression into a <a
	 * href="http://krum.rz.uni-mannheim.de/jas/">JAS</a> polynomial
	 * 
	 * @param exprPoly
	 * @return
	 * @throws ArithmeticException
	 * @throws ClassCastException
	 */
	public GenPolynomial<C> expr2Poly(final IExpr exprPoly) throws ArithmeticException, ClassCastException {
		if (exprPoly instanceof IAST) {
			final IAST ast = (IAST) exprPoly;
			GenPolynomial<C> result = fPolyFactory.getZERO();
			GenPolynomial<C> p = fPolyFactory.getZERO();
			if (ast.isASTSizeGE(F.Plus, 2)) {
				IExpr expr = ast.get(1);
				result = expr2Poly(expr);
				for (int i = 2; i < ast.size(); i++) {
					expr = ast.get(i);
					p = expr2Poly(expr);
					result = result.sum(p);
				}
				return result;
			} else if (ast.isASTSizeGE(F.Times, 2)) {
				IExpr expr = ast.get(1);
				result = expr2Poly(expr);
				for (int i = 2; i < ast.size(); i++) {
					expr = ast.get(i);
					p = expr2Poly(expr);
					result = result.multiply(p);
				}
				return result;
			} else if (ast.isAST(F.Power, 3)) {
				final IExpr expr = ast.get(1);
				for (int i = 0; i < fVariables.size(); i++) {
					if (fVariables.get(i).equals(expr)) {
						if (((IInteger) ast.get(2)).isNegative()) {
							throw new ArithmeticException("JASConvert:expr2Poly - negative exponent: " + ast.get(2).toString());
						}
						// may throw ClassCastExcepion or ArithmeticException
						ExpVector e = ExpVector.create(fVariables.size(), i, ((IInteger) ast.get(2)).toInt());
						return fPolyFactory.getONE().multiply(e);
					}
				}
				// return Constant.valueOf((IExpr) ast);
			}
		} else if (exprPoly instanceof ISymbol) {
			for (int i = 0; i < fVariables.size(); i++) {
				if (fVariables.get(i).equals(exprPoly)) {
					ExpVector e = ExpVector.create(fVariables.size(), i, 1L);
					return fPolyFactory.getONE().multiply(e);
				}
			}
			return new GenPolynomial(fPolyFactory, exprPoly);
		} else if (exprPoly instanceof IInteger) {
			// BigInteger bi = ((IInteger)
			// exprPoly).getBigNumerator().toJavaBigInteger();
			return fPolyFactory.fromInteger(((IInteger) exprPoly).toInt());
		} else if (exprPoly instanceof IFraction) {
			BigInteger n = ((IFraction) exprPoly).getBigNumerator().toJavaBigInteger();
			BigInteger d = ((IFraction) exprPoly).getBigDenominator().toJavaBigInteger();
			BigRational nr = new BigRational(n);
			BigRational dr = new BigRational(d);
			BigRational r = nr.divide(dr);
			return new GenPolynomial(fPolyFactory, r);// pfac.getONE().multiply(r);
		}
		if (exprPoly.isFree(Predicates.in(fVariables), true)) {
			return new GenPolynomial(fPolyFactory, exprPoly);
		}
		throw new ClassCastException(exprPoly.toString());
	}

	/**
	 * BigInteger from BigRational coefficients. Represent as polynomial with
	 * BigInteger coefficients by multiplication with the gcd of the numerators
	 * and the lcm of the denominators of the BigRational coefficients.
	 * 
	 * @param A
	 *          polynomial with BigRational coefficients to be converted.
	 * @return Object[] with 3 entries: [0]->gcd [1]->lcm and [2]->polynomial with
	 *         BigInteger coefficients.
	 */
	public Object[] factorTerms(GenPolynomial<BigRational> A) {
		return PolyUtil.integerFromRationalCoefficientsFactor(fBigIntegerPolyFactory, A);
	}

	/**
	 * @return the fPolyFactory
	 */
	public GenPolynomialRing<C> getPolynomialRingFactory() {
		return fPolyFactory;
	}

	/**
	 * BigInteger from BigRational coefficients. Represent as polynomial with
	 * BigInteger coefficients by multiplication with the lcm of the numerators of
	 * the BigRational coefficients.
	 * 
	 * @param A
	 *          polynomial with BigRational coefficients to be converted.
	 * @return polynomial with BigInteger coefficients.
	 */
	public GenPolynomial<edu.jas.arith.BigInteger> integerFromRationalCoefficients(GenPolynomial<BigRational> A) {
		return PolyUtil.integerFromRationalCoefficients(fBigIntegerPolyFactory, A);
	}

	public IAST integerPoly2Expr(final GenPolynomial<edu.jas.arith.BigInteger> poly) throws ArithmeticException, ClassCastException {
		if (poly.length() == 0) {
			return F.Plus(F.C0);
		}
		IAST result = F.Plus();
		for (Monomial<edu.jas.arith.BigInteger> monomial : poly) {
			edu.jas.arith.BigInteger coeff = monomial.coefficient();
			ExpVector exp = monomial.exponent();
			IInteger coeffValue = F.integer(coeff.getVal());
			IAST monomTimes = F.Times(coeffValue);
			long lExp;
			for (int i = 0; i < exp.length(); i++) {
				lExp = exp.getVal(i);
				if (lExp != 0) {
					monomTimes.add(F.Power(fVariables.get(i), F.integer(lExp)));
				}
			}
			result.add(monomTimes);
		}
		return result;
	}

	/**
	 * Convert a jas <code>Integral</code> into a matheclipse expression
	 * 
	 * @param integral
	 *          the JAS Integral
	 * @return
	 */
	public IAST integral2Expr(Integral<BigRational> integral) {
		IAST sum = F.Plus();
		GenPolynomial<BigRational> pol = integral.pol;
		List<GenPolynomial<BigRational>> rational = integral.rational;
		List<LogIntegral<BigRational>> logarithm = integral.logarithm;

		if (!pol.isZERO()) {
			sum.add(poly2Expr(pol, null));
		}
		if (rational.size() != 0) {
			int i = 0;
			while (i < rational.size()) {
				sum.add(F.Times(poly2Expr(rational.get(i++), null), F.Power(poly2Expr(rational.get(i++), null), F.CN1)));
			}
		}
		if (logarithm.size() != 0) {
			for (LogIntegral<BigRational> pf : logarithm) {
				sum.add(logIntegral2Expr(pf));
			}
		}
		return sum;
	}

	/**
	 * Convert a jas <code>LogIntegral</code> into a matheclipse expression
	 * 
	 * @param logIntegral
	 *          the JAS LogIntegral
	 * @return
	 */

	public IAST logIntegral2Expr(LogIntegral<BigRational> logIntegral) {
		IAST plus = F.Plus();

		List<BigRational> cfactors = logIntegral.cfactors;

		List<GenPolynomial<BigRational>> cdenom = logIntegral.cdenom;

		List<AlgebraicNumber<BigRational>> afactors = logIntegral.afactors;

		List<GenPolynomial<AlgebraicNumber<BigRational>>> adenom = logIntegral.adenom;

		if (cfactors.size() > 0) {
			for (int i = 0; i < cfactors.size(); i++) {
				BigRational cp = cfactors.get(i);
				GenPolynomial<BigRational> p = cdenom.get(i);
				plus.add(F.Times(F.fraction(cp.numerator(), cp.denominator()), F.Log(poly2Expr(p, null))));
			}
		}

		// TODO implement this conversion for AlgebraicNumbers...
		if (afactors.size() > 0) {
			for (int i = 0; i < afactors.size(); i++) {

				AlgebraicNumber<BigRational> ap = afactors.get(i);
				AlgebraicNumberRing<BigRational> ar = ap.factory();
				GenPolynomial<AlgebraicNumber<BigRational>> p = adenom.get(i);
				if (p.degree(0) < ar.modul.degree(0) && ar.modul.degree(0) > 2) {

				}
				GenPolynomial<BigRational> v = ap.getVal();
				IAST times = F.Times();

				AlgebraicNumber<BigRational> arGen = ar.getGenerator();
				// sb.append(" ## over " + ap.factory() + "\n");

				if (p.degree(0) < ar.modul.degree(0) && ar.modul.degree(0) > 2) {
					IAST rootOf = F.function(F.RootOf);
					rootOf.add(poly2Expr(ar.modul, null));
					// rootOf.add(variable);
					// sb.append("sum_(" + ar.getGenerator() + " in ");
					// sb.append("rootOf(" + ar.modul + ") ) ");
					times.add(rootOf);

					throw new UnsupportedOperationException("JASConvert#logIntegral2Expr()");
				}

				times.add(poly2Expr(v, null));
				times.add(F.Log(polyAlgebraicNumber2Expr(p)));
				plus.add(times);
			}

		}
		return plus;
	}

	public IAST modIntegerPoly2Expr(final GenPolynomial<ModInteger> poly) throws ArithmeticException, ClassCastException {
		if (poly.length() == 0) {
			return F.Plus(F.C0);
		}
		IAST result = F.Plus();
		for (Monomial<ModInteger> monomial : poly) {
			ModInteger coeff = monomial.coefficient();
			ExpVector exp = monomial.exponent();
			IInteger coeffValue = F.integer(coeff.getVal());
			IAST monomTimes = F.Times(coeffValue);
			long lExp;
			for (int i = 0; i < exp.length(); i++) {
				lExp = exp.getVal(i);
				if (lExp != 0) {
					monomTimes.add(F.Power(fVariables.get(i), F.integer(lExp)));
				}
			}
			result.add(monomTimes);
		}
		return result;
	}

	/**
	 * Converts a <a href="http://krum.rz.uni-mannheim.de/jas/">JAS</a> polynomial
	 * to a MathEclipse AST with head <code>Plus</code>
	 * 
	 * @param poly
	 *          a JAS polynomial
	 * @return
	 * @throws ArithmeticException
	 * @throws ClassCastException
	 */
	public IAST poly2Expr(final GenPolynomial<BigRational> poly) throws ArithmeticException, ClassCastException {
		return poly2Expr(poly, null);
	}

	/**
	 * Converts a <a href="http://krum.rz.uni-mannheim.de/jas/">JAS</a> polynomial
	 * to a MathEclipse AST with head <code>Plus</code>
	 * 
	 * @param poly
	 *          a JAS polynomial
	 * @param variable
	 * @return
	 * @throws ArithmeticException
	 * @throws ClassCastException
	 */
	public IAST poly2Expr(final GenPolynomial<BigRational> poly, IExpr variable) throws ArithmeticException, ClassCastException {
		if (poly.length() == 0) {
			return F.Plus(F.C0);
		}

		boolean getVar = variable == null;
		IAST result = F.Plus();
		for (Monomial<BigRational> monomial : poly) {
			BigRational coeff = monomial.coefficient();
			ExpVector exp = monomial.exponent();
			IFraction coeffValue = F.fraction(coeff.numerator(), coeff.denominator());
			IAST monomTimes = F.Times(coeffValue);
			long lExp;
			for (int i = 0; i < exp.length(); i++) {
				lExp = exp.getVal(i);
				if (lExp != 0) {
					if (getVar) {
						variable = fVariables.get(i);
					}
					monomTimes.add(F.Power(variable, F.integer(lExp)));
				}
			}
			result.add(monomTimes);
		}
		return result;
	}

	public IAST polyAlgebraicNumber2Expr(final GenPolynomial<AlgebraicNumber<BigRational>> poly) throws ArithmeticException,
			ClassCastException {
		if (poly.length() == 0) {
			return F.Plus(F.C0);
		}

		SortedMap<ExpVector, AlgebraicNumber<BigRational>> val = poly.getMap();
		if (val.size() == 0) {
			return F.Plus(F.C0);
		} else {
			IAST result = F.Plus();
			String symbolName = poly.factory().getVars()[0];
			IExpr sym = F.$s(symbolName);
			for (Map.Entry<ExpVector, AlgebraicNumber<BigRational>> m : val.entrySet()) {
				AlgebraicNumber<BigRational> c = m.getValue();

				IAST times = F.Times();
				ExpVector e = m.getKey();
				if (!c.isONE() || e.isZERO()) {
					times.add(algebraicNumber2Expr(c, sym));
				}
				if (e != null && sym != null) {
					long lExp = e.getVal(0);
					if (lExp != 0) {
						times.add(F.Power(sym, F.integer(lExp)));
					} else {
						times.add(F.Power(sym, F.integer(lExp)));
					}
				}
				if (times.size() > 1) {
					result.add(times);
				}

			}
			return result;
		}

	}

	public IAST algebraicNumber2Expr(final AlgebraicNumber<BigRational> coeff, IExpr variable) throws ArithmeticException,
			ClassCastException {
		GenPolynomial<BigRational> val = coeff.val;
		return poly2Expr(val, variable);
	}

	/**
	 * Convert a jas <code>Integral</code> into a matheclipse expression
	 * 
	 * @param integral
	 *          the JAS Integral
	 * @return
	 */
	public IAST quotIntegral2Expr(QuotIntegral<BigRational> integral) {
		IAST sum = F.Plus();
		List<Quotient<BigRational>> rational = integral.rational;
		List<LogIntegral<BigRational>> logarithm = integral.logarithm;

		// StringBuffer sb = new StringBuffer();
		// sb.append("integral( " + quot.toString() + " )" );
		// sb.append(" =\n");
		// boolean first = true;
		if (rational.size() != 0) {
			Quotient<BigRational> qTemp;
			GenPolynomial<BigRational> qNum;
			GenPolynomial<BigRational> qDen;

			for (int i = 0; i < rational.size(); i++) {
				qTemp = rational.get(i);
				qNum = qTemp.num;
				qDen = qTemp.den;
				sum.add(F.Times(poly2Expr(qNum, null), F.Power(poly2Expr(qDen, null), F.CN1)));
			}
		}
		if (logarithm.size() != 0) {
			for (LogIntegral<BigRational> pf : logarithm) {
				sum.add(logIntegral2Expr(pf));
			}
		}

		return sum;
	}

	public IAST rationalPoly2Expr(final GenPolynomial<BigRational> poly) throws ArithmeticException, ClassCastException {
		if (poly.length() == 0) {
			return F.Plus(F.C0);
		}
		IAST result = F.Plus();
		for (Monomial<BigRational> monomial : poly) {
			BigRational coeff = monomial.coefficient();
			ExpVector exp = monomial.exponent();
			IFraction coeffValue = F.fraction(coeff.numerator(), coeff.denominator());
			IAST monomTimes = F.Times(coeffValue);
			long lExp;
			for (int i = 0; i < exp.length(); i++) {
				lExp = exp.getVal(i);
				if (lExp != 0) {
					monomTimes.add(F.Power(fVariables.get(i), F.integer(lExp)));
				}
			}
			result.add(monomTimes);
		}
		return result;
	}

	public static IComplex jas2Complex(edu.jas.poly.Complex<BigRational> c) {
		IFraction re = F.fraction(c.getRe().numerator(), c.getRe().denominator());
		IFraction im = F.fraction(c.getIm().numerator(), c.getIm().denominator());
		return F.complex(re, im);
	}
}
