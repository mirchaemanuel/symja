package org.matheclipse.core.integrate.rubi;

import static org.matheclipse.core.expression.F.*;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Pattern;
import org.matheclipse.core.expression.Symbol;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * UtilityFunctions from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 * 
 * TODO the functions are only placeholders at the moment.
 * 
 */
public class UtilityFunctions {
	private static String PACKAGE_NAME = "org.matheclipse.core.integrate.rubi";

	private static String CLASS_NAME = "UtilityFunctions";

	/**
	 * Convert to Integrate[]
	 * 
	 * @param a0
	 * @param a1
	 * @return
	 */
	public static IAST Int(final IExpr a0, final IExpr a1) {
		return binary(Integrate, a0, a1);
	}

	public static ISymbol sym(String name, IAST ruleList) {
		ISymbol sym = symbol(name);
		EvalEngine.get().addRules(ruleList);
		return sym;
	}

	public static IAST quad(final IExpr head, final IExpr a0, final IExpr a1, final IExpr a2, final IExpr a3) {
		final IAST ast = ast(head);
		ast.add(a0);
		ast.add(a1);
		ast.add(a2);
		ast.add(a3);
		return ast;
	}

	public static IAST And(final IExpr a0, final IExpr a1) {
		return binary(And, a0, a1);
	}

	public static IAST ArcCoth(final IExpr a) {
		// TODO fix this
		return unary(symbol("ArcCoth"), a);
	}

	public static IAST Or(final IExpr a0, final IExpr a1) {
		return binary(Or, a0, a1);
	}

	public static IAST Block(final IExpr a0, final IExpr a1) {
		return binary(symbol("Block"), a0, a1);
	}

	public static IAST Cancel(final IExpr a) {
		// TODO fix this
		return unary(symbol("Cancel"), a);
	}

	public static IAST Coefficient(final IExpr a0, final IExpr a1, final IExpr a2) {
		return ternary(symbol("Coefficient"), a0, a1, a2);
	}

	public static IAST CosIntegral(final IExpr a) {
		// TODO fix this
		return unary(symbol("CosIntegral"), a);
	}

	static IAST NUMERIC_FACTOR = List(SetDelayed(NumericFactor(pattern("u")), If(NumberQ(symbol("u")), If(ZeroQ(Im(symbol("u"))),
			symbol("u"), If(ZeroQ(Re(symbol("u"))), Im(symbol("u")), C1)), If(PowerQ(symbol("u")), If(And(
			RationalQ(Part(symbol("u"), C1)), FractionQ(Part(symbol("u"), C2))), If(Greater(Part(symbol("u"), C2), C0), Times(C1, Power(
			Denominator(Part(symbol("u"), C1)), CN1)), Times(C1, Power(Denominator(Times(C1, Power(Part(symbol("u"), C1), CN1))), CN1))),
			C1), If(ProductQ(symbol("u")), Times(NumericFactor(First(symbol("u"))), NumericFactor(Rest(symbol("u")))), C1)))));

	static ISymbol NumericFactor = sym("NumericFactor", NUMERIC_FACTOR);

	public static IAST NumericFactor(final IExpr a0) {
		return unary(NumericFactor, a0);
	}

	public static IAST PowerQ(final IExpr a0) {
		return unary(symbol("PowerQ"), a0);
	}

	public static IAST ProductQ(final IExpr a0) {
		return unary(symbol("ProductQ"), a0);
	}

	public static IAST SumQ(final IExpr a0) {
		return unary(symbol("SumQ"), a0);
	}

	public static IAST Re(final IExpr a0) {
		return unary(symbol("Re"), a0);
	}

	public static IAST Im(final IExpr a0) {
		return unary(symbol("Im"), a0);
	}

	public static IAST First(final IExpr a0) {
		return unary(symbol("First"), a0);
	}

	public static IAST Rest(final IExpr a0) {
		return unary(symbol("Rest"), a0);
	}

	public static IAST Part(final IExpr a0, final IExpr a1) {
		return binary(symbol("Part"), a0, a1);
	}

	static IAST DIST_RULES = List(List(SetDelayed(Dist(C1, pattern("v")), symbol("v")), SetDelayed(Dist(pattern("u"), pattern("v")),
			Condition(Times(CN1, Dist(Times(CN1, symbol("u")), symbol("v"))), Less(NumericFactor(symbol("u")), C0))), SetDelayed(Plus(
			Dist(pattern("u"), pattern("v")), Dist(pattern("w"), pattern("v"))), If(ZeroQ(Plus(symbol("u"), symbol("w"))), C0, Dist(Plus(
			symbol("u"), symbol("w")), symbol("v")))), SetDelayed(Plus(Dist(pattern("u"), pattern("v")), Times(CN1, Dist(pattern("w"),
			pattern("v")))), If(ZeroQ(Plus(symbol("u"), Times(CN1, symbol("w")))), C0, Dist(Plus(symbol("u"), Times(CN1, symbol("w"))),
			symbol("v")))), SetDelayed(Times(pattern("w"), Dist(pattern("u"), pattern("v"))), Condition(Dist(Times(symbol("w"),
			symbol("u")), symbol("v")), UnsameQ(symbol("w"), CN1))), SetDelayed(Dist(pattern("u"), Dist(pattern("v"), pattern("w"))),
			Dist(Times(symbol("u"), symbol("v")), symbol("w"))), SetDelayed(Dist(pattern("u"), pattern("v")), Condition(Map(
			Function(Dist(symbol("u"), Slot1)), symbol("v")), SumQ(symbol("v")))), SetDelayed(Dist(pattern("u"), pattern("v")),
			Condition(Times(symbol("u"), symbol("v")),
					Or(FreeQ(symbol("v"), symbol("Int")), UnsameQ(symbol("ShowSteps"), symbol("True"))))), SetDelayed(Dist(pattern("u"),
			Times(pattern("v"), pattern("w"))), Condition(Dist(Times(symbol("u"), symbol("v")), symbol("w")), FreeQ(symbol("v"),
			symbol("Int"))))));

