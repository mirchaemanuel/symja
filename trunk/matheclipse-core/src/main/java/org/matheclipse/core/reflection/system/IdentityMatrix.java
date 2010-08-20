package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.UnaryIndexFunctionDiagonal;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.generic.nested.IndexTableGenerator;

/**
 * Create an identity matrix
 * 
 * <a href="http://en.wikipedia.org/wiki/Identity_matrix">Identity matrix</a>
 */
public class IdentityMatrix extends AbstractFunctionEvaluator {

	public IdentityMatrix() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			return null;
		}

		if (functionList.get(1) instanceof IInteger) {
			int indx = Validate.checkIntType(functionList, 1);
			final IAST resultList = F.List();
			final int[] indexArray = new int[2];
			indexArray[0] = indx;
			indexArray[1] = indx;
			final IExpr[] valueArray = { F.C0, F.C1 };
			final IndexTableGenerator<IExpr, IAST> generator = new IndexTableGenerator<IExpr, IAST>(indexArray, resultList,
					new UnaryIndexFunctionDiagonal(valueArray), AST.COPY);
			final IAST matrix = (IAST) generator.table();
			if (matrix != null) {
				matrix.addEvalFlags(IAST.IS_MATRIX);
			}
			return matrix;
		}
		return null;
	}
}
