package org.matheclipse.parser.client.math;

public class MathException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3520033778672500363L;

	/**
	 * <code>MathException</code> constructor.
	 */
	public MathException() {
		super();
	}

	/**
	 * <code>MathException</code> constructor with message.
	 * 
	 * @param message
	 */
	public MathException(String message) {
		super(message);
	}
}