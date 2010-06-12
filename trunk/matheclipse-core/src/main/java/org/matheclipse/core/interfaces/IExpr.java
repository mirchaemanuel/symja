package org.matheclipse.core.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.FieldElement;
import org.matheclipse.core.generic.util.INestedListElement;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;

/**
 * 
 * (I)nterface for a mathematical (Expr)ession
 * 
 */
public interface IExpr extends Comparable<IExpr>, // FieldElement<IExpr>,
		INestedListElement, Serializable {

	public final static int DOUBLEID = 2;

	public final static int DOUBLECOMPLEXID = 4;

	public final static int INTEGERID = 8;

	public final static int FRACTIONID = 16;

	public final static int COMPLEXID = 32;

	public final static int STRINGID = 64;

	public final static int SYMBOLID = 128;

	public final static int ASTID = 256;

	public final static int PATTERNID = 512;

	/**
	 * Returns the product of this object with the one specified.
	 * 
	 * @param that
	 *          the object multiplier.
	 * @return <code>this · that</code>.
	 */
	IExpr times(IExpr that);

	/**
	 * Returns the multiplicative inverse of this object. It it the object such as
	 * <code>this.times(this.inverse()) == ONE </code>, with <code>ONE</code>
	 * being the multiplicative identity.
	 * 
	 * @return <code>ONE / this</code>.
	 */
	IExpr inverse();

	/**
	 * Accept a visitor with return type T
	 */
	public <T> T accept(IVisitor<T> visitor);

	/**
	 * Accept a visitor with return type <code>boolean</code>
	 */
	public boolean accept(IVisitorBoolean visitor);

	/**
	 * Accept a visitor with return type <code>int</code>
	 * 
	 * @param visitor
	 * @return
	 */
	public int accept(IVisitorInt visitor);

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(IExpr obj);

	/**
	 * Call export for the runtime objects
	 * 
	 * @return
	 */
	// public IExpr copy();
	/**
	 * Copy this object to the global Java heap
	 * 
	 * @return
	 */
	// public IExpr copyNew();
	/**
	 * Recycle the current object
	 * 
	 * @return
	 */
	// public void recycle();
	/**
	 * Call moveHeap for the runtime objects
	 * 
	 * @return
	 */
	// public IExpr saveHeap();
	/**
	 * Compares this expression with the specified expression for order. Returns
	 * true if this expression is canonical less than the specified expression
	 * (&lt; relation).
	 * 
	 * @param expr
	 *          an expression to compare with
	 * @return true if this expression is canonical less than the specified
	 *         expression.
	 */
	public boolean isLTOrdered(IExpr expr);

	/**
	 * Compares this expression with the specified expression for order. Returns
	 * true if this expression is canonical less than or equal to the specified
	 * expression (&lt;= relation).
	 * 
	 * @param expr
	 *          an expression to compare with
	 * @return true if this expression is canonical less than or equal to the
	 *         specified expression.
	 */
	public boolean isLEOrdered(IExpr obj);

	/**
	 * Compares this expression with the specified expression for order. Returns
	 * true if this expression is canonical greater than the specified expression
	 * (&lt; relation).
	 * 
	 * @param expr
	 *          an expression to compare with
	 * @return true if this expression is canonical greater than the specified
	 *         expression.
	 */
	public boolean isGTOrdered(IExpr expr);

	/**
	 * Compares this expression with the specified expression for order. Returns
	 * true if this expression is canonical greater than or equal to the specified
	 * expression (&lt;= relation).
	 * 
	 * @param expr
	 *          an expression to compare with
	 * @return true if this expression is canonical greater than or equal to the
	 *         specified expression.
	 */
	public boolean isGEOrdered(IExpr obj);

	/**
	 * A unique integer ID for the implementation of this expression
	 * 
	 * @return a unique integer id for the implementation of this expression
	 */
	public int hierarchy();

	/**
	 * Test if this expression is an atomic expression (i.e. no AST expression)
	 * 
	 */
	public boolean isAtom();

	/**
	 * Test if this expression is a number
	 * 
	 */
	public boolean isNumber();

	/**
	 * Test if this expression is a list (i.e. an AST with head List)
	 * 
	 */
	public boolean isList();

	/**
	 * Test if this expression equals the symbol "True"
	 * 
	 */
	public boolean isTrue();

	/**
	 * Test if this expression equals the symbol "False"
	 * 
	 */
	public boolean isFalse();

	/**
	 * Test if this expression equals the given expression. If the compared
	 * expressions are of the same numeric type, they are equal to a given EPSILON
	 * 
	 */
	public boolean isSame(IExpr expression);

	/**
	 * Test if this expression equals the given expression. If the compared
	 * expressions are of the same numeric type, they are equal to a given EPSILON
	 * 
	 */
	public boolean isSame(IExpr expression, double epsilon);

	/**
	 * Test if this expression is a matrix and return the dimensions. This
	 * expression is only a matrix, if all elements are lists with the header
	 * <code>List</code> and have the same size.
	 * 
	 * @return <code>null</code> if the expression is no matrix
	 */
	public int[] isMatrix();

	/**
	 * Test if this expression is a vector and return the dimension. This
	 * expression is only a vector, if no element is itself a list.
	 * 
	 * @return <code>-1</code> if the expression is no vector
	 */
	public int isVector();

	/**
	 * Test if this expression is an AST (i.e. no atomic expression) with the
	 * given head expression
	 * 
	 */
	public boolean isAST(IExpr header);

	/**
	 * Test if this expression is an AST (i.e. no atomic expression) with the
	 * given head expression and the given size. The size includes the header
	 * element. I.e. isAST("Sin", 2) gives true for Sin[0].
	 * 
	 */
	public boolean isAST(IExpr header, int size);

	/**
	 * Test if this expression is an AST (i.e. no atomic expression) with the
	 * given head expression and size of elements greater equal than the
	 * AST#size()
	 * 
	 */
	public boolean isASTSizeGE(IExpr header, int size);

	/**
	 * Test if this expression is an AST (i.e. no atomic expression) with the
	 * given string equal to the string representation of the head
	 * 
	 */
	public boolean isAST(String symbol);

	/**
	 * Test if this expression is an AST (i.e. no atomic expression) with the
	 * given head expression and the given size. The size includes the header
	 * element. I.e. isAST("Sin", 2) gives true for Sin[0].
	 * 
	 */
	public boolean isAST(String symbol, int size);

	/**
	 * Test if the expression is free of (sub-)expressions which match the
	 * pattern.
	 * 
	 */
	public boolean isFree(IExpr pattern);

	/**
	 * Return the FullForm of this expression
	 */
	public String fullFormString();

	/**
	 * Return the internal Java form of this expression
	 */
	public String internalFormString();

	/**
	 * @returnthe head of the expression, which must not be null.
	 */
	public IExpr head();

	/**
	 * @return the 'highest level' head of the expression, before Symbol, Integer,
	 *         Real or String. for example while the head of a[b][c] is a[b], the
	 *         top head is a.
	 */
	public ISymbol topHead();

	/**
	 * @return a list of the the leaf expressions. Instances of ExprImpl should
	 *         return null, while any other expression may not return null (but
	 *         can return an empty list).
	 */
	public List<IExpr> leaves();

	/**
	 * @param leaves
	 * @return an IExpr instance with the current expression as head(), and leaves
	 *         as leaves().
	 */
	public IExpr apply(List<? extends IExpr> leaves);

	/**
	 * @param leaves
	 * @return an IExpr instance with the current expression as head(), and leaves
	 *         as leaves().
	 */
	public IExpr apply(IExpr... leaves);

	/**
	 * Convert the variables (i.e. ISymbol's with lower case character in the
	 * 0-th position of their name) in this expression into Slot[] s.
	 * 
	 * @return <code>null</code> if the expression contains a variable with a '$'
	 *         character in the 0-th position of its name and the math engine runs
	 *         in <i>server mode</i>.
	 */
	public IExpr variables2Slots(Map<IExpr, IExpr> map, List<IExpr> variableList);

	//
	// Groovy operator overloading
	//
	public IExpr negative();

	public IExpr plus(final IExpr that);

	public IExpr minus(final IExpr that);

	public IExpr multiply(final IExpr that);

	public IExpr power(final Integer n);

	public IExpr power(final IExpr that);

	public IExpr div(final IExpr that);

	public IExpr mod(final IExpr that);

	public IExpr and(final IExpr that);

	public IExpr or(final IExpr that);

	public IExpr getAt(final int index);

	public Object asType(Class clazz);
}
