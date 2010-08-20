package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Rule;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.Symbol;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Rule extends AbstractFunctionEvaluator {

	public Rule() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() == 3) {
			IExpr a0 = ast.get(1);
			IExpr arg2 = EvalEngine.evalNull(ast.get(2));
			if (a0 instanceof Symbol) {
				if (arg2 == null) {
					return null;
				}
				return Rule(a0, arg2);
			}

//			if (a0 instanceof AST) {
				// TODO check this part
//				final IExpr result = a0;// ((AST) a0).evaluateArgAttr(session);

//				if (result != null) {
//					a0 = result;

//					if (arg2 == null) {
//						arg2 = ast.get(2);
//					}
//				} else {
//					if (arg2 == null) {
//						return null;
//					}
//				}
//
//				return Rule(a0, arg2);
//			}
		}

		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
