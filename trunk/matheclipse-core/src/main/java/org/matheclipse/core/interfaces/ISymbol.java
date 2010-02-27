package org.matheclipse.core.interfaces;

import java.io.IOException;
import java.util.List;

import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.patternmatching.PatternMatcherAndInvoker;

/**
 * An expression representing a symbol (i.e. variable- constant- or
 * function-name)
 * 
 */
public interface ISymbol extends IExpr{ // Variable<IExpr>  
	

	/**
	 * ISymbol attribute to indicate that a symbol has a constant value
	 */
	public final static int CONSTANT = 0x0002;

	/**
	 * ISymbol attribute for an associative function transformation. The
	 * evaluation of the function will flatten the arguments list
	 * 
	 */
	public final static int FLAT = 0x0008;

	/**
	 * ISymbol attribute for a function, where the first argument should not be
	 * evaluated
	 * 
	 */
	public final static int HOLDFIRST = 0x0020;

	/**
	 * ISymbol attribute for a function, where only the first argument should be
	 * evaluated
	 * 
	 */
	public final static int HOLDREST = 0x0040;

	/**
	 * ISymbol attribute for a function, where no argument should be evaluated
	 * 
	 */
	public final static int HOLDALL = HOLDFIRST | HOLDREST;

	/**
	 * ISymbol attribute for a function with lists as arguments
	 * 
	 */
	public final static int LISTABLE = 0x0080;

	/**
	 * ISymbol attribute for a function, which should not be evaluated
	 * numerically
	 * 
	 */
	public final static int NHOLDALL = 0x2000;

	/**
	 * ISymbol attribute for a function, where the first argument should not be
	 * evaluated numerically
	 * 
	 */
	public final static int NHOLDFIRST = 0x4000;

	/**
	 * ISymbol attribute for a function, where the rest of the arguments should
	 * not be evaluated numerically.
	 * 
	 */
	public final static int NHOLDREST = 0x8000;

	/**
	 * ISymbol attribute which means that no attribute is set.
	 */
	public final static int NOATTRIBUTE = 0x0000;

	/**
	 * Description of the Field
	 */
	public final static int NUMERICFUNCTION = 0x0400;

	/**
	 * ISymbol attribute for a function transformation: f(x) ==> x
	 */
	public final static int ONEIDENTITY = 0x0001;

	/**
	 * ISymbol attribute for a commutative function transformation. The
	 * evaluation of the function will sort the arguments.
	 * 
	 */
	public final static int ORDERLESS = 0x0004;

	/**
	 * ISymbol attribute combination (ISymbol.FLAT and ISymbol.ORDERLESS)
	 * 
	 */
	public final static int FLATORDERLESS = FLAT | ORDERLESS;

	/**
	 * The default priority when associating a new rule to a symbol. Lower
	 * numbers have higher priorities
	 */
	public final static int DEFAULT_RULE_PRIORITY = 100000;

	/**
	 * Get the current evaluator for this symbol
	 * 
	 * @return the evaluator which is associated to this symbol or
	 *         <code>null</code> if no evaluator is associated
	 */
	public IEvaluator getEvaluator();

	/**
	 * Set the current evaluator which is associated to this symbol
	 */
	public void setEvaluator(IEvaluator module);

	/**
	 * Tests if this symbols name equals the given string
	 * 
	 * @param symbolName
	 * @return
	 */
	public boolean isString(String symbolName);

	/**
	 * Returns <code>true</code>, if the given symbol is bound to a value.
	 * 
	 */
	public boolean isValue(ISymbol symbol);
	
	/**
   * Returns <code>true</code>, if the given AST is bound to a value.
   * 
   */
  public boolean isValue(IAST ast);
  
	/**
	 * Get the Attributes of this symbol (i.e. LISTABLE, FLAT, ORDERLESS,...)
	 * 
	 * @return
	 */
	public int getAttributes();

	/**
	 * Set the Attributes of this symbol (i.e. LISTABLE, FLAT, ORDERLESS,...)
	 * 
	 * @param attributes
	 *            the Attributes of this symbol
	 */
	public void setAttributes(int attributes);

