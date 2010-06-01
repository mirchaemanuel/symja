package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;

/**
 */
public class WrongArgumentType extends MathException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -361382155486374959L;

	int fPosition;

	IExpr fArg;

	IAST fExpr;

	String fMessage;

	public WrongArgumentType(final IAST expr, final IExpr arg, final int position) {
		this(expr, arg, position, null);
	}

	public WrongArgumentType(final IAST expr, final IExpr arg, final int position, String message) {
		fPosition = position;
		fArg = arg;
		fExpr = expr;
		fMessage = message;
	}

	@Override
	public String getMessage() {
		if (fMessage == null) {
			return "The function: " + fExpr.toString() + " has wrong argument " + fArg.toString() + " at position:"
					+ Integer.toString(fPosition);
		} else {
			return "The function: " + fExpr.toString() + " has wrong argument " + fArg.toString() + " at position:"
					+ Integer.toString(fPosition) + ":\n" + fMessage;
		}
	}

}
