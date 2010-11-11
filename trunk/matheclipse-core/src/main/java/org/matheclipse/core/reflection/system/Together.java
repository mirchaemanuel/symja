package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.VisitorExpr;
import org.matheclipse.parser.client.SyntaxError;

import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;

/**
 * 
 */
public class Together extends AbstractFunctionEvaluator {
	class TogetherVisitor extends VisitorExpr {

		public TogetherVisitor() {
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
			if (astTemp.isPlus()) {
				return visitPlus(astTemp);
			}

			return astTemp;
		}

		private IExpr visitPlus(IAST plusAST) {
			if (plusAST.size() <= 1) {
				return plusAST;
			}
			IAST ni;
			IExpr temp;
			IAST numer = F.ast(F.Plus, plusAST.size(), false);
			IAST denom = F.ast(F.Times, plusAST.size(), false);
			for (int i = 1; i < plusAST.size(); i++) {
				numer.add(i, F.eval(F.Numerator(plusAST.get(i))));
				denom.add(i, F.eval(F.Denominator(plusAST.get(i))));
			}

			for (int i = 1; i < plusAST.size(); i++) {
				ni = F.Times(numer.get(i));
				for (int j = 1; j < plusAST.size(); j++) {
					if (i == j) {
						continue;
					}
					temp = denom.get(j);
					if (!temp.equals(F.C1)) {
						ni.add(temp);
					}
				}
				numer.set(i, ni);
			}
			int i = 1;
			while (denom.size() > i) {
				if (denom.get(i).equals(F.C1)) {
					denom.remove(i);
					continue;
				}
				i++;
			}
			IExpr exprNumerator = F.evalExpandAll(numer);
			if (denom.size() == 1) {
				return exprNumerator;
			}
			IExpr exprDenominator = F.evalExpandAll(denom);
			if (!exprDenominator.equals(F.C1)) {
				IExpr[] result = normalize(exprNumerator, exprDenominator);
				if (result != null) {
					return F.Times(result[0], F.Power(result[1], F.CN1));
				}
			}
			return F.Times(exprNumerator, F.Power(exprDenominator, F.CN1));
		}
	}

	public Together() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 2);
		if (ast.get(1).isAST()) {
			IExpr expr = ast.get(1).accept(new TogetherVisitor());
			if (expr != null) {
				return expr;
			}
		}
		return ast.get(1);
	}

	/**
	 * Calculate the result array
	 * <code>[ e1.divide(gcd(e1,e2)), e2.divide(gcd(e1,e2)) ]</code> if the given
	 * expressions e1 and e2 are univariate polynomials with equal variable name.
	 * 
	 * 
	 * @param e1
	 * @param e2
	 * @return <code>null</code> if the expressions couldn't be normalized
	 */
	public IExpr[] normalize(IExpr e1, IExpr e2) {

		try {
			ExprVariables eVar = new ExprVariables(e1);
			eVar.addVarList(e2);
			if (!eVar.isSize(1)) {
				// gcd only possible for univariate polynomials
				return null;
			}

			ASTRange r = new ASTRange(eVar.getVarList(), 1);
			JASConvert<BigRational> jas = new JASConvert<BigRational>(r.toList());
			GenPolynomial<BigRational> p1 = jas.expr2Poly(e1);
			GenPolynomial<BigRational> p2 = jas.expr2Poly(e2);
			GenPolynomial<BigRational> gcd = p1.gcd(p2);
			IExpr[] result = new IExpr[2];
			if (gcd.isONE()) {
				result[0] = jas.rationalPoly2Expr(p1);
				result[1] = jas.rationalPoly2Expr(p2);
			} else {
				result[0] = jas.rationalPoly2Expr(p1.divide(gcd));
				result[1] = jas.rationalPoly2Expr(p2.divide(gcd));
			}
			return result;
		} catch (Exception e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE);
		super.setUp(symbol);
	}
}