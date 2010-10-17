package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.Algorithms;

public class Nest implements IFunctionEvaluator {

	public Nest() {
	}

	public IExpr evaluate(final IAST ast) {
		return evaluateNest(ast);
	}

	public static IExpr evaluateNest(final IAST ast) {
		if ((ast.size() == 4) && (ast.get(3) instanceof IInteger)) {
			final int n = Validate.checkIntType(ast, 3);
			return Algorithms.nest(ast.get(2), n, Functors.append(F.ast(ast.get(1))));
		}

		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}
}
