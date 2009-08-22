package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.BinaryMap;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Fold implements IFunctionEvaluator {

	public Fold() {
	}

	public IExpr evaluate(final IAST ast) {
		return evaluateNestList(ast);
	}

	public static IExpr evaluateNestList(final IAST ast) {
		try {
			if ((ast.size() == 4) && (ast.get(3) instanceof IAST)) {
				final IAST list = (IAST)ast.get(3);
				return list.args().fold(new BinaryMap(F.ast(ast.get(1))),ast.get(2));
//				return Folding.fold(ast.get(2), list, 1, list.size(), new BinaryMap(f.createAST(ast.get(1))));
			}
		} catch (final ArithmeticException e) {

		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}
}
