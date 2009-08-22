package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.reflection.system.ToString;

/**
 * 
 */
public class IterationLimitExceeded extends RuntimeException {
	/**
	 * 
	 */
	int fLimit;

	IExpr fExpr;

	public IterationLimitExceeded(final int limit, final IExpr expr) {
		fLimit = limit;
		fExpr = expr;
	}

	@Override
	public String getMessage() {
		if (fExpr == null) {
			return "Iteration limit " + fLimit + " exceeded at: null";
		}
		return "Iteration limit " + fLimit + " exceeded at: " + ToString.outputForm(fExpr);
	}

	public static void throwIt(int iterationCounter, final IExpr expr) {
		// HeapContext.enter();
		// try {
		throw new IterationLimitExceeded(iterationCounter, expr);//expr.copy());
		// } finally {
		// HeapContext.exit();
		// }
	}
}
