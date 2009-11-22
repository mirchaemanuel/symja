package org.matheclipse.core.reflection.system;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;
import edu.jas.structure.Power;
import edu.jas.ufd.FactorAbstract;
import edu.jas.ufd.FactorFactory;
import edu.jas.ufd.GCDFactory;
import edu.jas.ufd.GreatestCommonDivisorAbstract;

/**
 * Evaluate the partial fraction decomposition of a univariate polynomial
 * fraction.
 * 
 * See <a href="http://en.wikipedia.org/wiki/Partial_fraction">Wikipedia -
 * Partial fraction decomposition</a>
 */
public class Apart extends AbstractFunctionEvaluator {

  public Apart() {
  }

  @Override
  public IExpr evaluate(final IAST lst) {
    if (lst.size() != 2) {
      return null;
    }
    IAST variableList = null;

    variableList = Variables.call(lst.get(1));
    if (variableList.size() != 2) {
      // factorization only possible for univariate polynomials
      return null;
    }
    try {
      final IExpr header = lst.get(1).head();
      if (header == F.Times || header == F.Power) {
        IExpr[] parts = Integrate.getFractionalParts2(lst.get(1));
        if (parts != null) {

          IExpr exprNumerator = F.eval(F.ExpandAll, parts[0]);
          IExpr exprDenominator = F.eval(F.ExpandAll, parts[1]);
          ASTRange r = new ASTRange(variableList, 1);
          List<IExpr> varList = r.toList();

          String[] varListStr = new String[1];
          varListStr[0] = variableList.get(1).toString();
          JASConvert<BigRational> jas = new JASConvert<BigRational>(varList);
          final GreatestCommonDivisorAbstract<BigRational> ufd = GCDFactory
              .getProxy(BigRational.ZERO);
          GenPolynomial<BigRational> numerator = jas.expr2Poly(exprNumerator);
          GenPolynomial<BigRational> denominator = jas
              .expr2Poly(exprDenominator);

          // get factors
          FactorAbstract<BigRational> factorAbstract = FactorFactory
              .getImplementation(BigRational.ZERO);
          SortedMap<GenPolynomial<BigRational>, Long> sfactors = factorAbstract
              .baseFactors(denominator);

          List<GenPolynomial<BigRational>> D = new ArrayList<GenPolynomial<BigRational>>(
              sfactors.keySet());
          List<GenPolynomial<BigRational>> DP = new ArrayList<GenPolynomial<BigRational>>();
          for (GenPolynomial<BigRational> f : D) {
            long e = sfactors.get(f);
            for (long i = 0; i < e; i++) {
              // GenPolynomial<BigRational> dp = Power
              // .<GenPolynomial<BigRational>> positivePower(f, e);
              DP.add(f);
            }
          }

          // List<GenPolynomial<BigRational>> Di = ufd.coPrime(DP);
          List<GenPolynomial<BigRational>> Ai = ufd.basePartialFraction(
              numerator, DP);
          // return [ A0, A1,..., An ] with
          // A/prod(D) = A0 + sum( Ai/Di ) with deg(Ai) < deg(Di).
          IAST result = F.Plus();
          if (Ai.size() > 0) {
            result.add(jas.poly2Expr(Ai.get(0), null));
            for (int i = 1; i < Ai.size(); i++) {
              result.add(F.Times(jas.poly2Expr(Ai.get(i), null), F.Power(jas
                  .poly2Expr(DP.get(i - 1), null), F.CN1)));
            }
          }
          return result;
        }
      } else {
        return lst.get(1);
      }
    } catch (Exception e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    }
    return null;
  }

}