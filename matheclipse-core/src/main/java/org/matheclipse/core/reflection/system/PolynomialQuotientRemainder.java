package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;

/**
 * 
 * See: <a
 * href="http://en.wikipedia.org/wiki/Polynomial_long_division">Wikipedia
 * :Polynomial long division</a>
 * 
 * @see org.matheclipse.core.reflection.system.PolynomialQuotient
 * @see org.matheclipse.core.reflection.system.PolynomialRemainder
 */
public class PolynomialQuotientRemainder extends AbstractFunctionEvaluator {

  public PolynomialQuotientRemainder() {
  }

  @Override
  public IExpr evaluate(final IAST lst) {
    if (lst.size() != 3) {
      return null;
    }
    IExpr[] result = quotientRemainder(lst);
    if (result == null) {
      return null;
    }
    IAST list = F.List();
    list.add(result[0]);
    list.add(result[1]);
    return list;
  }

  public static IExpr[] quotientRemainder(final IAST lst) {
    try {
      ExprVariables eVar = new ExprVariables(lst.get(1));
      eVar.addVarList(lst.get(2));
      IExpr expr = F.eval(F.ExpandAll, lst.get(1));
      ASTRange r = new ASTRange(eVar.getVarList(), 1);
      JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList());
      GenPolynomial<BigRational> poly1 = jas.expr2Poly(expr);
      expr = F.eval(F.ExpandAll, lst.get(2));
      GenPolynomial<BigRational> poly2 = jas.expr2Poly(expr);
      GenPolynomial<BigRational>[] divRem = poly1.divideAndRemainder(poly2);
      IExpr[] result = new IExpr[2];
      result[0] = jas.rationalPoly2Expr(divRem[0]);
      result[1] = jas.rationalPoly2Expr(divRem[1]);
      return result;
    } catch (Exception e) {
      if (Config.DEBUG) {
        e.printStackTrace();
      }
    }
    return null;
  }

}