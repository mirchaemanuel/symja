package org.matheclipse.core.generic;


import com.google.common.base.Function;

/**
 * A function object that takes one argument and returns a result.  The
 * argument is of type <code>T</code>, and the result is of type <code>T</code>.
 *
 */
public abstract class UnaryFunctorImpl<T> implements Function<T,T> {
	/**
     * Executes the function and returns the result.
	 * @throws FunctionException TODO
     */
	public abstract T apply(T obj);
}
