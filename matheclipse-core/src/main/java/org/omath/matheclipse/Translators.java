package org.omath.matheclipse;

import java.util.ArrayList;
import java.util.List;

import net.tqft.iterables.interfaces.Transformer;

import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.omath.interfaces.SyntaxException;
import org.omath.interfaces.expression.Expression;
import org.omath.interfaces.expression.ExpressionFactory;
import org.omath.interfaces.expression.IntegerExpression;
import org.omath.interfaces.expression.RawExpression;
import org.omath.interfaces.expression.RealExpression;
import org.omath.interfaces.expression.StringExpression;
import org.omath.interfaces.expression.SymbolExpression;
import org.omath.tungsten.expressions.AbstractFullFormExpression;
import org.omath.util.immutables.ImmutableList;

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
						// if the input is definitely a
						// AbstractFullFormExpression it could be
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

	/**
	 * Convert an OMath Expression into a Matheclipse IExpr
	 * 
	 * @param expression
	 *            the OMath expression
	 * @return the Matheclipse IExpr or <code>null</code> if no conversion is
	 *         possible
	 */
	public static IExpr expression2IExpr(final Expression expression) {
		if (expression instanceof AbstractFullFormExpression) {
			AbstractFullFormExpression affe = (AbstractFullFormExpression) expression;
			ImmutableList<Expression> leaves = affe.leaves();
			IAST ast = F.ast(expression2IExpr(affe.head()));
			for (int i = 0; i < leaves.size(); i++) {
				ast.add(expression2IExpr(leaves.get(i)));
			}
			return ast;
		}
		if (expression instanceof RawExpression) {
			if (expression instanceof SymbolExpression) {
				return F.symbol(((SymbolExpression) expression).getName());
			}
			if (expression instanceof IntegerExpression) {
				return F.integer(((IntegerExpression) expression)
						.bigIntegerValue());
			}
			if (expression instanceof RealExpression) {
				// TODO Is this correct?
				return F.num(((RealExpression) expression).valueAsBigDecimal()
						.doubleValue());
			}
			if (expression instanceof StringExpression) {
				return F.stringx(expression.toString());
			}
		}
		return null;
	}

	/**
	 * Convert a Matheclipse IExpr into an OMath Expression
	 * 
	 * @param expr
	 *            the MathEclipse expression
	 * @return the OMath Expression or <code>null</code> if no conversion is
	 *         possible
	 */
	public static Expression iexpr2Expression(final ExpressionFactory factory,
			final IExpr expr) {
		if (expr instanceof IAST) {
			IAST ast = (IAST) expr;
			Expression head = iexpr2Expression(factory, expr.head());
			List<Expression> leaves = new ArrayList<Expression>(ast.size() - 1);
			for (int i = 1; i < ast.size(); i++) {
				leaves.add(iexpr2Expression(factory, ast.get(i)));
			}
			return factory.getExpressionInstance(head, leaves);
		}
		if (expr instanceof ISymbol) {
			return factory.getSymbolExpressionInstance(expr.toString());
		}
		if (expr instanceof IStringX) {
			return factory.getStringExpressionInstance(expr.toString());
		}
		if (expr instanceof IInteger) {
			return factory.getIntegerExpressionInstance(((IInteger) expr)
					.getBigNumerator().toJavaBigInteger());
		}
		return null;
	}
}
