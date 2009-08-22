package org.matheclipse.core.interfaces;

import java.util.List;

import com.google.common.base.Predicate;

/**
 * Interface for the pattern matcher
 */
public abstract class IPatternMatcher<E> implements Predicate<E> {
	/**
	 * Returns the matched pattern in the order they appear in the pattern
	 * expression.
	 * 
	 * 
	 * @param resultList
	 *          a list instance
	 * @param patternExpr
	 *          the expression which contains the pattern objects
	 */
	public abstract void getPatterns(List<E> resultList, E patternExpr);

	/**
	 * Returns <code>true</code>, if the given expression contains no patterns
	 * 
	 * @return
	 */
	public abstract boolean isRuleWithoutPatterns();

	/**
	 * Start pattern matching.
	 * 
	 * @return
	 */
	public abstract boolean apply(E evalExpr);

	/**
	 * Sets an additional evaluation-condition for pattern-matching
	 * 
	 */
	public abstract void setCondition(E condition);
}
