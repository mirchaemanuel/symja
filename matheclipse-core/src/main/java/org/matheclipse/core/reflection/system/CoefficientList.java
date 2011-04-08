package org.matheclipse.core.reflection.system;

import java.util.List;

import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ExprRingFactory;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import edu.jas.arith.BigRational;
import edu.jas.poly.ExpVectorLong;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.PolyUtil;

/**
 * 
 */
public class CoefficientList extends AbstractFunctionEvaluator {

	public CoefficientList() {
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
		long degree = univariateCoefficientList(expr, (ISymbol) arg2, result);
		if (degree >= Short.MAX_VALUE) {
			throw new WrongArgumentType(ast, ast.get(1), 1, "Polynomial degree" + degree + " is larger than: " + " - " + Short.MAX_VALUE);
		}
		return result;
	}

	/**
	 * Get the coefficient list of a univariate polynomial
	 * 
	 * @param polynomial
	 * @param variable
	 * @param resultList
	 *          the coefficient list of the given univariate polynomial
	 * @return the degree of the univariate polynomial; if
	 *         <code>degree >= Short.MAX_VALUE</code>, the result list will be
	 *         empty.
	 */
	public static long univariateCoefficientList(IExpr polynomial, final ISymbol variable, List<IExpr> resultList) {
		JASConvert<IExpr> jas = new JASConvert<IExpr>(variable, new ExprRingFactory());
		GenPolynomial<IExpr> polyExpr = jas.expr2Poly(polynomial);
		long degree = polyExpr.degree();
		if (degree >= Short.MAX_VALUE) {
			return degree;
		}
		for (int i = 0; i <= degree; i++) {
			IExpr temp = polyExpr.coefficient(new ExpVectorLong(1, 0, i));
			resultList.add(temp);
		}
		return degree;
	}

	/**
	 * 
	 * @param polynomial
	 * @param variable
	 * @param resultList
	 *          the coefficient list of the given univariate polynomial
	 * @param resultListDiff
	 *          the coefficient list of the derivative of the given univariate
	 *          polynomial
	 * @return the degree of the univariate polynomial; if
	 *         <code>degree >= Short.MAX_VALUE</code>, the result list will be
	 *         empty.
	 */
	public static long univariateCoefficientList(IExpr polynomial, ISymbol variable, List<IExpr> resultList,
			List<IExpr> resultListDiff) {
		JASConvert<IExpr> jas = new JASConvert<IExpr>(variable, new ExprRingFactory());
		GenPolynomial<IExpr> polyExpr = jas.expr2Poly(polynomial);
		// derivative of the given polynomial
		GenPolynomial<IExpr> polyExprDiff = PolyUtil.<IExpr> baseDeriviative(polyExpr);

		long degree = polyExpr.degree();
		if (degree >= Short.MAX_VALUE) {
			return degree;
		}
		for (int i = 0; i <= degree; i++) {
			IExpr temp = polyExpr.coefficient(new ExpVectorLong(1, 0, i));
			resultList.add(temp);
		}

		long degreeDiff = polyExprDiff.degree();
		for (int i = 0; i <= degreeDiff; i++) {
			IExpr temp = polyExprDiff.coefficient(new ExpVectorLong(1, 0, i));
			resultListDiff.add(F.eval(temp));
		}
		return degree;
	}
}