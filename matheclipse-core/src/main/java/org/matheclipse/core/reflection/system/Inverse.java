package org.matheclipse.core.reflection.system;

import org.apache.commons.math.linear.FieldDecompositionSolver;
import org.apache.commons.math.linear.FieldLUDecompositionImpl;
import org.apache.commons.math.linear.FieldMatrix;
import org.matheclipse.core.eval.interfaces.AbstractMatrix1Matrix;
import org.matheclipse.core.interfaces.IExpr;

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
	public FieldMatrix<IExpr> matrixEval(FieldMatrix<IExpr> matrix) {
		final FieldLUDecompositionImpl<IExpr> lu = new FieldLUDecompositionImpl<IExpr>(matrix);
		FieldDecompositionSolver<IExpr> solver = lu.getSolver();
		if (!solver.isNonSingular()) {
			return null;
		}
		return solver.getInverse();
	}
}