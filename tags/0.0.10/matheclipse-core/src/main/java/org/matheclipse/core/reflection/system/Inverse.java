package org.matheclipse.core.reflection.system;

import org.apache.commons.math.linear.DecompositionSolver;
import org.apache.commons.math.linear.FieldDecompositionSolver;
import org.apache.commons.math.linear.FieldLUDecompositionImpl;
import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.matheclipse.core.eval.interfaces.AbstractMatrix1Matrix;
import org.matheclipse.core.expression.ExprFieldElement;

/**
 * Invert a matrix
 * 
 * See <a href="http://en.wikipedia.org/wiki/Invertible_matrix">Invertible
 * matrix</a>
 */
public class Inverse extends AbstractMatrix1Matrix {

  public Inverse() {
    super();
  }

  @Override
  public FieldMatrix<ExprFieldElement> matrixEval(FieldMatrix<ExprFieldElement> matrix) {
    final FieldLUDecompositionImpl<ExprFieldElement> lu = new FieldLUDecompositionImpl<ExprFieldElement>(
        matrix);
    FieldDecompositionSolver<ExprFieldElement> solver = lu.getSolver();
    if (!solver.isNonSingular()) {
      return null;
    }
    return solver.getInverse();
  }

  @Override
  public RealMatrix realMatrixEval(RealMatrix matrix) {
    final LUDecompositionImpl lu = new LUDecompositionImpl(matrix);
    DecompositionSolver solver = lu.getSolver();
    if (!solver.isNonSingular()) {
      return null;
    }
    return solver.getInverse();
  }
}