package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * 
 */
public class Discriminant extends AbstractFunctionEvaluator {

	public Discriminant() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 3);
		IExpr expr = F.evalExpandAll(ast.get(1));
		IExpr arg2 = ast.get(2);
		if (!arg2.isSymbol()) {
			// TODO allow multinomials
			return null;
		}
		IAST result = F.List();
		IAST resultListDiff = F.List();
		long degree = CoefficientList.univariateCoefficientList(expr, (ISymbol) arg2, result, resultListDiff);
		if (degree >= Short.MAX_VALUE) {
			throw new WrongArgumentType(ast, ast.get(1), 1, "Polynomial degree" + degree + " is larger than: " + " - " + Short.MAX_VALUE);
		}
		IExpr resultant = Resultant.resultant(result, resultListDiff);
		IExpr disc;
		degree *= (degree - 1);
		degree /= 2;
		IExpr factor = F.Power(result.get(result.size() - 1), F.CN1);
		if (degree % 2L != 0L) {
			factor = F.Times(F.CN1, factor);
		}
		if (resultant.isPlus()) {
			IAST res = (IAST) resultant;
			// distribute the factor over the sum
			res = res.map(Functors.replace1st(F.Times(F.Null, factor)));
			disc = F.eval(res);
		} else {
			disc = F.eval(F.Times(resultant, factor));
		}
		return disc;
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE);
		super.setUp(symbol);
	}
}