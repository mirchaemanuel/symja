package org.matheclipse.core.patternmatching;

import java.util.Comparator;

import org.matheclipse.core.interfaces.IPattern;

/**
 * Compares two patterns.
 * 
 */
public class PatternComparator implements Comparator<IPattern> {
	/**
	 * The only <code>PatternComparator</code> instance.
	 */
	public static PatternComparator CONST = new PatternComparator();

	private PatternComparator() {

	}

	/**
	 * Compares two patterns. Two patterns are considered as equal, if there
	 * associated symbols are equal and not <code>null</code>. If the associated
	 * symbols are <code>null</code>, then the patterns
	 * <code>System#identityHashCode()</code> value is used to compare them.
	 */
	@Override
	public int compare(IPattern o1, IPattern o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1.getSymbol() == null) {
			if (o2.getSymbol() == null) {
				if (System.identityHashCode(o1) > System.identityHashCode(o2)) {
					return 1;
				}
			}
			return -1;
		}
		if (o2.getSymbol() == null) {
			return 1;
		}
		return o1.getSymbol().compareTo(o2.getSymbol());
	}

}
