package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;

import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.interfaces.BiPredicate;

import edu.jas.arith.BigRational;

/**
 * Returns <code>True</code>, if the given expression is a polynoomial object
 * 
 */
public class PolynomialQ extends AbstractFunctionEvaluator implements BiPredicate<IExpr> {

	public PolynomialQ() {
	}

	/**
	 * Returns <code>True</code> if the given expression is a polynoomial
	 * object; <code>False</code> otherwise
	 */
	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 3) {
			throw new WrongNumberOfArguments(ast, 2, ast.size() - 1);
		}
		IAST list;
		if (ast.get(2).isList()) {
			list = (IAST) ast.get(2);
		} else {
			list = List(ast.get(2));
		}
		return F.bool(polynomialQ(ast.get(1), list));

	}

	// public static boolean isPolynomial(IExpr polnomialExpr, final IAST list) {
	// try {
	// polnomialExpr = F.eval(F.ExpandAll, polnomialExpr);
	// return polynomialQ(polnomialExpr, list);
	// } catch (final Exception e) {
	// // exception will be thrown if the expression is not a polynomial
	// }
	// return false;
	// }

	public static boolean polynomialQ(final IExpr polnomialExpr, final IAST variables) {
		try {
			IExpr expr = F.eval(F.ExpandAll, polnomialExpr);
			ASTRange r = new ASTRange(variables,1);
			JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList());
			return jas.expr2Poly(expr)!=null;
		} catch (final Exception e) {
			// exception will be thrown if the expression is not a polynomial
		}
		return false;
		// if (exprPoly instanceof IAST) {
		// final IAST ast = (IAST) exprPoly;
		// if (ast.isASTSizeGE(F.Plus, 2)) {
		// IExpr expr = ast.get(1);
		// if(!polynomialQ(expr, variables)) {
		// return false;
		// }
		// for (int i = 2; i < ast.size(); i++) {
		// checkCanceled();
		// expr = ast.get(i);
		// if(!polynomialQ(expr, variables)) {
		// return false;
		// }
		// }
		// return true;
		// } else if (ast.isASTSizeGE(F.Times, 2)) {
		// IExpr expr = ast.get(1);
		// if(!polynomialQ(expr, variables)) {
		// return false;
		// }
		// for (int i = 2; i < ast.size(); i++) {
		// checkCanceled();
		// expr = ast.get(i);
		// if(!polynomialQ(expr, variables)) {
		// return false;
		// }
		// }
		// return true;
		// } else if (ast.isAST(F.Power, 3)) {
		// final IExpr expr = ast.get(1);
		// for (int i = 0; i < variables.size(); i++) {
		// checkCanceled();
		// if (variables.get(i).equals(expr)) {
		// return true;
		// }
		// }
		// return true;
		// }
		// } else if (exprPoly instanceof ISymbol) {
		// for (int i = 0; i < variables.size(); i++) {
		// checkCanceled();
		// if (variables.get(i).equals(exprPoly)) {
		// return true;
		// }
		// }
		// }
		//
		// // check if this expression contains any variable
		// for (int i = 0; i < variables.size(); i++) {
		// checkCanceled();
		// if (!exprPoly.isFree(variables.get(i))) {
		// return false;
		// }
		// }
		//
		// return true;

	}

	@Override
	public void setUp(final ISymbol symbol) {
	}

	public boolean apply(final IExpr firstArg, final IExpr secondArg) {
		IAST list;
		if (secondArg.isList()) {
			list = (IAST) secondArg;
		} else {
			list = List(secondArg);
		}
		return polynomialQ(firstArg, list);
	}
}
