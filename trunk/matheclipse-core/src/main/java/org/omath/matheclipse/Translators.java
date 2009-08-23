package org.omath.matheclipse;

import net.tqft.iterables.interfaces.Transformer;

import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.omath.interfaces.SyntaxException;
import org.omath.interfaces.expression.Expression;
import org.omath.interfaces.expression.ExpressionFactory;

public class Translators {
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(Translators.class);

	private final ExpressionFactory factory;

	private EvalUtilities util;

	public Translators(ExpressionFactory factory) {
		// setup the evaluation engine (and bind to current thread)
		EvalEngine engine = new EvalEngine();
		EvalEngine.set(engine);
		engine.setSessionID("Translators");
		engine.setRecursionLimit(256);
		engine.setIterationLimit(1024 * 1024);
		this.util = new EvalUtilities(engine, false);
		// initialize most important functions
		F.initSymbols();

		this.factory = factory;

		factory.registerExpressionTranslator(IExpr.class,
				new Transformer<Expression, IExpr>() {
					public IExpr evaluate(Expression expression) {
						Parser parser = new Parser();
						ASTNode node = parser
								.parse(expression.fullFormString());
						// if the node is definitely a function it could be
						// safely cast to an IAST in the next step
						IExpr inExpr = AST2Expr.CONST.convert(node);
						IExpr result = util.evaluate(inExpr);
						if ((result != null) && !result.equals(F.Null)) {
							// got a result
						}
						return result;
					}
				});

		factory.registerObjectTranslator(IExpr.class,
				new Transformer<IExpr, Expression>() {
					public Expression evaluate(IExpr s) {
						try {
							return Translators.this.factory.parse(s
									.fullFormString());
						} catch (SyntaxException e) {
							log
									.warn("Receivable an unparseable FullForm from matheclipse: "
											+ s.fullFormString());
							return null;
						}
					}
				});
	}

}
