package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * @see Map
 */
public class Scan extends Map {

	public Scan() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		try {
			return super.evaluate(ast);
		} catch (final ReturnException e) {
			return e.getValue();
			// don't catch Throw[] here !
		}
	}

}
