package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.eval.exception.ThrowException;
import org.matheclipse.core.expression.F;
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
			super.evaluate(ast);
		} catch (final ReturnException e) {
		} catch (final ThrowException e) {
		}
		return F.Null;
	}

}
