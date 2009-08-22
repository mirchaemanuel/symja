package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.ReturnException;
import org.matheclipse.core.eval.exception.RuleCreationError;
import org.matheclipse.core.eval.interfaces.ICreatePatternMatcher;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class SetDelayed implements IFunctionEvaluator, ICreatePatternMatcher {

	public SetDelayed() {
	}

	public IExpr evaluate(final IAST functionList) {
		// try {
		if (functionList.size() == 3) {
			final IExpr leftHandSide = functionList.get(1);
			final IExpr rightHandSide = functionList.get(2);
			if (rightHandSide.isAST("Condition", 3)) {
				createPatternMatcher(leftHandSide, ((IAST) rightHandSide)
						.get(1), ((IAST) rightHandSide).get(2));
			} else {
				createPatternMatcher(leftHandSide, rightHandSide, null);
			}
			return F.Null;
		}
		// } catch (final RuleCreationError err) {
		// // TODO print message to engine's fError stream
		// err.printStackTrace();
		// }
		return F.Null;
	}

	public Object[] createPatternMatcher(IExpr leftHandSide,
			IExpr rightHandSide, IExpr condition) throws RuleCreationError {
		final Object[] result = new Object[2];
		final EvalEngine engine = EvalEngine.get();
//		HeapContext.enter();
//		try {
			try {
				if (leftHandSide instanceof IAST) {
					final IAST temp = engine
							.evalSetAttributes((IAST) leftHandSide);
					if (temp != null) {
						leftHandSide = temp;
					}
				}
			} catch (final ReturnException e) {
			}

			result[0] = null; // IPatternMatcher
//			rightHandSide = rightHandSide.copy();
			result[1] = rightHandSide;
			if (leftHandSide instanceof ISymbol) {
				final ISymbol lhsSymbol = (ISymbol) leftHandSide;
				if (lhsSymbol.hasLocalVariableStack()) {
					lhsSymbol.set(rightHandSide);
					return result;
				} else {
					condition = condition == null ? null : condition;//.copy();
					result[0] = lhsSymbol.putDownRule(F.SetDelayed, true,
							leftHandSide, rightHandSide, condition);
					return result;
				}
			}

			if (leftHandSide instanceof IAST) {
				final ISymbol lhsSymbol = ((IAST) leftHandSide).topHead();
//				leftHandSide = leftHandSide.copy();
				condition = condition == null ? null : condition;//.copy();
				result[0] = lhsSymbol.putDownRule(F.SetDelayed, false,
						leftHandSide, rightHandSide, condition);
				return result;
			}
//		} finally {
//			HeapContext.exit();
//		}
		throw new RuleCreationError(leftHandSide);
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
