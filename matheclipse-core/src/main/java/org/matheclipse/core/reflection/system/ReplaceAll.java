package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class ReplaceAll implements IFunctionEvaluator {

	public ReplaceAll() {
	}

	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 3) {
			return null;
		}
		final IAST fromList = F.ast(null);
		final IAST toList   = F.ast(null);
		IAST rule;
		if (functionList.get(2).isAST(F.List)) {
			for (final IExpr expr : (IAST) functionList.get(2)) {
				checkCanceled();
				if (expr.isAST(F.Rule, 3)) {
					rule = (IAST) expr;
					fromList.add(rule.get(1));
					toList.add(rule.get(2));
				}
			}
		} else {
			if (functionList.get(2).isAST(F.Rule, 3)) {
				rule = (IAST) functionList.get(2);
				fromList.add(rule.get(1));
				toList.add(rule.get(2));
			}
		}
		if (fromList.size() > 1) {
			final IExpr result = (IExpr) AST.COPY.replaceAll(functionList.get(1), fromList, toList);
			return (result == null) ? functionList.get(1) : result;
		}
		return functionList.get(1);
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}
}
