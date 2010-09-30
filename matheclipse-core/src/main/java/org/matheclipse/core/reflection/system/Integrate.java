package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Integrate;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Times;

import java.util.ArrayList;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.RecursionLimitExceeded;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.generic.UnaryBind1st;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;

import edu.jas.application.Quotient;
import edu.jas.application.QuotientRing;
import edu.jas.arith.BigRational;
import edu.jas.integrate.ElementaryIntegration;
import edu.jas.integrate.QuotIntegral;
import edu.jas.poly.GenPolynomial;

/**
 * Integration of a function. See <a
 * href="http://en.wikipedia.org/wiki/Integral">Integral</a>
 */
public class Integrate extends AbstractFunctionEvaluator implements IConstantHeaders {
	String[] RULES = {
			"Integrate[y_ * x_,z_Symbol]:= y*Integrate[x,z] /; FreeQ[y,z]",
			"Integrate[y_,x_Symbol]:=y*x /; FreeQ[y,x]",
			"Integrate[x_,x_Symbol]:= x^2/2",
			"Integrate[x_^n_NumberQ, x_Symbol]:=x^(n+1)/(n+1) /; n!=(-1)",
			"Integrate[x_^(-1), x_Symbol]:=Log[x]",
			"Integrate[(a_+x_)^(-1), x_Symbol]:=Log[x+a] /; FreeQ[a,x]",
			// "Integrate[(x_+b_)^n_NumberQ,x_Symbol]:= (x+b)^(n+1)/(n+1) /; n!=(-1)&&FreeQ[a,x]",
			"Integrate[(a_.*x_+b_)^n_NumberQ,x_Symbol]:= (a*x+b)^(n+1)/(a*(n+1)) /; (n!=(-1))&&FreeQ[{a,b},x]",
			// "Integrate[E_^x_, x_Symbol]:=E^x",
			"Integrate[E_^(a_.*x_), x_Symbol]:=a^(-1)*E^(a*x) /; FreeQ[a,x]",
			"Integrate[x_ * E_^(a_.*x_), x_Symbol]:=a^(-2)*E^(a*x)*(a*x-1) /; FreeQ[a,x]",
			// "Integrate[x_ * E_^x_, x_Symbol]:=E^x*(x-1)",
			"Integrate[x_^n_IntegerQ * E_^(a_.*x_), x_Symbol]:=a^(-1)*x^n*E^(a*x)-n/a*Integrate[x^(n-1)*E^(a*x),x] /; Positive[n]&&FreeQ[a,x]",
			// "Integrate[x_^n_IntegerQ * E_^x_, x_Symbol]:=x^n*E^x-n*Integrate[x^(n-1)*E^x,x] /; Positive[n]",
			// "Integrate[Log[x_], x_Symbol]:=x*Log[x]-x",
			"Integrate[Log[a_.*x_], x_Symbol]:=Log[a*x]*x-x /; FreeQ[a,x]",
			"Integrate[Sinh[x_], x_Symbol]:=Cosh[x]",
			"Integrate[Cosh[x_], x_Symbol]:=Sinh[x]",
			"Integrate[ArcSinh[x_], x_Symbol]:=x*ArcSinh[x]-Sqrt[x^2+1]",
			"Integrate[ArcCosh[x_], x_Symbol]:=x*ArcCosh[x]-Sqrt[x^2-1]",
			"Integrate[ArcTanh[x_], x_Symbol]:=x*ArcTanh[x]+1/2*Log[1-x^2]",
			// "Integrate[Sin[x_], x_Symbol]:= -Cos[x]",
			// "Integrate[Sin[a_*x_], x_Symbol]:= -Cos[a*x]/a /; FreeQ[a,x]",
			// "Integrate[Sin[x_]^n_, x_Symbol]:= (n-1)/n*Integrate[Sin[x]^(n-2),x]-Sin[x]^(n-1)*Cos[x]/n /; Positive[n]",
			"Integrate[Sin[a_.*x_]^n_IntegerQ, x_Symbol]:= -Sin[a*x]^(n-1)*Cos[a*x]/(n*a)+(n-1)/n*Integrate[Sin[a*x]^(n-2),x]/;Positive[n]&&FreeQ[a,x]",
			"Integrate[Sin[a_.+b_.*x_],x_Symbol] := -Cos[a+b*x]/b /; FreeQ[{a,b},x]",
			"Integrate[Cos[x_], x_Symbol]:= Sin[x]",
			"Integrate[Cos[a_*x_], x_Symbol]:= Sin[a*x]/a /; FreeQ[a,x]",
			// "Integrate[Cos[x_]^n_IntegerQ, x_Symbol]:= Cos[x]^(n-1)*Sin[x]/n+(n-1)/n*Integrate[Cos[x]^(n-2),x] /; Positive[n]",
			"Integrate[Cos[a_.*x_]^n_IntegerQ, x_Symbol]:= Cos[a*x]^(n-1)*Sin[a*x]/(n*a)+(n-1)/n*Integrate[Cos[a*x]^(n-2),x] /; Positive[n]&&FreeQ[a,x]",
			"Integrate[Tan[x_], x_Symbol]:= -Log[Cos[x]]",
			"Integrate[Tan[a_*x_], x_Symbol]:= -Log[Cos[a*x]]/a /; FreeQ[a,x]",
			"Integrate[Tan[x_]^n_IntegerQ, x_Symbol]:= 1/(n-1)*Tan[x]^(n-1)-Integrate[Tan[x]^(n-2),x] /; Positive[n]",
			"Integrate[Tan[a_*x_]^n_IntegerQ, x_Symbol]:= 1/(a*(n-1))*Tan[a*x]^(n-1)-Integrate[Tan[a*x]^(n-2),x] /; Positive[n]&&FreeQ[a,x]",
			"Integrate[ArcSin[x_], x_Symbol]:=x*ArcSin[x]+Sqrt[1-x^2]",
			"Integrate[ArcSin[a_*x_], x_Symbol]:= x*ArcSin[a*x]+Sqrt[1-a^2*x^2]/a /; FreeQ[a,x]",
			"Integrate[ArcCos[x_], x_Symbol]:= x*ArcCos[x]-Sqrt[1-x^2]",
			"Integrate[ArcCos[a_*x_], x_Symbol]:= x*ArcCos[a*x]-Sqrt[1-a^2*x^2]/a  /; FreeQ[a,x]",
			"Integrate[ArcTan[x_], x_Symbol]:= x*ArcTan[x]-1/2*Log[1+x^2]",
			"Integrate[ArcTan[a_*x_], x_Symbol]:= x*ArcTan[a*x]-1/2*Log[1+a^2*x^2]/a  /; FreeQ[a,x]"

	};

