package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Apart extends AbstractFunctionEvaluator {

  public Apart() {
  }

  @Override
  public IExpr evaluate(final IAST lst) {
    if (lst.size() != 3 && lst.size() != 4) {
      return null;
    }
//    IAST variableList = null;
//
//    variableList = Variables.call(lst.get(1));
//    if (variableList.size() != 2) {
//      // factorization only possible for univariate polynomials
//      return null;
//    }
//    // IExpr variable = variableList.get(1);
//    try {
//      IExpr exprDenominator = F.eval(F.ExpandAll, lst.get(1));
//      IExpr exprNumerator = F.eval(F.ExpandAll, lst.get(2));
//      ASTRange r = new ASTRange(variableList, 1);
//      List<IExpr> varList = r.toList();
//
//      // if (lst.size() == 3) {
//      // final EvalEngine engine = EvalEngine.get();
//      // final Options options = new Options(F.Factor, engine, lst, 2);
//      //        
//      // }
//      JASConvert<Rational> jasNumerator = new JASConvert<Rational>(varList);
//      GenPolynomial<Rational> pNumerator = jasNumerator.expr2Poly(exprNumerator);
//      JASConvert<Rational> jasDenominator = new JASConvert<Rational>(varList);
//      GenPolynomial<Rational> pDenominator = jasDenominator.expr2Poly(exprDenominator);      
//      // calculate factors
//      FactorAbstract<Rational> factorAbstract = FactorFactory
//          .getImplementation(Rational.ONE);
//      SortedMap<GenPolynomial<Rational>, Long> map = factorAbstract
//          .baseFactors(pDenominator);
//      
//
//      BlockFieldMatrix<Rational> matrix = new BlockFieldMatrix<Rational>(
//          RationalField.CONST, 2, 2);
//      matrix.setEntry(0, 0, Rational.ONE);
//      matrix.setEntry(0, 1, Rational.ONE);
//      matrix.setEntry(1, 0, Rational.valueOf(5, 1));
//      matrix.setEntry(1, 1, Rational.valueOf(-8, 1));
//
//      final FieldLUDecompositionImpl<Rational> lu = new FieldLUDecompositionImpl<Rational>(
//          matrix);
//      FieldDecompositionSolver<Rational> fds = lu.getSolver();
//      long degreeDenominator = pDenominator.degree();
//      FieldVector<Rational> b = new ArrayFieldVector<Rational>(
//          RationalField.CONST, (int)degreeDenominator-1);
//      for (Monomial<Rational> monomial : pDenominator) {
//        monomial.coefficient();
//        b.setEntry(0, Rational.ONE);
//      }
//      
//      b.setEntry(0, Rational.ONE);
//      b.setEntry(1, Rational.valueOf(3, 1));
//      FieldVector<Rational> lsResult = fds.solve(b);
//      System.out.println("[" + lsResult.getEntry(0) + ","
//          + lsResult.getEntry(1) + "]");
//      
//      
//      
//      int i = 0;
//      for (SortedMap.Entry<GenPolynomial<Rational>, Long> entry : map
//          .entrySet()) {
//        GenPolynomial<Rational> key = entry.getKey();
//        GenPolynomial<edu.jas.arith.BigInteger> iPoly = (GenPolynomial<edu.jas.arith.BigInteger>) jasDenominator
//            .factorTerms(key)[2];
//        long degree = iPoly.degree();
//        Long val = entry.getValue();
//        if (degree == 2) {
//
//        } else {
//          iPoly.trailingBaseCoefficient();
//        }
//      }
//      IAST result = F.Plus();
//      i = 0;
//      for (SortedMap.Entry<GenPolynomial<Rational>, Long> entry : map
//          .entrySet()) {
//        GenPolynomial<Rational> key = entry.getKey();
//        GenPolynomial<BigInteger> iPoly = (GenPolynomial<BigInteger>) jasDenominator
//            .factorTerms(key)[2];
//        long degree = iPoly.degree();
//        Long val = entry.getValue();
//        if (degree == 2) {
//
//        } else {
//          result.add(F.Times(F.fraction(lsResult.getEntry(i++)), F.Power(jasDenominator
//              .integerPoly2Expr(iPoly), F.CN1)));
//        }
//        // result.add(F.Power(jas.integerPoly2Expr(iPoly), F.integer(val)));
//      }
//      return result;
//      // BigInteger[] mExpr =
//      // Poly2BigIntegerConverter.expr2Polynomial(lst.get(1), variable);
//      // // for (int i = 0; i < mExpr.length; i++) {
//      // // System.out.println(i+":"+mExpr[i].toString());
//      // // }
//      // List univariateFactorizationOverZresult =
//      // UnivariateFactorizationOverZ.univariateFactorizationOverZ(mExpr,
//      // mExpr.length - 1);
//      // int runIndex;
//      // int index;
//      // IAST result = F.Times();
//      // for (runIndex = 0; runIndex <=
//      // (univariateFactorizationOverZresult.size() - 1); runIndex = runIndex +
//      // 3) {
//      // IAST factor = F.Plus();
//      // for (index = (Integer)
//      // univariateFactorizationOverZresult.get(runIndex); index >= 0; index--)
//      // {
//      // BigInteger temp = ((BigInteger[])
//      // univariateFactorizationOverZresult.get(runIndex + 1))[index];
//      // if (!temp.equals(BigInteger.ZERO)) {
//      // if (!temp.equals(BigInteger.ONE) || index <= 0) {
//      // factor.add(F.Times(F.integer(temp), F.Power(variable,
//      // F.integer(index))));
//      // } else {
//      // factor.add(F.Power(variable, F.integer(index)));
//      // }
//      // }
//      // }
//      //
//      // Integer itemp = (Integer)
//      // univariateFactorizationOverZresult.get(runIndex + 2);
//      // if (itemp != 1) {
//      // result.add(F.Power(factor, F.integer((Integer)
//      // univariateFactorizationOverZresult.get(runIndex + 2))));
//      // } else {
//      // result.add(factor);
//      // }
//      // }
//      //
//      // return result;
//    } catch (Exception e) {
//      if (Config.DEBUG) {
//        e.printStackTrace();
//      }
//    }
    return null;
  }

}