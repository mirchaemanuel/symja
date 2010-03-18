package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.parser.client.math.MathException;

/**
 */
public class WrongNumberOfArguments extends MathException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 754625729654866796L;

	int fAllowed;

	int fCurrent;

	IAST fExpr;

	public WrongNumberOfArguments(final IAST expr, final int allowed, final int current) {
		fAllowed = allowed;
		fCurrent = current;
		fExpr = expr;
	}

	@Override
	public String getMessage() {
		return "Allowed number of arguments: " + fAllowed + " but got " + fCurrent + " arguments:\n" + fExpr.toString();
	}

}
