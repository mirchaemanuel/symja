package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;

import com.google.common.base.Function;

public class Cases implements IFunctionEvaluator {

	public Cases() {
	}

	public IExpr evaluate(final IAST ast) {
		if ((ast.size() == 3) && (ast.get(1) instanceof IAST)) {
			return cases((IAST) ast.get(1), ast.get(2));
		}
		return null;
	}

	public static IAST cases(final IAST ast, final IExpr pattern) {
		if (pattern.isRuleAST()) {
			Function<IExpr, IExpr> function = Functors.rules((IAST) pattern);
			IAST[] results = ast.split(function);
			return results[0];
		}
		final PatternMatcher matcher = new PatternMatcher(pattern);
		return ast.filter(ast.copyHead(), matcher);
	}

	public IExpr numericEval(final IAST ast) {
		return evaluate(ast);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDREST);
	}

}
