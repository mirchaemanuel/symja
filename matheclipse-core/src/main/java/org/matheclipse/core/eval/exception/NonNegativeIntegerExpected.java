package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IAST;

/**
 */
public class NonNegativeIntegerExpected extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 161506792947148754L;

	int fCurrent;

	IAST fExpr;

	public NonNegativeIntegerExpected(final IAST expr, final int current) {
		fCurrent = current;
		fExpr = expr;
	}

	@Override
	public String getMessage() {
		return "Non negative int expectd at arguments position: " + fCurrent + " in expression:\n" + fExpr.toString();
	}

}
