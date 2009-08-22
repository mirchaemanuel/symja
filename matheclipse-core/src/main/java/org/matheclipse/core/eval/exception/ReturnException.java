package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IExpr;

public class ReturnException extends FlowControlException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6165872840807864554L;
	final private IExpr value;

	public ReturnException() {
		this(null);
	}

	public ReturnException(final IExpr val) {
		super("Return from a function definition.");
		value = val;
	}

	public IExpr getValue() {
		return value;
	}
}
