package org.matheclipse.core.patternmatching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.matheclipse.core.combinatoric.KPartitionsIterator;
import org.matheclipse.core.combinatoric.KPermutationsIterator;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.IPatternMatcher;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.list.algorithms.EvaluationSupport;

public class PatternMatcher extends IPatternMatcher<IExpr> implements Serializable {

	/**
	 * Matches a flat expression
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

		public boolean matchFlatList() {
			// AbstractExpressionFactory f = fSession.getExpressionFactory();
			final int n = lhsEvalList.size() - 1;
			final int k = fLhsPatternList.size() - 1;
			final KPartitionsIterator partitionIterator = new KPartitionsIterator(n, k);
			// save fPatternValuesArray in local variable
			final IExpr[] localPatternValuesArrayCopy = new IExpr[fPatternValuesArray.length];
			System.arraycopy(fPatternValuesArray, 0, localPatternValuesArrayCopy, 0, fPatternValuesArray.length);
			// generate all partitions:
			while ((fPartitionsIndex = partitionIterator.nextElement()) != null) {
				if (matchSingleFlatPartition() && checkCondition()) {
					return true;
				}
				// reset pattern values:
				System.arraycopy(localPatternValuesArrayCopy, 0, fPatternValuesArray, 0, fPatternValuesArray.length);
			}

			return false;
		}

		private boolean matchSingleFlatPartition() {
			IAST partitionElement;

			final int n = lhsEvalList.size() - 1;
			// 0 is always the first index of a partition
			int partitionStartIndex = 0;
			int partitionElementCounter = 0;

			for (int i = 1; i < fPartitionsIndex.length; i++) {

				if (partitionStartIndex + 1 == fPartitionsIndex[i]) {
					// OneIdentity check here
					if (fOneIdentity) {
						if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList.get(partitionStartIndex + 1))) {
							return false;
						}
					} else {
						partitionElement = F.function(fSymbol);
						partitionElement.add(lhsEvalList.get(partitionStartIndex + 1));
						if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
							return false;
						}
					}
				} else {
					partitionElement = F.function(fSymbol);
					for (int m = partitionStartIndex; m < fPartitionsIndex[i]; m++) {

						if (m + 1 < fPartitionsIndex[i]) {
							if ((lhsEvalList.get(m + 2)).isLTOrdered(lhsEvalList.get(m + 1))) {
								// wrong ordering inside partitionElement
								return false;
							}
						}
						partitionElement.add(lhsEvalList.get(m + 1));
					}
					if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
						return false;
					}
				}
				partitionElementCounter++;
				partitionStartIndex = fPartitionsIndex[i];

			}
			// generate all elements for the last partitionElement of a
			// partition:

			if (partitionStartIndex + 1 == n) {
				// OneIdentity check here
				if (fOneIdentity) {
					if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList.get(partitionStartIndex + 1))) {
						return false;
					}
				} else {
					partitionElement = F.function(fSymbol);
					partitionElement.add(lhsEvalList.get(partitionStartIndex + 1));
					if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
						return false;
					}
				}
			} else {
				partitionElement = F.function(fSymbol);
				for (int m = partitionStartIndex; m < n; m++) {

					if (m + 1 < n) {
						if ((lhsEvalList.get(m + 2)).isLTOrdered(lhsEvalList.get(m + 1))) {
							// wrong ordering inside partitionElement
							return false;
						}
					}
					partitionElement.add(lhsEvalList.get(m + 1));
				}
				if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
					return false;
				}
			}

			return true;
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

		public boolean matchFlatOrderlessList() {
			// AbstractExpressionFactory f = fSession.getExpressionFactory();
			final int n = lhsEvalList.size() - 1;
			final int k = fLhsPatternList.size() - 1;
			final KPermutationsIterator permutationIterator = new KPermutationsIterator(lhsEvalList, n, 1);
			final KPartitionsIterator partitionIterator = new KPartitionsIterator(n, k);

			// save fPatternValuesArray in local variable
			final IExpr[] localPatternValuesArrayCopy = new IExpr[fPatternValuesArray.length];
			System.arraycopy(fPatternValuesArray, 0, localPatternValuesArrayCopy, 0, fPatternValuesArray.length);

			// first generate all permutations:
			while ((fPermutationsIndex = permutationIterator.nextElement()) != null) {

				// second generate all partitions:
				while ((fPartitionsIndex = partitionIterator.nextElement()) != null) {

					if (matchSingleFlatOrderlessPartition() && checkCondition()) {
						return true;
					}
					// reset pattern values:
					System.arraycopy(localPatternValuesArrayCopy, 0, fPatternValuesArray, 0, fPatternValuesArray.length);
				}
				partitionIterator.reset();
			}
			return false;
		}

		private boolean matchSingleFlatOrderlessPartition() {
			IAST partitionElement;

			final int n = lhsEvalList.size() - 1;
			// 0 is always the first index of a partition
			int partitionStartIndex = 0;
			int partitionElementCounter = 0;

			for (int i = 1; i < fPartitionsIndex.length; i++) {

				if (partitionStartIndex + 1 == fPartitionsIndex[i]) {
					// OneIdentity check here
					if (fOneIdentity) {
						if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList
								.get(fPermutationsIndex[partitionStartIndex] + 1))) {
							return false;
						}
					} else {
						partitionElement = F.function(fSymbol);
						partitionElement.add(lhsEvalList.get(fPermutationsIndex[partitionStartIndex] + 1));
						if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
							return false;
						}
					}
				} else {
					partitionElement = F.function(fSymbol);
					for (int m = partitionStartIndex; m < fPartitionsIndex[i]; m++) {

						if (m + 1 < fPartitionsIndex[i]) {
							if ((lhsEvalList.get(fPermutationsIndex[m + 1] + 1)).isLTOrdered(lhsEvalList.get(fPermutationsIndex[m] + 1))) {
								// wrong ordering inside partitionElement
								return false;
							}
						}
						partitionElement.add(lhsEvalList.get(fPermutationsIndex[m] + 1));
					}
					if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
						return false;
					}
				}
				partitionElementCounter++;
				partitionStartIndex = fPartitionsIndex[i];

			}
			// generate all elements for the last partitionElement of a
			// partition:

			if (partitionStartIndex + 1 == n) {
				// OneIdentity check here
				if (fOneIdentity) {
					if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), lhsEvalList
							.get(fPermutationsIndex[partitionStartIndex] + 1))) {
						return false;
					}
				} else {
					partitionElement = F.function(fSymbol);
					partitionElement.add(lhsEvalList.get(fPermutationsIndex[partitionStartIndex] + 1));
					if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
						return false;
					}
				}
			} else {
				partitionElement = F.function(fSymbol);
				for (int m = partitionStartIndex; m < n; m++) {

					if (m + 1 < n) {
						if ((lhsEvalList.get(fPermutationsIndex[m + 1] + 1)).isLTOrdered(lhsEvalList.get(fPermutationsIndex[m] + 1))) {
							// wrong ordering inside partitionElement
							return false;
						}
					}
					partitionElement.add(lhsEvalList.get(fPermutationsIndex[m] + 1));
				}
				if (!matchExpr(fLhsPatternList.get(partitionElementCounter + 1), partitionElement)) {
					return false;
				}
			}

			return true;
		}
	}

	/**
	 * Matches an orderless expression
	 */
	public class OrderlessMatcher {

