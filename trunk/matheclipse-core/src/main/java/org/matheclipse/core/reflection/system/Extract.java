package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.generic.PositionConverter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

public class Extract extends AbstractFunctionEvaluator {

	public Extract() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST) && (functionList.get(2) instanceof IAST)) {
			return extract((IAST) functionList.get(1), (IAST) functionList.get(2));
		}
		// if (list.size() == 4 && list.get(1) instanceof IAST) {
		// LevelSpec level = new LevelSpecification(f, (IExpr) list.get(3));
		// return extract((IAST) list.get(1), list.get(2), level);
		// }
		return null;
	}

	public static IExpr extract(final IAST list, final IAST position) {
		final PositionConverter converter = new PositionConverter();
		if ((position.size() > 1) && (position.get(1) instanceof IInteger)) {
			return AST.COPY.extract(list, position, converter, 1);
		} else {
			// construct an array
			// final IAST resultList = List();
			// NestedFinding.position(list, resultList, pos, 1);
			// return resultList;
		}
		return null;
	}

}
