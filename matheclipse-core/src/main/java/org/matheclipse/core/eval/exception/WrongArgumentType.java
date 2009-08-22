package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 */
public class WrongArgumentType extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -361382155486374959L;

	int fPosition;

	IExpr fArg;

	IAST fExpr;

	public WrongArgumentType(final IAST expr, final IExpr arg, final int position) {
		fPosition = position;
		fArg = arg;
		fExpr = expr;
	}

	@Override
	public String getMessage() {
		return "The function: " + fExpr.toString() + " has wrong argument " + fArg.toString() + " at position:"
				+ Integer.toString(fPosition);
	}

}
