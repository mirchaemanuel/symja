package org.matheclipse.core.reflection.system;

import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.fitting.PolynomialFitter;
import org.apache.commons.math.optimization.general.LevenbergMarquardtOptimizer;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Polynomial fitting of a given data point set.
 * 
 * <p>
 * Syntax: <code>Fit[ data, degree, variable ] </code>
 * </p>
 * 
 * 
 * Examples:<br/>
 * <code>Fit[{{1,1},{2,4},{3,9},{4,16}},2,x]  gives  x^2.0</code><br/>
 * <code>Fit[{1,4,9,16},2,x]  gives  x^2.0</code>
 * 
 * <p>
 * See <a
 * href="http://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm"
 * >Levenberg–Marquardt algorithm</a>
 * </p>
 */
public class Fit extends AbstractFunctionEvaluator {

  public Fit() {
    super();
  }

  @Override
  public IExpr evaluate(final IAST function) {
    // switch to numeric calculation
    return numericEval(function);
  }

  @Override
  public IExpr numericEval(final IAST function) {
    try {
      if (function.size() == 4 && function.get(2) instanceof IInteger
          && function.get(3) instanceof ISymbol) {
        int rowSize = -1;
        int degree = ((IInteger) function.get(2)).toInt();
        PolynomialFitter fitter = new PolynomialFitter(degree,
            new LevenbergMarquardtOptimizer());
        int[] im = function.get(1).isMatrix();
        if (im != null && im[1] == 2) {
          IAST matrix = (IAST) function.get(1);
          IAST row;
          for (int i = 1; i < matrix.size(); i++) {
            row = matrix.getAST(i);
            fitter.addObservedPoint(1.0, ((ISignedNumber) row.get(1))
                .doubleValue(), ((ISignedNumber) row.get(2)).doubleValue());
          }
        } else {
          rowSize = function.get(1).isVector();
          if (rowSize < 0) {
            return null;
          }
          IAST vector = (IAST) function.get(1);
          for (int i = 1; i < vector.size(); i++) {
            fitter.addObservedPoint(1.0, i, ((ISignedNumber) vector.get(i))
                .doubleValue());
          }
        }
        PolynomialFunction fitted;
        fitted = fitter.fit();
        return Convert.polynomialFunction2Expr(fitted, (ISymbol) function
            .get(3));
      }
    } catch (final ArithmeticException ae) {
      if (Config.SHOW_STACKTRACE) {
        ae.printStackTrace();
      }
    } catch (final ClassCastException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    } catch (final IndexOutOfBoundsException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    } catch (OptimizationException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    }
    return null;
  }
}