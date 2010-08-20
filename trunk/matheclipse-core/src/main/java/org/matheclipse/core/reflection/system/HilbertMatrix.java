package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.NonNegativeIntegerExpected;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.generic.interfaces.IIndexFunction;
import org.matheclipse.generic.nested.IndexTableGenerator;

/**
 * Hilbert matrix, defined by A<sub>i,j</sub> = 1 / (i+j-1). See <a>
 * href="http://en.wikipedia.org/wiki/Hilbert_matrix">Wikipedia:Hilbert
 * matrix</a>
 */
public class HilbertMatrix extends AbstractFunctionEvaluator {

	public class HilbertFunctionDiagonal implements IIndexFunction<IExpr> {

		public HilbertFunctionDiagonal() {
		}

		public IExpr evaluate(final int[] index) {
			int res = index[0] + index[1] + 1;

			return F.Power(F.integer(res), F.CN1);
		}
	}

	public HilbertMatrix() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		int rowSize = 0;
		int columnSize = 0;
		if (functionList.size() == 2 && functionList.get(1) instanceof IInteger) {
			rowSize = Validate.checkIntType(functionList, 1);
			columnSize = rowSize;
		} else if (functionList.size() == 3 && functionList.get(1) instanceof IInteger && functionList.get(2) instanceof IInteger) {
			rowSize = Validate.checkIntType(functionList, 1);
			columnSize = Validate.checkIntType(functionList, 2);
		} else {
			return null;
		}

		final IAST resultList = F.List();
		final int[] indexArray = new int[2];
		indexArray[0] = rowSize;
		indexArray[1] = columnSize;
		final IndexTableGenerator<IExpr, IAST> generator = new IndexTableGenerator<IExpr, IAST>(indexArray, resultList,
				new HilbertFunctionDiagonal(), AST.COPY);
		final IAST matrix = (IAST) generator.table();
		if (matrix != null) {
			matrix.addEvalFlags(IAST.IS_MATRIX);
		}
		return matrix;
	}
}