		private IAST fLhsPatternList;

		// private boolean fOneIdentity;

		// private int[] fPartitionsIndex;
		private int[] fPermutationsIndex;

		// private ISymbol fSymbol;

		private IAST lhsEvalList;

		public OrderlessMatcher(final IAST lhsPatternList, final IAST lhsEvalList) {
			// this.fSymbol = sym;
			fLhsPatternList = lhsPatternList;
			this.lhsEvalList = lhsEvalList;
			// this.fSession = EvalEngine.get();
			// fOneIdentity = (sym.getAttributes() & ISymbol.ONEIDENTITY) ==
			// ISymbol.ONEIDENTITY;
		}

		public boolean matchOrderlessList() {
			final int n = lhsEvalList.size() - 1;
			final KPermutationsIterator permutationIterator = new KPermutationsIterator(lhsEvalList, n, 1);

			// save fPatternValuesArray in local variable
			final IExpr[] localPatternValuesArrayCopy = new IExpr[fPatternValuesArray.length];
			System.arraycopy(fPatternValuesArray, 0, localPatternValuesArrayCopy, 0, fPatternValuesArray.length);

			// first generate all permutations:
			while ((fPermutationsIndex = permutationIterator.nextElement()) != null) {

				// check all permutations:
				if (matchSingleOrderlessPermutation() && checkCondition()) {
					return true;
				}
				// reset pattern values:
				System.arraycopy(localPatternValuesArrayCopy, 0, fPatternValuesArray, 0, fPatternValuesArray.length);

			}
			return false;
		}