	public Integrate() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 3) {
			return null;
		}
		// PatternMatcher matcher = new PatternMatcher((IExpr) lst.get(2));
		// if (FreeQ.freeQ(matcher, (IExpr) lst.get(1))) {
		// return C0;
		// }
		if (ast.get(1) instanceof INumber) {
			// Integrate[x_NumberQ,y_] -> x*y
			return Times(ast.get(1), ast.get(2));
		}
		if (ast.get(1).equals(ast.get(2))) {
			// Integrate[x_,x_] -> x^2 / 2
			return Times(F.C1D2, Power(ast.get(1), F.C2));
		}
		if (ast.get(1) instanceof IAST) {
			IExpr arg = F.evalExpandAll(ast.get(1));
			if (!ast.get(1).equals(arg)) {
				IAST clon = (IAST) ast.clone();
				clon.set(1, arg);
				return clon;
			}
			final IAST arg1 = (IAST) ast.get(1);
			final IExpr header = arg1.head();
			if (arg1.size() >= 3) {
				if (header == F.Plus) {
					// Integrate[a_+b_+...,x_] -> Integrate[a,x]+Integrate[b,x]+...
					return arg1.args().map(F.Plus(), new UnaryBind1st(F.Integrate(F.Null, ast.get(2))));
				} else if (header == F.Times || header == F.Power) {
					if (ast.get(2) instanceof ISymbol) {
						ISymbol symbol = (ISymbol) ast.get(2);
						IExpr[] parts = Apart.getFractionalParts(arg1);
						if (parts != null) {
							try {
								ArrayList<IExpr> varList = new ArrayList<IExpr>();
								varList.add(symbol);
								String[] varListStr = new String[1];
								varListStr[0] = symbol.toString();
								JASConvert<BigRational> jas = new JASConvert<BigRational>(varList);
								GenPolynomial<BigRational> numerator = jas.expr2Poly(parts[0]);
								GenPolynomial<BigRational> denominator = jas.expr2Poly(parts[1]);
								QuotientRing<BigRational> qfac = new QuotientRing<BigRational>(jas.getPolynomialRingFactory());
								Quotient<BigRational> q = new Quotient<BigRational>(qfac, numerator, denominator);
								ElementaryIntegration<BigRational> eIntegrator = new ElementaryIntegration<BigRational>(BigRational.ZERO);
								QuotIntegral<BigRational> integral = eIntegrator.integrate(q);
								// if (Config.SHOW_STACKTRACE) {
								// System.out.println("Result: " + integral);
								// }
								return jas.quotIntegral2Expr(integral);
							} catch (UnsupportedOperationException uoe) {
								// JASConvert#logIntegral2Expr() method throws this exception
								if (Config.DEBUG) {
									System.out.println("Integrate: UnsupportedOperationException in JASConvert");
								}
							} catch (RuntimeException re) {
								// in case the expression couldn't be converted to JAS format
								if (Config.DEBUG) {
									re.printStackTrace();
								}
							}
							if (arg1.isTimes()) {
								IExpr result = integratePolynomialByParts(arg1, symbol);
								if (result != null) {
									return result;
								}
							}
						}
					}
				}
			}

		}

		return null;
	}

	private IExpr integratePolynomialByParts(final IAST arg1, ISymbol symbol) {
		IAST fTimes = F.Times();
		IAST gTimes = F.Times();
		collectPolynomialTerms(arg1, symbol, fTimes, gTimes);
		IExpr f = fTimes;
		IExpr g = gTimes;
		if (fTimes.size() == 1) {
			return null;
		} else if (fTimes.size() == 2) {
			// OneIdentity
			f = fTimes.get(1);
		}
		if (gTimes.size() == 1) {
			return null;
		} else if (gTimes.size() == 2) {
			// OneIdentity
			g = gTimes.get(1);
		}
		return integrateByParts(f, g, symbol);
	}

	/**
	 * See <a href="http://en.wikipedia.org/wiki/Integration_by_parts">Wikipedia-
	 * Integration by parts</a>
	 * 
	 * @param f
	 * @param g
	 * @param symbol
	 * @return
	 */
	private static IExpr integrateByParts(IExpr f, IExpr g, ISymbol symbol) {
		EvalEngine engine = EvalEngine.get();
		int limit = engine.getRecursionLimit();
		try {
			if (limit <= 0) {
				// set recursion limit
				engine.setRecursionLimit(128);
			}
			IExpr fIntegrated = F.eval(F.Integrate(f, symbol));
			if (!FreeQ.freeQ(fIntegrated, Integrate)) {
				return null;
			}
			IExpr gDerived = F.eval(F.D(g, symbol));
			return F.eval(F.Plus(F.Times(fIntegrated, g), F.Times(F.CN1, F.Integrate(F.Times(fIntegrated, gDerived), symbol))));
		} catch (RecursionLimitExceeded rle) {
			engine.setRecursionLimit(limit);
		} finally {
			engine.setRecursionLimit(limit);
		}
		return null;
	}

	/**
	 * Collect all found polynomial terms into <code>fTimes</code> and the rest
	 * into <code>gTimes</code>.
	 * 
	 * @param timesAST
	 *          an AST representing a <code>Times[...]</code> expression.
	 * @param symbol
	 * @param fTimes
	 * @param gTimes
	 */
	private static void collectPolynomialTerms(final IAST timesAST, ISymbol symbol, IAST fTimes, IAST gTimes) {
		IExpr temp;
		for (int i = 1; i < timesAST.size(); i++) {
			temp = timesAST.get(i);
			if (FreeQ.freeQ(temp, symbol)) {
				fTimes.add(temp);
				continue;
			} else if (temp.equals(symbol)) {
				fTimes.add(temp);
				continue;
			} else if (PolynomialQ.polynomialQ(temp, List(symbol))) {
				fTimes.add(temp);
				continue;
			}
			gTimes.add(temp);
		}
	}

	@Override
	public String[] getRules() {
		return RULES;
	}

}