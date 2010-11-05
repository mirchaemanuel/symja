package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Slot;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Function implements IFunctionEvaluator {

	public Function() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.head() instanceof IAST) {
			try {
				final IAST function = (IAST) functionList.head();
				if (function.size() == 2) {
					return replaceSlots(function.get(1), functionList);
				} else if (function.size() == 3) {
					IAST symbolSlots = null;
					if (function.get(1).isList()) {
						symbolSlots = (IAST) function.get(1);
					} else {
						symbolSlots = F.ast(null);
						symbolSlots.add(function.get(1));
					}
					final IExpr result = AST.COPY.replaceAll(function.get(2), symbolSlots, functionList, 1);
					return (result == null) ? function.get(2) : result;
				}
			} catch (final IndexOutOfBoundsException e) {
				// TODO add message to evaluation problemReporter
			}
		}
		return null;
	}

	public static IExpr replaceSlots(final IExpr expr, final IAST list) {
		final IAST intSlots = F.ast(null);
		for (int i = 1; i < list.size(); i++) {
			intSlots.add(Slot(i));
		}
		final IExpr result = AST.COPY.replaceAll(expr, intSlots, list, 1);
		return (result == null) ? expr : result;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