		private boolean matchSingleOrderlessPermutation() {
			// IAST permutationElement;

			for (int i = 0; i < fPermutationsIndex.length; i++) {

				// OneIdentity check here
				// if (fOneIdentity) {
				if (!matchExpr(fLhsPatternList.get(i + 1), lhsEvalList.get(fPermutationsIndex[i] + 1))) {
					return false;
				}
				// } else {
				// permutationElement = fFactory.createFunction(fSymbol);
				// permutationElement.add(lhsEvalList.getArg(fPermutationsIndex[i]));
				// if (!matchExpr((IExpr) fLhsPatternList.getArg(i),
				// permutationElement)) {
				// return false;
				// }
				// }
			}

			return true;
		}
	}

	/**
	 * Additional condition for pattern-matching maybe <code>null</code>
	 * 
	 */
	protected IExpr fCondition = null;

	/**
	 * Counts the number of patterns in fPatternExpr
	 */
	transient protected int fPatternCounter = 0;

	/**
	 * Contains the "pattern-matching" expression
	 */
	protected IExpr fLeftHandSide;

	/**
	 * contains the current values of the patterns
	 */
	transient protected IExpr[] fPatternValuesArray = null;

	/**
	 * contains the current symbols of the patterns
	 */
	transient protected ArrayList<ISymbol> fPatternSymbolsArray = null;

	/**
	 * Needed for serialization
	 * 
	 * @param patternExpr
	 */
	public PatternMatcher() {
		this(null);
	}

	public PatternMatcher(final IExpr patternExpr) {
		fLeftHandSide = patternExpr;
		init(fLeftHandSide);
	}

	protected final void init(IExpr patternExpr) {
		final HashMap<String, IExpr> patterns = new HashMap<String, IExpr>();
		fPatternSymbolsArray = new ArrayList<ISymbol>(5);
		determinePatterns(patternExpr, patterns);
		if (fPatternCounter != 0) {
			fPatternValuesArray = new IExpr[fPatternCounter];
		}
	}

	/**
	 * Check if this matchers pattern values equals the pattern values in the
	 * given <code>thatMatcher</code> for the same pattern symbols.
	 * 
	 * @param thatMatcher
	 * @return
	 */
	public boolean checkPatternMatcher(final PatternMatcher thatMatcher) {
		// PatternMatcher thatMatcher = (PatternMatcher) thtMatcher;
		if (fPatternCounter == 0 || thatMatcher.fPatternCounter == 0) {
			return true;
		}
		for (int i = 0; i < fPatternSymbolsArray.size(); i++) {
			for (int j = 0; j < thatMatcher.fPatternSymbolsArray.size(); j++) {
				if (fPatternSymbolsArray.get(i).equals(thatMatcher.fPatternSymbolsArray.get(j))) {
					return fPatternValuesArray[i].equals(thatMatcher.fPatternValuesArray[j]);
				}
			}
		}
		return false;
	}

