package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

public class GroebnerBasis extends AbstractFunctionEvaluator {

	public GroebnerBasis() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
//		if (lst.size() >= 3) {
//			try {
//
//				if (lst.get(2).isVector() < 0) {
//					return null;
//				}
//				final EvalEngine engine = EvalEngine.get();
//				
//			} catch (Exception e) {
//				if (Config.SHOW_STACKTRACE) {
//					e.printStackTrace();
//				}
//			}
//		}
		return null;
	}

}