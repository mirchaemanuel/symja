package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.generic.PositionConverter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.generic.nested.LevelSpec;
public class Position extends AbstractFunctionEvaluator {

	public Position() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST)) {
			final LevelSpec level = new LevelSpec(0, Integer.MAX_VALUE);
			return position((IAST) functionList.get(1), functionList.get(2), level);
		}
		if ((functionList.size() == 4) && (functionList.get(1) instanceof IAST)) {
			final LevelSpec level = new LevelSpecification(functionList.get(3));
			return position((IAST) functionList.get(1), functionList.get(2), level);
		}
		return null;
	}

	public static IAST position(final IAST list, final IExpr pattern, final LevelSpec level) {
		final PatternMatcher matcher = new PatternMatcher(pattern);
		final PositionConverter pos = new PositionConverter();

		final IAST cloneList = list.copyHead();
		final IAST resultList = List();
		AST.COPY.position(list, cloneList, resultList, level, matcher, pos, 1);
		return resultList;
	}

}