	public boolean checkCondition() {
		if (fCondition != null) {
			if (fPatternValuesArray != null) {
				// all patterns have values assigned?
				for (int i = 0; i < fPatternValuesArray.length; i++) {

					if (fPatternValuesArray[i] == null) {
						return true;
					}
				}
			}
			final EvalEngine engine = EvalEngine.get();
			boolean traceMode = false;
			try {
				final IExpr substConditon = EvaluationSupport.substituteLocalVariables(fCondition, fPatternSymbolsArray,
						fPatternValuesArray);
				traceMode = engine.isTraceMode();
				engine.setTraceMode(false);
				return engine.evaluate(substConditon).equals(F.True);
			} finally {
				if (traceMode) {
					engine.setTraceMode(true);
				}
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
	 * @return
	 */
	public static boolean equivalent(final IExpr patternExpr1, final IExpr patternExpr2) {
		if ((patternExpr1 instanceof IAST) && (patternExpr2 instanceof IAST)) {
			final IAST l1 = (IAST) patternExpr1;
			final IAST l2 = (IAST) patternExpr2;
			if (l1.size() != l2.size()) {
				return false;
			}
			if (!equivalent(l1.head(), l2.head())) {
				return false;
			}
			for (int i = 1; i < l1.size(); i++) {

				if (!equivalent(l1.get(i), l2.get(i))) {
					return false;
				}
			}
			return true;
		}
		if ((patternExpr1 instanceof IPattern) && (patternExpr2 instanceof IPattern)) {
			// test if the pattern indices are equal
			final IPattern p1 = (IPattern) patternExpr1;
			final IPattern p2 = (IPattern) patternExpr2;
			if (p1.getIndex() != p2.getIndex()) {
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
	 * @param pExpr
	 */
	private int determinePatterns(final IExpr lhsPatternExpression, final HashMap<String, IExpr> patternMap) {
		if (lhsPatternExpression instanceof IAST) {
			final IAST list = (IAST) lhsPatternExpression;
			int listEvalFlags = IAST.NO_FLAG;
			listEvalFlags |= determinePatterns(list.head(), patternMap);
			for (int i = 1; i < list.size(); i++) {

				listEvalFlags |= determinePatterns(list.get(i), patternMap);
			}
			list.setEvalFlags(listEvalFlags);
			return listEvalFlags;
		} else {
			if (lhsPatternExpression instanceof IPattern) {
				if (((IPattern) lhsPatternExpression).getSymbol() == null) {
					// for "unnamed" patterns:
					((IPattern) lhsPatternExpression).setIndex(fPatternCounter++);
					fPatternSymbolsArray.add(null);
				} else {
					// for "named" patterns:
					final IPattern temp = (IPattern) patternMap.get(lhsPatternExpression.toString());
					if (temp != null) {
						((IPattern) lhsPatternExpression).setIndex(temp.getIndex());
					} else {
						((IPattern) lhsPatternExpression).setIndex(fPatternCounter++);
						fPatternSymbolsArray.add(((IPattern) lhsPatternExpression).getSymbol());
						patternMap.put(lhsPatternExpression.toString(), lhsPatternExpression);
					}
				}
				return IAST.CONTAINS_PATTERN;
			}
		}
		return IAST.NO_FLAG;
	}

	public IExpr getCondition() {
		return fCondition;
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
			if (pExpr instanceof IPattern) {
				final IExpr value = fPatternValuesArray[((IPattern) pExpr).getIndex()];
				resultList.add(value);
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
			if (pExpr instanceof IPattern) {
				ISymbol sym = ((IPattern) pExpr).getSymbol();
				if (!sym.hasLocalVariableStack()) {
					throw new UnsupportedOperationException("Pattern symbol has to be defined with local stack");
				}
				final IExpr value = fPatternValuesArray[((IPattern) pExpr).getIndex()];
				sym.set(value);
			}
		}
	}

	protected void initPattern() {
		for (int i = 0; i < fPatternCounter; i++) {
			fPatternValuesArray[i] = null;
		}
	}

	/**
	 * Returns true if the given expression contains no patterns
	 * 
	 * @return
	 */
	@Override
	final public boolean isRuleWithoutPatterns() {
		return fPatternCounter == 0;
	}

	@Override
	public boolean apply(final IExpr evalExpr) {

		if (fPatternCounter == 0) {
			// no patterns found match equally:
			return fLeftHandSide.equals(evalExpr);
		}

		initPattern();
		return matchExpr(fLeftHandSide, evalExpr);
	}

	/**
	 * Checks if the two expressions match each other
	 * 
	 * @param patternObj
	 * @param obj
	 * @return
	 */
	protected boolean matchExpr(final IExpr lhsPatternExpression, final IExpr rhsExpression) {
		if (lhsPatternExpression instanceof IAST) {
			return matchAST((IAST) lhsPatternExpression, rhsExpression);
		} else {
			if (lhsPatternExpression instanceof IPattern) {
				return matchPattern((IPattern) lhsPatternExpression, rhsExpression);
			}
		}
		return lhsPatternExpression.equals(rhsExpression);
	}

	private boolean matchFlatList(final ISymbol sym, final IAST lhsPatternList, final IAST lhsEvalList) {
		if ((sym.getAttributes() & ISymbol.ORDERLESS) == ISymbol.ORDERLESS) {
			final FlatOrderlessMatcher foMatcher = new FlatOrderlessMatcher(sym, lhsPatternList, lhsEvalList);
			return foMatcher.matchFlatOrderlessList();
		} else {
			final FlatMatcher fMatcher = new FlatMatcher(sym, lhsPatternList, lhsEvalList);
			return fMatcher.matchFlatList();
		}
	}

	private boolean matchAST(final IAST lhsPatternExpression, final IExpr lhsEvalExpression) {
		if (lhsEvalExpression instanceof IAST) {
			final IAST lhsEvalList = (IAST) lhsEvalExpression;
			final ISymbol sym = lhsPatternExpression.topHead();
			if (lhsEvalList.size() > lhsPatternExpression.size()) {
				if (((sym.getAttributes() & ISymbol.FLAT) == ISymbol.FLAT) && lhsPatternExpression.head().equals(lhsEvalList.head())) {
					if (!matchExpr(lhsPatternExpression.head(), lhsEvalList.head())) {
						return false;
					}
					return matchFlatList(sym, lhsPatternExpression, lhsEvalList);
				}
				return false;
			}

			if (lhsEvalList.size() != lhsPatternExpression.size()) {
				return false;
			}

			if (!matchExpr(lhsPatternExpression.head(), lhsEvalList.head())) {
				return false;
			}

			if ((sym.getAttributes() & ISymbol.ORDERLESS) == ISymbol.ORDERLESS) {
				final OrderlessMatcher foMatcher = new OrderlessMatcher(lhsPatternExpression, lhsEvalList);
				return foMatcher.matchOrderlessList();
			}

			// int evalFlags = lhsPatternExpression.getEvalFlags();
			// distinguish between "equally" matched list-expressions and
			// list.expressions with "CONTAINS_PATTERN" flag
			IExpr temp;
			// first loop for list-arguments, where CONTAINS_PATTERN flag is
			// enabled
			for (int i = 1; i < lhsPatternExpression.size(); i++) {

				temp = lhsPatternExpression.get(i);
				if ((temp instanceof IAST) && ((IAST) temp).isEvalFlagOn(IAST.CONTAINS_PATTERN)) {
					continue;
				}
				if (!matchExpr(temp, lhsEvalList.get(i))) {
					return false;
				}
			}
			// second loop for list-arguments, where CONTAINS_PATTERN flag is
			// disabled
			for (int i = 1; i < lhsPatternExpression.size(); i++) {

				temp = lhsPatternExpression.get(i);
				if (!(temp instanceof IAST)) {
					continue;
				}
				if (((IAST) temp).isEvalFlagOn(IAST.CONTAINS_PATTERN)) {
					if (!matchExpr(temp, lhsEvalList.get(i))) {
						return false;
					}
				}

			}
			return true && checkCondition();
		}

		return false;
	}

	private boolean matchPattern(final IPattern pattern, final IExpr expr) {

		if (!pattern.isConditionMatched(expr)) {
			return false;
		}

		final IExpr value = fPatternValuesArray[pattern.getIndex()];
		if (value != null) {
			return expr.equals(value);
		}

		fPatternValuesArray[pattern.getIndex()] = expr;
		return true;
	}

	@Override
	public void setCondition(final IExpr condition) {
		fCondition = condition;
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
		if (!(obj instanceof PatternMatcher)) {
			return false;
		}
		final PatternMatcher pm = (PatternMatcher) obj;
		if (fPatternCounter != pm.fPatternCounter) {
			return false;
		}
		if (fPatternCounter == 0) {
			return fLeftHandSide.equals(pm.fLeftHandSide);
		}
		if ((fCondition != null) && (pm.fCondition != null)) {
			return fCondition.equals(pm.fCondition);
		}
		if ((fCondition != null) || (pm.fCondition != null)) {
			return false;
		}
		return equivalent(fLeftHandSide, pm.fLeftHandSide);
	}

	@Override
	public int hashCode() {
		return fLeftHandSide.hashCode();
	}

	@Override
	public Object clone() {
		// try {
		PatternMatcher v = (PatternMatcher) super.clone();
		v.fCondition = fCondition;
		v.fPatternCounter = fPatternCounter;
		v.fLeftHandSide = fLeftHandSide;
		v.fPatternValuesArray = Arrays.copyOf(fPatternValuesArray, fPatternValuesArray.length);
		v.fPatternSymbolsArray = (ArrayList<ISymbol>) fPatternSymbolsArray.clone();
		return v;
		// } catch (CloneNotSupportedException e) {
		// // this shouldn't happen, since we are Cloneable
		// throw new InternalError();
		// }
	}
}