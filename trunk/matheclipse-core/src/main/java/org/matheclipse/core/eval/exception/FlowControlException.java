package org.matheclipse.core.eval.exception;

/**
 * Base exeception for BreakException and ContinueException
 *
 *
 */
public class FlowControlException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7700982641897767896L;

	public FlowControlException(final String message) {
		super(message);
	}
}
