package org.matheclipse.core.reflection.system;

import org.apache.commons.math.linear.FieldDecompositionSolver;
import org.apache.commons.math.linear.FieldLUDecompositionImpl;
import org.apache.commons.math.linear.FieldMatrix;
import org.apache.commons.math.linear.FieldVector;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ExprFieldElement;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Determine <code>x</code> for Matrix <code>A</code> in the equation
 * <code>A.x==b</code>
 * 
 */
public class LinearSolve extends AbstractFunctionEvaluator {

	public LinearSolve() {
		super();
	}

	@Override
	public IExpr evaluate(final IAST function) {
		FieldMatrix<ExprFieldElement> aMatrix;
		FieldVector<ExprFieldElement> bVector;
		try {
			if (function.size() == 3) {
				aMatrix = Convert.list2Matrix((IAST) function.get(1));
				bVector = Convert.list2Vector((IAST) function.get(2));
				final FieldLUDecompositionImpl<ExprFieldElement> lu = new FieldLUDecompositionImpl<ExprFieldElement>(aMatrix);

				FieldDecompositionSolver<ExprFieldElement> fds = lu.getSolver();
				FieldVector<ExprFieldElement> xVector = fds.solve(bVector);
				return Convert.vector2List(xVector);
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
	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

}