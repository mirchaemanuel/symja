package org.matheclipse.core.reflection.system;

import org.apache.commons.math.linear.EigenDecompositionImpl;
import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.util.MathUtils;
import org.matheclipse.core.eval.exception.WrappedException;
import org.matheclipse.core.eval.interfaces.AbstractMatrix1Expr;
import org.matheclipse.core.expression.ExprFieldElement;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Compute the numerical Eigenvalues of a real symmetric matrix
 * 
 * See: <a
 * href="http://en.wikipedia.org/wiki/Eigenvalue,_eigenvector_and_eigenspace"
 * >Eigenvalue, eigenvector and eigenspace</a>
 */
public class Eigenvalues extends AbstractMatrix1Expr {

	public Eigenvalues() {
		super();
	}

	@Override
	public IExpr evaluate(final IAST function) {
		// switch to numeric calculation
		return numericEval(function);
	}

	public IAST realMatrixEval(RealMatrix matrix) {
		try {
			IAST list = F.List();
			EigenDecompositionImpl ed = new EigenDecompositionImpl(matrix, MathUtils.SAFE_MIN);
			double[] realValues = ed.getRealEigenvalues();
			double[] imagValues = ed.getImagEigenvalues();
			for (int i = 0; i < realValues.length; i++) {
				if (imagValues[i] == 0.0d) {
					list.add(F.num(realValues[i]));
				} else {
					list.add(F.complexNum(realValues[i], imagValues[i]));
				}
			}
			return list;
		} catch (InvalidMatrixException ime) {
			throw new WrappedException(ime);
		}
	}

	@Override
	public ExprFieldElement matrixEval(FieldMatrix<ExprFieldElement> matrix) {
		return null;
	}
}