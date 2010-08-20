package org.matheclipse.core.reflection.system;

import org.apache.commons.math.linear.BlockFieldMatrix;
import org.apache.commons.math.linear.FieldMatrix;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.exception.NonNegativeIntegerExpected;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ExprField;
import org.matheclipse.core.expression.ExprFieldElement;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class MatrixPower extends AbstractFunctionEvaluator {

	public MatrixPower() {
		super();
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 3) {
			return null;
		}
		FieldMatrix<ExprFieldElement> matrix;
		FieldMatrix<ExprFieldElement> resultMatrix;
		try {
			matrix = Convert.list2Matrix((IAST) lst.get(1));
			final int p = Validate.checkIntType(lst, 2, Integer.MIN_VALUE);
			if (p < 0) {
				return null;
			}
			if (p == 1) {
				((IAST)lst.get(1)).addEvalFlags(IAST.IS_MATRIX);
				return lst.get(1);
			}
			if (p == 0) {
				resultMatrix = new BlockFieldMatrix<ExprFieldElement>(ExprField.CONST, matrix.getRowDimension(), matrix.getColumnDimension());
				int min = matrix.getRowDimension();
				if (min > matrix.getColumnDimension()) {
					min = matrix.getColumnDimension();
				}
				for (int i = 0; i < min; i++) {
					resultMatrix.setEntry(i, i, ExprField.CONST.getOne());
				}

				return Convert.matrix2List(resultMatrix);
			}
			resultMatrix = matrix;
			for (int i = 1; i < p; i++) {
				resultMatrix = resultMatrix.multiply(matrix);
			}
			return Convert.matrix2List(resultMatrix);

		} catch (final ClassCastException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		} catch (final ArithmeticException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
			throw new NonNegativeIntegerExpected(lst, 2);
		} catch (final IndexOutOfBoundsException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
		return null;
	}
}