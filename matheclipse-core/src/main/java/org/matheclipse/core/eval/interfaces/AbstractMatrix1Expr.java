package org.matheclipse.core.eval.interfaces;

import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public abstract class AbstractMatrix1Expr extends AbstractFunctionEvaluator {

  public AbstractMatrix1Expr() {
  }

  @Override
  public IExpr evaluate(final IAST function) {
    FieldMatrix<IExpr> matrix;
    try {
      if (function.size() == 2) {
        final IAST list = (IAST) function.get(1);
        matrix = Convert.list2Matrix(list);
        return matrixEval(matrix);
      }
    } catch (final ClassCastException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    } catch (final IndexOutOfBoundsException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    }

    return null;
  }

  @Override
  public IExpr numericEval(final IAST function) {
    RealMatrix matrix;
    try {
      if (function.size() == 2) {
        final IAST list = (IAST) function.get(1);
        matrix = Convert.list2RealMatrix(list);
        return realMatrixEval(matrix);
      }
    } catch (final ClassCastException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    } catch (final IndexOutOfBoundsException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    }

    // fall back to symbolic mode
    return evaluate(function);
  }

  public abstract IExpr matrixEval(FieldMatrix<IExpr> matrix);

  public abstract IExpr realMatrixEval(RealMatrix matrix);
}