package org.matheclipse.core.patternmatching;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.list.algorithms.EvaluationSupport;
import org.matheclipse.core.reflection.system.Module;

public class PatternMatcherAndEvaluator extends PatternMatcher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2241135467123931061L;

	private IExpr fRightHandSide;
	private ISymbol fSetSymbol;
	private IExpr fLastResult;
	/**
	 * Additional Module[] initializer for pattern-matching maybe
	 * <code>null</code>
	 * 
	 */
	protected IAST fModuleInitializer;

	/**
	 * Additional condition for pattern-matching maybe <code>null</code>
	 * 
	 */
	protected IExpr fCondition;

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
		fLastResult = null;
		fModuleInitializer = null;
		fCondition = null;
	}

	@Override
	public Object clone() {
		// try {
		PatternMatcherAndEvaluator v = (PatternMatcherAndEvaluator) super.clone();
		v.fRightHandSide = fRightHandSide;
		v.fSetSymbol = fSetSymbol;
		v.fCondition = fCondition;
		v.fModuleInitializer = fModuleInitializer;
		return v;
		// } catch (CloneNotSupportedException e) {
		// // this shouldn't happen, since we are Cloneable
		// throw new InternalError();
		// }
	}

	@Override
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
				traceMode = engine.isTraceMode();
				engine.setTraceMode(false);

				if (fModuleInitializer != null) {
					final Map<IExpr, IExpr> rulesMap = new HashMap<IExpr, IExpr>();
					for (int i = 0; i < fPatternSymbolsArray.size(); i++) {
						rulesMap.put(fPatternSymbolsArray.get(i), fPatternValuesArray[i]);
					}
					try {
						fLastResult = Module.evalModuleCondition(fModuleInitializer, fRightHandSide, fCondition, engine, Functors
								.rules(rulesMap));
					} catch (final ReturnException e) {
						fLastResult = e.getValue();
					}
					return fLastResult != null;
				}

				final IExpr substConditon = EvaluationSupport.substituteLocalVariables(fCondition, fPatternSymbolsArray,
						fPatternValuesArray);
				return engine.evaluate(substConditon).equals(F.True);
			} finally {
				if (traceMode) {
					engine.setTraceMode(true);
				}
			}
		}
		return true;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof PatternMatcherAndEvaluator) {
			PatternMatcherAndEvaluator pm = (PatternMatcherAndEvaluator) obj;
			// don't compare fSetSymbol and fRightHandSide here
			if (super.equals(obj)) {
				if ((fCondition != null) && (pm.fCondition != null)) {
					return fCondition.equals(pm.fCondition);
				}
				if ((fCondition != null) || (pm.fCondition != null)) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 53;
	}

	/**
	 * Match the (left-hand-side) pattern with the given expression. If true
	 * evaluate the right-hand-side for the determined values of the patterns
	 * 
	 * @param ee
	 * @param evalExpr
	 * @return
	 */
	@Override
	public IExpr eval(final IExpr leftHandSide) {
		// if(fRightHandSide.isAST("Condition")) {
		// System.out.println("2:"+fRightHandSide);
		// }
		if (fPatternCounter == 0) {
			// no patterns found match equally:
			if (fLeftHandSide.equals(leftHandSide)) {
				IExpr result = fRightHandSide;
				try {
				  IExpr temp = F.eval(result);
				  if (temp!=null){
				  	return temp;
				  }
				  return result;
				} catch (final ReturnException e) {
					return e.getValue();
				}
			}
			return null;
		}
		initPattern();
		IExpr rightHandSide = fRightHandSide;
		if (matchExpr(fLeftHandSide, leftHandSide) && checkCondition()) {
			if (fLastResult != null) {
				rightHandSide = fLastResult;
			}
			IExpr result = EvaluationSupport.substituteLocalVariables(rightHandSide, fPatternSymbolsArray, fPatternValuesArray);
			try {
			  IExpr temp = F.eval(result);
			  if (temp!=null){
			  	return temp;
			  }
			  return result;
			} catch (final ReturnException e) {
				return e.getValue();
			}
		}
		return null;
	}

	public IExpr getRHS() {
		return fRightHandSide;
	}

	public IExpr getCondition() {
		return fCondition;
	}

	public IAST getInitializer() {
		return fModuleInitializer;
	}

	public ISymbol getSetSymbol() {
		return fSetSymbol;
	}

	/**
	 * Sets an additional evaluation-condition for pattern-matching
	 * 
	 */
	public void setCondition(final IExpr condition) {
		fCondition = condition;
	}

	public void setInitializer(final IAST moduleInitializer) {
		fModuleInitializer = moduleInitializer;
	}

}