package org.matheclipse.core.eval.interfaces;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcherAndInvoker;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.ast.ASTNode;

/**
 * 
 */
public abstract class AbstractFunctionEvaluator implements IFunctionEvaluator {

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	/**
	 * Get the predefined rules for this function symbol. If no rules are
	 * available return <code>null</code>
	 * 
	 * @return
	 */
	public String[] getRules() {
		return null;
	}

	/**
	 * Evaluate built-in rules and define Attributes for a function.
	 * 
	 */
	public void setUp(final ISymbol symbol) throws SyntaxError {
		String[] rules;
		if ((rules = getRules()) != null) {
			final Parser parser = new Parser();
			final EvalEngine engine = EvalEngine.get();
			// if (session != null) {
			// parser.setFactory(ExpressionFactory.get());
			// }
			if (Config.DEBUG) {
				try {
					setUpRules(rules, parser, engine);
				} catch (final Throwable th) {
					th.printStackTrace();
				}
			} else {
				setUpRules(rules, parser, engine);
			}
		}
	}

	private void setUpRules(final String[] rules, final Parser parser, final EvalEngine engine) {
		for (int i = 0; i < rules.length; i++) {
			final ASTNode parsedAST = parser.parse(rules[i]);
			final IExpr obj = AST2Expr.CONST.convert(parsedAST);
			// engine.init();
			engine.evaluate(obj);
		}

	}

	abstract public IExpr evaluate(final IAST functionList);

	/**
	 * Create a rule which invokes the method name in this class instance.
	 * 
	 * @param symbol
	 * @param patternString
	 * @param methodName
	 */
	public void createRuleFromMethod(ISymbol symbol, String patternString, String methodName) {
		PatternMatcherAndInvoker pm = new PatternMatcherAndInvoker(patternString, this, methodName);
		symbol.putDownRule(pm);
	}

}