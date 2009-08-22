package org.matheclipse.core.convert.experimental;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class Poly2BigIntegerConverter {
	private Poly2BigIntegerConverter() {
	}

	public static BigInteger[] expr2Polynomial(final IExpr expr, final ISymbol variable) throws ArithmeticException,
			ClassCastException {
		BigInteger[] result = null;
		List<Term> list = new ArrayList<Term>();
		expr2TermList(expr, variable, list);
		Term term;
		int exponent = 0;
		for (int i = 0; i < list.size(); i++) {
			term = list.get(i);
			if (term.getExponent() > exponent) {
				exponent = term.getExponent();
			}
		}
		result = new BigInteger[exponent + 1];
		for (int i = 0; i < result.length; i++) {
			result[i] = BigInteger.ZERO;
		}
		for (int i = 0; i < list.size(); i++) {
			term = list.get(i);
			result[term.getExponent()] = term.getCoefficient();
		}
		return result;
	}

	/**
	 * Converts an expression into a list of terms. Throws different exceptions if
	 * the conversion is not possible:<br>
	 * 
	 * @param expr
	 *          an expression which should be converted
	 * 
	 * @throws ArithmeticException
	 *           if the exponent of a <code>Power</code> expression doesn't fit
	 *           into a Java <code>int</code>
	 * @throws ClassCastException
	 *           if the expression is an AST with an unsuitable head (i.e. no
	 *           <code>Plus, Times, Power</code> head)
	 */
	public static void expr2TermList(final IExpr expr, final ISymbol variable, List<Term> list) throws ArithmeticException,
			ClassCastException {
		if (expr instanceof IAST) {
			final IAST ast = (IAST) expr;
			if (ast.isASTSizeGE(F.Plus, 2)) {
				for (int i = 1; i < ast.size(); i++) {
					list.add(expr2term(ast.get(i), variable));
				}
				return;
			}

		} else {
			list.add(expr2term(expr, variable));
			return;
		}
		throw new ClassCastException("This expression conversion is not yet implemented: " + expr.toString());
	}

	public static Term expr2term(final IExpr expr, final ISymbol variable) throws ArithmeticException, ClassCastException {
		if (expr instanceof IAST) {
			final IAST ast = (IAST) expr;
			if (ast.isAST(F.Times, 3) && (ast.get(1) instanceof IInteger)) {
				IExpr arg2 = ast.get(2);
				if (arg2 instanceof IAST) {
					int exponent = getExponent((IAST)arg2, variable);
					if (exponent > 0) {
						return new Term(exponent, integer2BigInteger((IntegerSym) ast.get(1)));
					}
				} else if (arg2 instanceof ISymbol && variable.equals(arg2)) {
					return new Term(1, integer2BigInteger((IntegerSym) ast.get(1)));
				}
			} else {
				int exponent = getExponent(ast, variable);
				if (exponent > 0) {
					return new Term(exponent, BigInteger.ONE);
				}
			}
		} else if (expr instanceof ISymbol && variable.equals(expr)) {
			return new Term(1, BigInteger.ONE);
		} else if (expr instanceof IInteger) {
			return new Term(0, integer2BigInteger((IntegerSym) expr));
		}
		throw new ClassCastException("expr2term(): this expression conversion is not yet implemented: " + expr.toString());
	}

	private static int getExponent(final IAST ast, final ISymbol variable) {
		if (ast.isAST(F.Power, 3) && variable.equals(ast.get(1))) {
			return ((IInteger) ast.get(2)).toInt();
		}
		return -1;
	}

	public static BigInteger integer2BigInteger(final IntegerSym expr) {
		return new BigInteger(expr.getBigNumerator().toByteArray());
	}

	/**
	 * Converts a BigInteger polynomial expression into an internal MathEclipse
	 * expression.
	 * 
	 * @param polyArray
	 *          an array of polynomial coefficients which should be converted into
	 *          a MathEclipse expression
	 * @return the corresponding MathEclipse expression
	 */
	public static IExpr polynomial2Expr(final BigInteger[] polyArray, final ISymbol variable) {
		if (polyArray == null) {
			return null;
		}
		IAST plus = F.Plus();
		for (int i = 0; i < polyArray.length; i++) {
			if (!polyArray[i].equals(BigInteger.ZERO)) {
				plus.add(F.Times(F.integer(polyArray[i]), F.Power(variable, F.integer(i))));
			}
		}
		return plus;
	}

}
