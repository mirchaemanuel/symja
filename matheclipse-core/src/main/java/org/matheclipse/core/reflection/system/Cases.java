package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;

public class Cases implements IFunctionEvaluator {

	public Cases() {
	}

	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST)) {
			return cases((IAST) functionList.get(1), functionList.get(2));
		}
		return null;
	}

	public static IAST cases(final IAST list, final IExpr pattern) {
		final PatternMatcher matcher = new PatternMatcher(pattern);
		return list.args().select(list.copyHead(), matcher);
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}

}
