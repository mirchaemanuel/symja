package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Transpose a matrix.
 * 
 * See <a href="http://en.wikipedia.org/wiki/Transpose">Transpose</a>
 */
public class Transpose implements IFunctionEvaluator {

	public Transpose() {

	}

	public IExpr evaluate(final IAST ast) {
		// TODO generalize transpose for all levels
		Validate.checkRange(ast, 2);

		final int[] dim = ast.get(1).isMatrix();
		if (dim != null) {
			final IAST mat = (IAST) ast.get(1);
			final IAST transposed = F.ast(F.List, dim[1], true);
			for (int i = 1; i <= dim[1]; i++) {
				transposed.set(i, F.ast(F.List, dim[0], true));
			}

			IAST row;
			IAST trRow;
			for (int i = 1; i <= dim[0]; i++) {
				row = (AST) mat.get(i);
				for (int j = 1; j <= dim[1]; j++) {
					trRow = (IAST) transposed.get(j);
					trRow.set(i, row.get(j));
				}
			}
			transposed.addEvalFlags(IAST.IS_MATRIX);
			return transposed;
		}
		return null;
	}

	public IExpr numericEval(final IAST ast) {
		return evaluate(ast);
	}

	public void setUp(final ISymbol symbol) {
	}

}
