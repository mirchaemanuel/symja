package org.matheclipse.core.interfaces;

import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.generic.util.INestedRealtimeList;
import org.matheclipse.generic.interfaces.BiFunction;

//import org.matheclipse.generic.INestedList;

/**
 * 
 * (I)nterface for the (A)bstract (S)yntax (T)ree of a given function
 * 
 * The IAST represents one node of the tree and contains
 * <ul>
 * <li>the operator of the tree (i.e. the "header"-symbol: Sin, Cos,
 * Inverse,...)</li>
 * <li>the arguments of the function</li>
 * </ul>
 * the arguments of the function are represented as a list (the i-th argument is
 * the i-th element in the list) an argument in the IAST is either
 * <ul>
 * <li>an IAST or</li>
 * <li>an atomic IExpr</li>
 * </ul>
 */
public interface IAST extends IExpr, INestedRealtimeList<IExpr> {
	/**
	 * NO_FLAG ACTIVATED
	 */
	public final int NO_FLAG = 0x0000;

	/**
	 * The head or one of the arguments of the list contains a pattern object
	 */
	public final int CONTAINS_PATTERN = 0x0001;

	/**
	 * This expression represents a matrix
	 */
	public final int IS_MATRIX = 0x0020;

	/**
	 * This expression represents a vector
	 */
	public final int IS_VECTOR = 0x0040;

	/**
	 * Get the evaluation flags for this list
	 * 
	 * @return
	 */
	public int getEvalFlags();

	/**
	 * Returns the header. If the header itself is an ISymbol it will return the
	 * symbol object. If the header itself is an IAST it will recursively call
	 * headSymbol(). If the head is of type INumbers The head will return one of
	 * these headers: "DoubleComplex", "Double", "Integer", "Fraction",
	 * "Complex" All other objects return <code>null</code>
	 */
	public ISymbol topHead();

	/**
	 * Are the evaluation flags disabled for this list ?
	 * 
	 * @return
	 */
	public boolean isEvalFlagOff(int i);

	/**
	 * Are the evaluation flags enabled for this list ?
	 * 
	 * @return
	 */
	public boolean isEvalFlagOn(int i);

	/**
	 * Set the flags for this list
	 * 
	 * @param i
	 */
	public void setEvalFlags(int i);

	/**
	 * Add a flag to the existing onest
	 * 
	 * @param i
	 */
	public void addEvalFlags(int i);

	/**
	 * Is this a list (i.e. with header == List)
	 * 
	 * @return
	 */
	public boolean isList();

	/**
	 * Apply the given head to this expression (i.e. create a list clone and
	 * replace the old head with the given one)
	 * 
	 * @param head
	 * @return
	 */
	public IAST apply(IExpr head);

	/**
	 * Apply the given head to this expression (i.e. create a sublist clone
	 * starting from index start and replacing the old head with the given one)
	 * 
	 * @param head
	 * @return
	 */
	public IAST apply(IExpr head, int start);

	/**
	 * Apply the given head to this expression (i.e. create a sublist clone from
	 * index start to end, and replacing the old head with the given one)
	 * 
	 * @param head
	 * @return
	 */
	public IAST apply(IExpr head, int start, int end);

	/**
	 * Apply the given head to all elements of the list
	 * 
	 * @param head
	 * @return
	 */
	public IAST map(IExpr head);

	/**
	 * Maps the elements of this IAST with the elements of the
	 * <code>secondAST</code>.
	 * 
	 * @return the given resultAST.
	 * @throws IndexOutOfBoundsException
	 *             if the secondAST size is lesser than this AST size
	 */
	public IAST map(IAST resultAST, IAST secondAST,
			BiFunction<IExpr, IExpr, IExpr> function);

	/**
	 * Set the head element of this list
	 */
	public void setHeader(IExpr expr);

	/**
	 * @deprecated use <tt>get(index + 1)</tt> instead
	 */
	// @Deprecated
	// public int argsSize();
	/**
	 * Returns a shallow copy of this <code>INestedList</code> instance. (The
	 * elements themselves are not copied.)
	 * 
	 * @return a clone of this <code>IAST</code> instance.
	 */
	public Object clone();

	/**
	 * Create a copy of this <code>IAST</code>, which only contains the head
	 * element of the list (i.e. the element with index 0).
	 */
	public IAST copyHead();

	/**
	 * Calculate a special hash value for pattern matching
	 * 
	 * @return
	 */
	public int patternHashCode();

	/**
	 * Get the range of elements [1..ast.size()[. This range elements are the arguments
	 * of a function.
	 * 
	 * @return
	 */
	public ASTRange args();

	/**
	 * Get the range of elements [0..ast.size()[ of the AST. This range elements
	 * are the head of the function prepended  by the arguments of a function.
	 * 
	 * @return
	 */
	public ASTRange range();

	/**
	 * Get the range of elements [start..sizeOfAST[ of the AST
	 * 
	 * @return
	 */
	public ASTRange range(int start);

	/**
	 * Get the range of elements [start..end[ of the AST
	 * 
	 * @return
	 */
	public ASTRange range(int start, int end);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>IInteger</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *             if the cast is not possible
	 */
	public IInteger getInt(int index);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>INumber</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *             if the cast is not possible
	 */
	public INumber getNumber(int index);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>IAST</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *             if the cast is not possible
	 */
	public IAST getAST(int index);

	/**
	 * Casts an <code>IExpr</code> which is a list at position
	 * <code>index</code> to an <code>IAST</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 */
	public IAST getList(int index);
}
