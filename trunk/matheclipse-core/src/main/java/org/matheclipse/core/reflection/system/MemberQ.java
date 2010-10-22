package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;

/**
 */
public class MemberQ implements IFunctionEvaluator {

	public MemberQ() {
	}

	public IExpr evaluate(final IAST ast) {
		if ((ast.size() == 3) && (ast.get(1).isAST())) {
			final IAST arg1 = (IAST) ast.get(1);
			final PatternMatcher matcher = new PatternMatcher(ast.get(2));
			for (int i = 1; i < arg1.size(); i++) {
				if (matcher.apply(arg1.get(i))) {
					return F.True;
				}
			}
		}
		return F.False;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}

}
