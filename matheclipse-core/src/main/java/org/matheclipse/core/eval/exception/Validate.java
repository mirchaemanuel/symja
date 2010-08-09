package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IAST;

/**
 * Simple static methods to be called at the start of your own methods to verify
 * correct arguments and state.
 * 
 */
public final class Validate {
	/**
	 * 
	 * @throws WrongNumberOfArguments
	 *           if {@code size} is not in the range {@code from} to {@code
	 *           Integer.MAX_VALUE}
	 */
	public static IAST checkRange(IAST list, int from) {
		return checkRange(list, from, Integer.MAX_VALUE);
	}

	/**
	 * 
	 * @throws WrongNumberOfArguments
	 *           if {@code size} is not in the range {@code from} to {@code to}
	 */
	public static IAST checkRange(IAST list, int from, int to) {
		if (list.size() < from || list.size() > to) {
			throw new WrongNumberOfArguments(list, 1, list.size() - 1);
		}
		return list;
	}

	/**
	 * 
	 * @throws WrongNumberOfArguments
	 *           if {@code size} unequals the list size
	 */
	public static IAST checkSize(IAST list, int size) {
		if (list.size() != size) {
			throw new WrongNumberOfArguments(list, size - 1, list.size() - 1);
		}
		return list;
	}

	private Validate() {
	}

}
