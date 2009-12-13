package org.matheclipse.core.reflection.system;

import java.util.List;
import java.util.SortedMap;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
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

    ExprVariables eVar = new ExprVariables(lst.get(1));
    if (!eVar.isSize(1)) {
      // factor only possible for univariate polynomials
      return null;
    }
    try {
      IExpr expr = F.eval(F.ExpandAll, lst.get(1));
      ASTRange r = new ASTRange(eVar.getVarList(), 1);
      List<IExpr> varList = r.toList();

      JASConvert<BigRational> jas = new JASConvert<BigRational>(varList);
      GenPolynomial<BigRational> poly = jas.expr2Poly(expr);

      FactorAbstract<BigRational> factorAbstract = FactorFactory
          .getImplementation(BigRational.ONE);
      SortedMap<GenPolynomial<BigRational>, Long> map = factorAbstract
          .baseFactors(poly);
      IAST result = F.List();// F.function(F.Or);
      IInteger a;
      IInteger b;
      IInteger c;
      for (SortedMap.Entry<GenPolynomial<BigRational>, Long> entry : map
          .entrySet()) {
        GenPolynomial<BigRational> key = entry.getKey();
        GenPolynomial<edu.jas.arith.BigInteger> iPoly = (GenPolynomial<edu.jas.arith.BigInteger>) jas
            .factorTerms(key)[2];
        Long val = entry.getValue();
        long varDegree = iPoly.degree(0);
        if (iPoly.isONE()) {
          continue;
        }
        if (varDegree <= 2) {
          // solve Quadratic equation
          a = F.C0;
          b = F.C0;
          c = F.C0;
          for (Monomial<edu.jas.arith.BigInteger> monomial : iPoly) {
            edu.jas.arith.BigInteger coeff = (edu.jas.arith.BigInteger) monomial
                .coefficient();
            long lExp = monomial.exponent().getVal(0);
            if (lExp == 2) {
              a = F.integer(coeff.getVal());
            } else if (lExp == 1) {
              b = F.integer(coeff.getVal());
            } else if (lExp == 0) {
              c = F.integer(coeff.getVal());
            }
          }
          if (a.equals(F.C0)) {
            IFraction rat = F.fraction(c, b);
            result.add(rat.negate());
          } else {
            IAST sqrt = F.Sqrt(F.Plus(F.Sqr(b), F.Times(F.integer(-4), a, c)));
            IFraction rev2a = F.fraction(F.C1, a.multiply(F.C2));
            result.add(F.Times(rev2a, F.Plus(b.negate(), sqrt)));
            result.add(F.Times(rev2a, F.Plus(b.negate(), sqrt.negative())));
          }
        } else if (varDegree <= 3) {
          // solve Cubic equation: x^3 + a*x^2 + b*x + c = 0
          a = F.C0;
          b = F.C0;
          c = F.C0;
          for (Monomial<edu.jas.arith.BigInteger> monomial : iPoly) {
            edu.jas.arith.BigInteger coeff = (edu.jas.arith.BigInteger) monomial
                .coefficient();
            long lExp = monomial.exponent().getVal(0);
            if (lExp == 2) {
              a = F.integer(coeff.getVal());
            } else if (lExp == 1) {
              b = F.integer(coeff.getVal());
            } else if (lExp == 0) {
              c = F.integer(coeff.getVal());
            }
          }
          // m = 2*a^3 - 9*a*b + 27* c
          IInteger m = F.C2.multiply(a.pow(3)).subtract(
              a.multiply(b.multiply(F.integer(9)))).add(
              c.multiply(F.integer(27)));
          // k = a^2 - 3*b
          IInteger k = a.pow(2).subtract(F.C3.multiply(b));
          // n = m^2 - 4*k^3
          IInteger n = m.pow(2).subtract(F.C4.multiply(k.pow(3)));
          // o1 = -(1/2) + 1/2* I^(1/3)
          IExpr omega1 = F
              .Plus(F.CN1D2, F.Times(F.C1D2, F.Power(F.CI, F.C1D3)));
          // o1 = -(1/2) - 1/2* I^(1/3)
          IExpr omega2 = F.Plus(F.CN1D2, F
              .Times(F.CN1D2, F.Power(F.CI, F.C1D3)));
          IExpr t1 = F.Power(F.Times(F.C1D2, F.Plus(m, F.Sqrt(n))), F.C1D3);
          IExpr t2 = F.Power(F.Times(F.C1D2, F.Subtract(m, F.Sqrt(n))), F.C1D3);
          result.add(F.Times(F.CN1D3, F.Plus(a, t1, t2)));
          result.add(F.Times(F.CN1D3, F.Plus(a, F.Times(omega2, t1), F.Times(
              omega1, t2))));
          result.add(F.Times(F.CN1D3, F.Plus(a, F.Times(omega1, t1), F.Times(
              omega2, t2))));
        } else {
          result.add(F.Power(jas.integerPoly2Expr(iPoly), F.integer(val)));
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