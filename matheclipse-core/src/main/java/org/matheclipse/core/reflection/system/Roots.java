package org.matheclipse.core.reflection.system;

import java.util.List;
import java.util.SortedMap;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.eval.util.Options;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.IRational;

import apache.harmony.math.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.Monomial;
import edu.jas.ufd.FactorAbstract;
import edu.jas.ufd.FactorFactory;

/**
 * Determine the roots of a univariate polynomial
 * 
 */
public class Roots extends AbstractFunctionEvaluator {

  public Roots() {
  }

  @Override
  public IExpr evaluate(final IAST lst) {
    if (lst.size() != 2 && lst.size() != 3) {
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

      if (lst.size() == 3) {
        final EvalEngine engine = EvalEngine.get();
        final Options options = new Options(F.Factor, engine, lst, 2);
        IExpr option = options.getOption("Modulus");
        if (option != null && option instanceof IInteger) {
          try {
            // found "Modulus" option
            final BigInteger value = ((IInteger) option).getBigNumerator();
            int intValue = ((IInteger) option).toInt();
            ModIntegerRing modIntegerRing = new ModIntegerRing(intValue, value
                .isProbablePrime(32));
            JASConvert<ModInteger> jas = new JASConvert<ModInteger>(varList,
                modIntegerRing);
            GenPolynomial<ModInteger> poly = jas.expr2Poly(expr);

            FactorAbstract<ModInteger> factorAbstract = FactorFactory
                .getImplementation(modIntegerRing);
            SortedMap<GenPolynomial<ModInteger>, Long> map = factorAbstract
                .baseFactors(poly);
            IAST result = F.Times();
            for (SortedMap.Entry<GenPolynomial<ModInteger>, Long> entry : map
                .entrySet()) {
              GenPolynomial<ModInteger> iPoly = entry.getKey();
              Long val = entry.getValue();
              result.add(F
                  .Power(jas.modIntegerPoly2Expr(iPoly), F.integer(val)));
            }
            return result;
          } catch (ArithmeticException ae) {
            // toInt() conversion failed
            if (Config.DEBUG) {
              ae.printStackTrace();
            }
            return null; // no evaluation
          }
        }
      }
      JASConvert<BigRational> jas = new JASConvert<BigRational>(varList);
      GenPolynomial<BigRational> poly = jas.expr2Poly(expr);

      FactorAbstract<BigRational> factorAbstract = FactorFactory
          .getImplementation(BigRational.ONE);
      SortedMap<GenPolynomial<BigRational>, Long> map = factorAbstract
          .baseFactors(poly);
      IAST result = F.List();//F.function(F.Or);
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