package org.matheclipse.core.reflection.system;

import org.apache.commons.math.linear.FieldLUDecompositionImpl;
import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.matheclipse.core.eval.interfaces.AbstractMatrix1Expr;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Compute the determinant of a matrix
 * 
 * See <a href="http://en.wikipedia.org/wiki/Determinant">Determinant</a>
 * 
 */
public class Det extends AbstractMatrix1Expr {

	public Det() {
		super();
	}

	@Override
	public IExpr matrixEval(final FieldMatrix<IExpr> matrix) {
		final FieldLUDecompositionImpl<IExpr> lu = new FieldLUDecompositionImpl<IExpr>(matrix);
		return lu.getDeterminant();
		// return matrix.determinant();
	}
	
	@Override
  public IExpr realMatrixEval(RealMatrix matrix){
	  final LUDecompositionImpl lu = new LUDecompositionImpl(matrix);
    return F.num(lu.getDeterminant());
	}
}