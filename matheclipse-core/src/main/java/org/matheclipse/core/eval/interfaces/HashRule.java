package org.matheclipse.core.eval.interfaces;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.visit.HashValueVisitor;

public class HashRule {
	int hash1;
	int hash2;
	private final PatternMatcher fLHS1;
	private final PatternMatcher fLHS2;
	private final IExpr fRHS;

	/**
	 * 
	 * @param lhsPattern1
	 *          first left-hand-side pattern
	 * @param lhsPattern2
	 *          second left-hand-side pattern
	 * @param rhsResult
	 *          the right-hand-side result
	 */
	public HashRule(IExpr lhsPattern1, IExpr lhsPattern2, IExpr rhsResult) {
		fLHS1 = new PatternMatcher(lhsPattern1);
		fLHS2 = new PatternMatcher(lhsPattern2);
		fRHS = rhsResult;
		HashValueVisitor v = new HashValueVisitor();
		hash1 = lhsPattern1.accept(v);
		v.setUp();
		hash2 = lhsPattern2.accept(v);
		v.setUp();
	}

	/**
	 * @return the first left-hand-side pattern matcher
	 */
	public PatternMatcher getLHS1() {
		return fLHS1;
	}

	/**
	 * @return the second left-hand-side pattern matcher
	 */
	public PatternMatcher getLHS2() {
		return fLHS2;
	}

	/**
	 * @return the right-hand-side result
	 */
	public IExpr getRHS() {
		return fRHS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hash1;
		result = prime * result + hash2;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HashRule other = (HashRule) obj;
		if (hash1 != other.hash1)
			return false;
		if (hash2 != other.hash2)
			return false;
		if (fLHS1 == null) {
			if (other.fLHS1 != null)
				return false;
		} else if (!fLHS1.equals(other.fLHS1))
			return false;
		if (fLHS2 == null) {
			if (other.fLHS2 != null)
				return false;
		} else if (!fLHS2.equals(other.fLHS2))
			return false;
		if (fRHS == null) {
			if (other.fRHS != null)
				return false;
		} else if (!fRHS.equals(other.fRHS))
			return false;
		return true;
	}

	/**
	 * @return the hash1
	 */
	public int getHash1() {
		return hash1;
	}

	/**
	 * @return the hash2
	 */
	public int getHash2() {
		return hash2;
	}

}
