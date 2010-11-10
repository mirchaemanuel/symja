package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Power;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.VisitorExpr;
import org.matheclipse.parser.client.SyntaxError;

/**
 * Try to simplify a given expression
 */
public class PowerExpand extends AbstractFunctionEvaluator {

	class PowerExpandVisitor extends VisitorExpr {

		public PowerExpandVisitor() {
			super();
		}

		@Override
		public IExpr visit(IAST ast) {
			IExpr temp = visitAST(ast);
			IAST astTemp = ast;
			if (temp != null) {
				if (temp.isAST()) {
					astTemp = (IAST) temp;
				} else {
					return temp;
				}
			}
			if (astTemp.isPower()) {
				if (astTemp.get(1).isPower()) {
					IAST arg1 = (IAST) astTemp.get(1);
					return F.Power(arg1.get(1), F.Times(arg1.get(2), astTemp.get(2)));
				}
				if (astTemp.get(1).isTimes()) {
					IAST arg1 = (IAST) astTemp.get(1);
					return arg1.map(Functors.replace1st(Power(F.Null, astTemp.get(2))));
				}
			}
			if (astTemp.isLog()) {
				if (astTemp.get(1).isPower()) {
					IAST arg1 = (IAST) astTemp.get(1);
					// Log[a^b] => b*Log[a]
					return F.Times(arg1.get(2), F.Log(arg1.get(1)));
				}
			}

			return astTemp;
		}

	}

	public PowerExpand() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 2);
		if (ast.get(1).isAST()) {
			IExpr expr = ast.get(1).accept(new PowerExpandVisitor());
			if (expr != null) {
				return expr;
			}
		}
		return ast.get(1);
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE);
		super.setUp(symbol);
	}
}
