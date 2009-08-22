package org.matheclipse.core.expression;

import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.Range;

public class ASTRange extends Range<IExpr, IAST> {
	/**
	 * ASTRange [start..end[
	 * 
	 * @param list
	 */
	public ASTRange(final IAST list, final int start, final int end) {
		super(list, start, end);
	}

	/**
	 * ASTRange [start..sizeOfList[
	 * 
	 * @param list
	 */
	public ASTRange(final IAST list, final int start) {
		super(list, start);
	}

	/**
	 * ASTRange [0..sizeOfList[ This range includes all arguments of a function.
	 * The <code>head</code> of the function is stored at index 0
	 * 
	 * @param list
	 */
	public ASTRange(final IAST list) {
		super(list, 0);
	}

	/**
	 * Append the ranges elements to a new created List
	 * 
	 */
	public List<IExpr> toList() {
		ArrayList<IExpr> list = new ArrayList<IExpr>();
		return super.toList(list);
	}
}
