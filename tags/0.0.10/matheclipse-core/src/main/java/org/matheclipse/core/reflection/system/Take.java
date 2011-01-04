package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.eval.util.Sequence;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class Take extends AbstractFunctionEvaluator {

	public Take() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		try {
			if ((ast.size() >= 3) && (ast.get(1) instanceof IAST)) {
				final Sequence[] sequ = Sequence.createSequences(ast, 2);
				final IAST arg1 = (IAST) ast.get(1);
				if (sequ != null) {
					return AST.COPY.take(arg1, 0, sequ);
				}
			}
		} catch (final Exception e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
