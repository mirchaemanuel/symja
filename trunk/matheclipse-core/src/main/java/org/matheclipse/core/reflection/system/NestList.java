package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.Algorithms;

public class NestList implements IFunctionEvaluator {

	public NestList() {
	}

	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 4);

		return evaluateNestList(ast, List());
	}

	public static IExpr evaluateNestList(final IAST ast, final IAST resultList) {
		if (ast.get(3).isInteger()) {
			final int n = Validate.checkIntType(ast, 3);
			Algorithms.nestList(ast.get(2), n, Functors.append(F.ast(ast.get(1))), resultList);
			return resultList;
		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}
}
