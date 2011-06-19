package org.matheclipse.core.patternmatching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.ConditionException;
import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.IPatternMatcher;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.combinatoric.KPartitionsIterable;
import org.matheclipse.generic.combinatoric.KPermutationsIterable;

public class PatternMatcher extends IPatternMatcher<IExpr> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6708462090303928690L;

	private static class Entry {
		IExpr patternExpr;
		IExpr evalExpr;

		public Entry(IExpr patternExpr, IExpr evalExpr) {
			this.patternExpr = patternExpr;
			this.evalExpr = evalExpr;
		}
	}

	/**
	 * Manage a stack of pairs of expressions, which have to match each other
	 * 
	 */
	protected class StackMatcher {

		private ArrayList<Entry> stack = new ArrayList<Entry>();

		public StackMatcher() {

		}

		/**
		 * @return
		 * @see java.util.Stack#peek()
		 */
		public Entry peek() {
			return stack.get(stack.size() - 1);
		}

		/**
		 * @return
		 * @see java.util.Stack#pop()
		 */
		public Entry pop() {
			return stack.remove(stack.size() - 1);
		}

		/**
		 * Remove all elements starting at the given <code>fromPosition</code>.
		 * 
		 * @param fromPosition
		 */
		public void removeFrom(int fromPosition) {
			int len = stack.size();
			while (len > fromPosition) {
				stack.remove(len - 1);
				len--;
			}
		}

		public boolean push(IExpr patternExpr, IExpr evalExpr) {
			if (patternExpr.isAST() && ((IAST) patternExpr).isEvalFlagOn(IAST.CONTAINS_PATTERN)) {
				// insert for delayed evaluation in matchRest() method
				stack.add(new Entry(patternExpr, evalExpr));
				return true;
			}
			if (!patternExpr.isPattern()) {
				return patternExpr.equals(evalExpr);
			} else {
				return matchExpr(patternExpr, evalExpr);
			}
		}

		/**
		 * Match the entries of the stack recursively starting from the top entry.
		 * 
		 * @return <code>true</code> if all expressions could be matched.
		 */
		public boolean matchRest() {
			if (isEmpty()) {
				return checkCondition();
			}
			boolean matched = true;
			Entry entry = null;
			try {
				entry = pop();
				matched = matchExpr(entry.patternExpr, entry.evalExpr, this);
				return matched;
			} finally {
				if (!matched && entry != null) {
					stack.add(entry);
				}
			}
		}

		/**
		 * Check if this stack is empty.
		 * 
		 * @return
		 * @see java.util.ArrayList#isEmpty()
		 */
		public boolean isEmpty() {
			return stack.isEmpty();
		}

		/**
		 * The size of the stack.
		 * 
		 * @return
		 * @see java.util.ArrayList#size()
		 */
		public int size() {
			return stack.size();
		}
	}

	/**
	 * Matches an <code>IAST</code> with header attribute <code>Flat</code>.
	 * 
	 * @see ISymbol#FLAT
	 */
	public class FlatMatcher {

		private IAST fLhsPatternList;

		final private boolean fOneIdentity;

		private int[] fPartitionsIndex;

		private ISymbol fSymbol;

		private IAST lhsEvalList;

		public FlatMatcher(final ISymbol sym, final IAST lhsPatternList, final IAST lhsEvalList) {
			fSymbol = sym;
			fLhsPatternList = lhsPatternList;
			this.lhsEvalList = lhsEvalList;
			fOneIdentity = (sym.getAttributes() & ISymbol.ONEIDENTITY) == ISymbol.ONEIDENTITY;
		}

		public boolean matchFlatAST(StackMatcher stackMatcher) {
			// AbstractExpressionFactory f = fSession.getExpressionFactory();
			final int n = lhsEvalList.size() - 1;
			final int k = fLhsPatternList.size() - 1;
			final KPartitionsIterable partitionIterator = new KPartitionsIterable(n, k);
			// copy pattern values to local variable
			IExpr[] patternValues = fPatternMap.copyPattern();
			for (int partitionsIndex[] : partitionIterator) {
				fPartitionsIndex = partitionsIndex;
				if (matchSingleFlatPartition(stackMatcher)) {
					return true;
				}
				// reset pattern values:
				fPatternMap.resetPattern(patternValues);
			}

			return false;
		}

		private boolean matchSingleFlatPartition(StackMatcher stackMatcher) {
			IAST partitionElement;

			final int n = lhsEvalList.size() - 1;
			// 0 is always the first index of a partition
			int partitionStartIndex = 0;
			int partitionElementCounter = 0;
			int lastStackSize = stackMatcher.size();
			boolean matched = true;
			try {
				for (int i = 1; i < fPartitionsIndex.length; i++) {

					if (partitionStartIndex + 1 == fPartitionsIndex[i]) {
						// OneIdentity check here
						if (fOneIdentity) {
							if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList.get(partitionStartIndex + 1))) {
								matched = false;
								return false;
							}
						} else {
							partitionElement = F.function(fSymbol);
							partitionElement.add(lhsEvalList.get(partitionStartIndex + 1));
							if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
								matched = false;
								return false;
							}
						}
					} else {
						partitionElement = F.function(fSymbol);
						for (int m = partitionStartIndex; m < fPartitionsIndex[i]; m++) {

							if (m + 1 < fPartitionsIndex[i]) {
								if ((lhsEvalList.get(m + 2)).isLTOrdered(lhsEvalList.get(m + 1))) {
									// wrong ordering inside partitionElement
									matched = false;
									return false;
								}
							}
							partitionElement.add(lhsEvalList.get(m + 1));
						}
						if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
							matched = false;
							return false;
						}
					}
					partitionElementCounter++;
					partitionStartIndex = fPartitionsIndex[i];

				}
				// generate all elements for the last partitionElement of a partition:

				if (partitionStartIndex + 1 == n) {
					// OneIdentity check here
					if (fOneIdentity) {
						if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList.get(partitionStartIndex + 1))) {
							matched = false;
							return false;
						} 
					} else {
						partitionElement = F.function(fSymbol);
						partitionElement.add(lhsEvalList.get(partitionStartIndex + 1));
						if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
							matched = false;
							return false;
						} 
					}
				} else {
					partitionElement = F.function(fSymbol);
					for (int m = partitionStartIndex; m < n; m++) {

						if (m + 1 < n) {
							if ((lhsEvalList.get(m + 2)).isLTOrdered(lhsEvalList.get(m + 1))) {
								// wrong ordering inside partitionElement
								matched = false;
								return false;
							}
						}
						partitionElement.add(lhsEvalList.get(m + 1));
					}
					if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
						matched = false;
						return false;
					} 
				}

				if (!stackMatcher.matchRest()) {
					matched = false;
					return false;
				}
				return true;
			} finally {
				if (!matched) {
					stackMatcher.removeFrom(lastStackSize);
				}
			}
			// return true;
		}
	}

	/**
	 * Matches a flat orderless expression
	 */
	public class FlatOrderlessMatcher {

		private IAST fLhsPatternList;

		private boolean fOneIdentity;

		private int[] fPartitionsIndex;

		private int[] fPermutationsIndex;

		private ISymbol fSymbol;

		private IAST lhsEvalList;

		public FlatOrderlessMatcher(final ISymbol sym, final IAST lhsPatternList, final IAST lhsEvalList) {
			fSymbol = sym;
			fLhsPatternList = lhsPatternList;
			this.lhsEvalList = lhsEvalList;
			// this.fSession = EvalEngine.get();
			fOneIdentity = (sym.getAttributes() & ISymbol.ONEIDENTITY) == ISymbol.ONEIDENTITY;
		}

		private boolean matchFlatOrderlessList(StackMatcher stackMatcher) {
			// AbstractExpressionFactory f = fSession.getExpressionFactory();
			final int n = lhsEvalList.size() - 1;
			final int k = fLhsPatternList.size() - 1;
			final KPermutationsIterable permutationIterator = new KPermutationsIterable(lhsEvalList, n, 1);
			final KPartitionsIterable partitionIterator = new KPartitionsIterable(n, k);

			// copy pattern values to local variable
			IExpr[] patternValues = fPatternMap.copyPattern();

			// first generate all permutations:
			for (int permutationsIndex[] : permutationIterator) {
				fPermutationsIndex = permutationsIndex;
				// second generate all partitions:
				for (int partitionsIndex[] : partitionIterator) {
					fPartitionsIndex = partitionsIndex;
					if (matchSingleFlatOrderlessPartition(stackMatcher)) {
						return true;
					}
					// reset pattern values:
					fPatternMap.resetPattern(patternValues);
				}
				partitionIterator.reset();
			}
			return false;
		}

		private boolean matchSingleFlatOrderlessPartition(StackMatcher stackMatcher) {
			IAST partitionElement;
			// if (Config.SHOW_STACKTRACE == true) {
			// StringBuffer buf = new StringBuffer();
			// for (int i = 0; i < fPartitionsIndex.length; i++) {
			// buf.append(fPartitionsIndex[i]);
			// buf.append(",");
			// }
			// System.out.println(buf.toString() + " <FO> " + lhsEvalList.toString());
			// }
			final int n = lhsEvalList.size() - 1;
			// 0 is always the first index of a partition
			int partitionStartIndex = 0;
			int partitionElementCounter = 0;
			int lastStackSize = stackMatcher.size();
			boolean matched = true;
			try {

				for (int i = 1; i < fPartitionsIndex.length; i++) {

					if (partitionStartIndex + 1 == fPartitionsIndex[i]) {
						// OneIdentity check here
						if (fOneIdentity) {
							if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList
									.get(fPermutationsIndex[partitionStartIndex] + 1))) {
								matched = false;
								return false;
							}
						} else {
							partitionElement = F.function(fSymbol);
							partitionElement.add(lhsEvalList.get(fPermutationsIndex[partitionStartIndex] + 1));
							if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
								matched = false;
								return false;
							}
						}
					} else {
						partitionElement = F.function(fSymbol);
						for (int m = partitionStartIndex; m < fPartitionsIndex[i]; m++) {

							if (m + 1 < fPartitionsIndex[i]) {
								if ((lhsEvalList.get(fPermutationsIndex[m + 1] + 1)).isLTOrdered(lhsEvalList.get(fPermutationsIndex[m] + 1))) {
									// wrong ordering inside partitionElement
									matched = false;
									return false;
								}
							}
							partitionElement.add(lhsEvalList.get(fPermutationsIndex[m] + 1));
						}
						if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
							matched = false;
							return false;
						}
					}
					partitionElementCounter++;
					partitionStartIndex = fPartitionsIndex[i];

				}
				// generate all elements for the last partitionElement of a partition:

				if (partitionStartIndex + 1 == n) {
					// OneIdentity check here
					if (fOneIdentity) {
						if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList
								.get(fPermutationsIndex[partitionStartIndex] + 1))) {
							matched = false;
							return false;
						}
					} else {
						partitionElement = F.function(fSymbol);
						partitionElement.add(lhsEvalList.get(fPermutationsIndex[partitionStartIndex] + 1));
						if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
							matched = false;
							return false;
						}
					}
				} else {
					partitionElement = F.function(fSymbol);
					for (int m = partitionStartIndex; m < n; m++) {

						if (m + 1 < n) {
							if ((lhsEvalList.get(fPermutationsIndex[m + 1] + 1)).isLTOrdered(lhsEvalList.get(fPermutationsIndex[m] + 1))) {
								// wrong ordering inside partitionElement
								matched = false;
								return false;
							}
						}
						partitionElement.add(lhsEvalList.get(fPermutationsIndex[m] + 1));
					}
					if (!stackMatcher.push(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
						matched = false;
						return false;
					}
				}

				if (!stackMatcher.matchRest()) {
					matched = false;
					return false;
				}
				return true;
			} finally {
				if (!matched) {
					stackMatcher.removeFrom(lastStackSize);
				}
			}
		}
	}

	/**
	 * Matches an <code>IAST</code> with header attribute <code>Orderless</code>.
	 * 
	 * @see ISymbol#ORDERLESS
	 */
	public class OrderlessMatcher {

		private IAST fLHSPatternAST;

		private IAST fLHSEvalAST;

		/**
		 * The used (i.e. matched) expression indexes in the LHS evaluation
		 * expression; <code>-1</code> indicates an unused index.
		 */
		private int[] fUsedIndex;

		/**
		 * Match a pattern expression against an evaluation expression, there the
		 * arguments are commutative (i.e. the head of the AST expression has
		 * attribute <code>Orderless</code>)
		 * 
		 * @param lhsPatternAST
		 *          the pattern AST
		 * @param lhsEvalAST
		 *          the evaluation AST
		 */
		public OrderlessMatcher(final IAST lhsPatternAST, final IAST lhsEvalAST) {
			this.fLHSPatternAST = lhsPatternAST;
			this.fLHSEvalAST = lhsEvalAST;
			this.fUsedIndex = new int[fLHSPatternAST.size() - 1];
			for (int l = 0; l < fUsedIndex.length; l++) {
				fUsedIndex[l] = -1;
			}
		}

		/**
		 * 
		 * @param lhsPosition
		 *          the position in the LHS expression which should actually be
		 *          matched.
		 * @param stackMatcher
		 *          TODO
		 * @return
		 */
		public boolean matchOrderlessAST(int lhsPosition, StackMatcher stackMatcher) {
			if (lhsPosition >= fLHSPatternAST.size()) {
				return stackMatcher.matchRest();
			}
			boolean isNotInUse;
			IExpr subPattern = fLHSPatternAST.get(lhsPosition);
			IExpr[] patternValues = fPatternMap.copyPattern();
			for (int j = 1; j < fLHSEvalAST.size(); j++) {
				isNotInUse = true;
				for (int k = 0; k < fLHSPatternAST.size() - 1; k++) {
					if (fUsedIndex[k] == j) {
						isNotInUse = false;
						break;
					}
				}
				if (isNotInUse) {
					boolean matched = false;
					int lastStackSize = stackMatcher.size();
					try {
						if (stackMatcher.push(subPattern, fLHSEvalAST.get(j))) {
							// if (matchExpr(subPattern, fLHSEvalAST.get(j))) {
							fUsedIndex[lhsPosition - 1] = j;
							if (matchOrderlessAST(lhsPosition + 1, stackMatcher)) {
								matched = true;
								return true;
							}
						}
					} finally {
						if (!matched) {
							fPatternMap.resetPattern(patternValues);
							stackMatcher.removeFrom(lastStackSize);
							fUsedIndex[lhsPosition - 1] = -1;
						}
					}
				}
			}
			return false;
		}

		public void filterResult(IAST result) {
			for (int i = 0; i < fUsedIndex.length; i++) {
				result.set(fUsedIndex[i], null);
			}
			int indx = 1;
			while (indx < result.size()) {
				if (result.get(indx) == null) {
					result.remove(indx);
				} else {
					indx++;
				}
			}
		}
	}

	/**
	 * Contains the "pattern-matching" expression
	 */
	protected IExpr fLeftHandSide;

	/**
	 * Additional condition for pattern-matching maybe <code>null</code>
	 * 
	 */
	protected IExpr fPatternCondition;

	protected PatternMap fPatternMap;

	/**
	 * Needed for serialization
	 * 
	 * @param patternExpr
	 */
	public PatternMatcher() {
		this(null);
	}

	public PatternMatcher(final IExpr patternExpr) {
		this.fLeftHandSide = patternExpr;
		this.fPatternCondition = null;
		if (patternExpr.isCondition()) {
			this.fLeftHandSide = ((IAST) patternExpr).get(1);
			this.fPatternCondition = ((IAST) patternExpr).get(2);
		}
		this.fPatternMap = new PatternMap();
		init(fLeftHandSide);
	}

	protected final void init(IExpr patternExpr) {
		determinePatterns(patternExpr);
		fPatternMap.allocValuesArray();
	}

	/**
	 * Check if the condition for the right-hand-sides
	 * <code>Module[] or Condition[]</code> expressions evaluates to
	 * <code>true</code>. Override it in subclasses.
	 * 
	 * @return <code>true</code>
	 * @see PatternMatcherAndEvaluator#checkRHSCondition(EvalEngine)
	 */
	public boolean checkRHSCondition(EvalEngine engine) {
		return true;
	}

	/**
	 * Check if the condition for this pattern matcher evaluates to
	 * <code>true</code>.
	 */
	public boolean checkCondition() {

		if (fPatternCondition != null) {
			final EvalEngine engine = EvalEngine.get();
			boolean traceMode = false;
			try {
				traceMode = engine.isTraceMode();
				engine.setTraceMode(false);
				final IExpr substConditon = fPatternMap.substitutePatternSymbols(fPatternCondition);
				if (engine.evalTrue(substConditon)) {
					return checkRHSCondition(engine);
				}
				return false;
			} finally {
				engine.setTraceMode(traceMode);
			}
		}
		return true;
	}

	/**
	 * Check if the two left-hand-side pattern expressions are equivalent. (i.e.
	 * <code>f[x_,y_]</code> is equivalent to <code>f[a_,b_]</code> )
	 * 
	 * @param patternExpr1
	 * @param patternExpr2
	 * @param pm1
	 *          TODO
	 * @param pm2
	 *          TODO
	 * @return
	 */
	public static boolean equivalent(final IExpr patternExpr1, final IExpr patternExpr2, PatternMap pm1, PatternMap pm2) {
		if (patternExpr1 == patternExpr2) {
			return true;
		}
		if ((patternExpr1.isAST()) && (patternExpr2.isAST())) {
			final IAST l1 = (IAST) patternExpr1;
			final IAST l2 = (IAST) patternExpr2;
			if (l1.size() != l2.size()) {
				return false;
			}
			if (!equivalent(l1.head(), l2.head(), pm1, pm2)) {
				return false;
			}
			for (int i = 1; i < l1.size(); i++) {

				if (!equivalent(l1.get(i), l2.get(i), pm1, pm2)) {
					return false;
				}
			}
			return true;
		}
		if (patternExpr1.isPattern() && patternExpr2.isPattern()) {
			// test if the pattern indices are equal
			final IPattern p1 = (IPattern) patternExpr1;
			final IPattern p2 = (IPattern) patternExpr2;
			if (pm1.getIndex(p1) != pm2.getIndex(p2)) {
				return false;
			}
			// test if the "check" expressions are equal
			final Object o1 = p1.getCondition();
			final Object o2 = p2.getCondition();
			if ((o1 == null) || (o2 == null)) {
				return o1 == o2;
			}
			return o1.equals(o2);
		}
		return patternExpr1.equals(patternExpr2);
	}

	/**
	 * Determine all patterns (i.e. all objects of instance IPattern) in the given
	 * expression
	 * 
	 * Increments this classes pattern counter.
	 * 
	 * @param lhsExprWithPattern
	 * @param patternIndexMap
	 */
	private int determinePatterns(final IExpr lhsExprWithPattern) {
		if (lhsExprWithPattern instanceof IAST) {
			final IAST ast = (IAST) lhsExprWithPattern;
			int listEvalFlags = IAST.NO_FLAG;
			listEvalFlags |= determinePatterns(ast.head());
			for (int i = 1; i < ast.size(); i++) {
				if (ast.get(i).isPattern()) {
					IPattern pat = (IPattern) ast.get(i);
					fPatternMap.addPattern(pat);
					if (pat.isDefault()) {
						// the ast contains a pattern with default value (i.e. "x_.")
						listEvalFlags |= IAST.CONTAINS_DEFAULT_PATTERN;
					} else {
						// the ast contains a pattern without value (i.e. "x_")
						listEvalFlags |= IAST.CONTAINS_PATTERN;
					}
				} else {
					listEvalFlags |= determinePatterns(ast.get(i));
				}
			}
			ast.setEvalFlags(listEvalFlags);
			// disable flag "pattern with default value"
			listEvalFlags &= IAST.CONTAINS_NO_DEFAULT_PATTERN_MASK;
			return listEvalFlags;
		} else {
			if (lhsExprWithPattern.isPattern()) {
				fPatternMap.addPattern((IPattern) lhsExprWithPattern);
				return IAST.CONTAINS_PATTERN;
			}
		}
		return IAST.NO_FLAG;
	}

	// /**
	// * Set the index of <code>fPatternSymbolsArray</code> where the
	// * <code>pattern</code> stores it's assigned value during pattern matching.
	// *
	// * @param pattern
	// * @param patternIndexMap
	// */
	// private void determinePatternParameters(IPattern pattern) {
	// if (pattern.getSymbol() == null) {
	// // for "unnamed" patterns (i.e. "_" or "_IntegerQ")
	//
	// // needs to increase fPatternCounter, otherwise a rule could eventually be
	// // valued as an "equals rule"
	// fPatternMap.addNull(pattern);
	// } else {
	// // for "named" patterns (i.e. "x_" or "x_IntegerQ")
	// fPatternMap.addSymbol(pattern);
	// }
	// }

	public IExpr getRightHandside() {
		return null;
	}

	/**
	 * Returns the matched pattern in the order they appear in the pExpr
	 * 
	 * @param resultList
	 * @param pExpr
	 */
	@Override
	public void getPatterns(final List<IExpr> resultList, final IExpr pExpr) {
		if (pExpr instanceof IAST) {
			final IAST list = (IAST) pExpr;
			getPatterns(resultList, list.head());
			for (int i = 0; i < list.size(); i++) {
				getPatterns(resultList, list.get(i));
			}
		} else {
			if (pExpr.isPattern()) {
				resultList.add(fPatternMap.getValue((IPattern) pExpr));
			}
		}
	}

	/**
	 * Set the symbol values for the matched patterns.
	 * 
	 * @param resultList
	 * @param pExpr
	 */
	public void setPatternValue2Local(final IExpr pExpr) {
		if (pExpr instanceof IAST) {
			final IAST list = (IAST) pExpr;
			setPatternValue2Local(list.head());
			for (int i = 0; i < list.size(); i++) {
				setPatternValue2Local(list.get(i));
			}
		} else {
			if (pExpr.isPattern()) {
				ISymbol sym = ((IPattern) pExpr).getSymbol();
				if (!sym.hasLocalVariableStack()) {
					throw new UnsupportedOperationException("Pattern symbol has to be defined with local stack");
				}
				sym.set(fPatternMap.getValue((IPattern) pExpr));
			}
		}
	}

	/**
	 * Returns true if the given expression contains no patterns
	 * 
	 * @return
	 */
	@Override
	final public boolean isRuleWithoutPatterns() {
		return fPatternMap.isRuleWithoutPatterns();
	}

	@Override
	public boolean apply(final IExpr evalExpr) {

		if (isRuleWithoutPatterns()) {
			// no patterns found match equally:
			return fLeftHandSide.equals(evalExpr);
		}

		fPatternMap.initPattern();
		return matchExpr(fLeftHandSide, evalExpr);
	}

	/**
	 * Checks if the two expressions match each other
	 * 
	 * 
	 * @return
	 */
	protected boolean matchExpr(IExpr lhsPatternExpression, final IExpr lhsEvalExpression) {
		return matchExpr(lhsPatternExpression, lhsEvalExpression, new StackMatcher());
	}

	/**
	 * Checks if the two expressions match each other
	 * 
	 * @return
	 */
	protected boolean matchExpr(IExpr lhsPatternExpression, final IExpr lhsEvalExpression, StackMatcher stackMatcher) {
		boolean matched = false;
		if (lhsPatternExpression.isCondition()) {
			// expression /; test
			lhsPatternExpression = fPatternMap.substitutePatternSymbols(lhsPatternExpression);
			PatternMatcher.evalLeftHandSide(lhsPatternExpression);
			final PatternMatcher matcher = new PatternMatcher(lhsPatternExpression);
			if (matcher.apply(lhsEvalExpression)) {
				matched = true;
				fPatternMap.copyPatternValuesFromPatternMatcher(matcher.fPatternMap);
			}
		} else if (lhsPatternExpression instanceof IAST) {
			IAST lhsPatternAST = (IAST) lhsPatternExpression;
			IExpr[] patternValues = fPatternMap.copyPattern();
			try {
				matched = matchAST(lhsPatternAST, lhsEvalExpression, stackMatcher);
				if ((lhsPatternAST.getEvalFlags() & IAST.CONTAINS_DEFAULT_PATTERN) == IAST.CONTAINS_DEFAULT_PATTERN) {
					if (!matched) {
						IExpr temp = null;
						ISymbol symbol = lhsPatternAST.topHead();
						int attr = symbol.getAttributes();
						fPatternMap.resetPattern(patternValues);
						temp = matchDefaultAST(symbol, attr, lhsPatternAST);
						if (temp != null) {
							matched = matchExpr(temp, lhsEvalExpression, stackMatcher);
						}
					}
				}
			} finally {
				if (!matched) {
					fPatternMap.resetPattern(patternValues);
				}
			}

		} else if (lhsPatternExpression.isPattern()) {
			matched = matchPattern((IPattern) lhsPatternExpression, lhsEvalExpression);
		} else {
			matched = lhsPatternExpression.equals(lhsEvalExpression);
		}
		if (matched) {
			return stackMatcher.matchRest();
		}
		return false;

	}

	/**
	 * Match the <code>ast</code> with its <code>Default[]</code> values.
	 * 
	 * @param symbol
	 * @param attr
	 * @param ast
	 * @return
	 */
	private IExpr matchDefaultAST(ISymbol symbol, int attr, IAST ast) {
		IAST cloned = F.ast(ast.head(), ast.size(), false);
		for (int i = 1; i < ast.size(); i++) {
			if (ast.get(i).isPattern() && ((IPattern) ast.get(i)).isDefault()) {
				IExpr positionDefaultValue = symbol.getDefaultValue(i);
				if (positionDefaultValue != null) {
					if (!matchPattern((IPattern) ast.get(i), positionDefaultValue)) {
						return null;
					}
					continue;
				} else {
					IExpr commonDefaultValue = symbol.getDefaultValue();
					if (commonDefaultValue != null) {
						if (!matchPattern((IPattern) ast.get(i), commonDefaultValue)) {
							return null;
						}
						continue;
					}
				}

			}
			cloned.add(ast.get(i));
		}
		if (cloned.size() == 2) {
			return cloned.get(1);
		}
		// if ((attr & ISymbol.ORDERLESS) == ISymbol.ORDERLESS) {
		// EvaluationSupport.sort(cloned);
		// }
		return null;
	}

	private boolean matchFlatAST(final ISymbol sym, final IAST lhsPatternAST, final IAST lhsEvalAST, StackMatcher stackMatcher) {
		if ((sym.getAttributes() & ISymbol.ORDERLESS) == ISymbol.ORDERLESS) {
			final FlatOrderlessMatcher foMatcher = new FlatOrderlessMatcher(sym, lhsPatternAST, lhsEvalAST);
			return foMatcher.matchFlatOrderlessList(stackMatcher);
		} else {
			final FlatMatcher fMatcher = new FlatMatcher(sym, lhsPatternAST, lhsEvalAST);
			return fMatcher.matchFlatAST(stackMatcher);
		}
	}

	protected boolean matchAST(final IAST lhsPatternAST, final IExpr lhsEvalExpression, StackMatcher stackMatcher) {
		if (lhsEvalExpression instanceof IAST) {
			if ((!lhsPatternAST.isEvalFlagOn(IAST.CONTAINS_PATTERN)) && lhsPatternAST.equals(lhsEvalExpression)) {
				return stackMatcher.matchRest();
			}

			final IAST lhsEvalAST = (IAST) lhsEvalExpression;
			final ISymbol sym = lhsPatternAST.topHead();
			if (lhsPatternAST.size() < lhsEvalAST.size()) {
				if (((sym.getAttributes() & ISymbol.FLAT) == ISymbol.FLAT) && lhsPatternAST.head().equals(lhsEvalAST.head())) {
					if (!matchExpr(lhsPatternAST.head(), lhsEvalAST.head())) {
						return false;
					}
					return matchFlatAST(sym, lhsPatternAST, lhsEvalAST, stackMatcher);
				}
				return false;
			}

			if (lhsPatternAST.size() != lhsEvalAST.size()) {
				return false;
			}
			if (!matchExpr(lhsPatternAST.head(), lhsEvalAST.head())) {
				return false;
			}

			if (lhsPatternAST.isOrderlessAST()) {
				final OrderlessMatcher foMatcher = new OrderlessMatcher(lhsPatternAST, lhsEvalAST);
				return foMatcher.matchOrderlessAST(1, stackMatcher);
			}

			return matchASTSequence(lhsPatternAST, lhsEvalAST, 0, stackMatcher);
		}

		return false;
	}

	private boolean matchASTSequence(final IAST lhsPatternAST, final IAST lhsEvalAST, final int lhsEvalOffset,
			StackMatcher stackMatcher) {
		// distinguish between "equally" matched list-expressions and
		// AST expressions with "CONTAINS_PATTERN" flag
		IExpr[] patternValues = fPatternMap.copyPattern();
		int lastStackSize = stackMatcher.size();
		boolean matched = true;
		try {
			// loop from the end
			for (int i = lhsPatternAST.size() - 1; i > 0; i--) {
				if (!stackMatcher.push(lhsPatternAST.get(i), lhsEvalAST.get(lhsEvalOffset + i))) {
					matched = false;
					return false;
				}
			}
			if (!stackMatcher.matchRest()) {
				matched = false;
				return false;
			}
			return true;
		} finally {
			if (!matched) {
				stackMatcher.removeFrom(lastStackSize);
				fPatternMap.resetPattern(patternValues);
			}
		}
	}

	protected IExpr evalAST(final IAST lhsPatternAST, final IAST lhsEvalAST, IExpr rhsExpr, StackMatcher stackMatcher) {
		if (lhsPatternAST.size() < lhsEvalAST.size()) {
			if (lhsPatternAST.isOrderlessAST()) {
				if (!matchExpr(lhsPatternAST.head(), lhsEvalAST.head(), new StackMatcher())) {
					return null;
				}
				final OrderlessMatcher foMatcher = new OrderlessMatcher(lhsPatternAST, lhsEvalAST);
				boolean matched = foMatcher.matchOrderlessAST(1, stackMatcher);
				if (matched) {
					IAST lhsResultAST = ((IAST) lhsEvalAST).clone();
					foMatcher.filterResult(lhsResultAST);
					try {
						IExpr result = fPatternMap.substitutePatternSymbols(rhsExpr);
						result = F.eval(result);
						lhsResultAST.add(result);
						return lhsResultAST;
					} catch (final ConditionException e) {
						// fall through
					} catch (final ReturnException e) {
						lhsResultAST.add(e.getValue());
						return lhsResultAST;
					}
					return null;
				}

			}
			if (lhsPatternAST.isFlatAST()) {
				if (!matchExpr(lhsPatternAST.head(), lhsEvalAST.head(), new StackMatcher())) {
					return null;
				}
				int len = lhsEvalAST.size() - lhsPatternAST.size();
				for (int i = 0; i < len; i++) {
					if (matchASTSequence(lhsPatternAST, lhsEvalAST, i, stackMatcher)) {
						IAST lhsResultAST = ((IAST) lhsEvalAST).clone();
						for (int j = 1; j < lhsPatternAST.size(); j++) {
							lhsResultAST.remove(i + 1);
						}
						try {
							IExpr result = fPatternMap.substitutePatternSymbols(rhsExpr);
							result = F.eval(result);
							lhsResultAST.add(i + 1, result);
							return lhsResultAST;
						} catch (final ConditionException e) {
							// fall through
						} catch (final ReturnException e) {
							lhsResultAST.add(i + 1, e.getValue());
							return lhsResultAST;
						}
						return null;
					}
				}
			}
		}
		return null;
	}

	private boolean matchPattern(final IPattern pattern, final IExpr expr) {
		if (!pattern.isConditionMatched(expr)) {
			return false;
		}

		final IExpr value = fPatternMap.getValue(pattern);
		if (value != null) {
			return expr.equals(value);
		}

		fPatternMap.setValue(pattern, expr);
		return true;
	}

	public IExpr getLHS() {
		return fLeftHandSide;
	}

	@Override
	public IExpr eval(final IExpr leftHandSide) {
		return null;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof PatternMatcher) {
			final PatternMatcher pm = (PatternMatcher) obj;
			if (fPatternMap.size() != pm.fPatternMap.size()) {
				return false;
			}
			if (isRuleWithoutPatterns()) {
				return fLeftHandSide.equals(pm.fLeftHandSide);
			}
			if (equivalent(fLeftHandSide, pm.fLeftHandSide, fPatternMap, pm.fPatternMap)) {
				if ((fPatternCondition != null) && (pm.fPatternCondition != null)) {
					return fPatternCondition.equals(pm.fPatternCondition);
				}
				if ((fPatternCondition != null) || (pm.fPatternCondition != null)) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return fLeftHandSide.hashCode();
	}

	@Override
	public Object clone() {
		PatternMatcher v = (PatternMatcher) super.clone();
		v.fLeftHandSide = fLeftHandSide;
		v.fPatternCondition = fPatternCondition;
		v.fPatternMap = fPatternMap.clone();
		return v;
	}

	/**
	 * Get the additional condition for pattern-matching
	 * 
	 */
	public IExpr getCondition() {
		return fPatternCondition;
	}

	/**
	 * Sets an additional evaluation-condition for pattern-matching
	 * 
	 */
	public void setCondition(final IExpr condition) {
		fPatternCondition = condition;
	}

	public static IExpr evalLeftHandSide(IExpr leftHandSide, final EvalEngine engine) {
		if (leftHandSide instanceof IAST) {
			final IExpr temp = engine.evalSetAttributes((IAST) leftHandSide);
			if (temp != null) {
				leftHandSide = temp;
			}
		}
		return leftHandSide;
	}

	public static IExpr evalLeftHandSide(IExpr leftHandSide) {
		return evalLeftHandSide(leftHandSide, EvalEngine.get());
	}

}