package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Operator /=
 *
 */
public class DivideBy extends AddTo {

	@Override
	public IExpr execute(final IExpr first, final IExpr second, final EvalEngine engine) {
		return engine.evaluate(F.Times(first, F.Power(second, F.CN1)));
	}

}