package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.NonNegativeIntegerExpected;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.UnaryMap;
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
			int n = 0;
			try {
				n = ((IInteger) ast.get(3)).toInt();
				if (n < 0) {
					throw new NonNegativeIntegerExpected(ast, 3);
				}
			} catch (final ArithmeticException e) {
				throw new NonNegativeIntegerExpected(ast, 3);
			}
			return Algorithms.nest(ast.get(2), n, new UnaryMap(F.ast(ast.get(1))));
		}

		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}
}