	static ISymbol Dist = sym("Dist", DIST_RULES);

	public static IAST Dist(final IExpr a0, final IExpr a1) {
		return binary(Dist, a0, a1);
	}

	public static IAST ExpnExpand(final IExpr a0, final IExpr a1) {
		return binary(symbol("ExpnExpand"), a0, a1);
	}

	public static IAST EllipticE(final IExpr a0, final IExpr a1) {
		return binary(symbol("EllipticE"), a0, a1);
	}

	public static IAST EllipticF(final IExpr a0, final IExpr a1) {
		return binary(symbol("EllipticF"), a0, a1);
	}

	public static IAST Exponent(final IExpr a0, final IExpr a1) {
		return binary(symbol("Exponent"), a0, a1);
	}

	public static IAST EvenQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("EvenQ"), a);
	}

	public static IAST FractionQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("FractionQ"), a);
	}

	public static IAST FractionOrNegativeQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("FractionOrNegativeQ"), a);
	}

	public static IAST FresnelC(final IExpr a) {
		// TODO fix this
		return unary(symbol("FresnelC"), a);
	}

	public static IAST FresnelS(final IExpr a) {
		// TODO fix this
		return unary(symbol("FresnelS"), a);
	}

	public static IAST FunctionOfQ(final IExpr a0, final IExpr a1, final IExpr a2, final IExpr a3) {
		return quad(symbol("FunctionOfQ"), a0, a1, a2, a3);
	}

	public static IAST FunctionOfTrig(final IExpr a0, final IExpr a1) {
		return binary(symbol("FunctionOfTrig"), a0, a1);
	}

	public static IAST FunctionOfTrigQ(final IExpr a0, final IExpr a1, final IExpr a2) {
		return ternary(symbol("FunctionOfTrigQ"), a0, a1, a2);
	}

	public static IAST HalfIntegerQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("HalfIntegerQ"), a);
	}

	public static IAST IndependentQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("IndependentQ"), a0, a1);
	}

	public static IAST IntegerQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("IntegerQ"), a);
	}

	public static IAST OddQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("OddQ"), a);
	}

	public static IAST MatchQ(final IExpr a0, final IExpr a1) {
		// TODO fix this
		return binary(symbol("MatchQ"), a0, a1);
	}

	public static IAST Module(final IExpr a0, final IExpr a1) {
		return binary(symbol("Module"), a0, a1);
	}

	public static IAST NegQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("NegQ"), a);
	}

	public static IAST NonzeroQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("NonzeroQ"), a);
	}

	public static IAST Not(final IExpr a) {
		// TODO fix this
		return unary(symbol("NegQ"), a);
	}

	public static IAST NotFalseQ(final IExpr u) {
		return binary(symbol("UnsameQ"), u, False);
	}

	public static IAST PosQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("PosQ"), a);
	}

	public static IAST PolynomialQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("PolynomialQ"), a0, a1);
	}

	public static IAST PositiveQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("PositiveQ"), a);
	}

	public static IAST RationalQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("RationalQ"), a);
	}

	public static IExpr Regularize(final IExpr u, final IExpr x) {
		// TODO fix this
		return u;
	}

	public static IAST Rt(final IExpr a0, final IExpr a1) {
		return binary(symbol("Rt"), a0, a1);
	}

	public static IAST Rt(final IExpr a0, final IExpr a1, final IExpr a2) {
		return ternary(symbol("Rt"), a0, a1, a2);
	}

	public static IAST SameQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("SameQ"), a0, a1);
	}

	public static IAST UnsameQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("UnsameQ"), a0, a1);
	}

	public static IAST SinIntegral(final IExpr a) {
		// TODO fix this
		return unary(symbol("SinIntegral"), a);
	}

	static IAST SUBST_RULES = List(SetDelayed(Subst(pattern("u"), pattern("v"), pattern("w")), Condition(If(SameQ(symbol("u"),
			symbol("v")), symbol("w"), If(AtomQ(symbol("u")), symbol("u"), If(PowerQ(symbol("u")), If(And(And(PowerQ(symbol("v")), SameQ(
			Part(symbol("u"), C1), Part(symbol("v"), C1))), SumQ(Part(symbol("u"), C2))), Times(Subst(Power(Part(symbol("u"), C1),
			First(Part(symbol("u"), C2))), symbol("v"), symbol("w")), Subst(Power(Part(symbol("u"), C1), Rest(Part(symbol("u"), C2))),
			symbol("v"), symbol("w"))), Power(Subst(Part(symbol("u"), C1), symbol("v"), symbol("w")), Subst(Part(symbol("u"), C2),
			symbol("v"), symbol("w")))), If(And(SubstQ(symbol("u")), Or(SameQ(Part(symbol("u"), C2), symbol("v")), FreeQ(Part(
			symbol("u"), C1), symbol("v")))), Subst(Part(symbol("u"), C1), Part(symbol("u"), C2), Subst(Part(symbol("u"), C3),
			symbol("v"), symbol("w"))), Map(Function(Subst(Slot1, symbol("v"), symbol("w"))), symbol("u")))))), Or(Or(AtomQ(symbol("u")),
			And(SubstQ(symbol("u")), Or(SameQ(Part(symbol("u"), C2), symbol("v")), FreeQ(Part(symbol("u"), C1), symbol("v"))))), Not(Or(
			And(CalculusQ(symbol("u")), Not(FreeQ(symbol("v"), Part(symbol("u"), C2)))), MemberQ(List(symbol("Pattern"), symbol("Defer"),
					symbol("Hold"), symbol("HoldForm")), Head(symbol("u")))))))));

	static ISymbol Subst = sym("Subst", SUBST_RULES);

	public static IAST AtomQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("AtomQ"), a);
	}

	public static IAST CalculusQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("CalculusQ"), a);
	}

	public static IAST SubstQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("SubstQ"), a);
	}

	public static IAST Head(final IExpr a) {
		// TODO fix this
		return unary(symbol("Head"), a);
	}

	public static IAST Map(final IExpr a0, final IExpr a1) {
		return binary(symbol("Map"), a0, a1);
	}

	/**
	 * Subst[u,v,w] returns u with all nondummy occurences of v replaced by w
	 * 
	 * @param a0
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static IExpr Subst(final IExpr u, final IExpr v, final IExpr w) {
		return ternary(Subst, u, v, w);
	}

	/**
	 * u is a function v. SubstFor[v,u,x] returns f (x).
	 * 
	 * <pre>
	 * SubstFor[v_,u_,x_] :=
	 *   If[AtomQ[v],
	 *     Subst[u,v,x],
	 *   If[PowerQ[v] && FreeQ[v[[2]],x] (* && NonzeroQ[v[[2]]+1] *),
	 *     SubstForPower[u,v[[1]],v[[2]],x],
	 * 
	 *   If[SinQ[v],
	 *     SubstForTrig[u,x,Sqrt[1-x^2],v[[1]],x],
	 *   If[CosQ[v],
	 *     SubstForTrig[u,Sqrt[1-x^2],x,v[[1]],x],
	 *   If[TanQ[v],
	 *     SubstForTrig[u,x/Sqrt[1+x^2],1/Sqrt[1+x^2],v[[1]],x],
	 *   If[CotQ[v],
	 *     SubstForTrig[u,1/Sqrt[1+x^2],x/Sqrt[1+x^2],v[[1]],x],
	 *   If[SecQ[v],
	 *     SubstForTrig[u,1/Sqrt[1-x^2],1/x,v[[1]],x],
	 *   If[CscQ[v],
	 *     SubstForTrig[u,1/x,1/Sqrt[1-x^2],v[[1]],x],
	 * 
	 *   If[SinhQ[v],
	 *     SubstForHyperbolic[u,x,Sqrt[1+x^2],v[[1]],x],
	 *   If[CoshQ[v],
	 *     SubstForHyperbolic[u,Sqrt[-1+x^2],x,v[[1]],x],
	 *   If[TanhQ[v],
	 *     SubstForHyperbolic[u,x/Sqrt[1-x^2],1/Sqrt[1-x^2],v[[1]],x],
	 *   If[CothQ[v],
	 *     SubstForHyperbolic[u,1/Sqrt[-1+x^2],x/Sqrt[-1+x^2],v[[1]],x],
	 *   If[SechQ[v],
	 *     SubstForHyperbolic[u,1/Sqrt[-1+x^2],1/x,v[[1]],x],
	 *   If[CschQ[v],
	 *     SubstForHyperbolic[u,1/x,1/Sqrt[1+x^2],v[[1]],x],
	 * 
	 *   SubstForExpn[u,v,x]]]]]]]]]]]]]]]
	 * </pre>
	 * 
	 * @param a0
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static IAST SubstFor(final IExpr v, final IExpr u, final IExpr x) {
		return ternary(symbol("SubstFor"), v, u, x);
	}

	public static IAST TryPureTanSubst(final IExpr a0, final IExpr a1) {
		return binary(symbol("TryPureTanSubst"), a0, a1);
	}

	public static IAST Unequal(final IExpr a0, final IExpr a1) {
		return binary(symbol("Unequal"), a0, a1);
	}

	public static IAST ZeroQ(final IExpr a) {
		// TODO fix this
		return unary(symbol("ZeroQ"), a);
	}
}