package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Times;
import static org.matheclipse.core.expression.F.C0;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
/**
 * Multiply an expression for the given list of iterators
 */
public class Product extends Table {

	public Product() {
	}


	@Override
	public IExpr evaluate(final IAST ast) {
		IAST resultList = Times();
		IExpr temp = evaluateTable(ast, resultList, C0);
		if (temp.equals(resultList)) {
			return null;
		}
		return temp;
	}
}
