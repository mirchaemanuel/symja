package org.matheclipse.core.patternmatching;

import java.io.Serializable;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.list.algorithms.EvaluationSupport;

public class PatternMatcherAndEvaluator extends PatternMatcher implements Serializable {

	final private IExpr fRightHandSide;
	final private ISymbol fSetSymbol;

	/**
	 * 
	 * @param setSymbol
	 *          the symbol which defines this pattern-matching rule (i.e. Set,
	 *          SetDelayed,...)
	 * @param leftHandSide
	 *          could contain pattern expressions for "pattern-matching"
	 * @param rightHandSide
	 *          the result which should be evaluated if the "pattern-matching"
	 *          succeeds
	 */
	public PatternMatcherAndEvaluator(final ISymbol setSymbol, final IExpr leftHandSide, final IExpr rightHandSide) {
		super(leftHandSide);
		fSetSymbol = setSymbol;
		fRightHandSide = rightHandSide;
	}

	/**
	 * Match the (left-hand-side) pattern with the given expression. If true
	 * evaluate the right-hand-side for the determined values of the patterns
	 * 
	 * @param ee
	 * @param evalExpr
	 * @return
	 */
	public IExpr eval(final IExpr leftHandSide) {
		// if(fRightHandSide.isAST("Condition")) {
		// System.out.println("2:"+fRightHandSide);
		// }
		if (fPatternCounter == 0) {
			// no patterns found match equally:
			if (fLeftHandSide.equals(leftHandSide)) {
				return fRightHandSide;
			}
			return null;
		}
		initPattern();
		if (matchExpr(fLeftHandSide, leftHandSide) && checkCondition()) {
			return EvaluationSupport.substituteLocalVariables(fRightHandSide, fPatternSymbolsArray, fPatternValuesArray);
		}
		return null;
	}

	public IExpr getRHS() {
		return fRightHandSide;
	}

	public ISymbol getSetSymbol() {
		return fSetSymbol;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof PatternMatcherAndEvaluator)) {
			return false;
		}
		// don't compare fSetSymbol and fRightHandSide here
		return super.equals(obj);
	}

}