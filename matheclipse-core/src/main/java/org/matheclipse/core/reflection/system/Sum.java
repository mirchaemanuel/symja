package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.C0;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Add an expression for the given list of iterators
 */
public class Sum extends Table {

	public Sum() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		IAST resultList = Plus();
		IExpr temp = evaluateTable(ast, resultList, C0);
		if (temp.equals(resultList)) {
			return null;
		}
		return temp;
	}
}
