package org.matheclipse.core.interfaces;

/**
 * Interface for pattern objects. A pettern object cannot be shared between
 * multiple rules
 *
 */
public interface IPattern extends IExpr {
	/**
	 * Get the index in which this pattern is stored during pattern-matching for a
	 * given rule.
	 *
	 * @param index
	 */
	public int getIndex();

	/**
	 * Set the index in which this pattern is stored during pattern-matching for a
	 * given rule.
	 *
	 * @param index
	 */
	public void setIndex(int index);

	/**
	 * Get the associated symbol for this pattern-object
	 *
	 * @return
	 */
	public ISymbol getSymbol();

	/**
	 * Get the additional patterns condition expression
	 *
	 * @return may return null;
	 */
	public IExpr getCondition();
	
	/**
	 * Return <code>true</code>, if the expression fullfills the patterns
	 * additional condition
	 *
	 * @param expr
	 * @return
	 */
	public boolean isConditionMatched(IExpr expr);
	
	/**
	 * Return <code>true</code>, if the expression is a blank pattern
	 *
	 * @return
	 */
	public boolean isBlank();
}
