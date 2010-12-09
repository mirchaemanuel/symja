package org.matheclipse.core.interfaces;

import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.generic.util.INestedList;
import org.matheclipse.core.reflection.system.Apart;
import org.matheclipse.generic.interfaces.BiFunction;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

//import org.matheclipse.generic.INestedList;

/**
 * 
 * (I)nterface for the (A)bstract (S)yntax (T)ree of a given function
 * 
 * The IAST represents one node of the tree and contains
 * <ul>
 * <li>the operator of the tree (i.e. the &quot;header&quot;-symbol: Sin, Cos,
 * Inverse,...)</li>
 * <li>the arguments of the function</li>
 * </ul>
 * the arguments of the function are represented as a list (the i-th argument is
 * the i-th element in the list; the 0-element is the &quot;header&quot;-symbol)
 * an argument in the IAST is either
 * <ul>
 * <li>an IAST or</li>
 * <li>an atomic IExpr</li>
 * </ul>
 */
public interface IAST extends IExpr, INestedList<IExpr> {
	/**
	 * NO_FLAG ACTIVATED
	 */
	public final int NO_FLAG = 0x0000;

	/**
	 * The head or one of the arguments of the list contains a pattern object
	 */
	public final int CONTAINS_PATTERN = 0x0001;

	/**
	 * One of the arguments of the list contains a pattern object which can be set
	 * to a default value.
	 */
	public final int CONTAINS_DEFAULT_PATTERN = 0x0002;

	/**
	 * Negative flag mask for CONTAINS_DEFAULT_PATTERN
	 */
	public final int CONTAINS_NO_DEFAULT_PATTERN_MASK = 0xFFFD;

	/**
	 * This expression represents a matrix
	 */
	public final int IS_MATRIX = 0x0020;

	/**
	 * This expression represents a vector
	 */
	public final int IS_VECTOR = 0x0040;

	/**
	 * This expression represents an already decomposed partial fraction
	 * 
	 * @see Apart
	 */
	public final int IS_DECOMPOSED_PARTIAL_FRACTION = 0x0080;

	/**
	 * This expression is an already flattened expression
	 */
	public final int IS_FLATTENED = 0x0100;

	/**
	 * This expression is an already sorted expression
	 */
	public final int IS_SORTED = 0x0200;

	/**
	 * This expression is an already flattende or sorted expression
	 */
	public final int IS_FLATTENED_OR_SORTED_MASK = 0x0300;

	/**
	 * Get the evaluation flags for this list.
	 * 
	 * @return
	 */
	public int getEvalFlags();

	/**
	 * Returns the header. If the header itself is an ISymbol it will return the
	 * symbol object. If the header itself is an IAST it will recursively call
	 * headSymbol(). If the head is of type INumbers, the head will return one of
	 * these headers: "DoubleComplex", "Double", "Integer", "Fraction", "Complex".
	 * All other objects return <code>null</code>.
	 */
	public ISymbol topHead();

	/**
	 * Are the given evaluation flags disabled for this list ?
	 * 
	 * @return
	 */
	public boolean isEvalFlagOff(int i);

	/**
	 * Are the given evaluation flags enabled for this list ?
	 * 
	 * @return
	 */
	public boolean isEvalFlagOn(int i);

	/**
	 * Set the evaluation flags for this list.
	 * 
	 * @param i
	 */
	public void setEvalFlags(int i);

	/**
	 * Add an evaluation flag to the existing ones.
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

	public boolean isPlus();

	public boolean isPower();

	public boolean isTimes();

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
	 * Maps the elements of this IAST with the unary functor. If the function
	 * returns <code>null</code> the original element is used. If the function
	 * returns <code>null</code> for every argument this AST is returned.
	 * 
	 * <br />
	 * <br />
	 * Example for mapping with <code>Functors#replace1st()</code>, where the
	 * first argument will be replaced by the current argument of this AST:
	 * 
	 * <pre>
	 * plusAST.map(Functors.replace1st(F.D(F.Null, dAST.get(2))));
	 * </pre>
	 * 
	 * @param head
	 * @return
	 */
	public IAST map(final Function<IExpr, IExpr> function);

	/**
	 * Maps the elements of this IAST with the elements of the
	 * <code>secondAST</code>.
	 * 
	 * @return the given resultAST.
	 * @throws IndexOutOfBoundsException
	 *           if the secondAST size is lesser than this AST size
	 */
	public IAST map(IAST resultAST, IAST secondAST, BiFunction<IExpr, IExpr, IExpr> function);

	/**
	 * Apply the predicate to each element in the range and append the elements
	 * which match the predicate to the filterList, or otherwise append it to the
	 * restList.
	 * 
	 * @param filterAST
	 *          the elements which match the predicate
	 * @param restAST
	 *          the elements which don't match the predicate
	 * @param predicate
	 *          the predicate which filters each element in the range
	 * @return the <code>filterList</code>
	 */
	public IAST select(IAST filterAST, IAST restAST, Predicate<IExpr> predicate);
		 
	/**
	 * Set the head element of this list
	 */
	public void setHeader(IExpr expr);

	/**
	 * Returns a shallow copy of this <code>INestedList</code> instance. (The
	 * elements themselves are not copied.)
	 * 
	 * @return a clone of this <code>IAST</code> instance.
	 */
	public IAST clone();

	/**
	 * Create a copy of this <code>IAST</code>, which only contains the head
	 * element of the list (i.e. the element with index 0).
	 */
	public IAST copyHead();

	/**
	 * Create a copy of this <code>IAST</code>, which contains alls elements up to
	 * <code>index</code> (exclusive).
	 */
	public IAST copyUntil(int index);

	/**
	 * Calculate a special hash value for pattern matching
	 * 
	 * @return
	 */
	public int patternHashCode();

	/**
	 * Get the range of elements [1..ast.size()[. This range elements are the
	 * arguments of a function.
	 * 
	 * @return
	 */
	public ASTRange args();

	/**
	 * Get the range of elements [0..ast.size()[ of the AST. This range elements
	 * are the head of the function prepended by the arguments of a function.
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
	 *           if the cast is not possible
	 */
	public IInteger getInt(int index);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>INumber</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *           if the cast is not possible
	 */
	public INumber getNumber(int index);

	/**
	 * Casts an <code>IExpr</code> at position <code>index</code> to an
	 * <code>IAST</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 *           if the cast is not possible
	 */
	public IAST getAST(int index);

	/**
	 * Casts an <code>IExpr</code> which is a list at position <code>index</code>
	 * to an <code>IAST</code>.
	 * 
	 * @param index
	 * @return
	 * @throws WrongArgumentType
	 */
	public IAST getList(int index);
}
