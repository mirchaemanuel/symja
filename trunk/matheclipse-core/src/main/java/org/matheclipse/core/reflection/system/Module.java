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

import com.google.common.base.Function;

public class Module extends AbstractFunctionEvaluator {
	public Module() {
	}

	/**
	 *
	 */
	@Override
	public IExpr evaluate(final IAST ast) {

		if ((ast.size() == 3) && (ast.get(1)).isList()) {
			IAST lst = (IAST) ast.get(1);
			IExpr arg2 = ast.get(2);
			final EvalEngine engine = EvalEngine.get();
			if (arg2.isAST(F.Condition, 3)) {
				return evalModuleCondition(lst, ((IAST) arg2).get(1), ((IAST) arg2).get(2), engine, null);
			}
			return evalModule(lst, arg2, engine);
		}

		return null;
	}

	/**
	 * <code>Module[{variablesList}, rhs ]</code>
	 * 
	 * @param variablesList
	 * @param rightHandSide
	 * @param engine
	 * @return
	 */
	private static IExpr evalModule(IAST variablesList, IExpr rightHandSide, final EvalEngine engine) {
		final int moduleCounter = engine.incModuleCounter();
		final String varAppend = "$" + moduleCounter;
		// final IAST lst = (IAST) ast.get(1);
		final java.util.Map<ISymbol, ISymbol> variables = new HashMap<ISymbol, ISymbol>();

		try {
			rememberVariables(variablesList, engine, varAppend, variables);
			IExpr result = rightHandSide.replaceAll(Functors.rules(variables));
			return engine.evaluate(result);
		} finally {
			removeVariables(engine, variables);
		}
	}

	/**
	 * <code>Module[{variablesList}, rhs /; condition]</code>
	 * 
	 * @param variablesList
	 * @param rightHandSide
	 * @param condition
	 * @param engine
	 * @param rules
	 * @return
	 */
	public static IExpr evalModuleCondition(IAST variablesList, IExpr rightHandSide, IExpr condition, final EvalEngine engine,
			Function<IExpr, IExpr> rules) {
		final int moduleCounter = engine.incModuleCounter();
		final String varAppend = "$" + moduleCounter;
		// final IAST lst = (IAST) ast.get(1);
		final java.util.Map<ISymbol, ISymbol> variables = new HashMap<ISymbol, ISymbol>();

		try {
			IAST substList = variablesList;
			if (rules != null) {
				substList = (IAST) variablesList.replaceAll(rules);
				substList = (substList == null) ? variablesList : substList;
			}
			rememberVariables(substList, engine, varAppend, variables);
			IExpr substCondition = condition;
			if (rules != null) {
				substCondition = (IAST) condition.replaceAll(rules);
				substCondition = (substCondition == null) ? condition : substCondition;
			}
			IExpr result = substCondition.replaceAll(Functors.rules(variables));
			if (engine.evaluate(result).equals(F.True)) {
				IExpr substRHS = rightHandSide;
				if (rules != null) {
					substRHS = (IAST) rightHandSide.replaceAll(rules);
					substRHS = (substRHS == null) ? rightHandSide : substRHS;
				}
				result = substRHS.replaceAll(Functors.rules(variables));
				return engine.evaluate(result);
			}
		} finally {
			removeVariables(engine, variables);
		}
		return null;
	}

	private static void removeVariables(final EvalEngine engine, final java.util.Map<ISymbol, ISymbol> variables) {
		// remove all module variables from eval engine
		Map<String, ISymbol> variableMap = engine.getVariableMap();
		for (ISymbol symbol : variables.values()) {
			variableMap.remove(symbol.toString());
		}
	}

	private static void rememberVariables(IAST variablesList, final EvalEngine engine, final String varAppend,
			final java.util.Map<ISymbol, ISymbol> variables) {
		ISymbol oldSymbol;
		ISymbol newSymbol;
		// remember which local variables we use:
		for (int i = 1; i < variablesList.size(); i++) {
			if (variablesList.get(i).isSymbol()) {
				oldSymbol = (ISymbol) variablesList.get(i);
				newSymbol = F.$s(oldSymbol.toString() + varAppend);
				variables.put(oldSymbol, newSymbol);
				newSymbol.pushLocalVariable();
			} else {
				if ((variablesList.get(i) instanceof IAST) && ((IAST) variablesList.get(i)).isAST(F.Set)
						&& (((IAST) variablesList.get(i)).size() == 3)) {
					final IAST setFun = (IAST) variablesList.get(i);
					if (setFun.get(1).isSymbol()) {
						oldSymbol = (ISymbol) setFun.get(1);
						newSymbol = F.$s(oldSymbol.toString() + varAppend);
						variables.put(oldSymbol, newSymbol);
						newSymbol.pushLocalVariable(engine.evaluate(setFun.get(2)));
					}
				}
			}
		}
	}

	@Override
	public void setUp(ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
