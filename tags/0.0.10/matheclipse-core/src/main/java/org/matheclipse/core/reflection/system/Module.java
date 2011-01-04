package org.matheclipse.core.reflection.system;

import java.util.HashMap;
import java.util.Map;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Module extends AbstractFunctionEvaluator {
	public Module() {
	}

	/**
	 * TODO works as Block[] at the moment
	 */
	@Override
	public IExpr evaluate(final IAST ast) {

		if ((ast.size() == 3) && (ast.get(1)).isList()) {
			final EvalEngine engine = EvalEngine.get();
			final int moduleCounter = engine.incModuleCounter();
			final String varAppend = "$" + moduleCounter;
			final IAST lst = (IAST) ast.get(1);
			final java.util.Map<ISymbol, ISymbol> variables = new HashMap<ISymbol, ISymbol>();
			ISymbol oldSymbol;
			ISymbol newSymbol;

			try {
				// remember which local variables we use:
				for (int i = 1; i < lst.size(); i++) {
					if (lst.get(i).isSymbol()) {
						oldSymbol = (ISymbol) lst.get(i);
						newSymbol = F.symbol(oldSymbol.toString() + varAppend);
						variables.put(oldSymbol, newSymbol);
						newSymbol.pushLocalVariable();
					} else {
						if ((lst.get(i) instanceof IAST) && ((IAST) lst.get(i)).isAST(F.Set) && (((IAST) lst.get(i)).size() == 3)) {
							final IAST setFun = (IAST) lst.get(i);
							if (setFun.get(1).isSymbol()) {
								oldSymbol = (ISymbol) setFun.get(1);
								newSymbol = F.symbol(oldSymbol.toString() + varAppend);
								variables.put(oldSymbol, newSymbol);
								newSymbol.pushLocalVariable(engine.evaluate(setFun.get(2)));
							}
						}
					}
				}
				IExpr result = ast.get(2).replaceAll(Functors.rules(variables));
				return engine.evaluate(result);
			} finally {
				// remove all module variables from eval engine
				Map<String, ISymbol> variableMap = engine.getVariableMap();
				for (ISymbol symbol : variables.values()) {
					variableMap.remove(symbol.toString());
				}
			}
		}

		return null;
	}

	@Override
	public void setUp(ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