	/**
	 * Create a new variable placeholder on the symbols variable stack
	 * 
	 */
	public void pushLocalVariable();

	/**
	 * Create a new variable placeholder on the symbols variable stack and set
	 * the local value
	 * 
	 */
	public void pushLocalVariable(IExpr localValue);

	/**
	 * Delete the topmost placeholder from the local variable stack
	 * 
	 */
	public void popLocalVariable();

	/**
	 * Is a local variable stack created for this symbol ?
	 * 
	 * @return <code>true</code> if this symbol has a local variable stack
	 */
	boolean hasLocalVariableStack();

	/**
	 * Get the topmost value from the local variable stack
	 * 
	 * @return <code>null</code> if no local variable is defined
	 */
	public IExpr get();

	/**
	 * Set the value of the local variable on top of the local variable stack
	 * 
	 */
	public void set(IExpr value);

	/**
	 * Associate a new rule with default priority to this symbol.
	 * 
	 * @param equalRule
	 *            <code>true</code> if the leftHandSide could be matched with
	 *            equality
	 * @param leftHandSide
	 * @param rightHandSide
	 * @return
	 * 
	 * @see ISymbol#DEFAULT_RULE_PRIORITY
	 */
	public IPatternMatcher<IExpr> putDownRule(ISymbol symbol, boolean equalRule, IExpr leftHandSide,
			IExpr rightHandSide);

	/**
	 * Associate a new rule with default priority to this symbol.
	 * 
	 * @param equalRule
	 *            <code>true</code> if the leftHandSide could be matched with
	 *            equality
	 * @param leftHandSide
	 * @param rightHandSide
	 * @param condition
	 *            additional condition for rules containing patterns
	 * @return
	 * 
	 * @see ISymbol#DEFAULT_RULE_PRIORITY
	 */
	public IPatternMatcher putDownRule(ISymbol symbol, boolean equalRule, IExpr leftHandSide,
			IExpr rightHandSide, IExpr condition);

	/**
	 * Associate a new rule with the given priority to this symbol.<br/> Rules
	 * with lower numbers have higher priorities.
	 * 
	 * @param equalRule
	 *            <code>true</code> if the leftHandSide could be matched with
	 *            equality
	 * @param leftHandSide
	 * @param rightHandSide
	 * @return
	 * 
	 * @see ISymbol#DEFAULT_RULE_PRIORITY
	 */
	public IPatternMatcher putDownRule(ISymbol symbol, boolean equalRule, IExpr leftHandSide,
			IExpr rightHandSide, IExpr condition, int priority);
	
	/**
	 * Associate a new rule, which invokes a method, to this symbol.
	 * 
	 */
	public PatternMatcher putDownRule(
			final PatternMatcherAndInvoker pmEvaluator);

	/**
	 * Evaluate the given expression for the rules associated with this symbol
	 * 
	 * @param engine
	 * @param expression
	 * @return
	 */
	public IExpr evalDownRule(IEvaluationEngine engine, IExpr expression);

	/**
	 * Clear the associated rules for this symbol
	 * 
	 */
	public void clear();

	/**
	 * Clear all associated rules and attributes for this symbol
	 * 
	 */
	public void clearAll();

	/**
	 * Return a list of the rules associated to this symbol 
	 * 
	 * @return
	 */
	public List<IAST> definition();
	
	/**
	 * Return the rules associated to this symbol in String representation
	 * 
	 * @return
	 */
	public String definitionToString() throws IOException;

	/**
	 * Deserialize the rules associated to this object
	 * 
	 * @param stream
	 * @throws IOException
	 */
	public void readSymbol(java.io.ObjectInputStream stream)
			throws IOException;

	/**
	 * Serialize the rules associated to this object
	 * 
	 * @param stream
	 * @throws java.io.IOException
	 */
	public void writeSymbol(java.io.ObjectOutputStream stream)
			throws java.io.IOException;
}
