package org.matheclipse.core.reflection.system;

import java.util.List;

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
import edu.jas.root.ComplexRootsAbstract;
import edu.jas.root.ComplexRootsSturm;
import edu.jas.root.InvalidBoundaryException;
import edu.jas.root.Rectangle;
import edu.jas.structure.Complex;
import edu.jas.structure.ComplexRing;
import edu.jas.ufd.Squarefree;
import edu.jas.ufd.SquarefreeFactory;

/**
 * Determine complex root intervals of a univariate polynomial
 * 
 */
public class RootIntervals extends AbstractFunctionEvaluator {

  public RootIntervals() {
  }

  @Override
  public IExpr evaluate(final IAST lst) {
    if (lst.size() != 2) {
      return null;
    }
    return croots(lst);
  }

  /**
   * Complex roots intervals.
   * 
   * @param lst
   * @return
   */
  private static IAST croots(final IAST lst) {

    try {
      ExprVariables eVar = new ExprVariables(lst.get(1));
      if (!eVar.isSize(1)) {
        // factor only possible for univariate polynomials
        return null;
      }
      IExpr expr = F.evalExpandAll(lst.get(1));
      ASTRange r = new ASTRange(eVar.getVarList(), 1);
      List<IExpr> varList = r.toList();

      ComplexRing<BigRational> cfac = new ComplexRing<BigRational>(
          new BigRational(1));
      ComplexRootsAbstract<BigRational> cr = new ComplexRootsSturm<BigRational>(
          cfac);

      JASConvert<Complex<BigRational>> jas = new JASConvert<Complex<BigRational>>(
          varList, cfac);
      GenPolynomial<Complex<BigRational>> poly = jas.expr2Poly(expr);

      Squarefree<Complex<BigRational>> engine = SquarefreeFactory
          .<Complex<BigRational>> getImplementation(cfac);
      poly = engine.squarefreePart(poly);

      List<Rectangle<BigRational>> roots = cr.complexRoots(poly);
      // System.out.println("a = " + a);
      // System.out.println("roots = " + roots);
      // assertTrue("#roots == deg(a) ", roots.size() == poly.degree(0));

      BigRational len = new BigRational(1, 1000);
      // System.out.println("len = " + len);
      try {
        IAST resultList = F.List();
        IAST rectangleList;
        for (Rectangle<BigRational> root : roots) {

          rectangleList = F.List();
          // System.out.println(root.toString());
          Rectangle<BigRational> refine = cr.complexRootRefinement(root, poly,
              len);
          rectangleList.add(JASConvert.jas2Complex(refine.getNW()));
          rectangleList.add(JASConvert.jas2Complex(refine.getSW()));
          rectangleList.add(JASConvert.jas2Complex(refine.getSE()));
          rectangleList.add(JASConvert.jas2Complex(refine.getNE()));
          resultList.add(rectangleList);
          // System.out.println("refine = " + refine);

        }
        return resultList;
      } catch (InvalidBoundaryException e) {
        return null;
        // fail("" + e);
      }
    } catch (Exception e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    }
    return null;
  }

}