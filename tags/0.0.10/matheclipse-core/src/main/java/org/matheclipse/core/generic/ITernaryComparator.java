package org.matheclipse.core.generic;

import org.matheclipse.core.interfaces.IExpr;

public interface ITernaryComparator<T extends IExpr> {
	/**
	 * <ul>
	 * <li>Return 1 if the comparison is <code>true</code> </li>
	 * <li>Return -1 if the comparison is <code>false</code> </li>
	 * <li>Return 0 if the comparison is undetermined (i.e. could not be
	 * evaluated)</li>
	 * </ul>
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	int compare(T o1, T o2);

}
