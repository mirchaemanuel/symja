package org.matheclipse.core.eval.exception;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IInteger;

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
		if (list.size() < from) {
			throw new WrongNumberOfArguments(list, from, list.size() - 1);
		}
		if (list.size() > to) {
			throw new WrongNumberOfArguments(list, to, list.size() - 1);
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

	/**
	 * Check the argument, if it's a Java {@code int} value in the range
	 * [{@code startValue}, Integer.MAX_VALUE]
	 * 
	 * @throws WrongArgumentType
	 *           if {@code size} unequals the list size
	 */
	public static int checkIntType(IAST list, int pos, int startValue) {
		if (list.get(pos) instanceof IInteger) {
			try {
				int result = ((IInteger) list.get(pos)).toInt();
				if (startValue > result) {
					throw new WrongArgumentType(list, list.get(pos), pos, "Trying to convert the argument into the integer range: "
							+ startValue + " - " + Integer.MAX_VALUE);
				}
				return result;
			} catch (ArithmeticException ae) {
				throw new WrongArgumentType(list, list.get(pos), pos, "Trying to convert the argument into the integer range: "
						+ startValue + " - " + Integer.MAX_VALUE);
			}
		}
		throw new WrongArgumentType(list, list.get(pos), pos, "Trying to convert the argument into the integer range: " + startValue
				+ " - " + Integer.MAX_VALUE);
	}

	/**
	 * Check the argument, if it's a Java {@code int} value in the range [0,
	 * Integer.MAX_VALUE]
	 * 
	 * @throws WrongArgumentType
	 *           if {@code size} unequals the list size
	 */
	public static int checkIntType(IAST list, int pos) {
		return checkIntType(list, pos, 0);
	}

	private Validate() {
	}

}
