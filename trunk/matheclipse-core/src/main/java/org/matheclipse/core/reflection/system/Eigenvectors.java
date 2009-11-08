package org.matheclipse.core.reflection.system;

import org.apache.commons.math.linear.EigenDecompositionImpl;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.util.MathUtils;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.exception.WrappedException;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Compute the numerical Eigenvectors of a real symmetric matrix
 * 
 * See: <a 
 * href="http://en.wikipedia.org/wiki/Eigenvalue,_eigenvector_and_eigenspace"
 * >Eigenvalue, eigenvector and eigenspace</a>
 */
public class Eigenvectors extends AbstractFunctionEvaluator {

  public Eigenvectors() {
    super();
  }

  @Override
  public IExpr evaluate(final IAST function) {
    // switch to numeric calculation
    return numericEval(function);
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
    return evaluate(function);
  }

  public IAST realMatrixEval(RealMatrix matrix) {
    try {
      IAST list = F.List();
      EigenDecompositionImpl ed = new EigenDecompositionImpl(matrix,
          MathUtils.SAFE_MIN);
      for (int i = 0; i < matrix.getColumnDimension(); i++) {
        RealVector rv = ed.getEigenvector(i);
        list.add(Convert.realVector2List(rv));
      }
      return list;
    } catch (InvalidMatrixException ime) {
      throw new WrappedException(ime);
    }
  }
}