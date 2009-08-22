package org.matheclipse.core.eval.interfaces;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IEvaluator;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Common interface for built-in MathEclipse functions.
 * 
 */
public interface IFunctionEvaluator extends IEvaluator {
	/**
	 * Symbolic evaluation of a function. <code>functionList.get(0)</code> must
	 * contain the <i>head</i> (i.e. the function symbol) of this list.
	 * 
	 * @param functionList
	 *          the list which should be evaluated
	 * @return the evaluated object or <code>null</code>, if evaluation isn't
	 *         possible
	 */
	public IExpr evaluate(IAST functionList);

	/**
	 * Numeric evaluation of a function. <code>functionList.get(0)</code> must
	 * contain the <i>head</i> (i.e. the function symbol) of this list.
	 * 
	 * @param functionList
	 *          the list which should be evaluated
	 * @return the evaluated object or <code>null</code>, if evaluation isn't
	 *         possible
	 */
	public IExpr numericEval(IAST functionList);

}
