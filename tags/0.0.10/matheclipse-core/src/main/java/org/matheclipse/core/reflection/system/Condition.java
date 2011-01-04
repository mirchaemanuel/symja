package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Condition implements IFunctionEvaluator {

	public Condition() {
	}

	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 3) {
			throw new WrongNumberOfArguments(ast, 2, ast.size() - 1);
		}
		if (F.evalTrue(ast.get(2))) {
			return F.eval(ast.get(1));
		}
		return null;
		// throw new ConditionException(ast.get(2));
	}

	public IExpr numericEval(final IAST ast) {
		return evaluate(ast);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
