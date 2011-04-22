package org.matheclipse.core.generic;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.generic.nested.LevelSpec;

/**
 * Level specification for expression trees
 */
public class LevelSpecification extends LevelSpec {

	/**
	 * Create a LevelSpecification with level from 0 to Integer.MAX_VALUE
	 * 
	 */
	public LevelSpecification() {
		super();
		fToLevel = Integer.MAX_VALUE;
		fFromDepth = Integer.MIN_VALUE;
		fToDepth = -1;
	}

	/**
	 * Create a LevelSpecification from an IInteger or IAST list-object.<br>
	 * <br>
	 * 
	 * If <code>obj</code> is a non-negative IInteger iValue set Level {0,iValue};<br>
	 * If <code>obj</code> is a negative IInteger iValue set Level {iValue, 0};<br>
	 * If <code>obj</code> is a List {i0Value, i1Value} set Level {i0Value,
	 * i1Value};<br>
	 * 
	 * @param obj
	 * 
	 * @see
	 */
	public LevelSpecification(final IExpr obj) {
		super();
		fFromLevel = fToLevel = -1;
		fFromDepth = fToDepth = 0;
		if (obj instanceof IInteger) {
			final IInteger value = (IInteger) obj;

			if (value.isNegative()) {
				fFromDepth = Integer.MIN_VALUE;
				fToDepth = value.getBigNumerator().intValue();
				fFromLevel = 0;
				fToLevel = Integer.MAX_VALUE;
			} else {
				fToLevel = value.getBigNumerator().intValue();
				fFromLevel = 0;
				fFromDepth = Integer.MIN_VALUE;
				fToDepth = -1;
			}
			return;
		}
		if (obj.isList()) {
			final IAST lst = (IAST) obj;

			if (lst.size() == 2) {
				if (lst.get(1) instanceof IInteger) {
					final IInteger i = (IInteger) lst.get(1);

					if (i.isNegative()) {
						fFromDepth = i.getBigNumerator().intValue();
						fToDepth = i.getBigNumerator().intValue();
						fFromLevel = 0;
						fToLevel = Integer.MAX_VALUE;
						if (fToDepth < fFromDepth) {
							throw new Error("Error in LevelSpecification 1");
						}
					} else {
						fToLevel = i.getBigNumerator().intValue();
						fFromLevel = i.getBigNumerator().intValue();
						fFromDepth = Integer.MIN_VALUE;
						fToDepth = -1;
						if (fToLevel < fFromLevel) {
							throw new Error("Error in LevelSpecification 2");
						}
					}
					return;
				}
			} else {
				if ((lst.size() == 3) && (lst.get(1) instanceof IInteger) && (lst.get(2) instanceof IInteger)) {
					final IInteger i0 = (IInteger) lst.get(1);
					final IInteger i1 = (IInteger) lst.get(2);
					if (i0.isNegative() && i1.isNegative()) {
						fFromDepth = i0.getBigNumerator().intValue();
						fToDepth = i1.getBigNumerator().intValue();
						fFromLevel = 0;
						fToLevel = Integer.MAX_VALUE;
					} else if (i0.isNegative()) {
						throw new Error("Invalid Level specification!");
					} else if (i1.isNegative()) {
						fFromDepth = Integer.MIN_VALUE;
						fToDepth = i1.getBigNumerator().intValue();
						fFromLevel = i0.getBigNumerator().intValue();
						fToLevel = Integer.MAX_VALUE;
					} else {
						fFromDepth = Integer.MIN_VALUE;
						fToDepth = -1;
						fFromLevel = i0.getBigNumerator().intValue();
						fToLevel = i1.getBigNumerator().intValue();
					}
					return;
				}
			}
		}
		if (obj.equals(F.CInfinity)) {
			fToLevel = Integer.MAX_VALUE;
			fFromLevel = 0;
			fFromDepth = Integer.MIN_VALUE;
			fToDepth = -1;
			return;
		}
		throw new Error("Invalid Level specification!");
	}

	/**
	 * Create a LevelSpecification with only the given level
	 * 
	 */
	public LevelSpecification(final int level) {
		this(level, level);
	}

	/**
	 * Create a LevelSpecification with the given level range
	 * 
	 */
	public LevelSpecification(final int levelFrom, final int levelTo) {
		super();
		fFromLevel = levelFrom;
		fToLevel = levelTo;
		fFromDepth = Integer.MIN_VALUE;
		fToDepth = -1;
	}

	/**
	 * Get the low level-limit
	 * 
	 * @return the <code>from</code> value of the level instance
	 */
	public int getFrom() {
		return fFromLevel;
	}

	/**
	 * Get the high level-limit.
	 * 
	 * @return the <code>to</code> value of the level instance
	 */
	public int getTo() {
		return fToLevel;
	}

	public final boolean includesDepth(int i) {
		i *= -1;
		return ((fFromDepth <= i) && (fToDepth >= i));
	}

	public final boolean includesLevel(final int i) {
		return ((fFromLevel <= i) && (fToLevel >= i));
	}

	public final int compareDepth(int i) {
		i *= -1;
		if (fFromDepth > i) {
			return -1;
		}
		if (fToDepth < i) {
			return 1;
		}
		return 0;
	}

	public final int compareLevel() {
		return compareLevel(fCurrentLevel);
	}

	public final int compareLevel(final int i) {
		if (fFromLevel > i) {
			return -1;
		}
		if (fToLevel < i) {
			return 1;
		}
		return 0;
	}

	/**
	 * @param i
	 */
	public void setFrom(final int i) {
		fFromLevel = i;
	}

	/**
	 * @param i
	 */
	public void setTo(final int i) {
		fToLevel = i;
	}
}
