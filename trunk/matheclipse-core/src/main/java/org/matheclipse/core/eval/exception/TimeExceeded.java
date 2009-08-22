package org.matheclipse.core.eval.exception;

public class TimeExceeded extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3341162294495378861L;

	public TimeExceeded() {
		super("[Time exceeded] Evaluation stopped.\n");
	}
}
