package org.matheclipse.core.integrate.rubi;

import static org.matheclipse.core.expression.F.*;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * UtilityFunctions from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 * 
 * TODO a lot of functions are only placeholders at the moment.
 * 
 */
public class UtilityFunctions {
	public final static String PACKAGE_NAME = "org.matheclipse.core.integrate.rubi";

	public final static String CLASS_NAME = "UtilityFunctions";

	public final static String INTEGRATE_PREFIX = "Integrate::";

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

	public static IAST quad(final IExpr head, final IExpr a0, final IExpr a1, final IExpr a2, final IExpr a3) {
		final IAST ast = ast(head);
		ast.add(a0);
		ast.add(a1);
		ast.add(a2);
		ast.add(a3);
		return ast;
	}

	public static IAST ArcCoth(final IExpr a) {
		// TODO fix this
		return unary($s("ArcCoth"), a);
	}

	public static IAST Cancel(final IExpr a) {
		// TODO fix this
		return unary($s("Cancel"), a);
	}

	public static IAST Coefficient(final IExpr a0, final IExpr a1, final IExpr a2) {
		return ternary($s("Coefficient"), a0, a1, a2);
	}

	public static IAST CosIntegral(final IExpr a) {
		// TODO fix this
		return unary($s("CosIntegral"), a);
	}

	static ISymbol Dist = predefinedSymbol(INTEGRATE_PREFIX + "Dist");
	static ISymbol NegQ = predefinedSymbol(INTEGRATE_PREFIX + "NegQ");
	static ISymbol NumericFactor = predefinedSymbol(INTEGRATE_PREFIX + "NumericFactor");
	static ISymbol PosQ = predefinedSymbol(INTEGRATE_PREFIX + "PosQ");
	static ISymbol Subst = predefinedSymbol(INTEGRATE_PREFIX + "Subst");

	public static IExpr isFraction(IAST ast) {
		if (ast.size() > 1) {
			if (ast.get(1).isList()) {
				IAST list = (IAST) ast.get(1);
				// MapAnd
				for (int i = 1; i < list.size(); i++) {
					if (!(list.get(1) instanceof IFraction)) {
						return F.False;
					}
				}
				return F.True;
			}
			return F.bool(ast.get(1) instanceof IFraction);
		}
		return F.False;
	}

	public static IExpr isPower(IAST ast) {
		if (ast.size() > 1) {
			return F.bool(ast.get(1).isPower());
		}
		return F.False;
	}

	public static IExpr isTimes(IAST ast) {
		if (ast.size() > 1) {
			return F.bool(ast.get(1).isTimes());
		}
		return F.False;
	}

	public static IExpr isPlus(IAST ast) {
		if (ast.size() > 1) {
			return F.bool(ast.get(1).isPlus());
		}
		return F.False;
	}

	public static IExpr isSubst(IAST ast) {
		if (ast.size() > 1) {
			return F.bool(ast.get(1).isAST(Subst));
		}
		return F.False;
	}

	public static IExpr isCalculus(IAST ast) {
		// CalculusFunctions={D,Integrate,Sum,Product,Int,Dif,Subst};
		IAST CalculusFunctions = F.List(D, Integrate, Sum, Product, Subst);
		if (ast.size() > 1) {
			IExpr head = ast.get(1).head();
			return F.bool(org.matheclipse.core.reflection.system.MemberQ.isMember(CalculusFunctions, head));
		}
		return F.False;
	}

	static ISymbol FractionQ = F.method(INTEGRATE_PREFIX + "FractionQ", PACKAGE_NAME, CLASS_NAME, "isFraction");
	static ISymbol PowerQ = F.method(INTEGRATE_PREFIX + "PowerQ", PACKAGE_NAME, CLASS_NAME, "isPower");
	static ISymbol ProductQ = F.method(INTEGRATE_PREFIX + "ProductQ", PACKAGE_NAME, CLASS_NAME, "isTimes");
	static ISymbol SumQ = F.method(INTEGRATE_PREFIX + "SumQ", PACKAGE_NAME, CLASS_NAME, "isPlus");
	static ISymbol SubstQ = F.method(INTEGRATE_PREFIX + "SubstQ", PACKAGE_NAME, CLASS_NAME, "isSubst");
	static ISymbol CalculusQ = F.method(INTEGRATE_PREFIX + "CalculusQ", PACKAGE_NAME, CLASS_NAME, "isCalculus");

	public static void init() {
		// (* If u is not 0 and has a positive form, PosQ[u] returns True, else it
		// returns False. *)
		// PosQ[u_] :=
		// If[PossibleZeroQ[u],
		// False,
		// If[NumericQ[u],
		// If[NumberQ[u],
		// If[PossibleZeroQ[Re[u]],
		// Im[u]>0,
		// Re[u]>0],
		// Module[{v=N[u]},
		// If[PossibleZeroQ[Re[v]],
		// Im[v]>0,
		// Re[v]>0]]],
		// (*If[ProductQ[u],
		// PosQ[First[u]], *)
		// Module[{v=Simplify[u]},
		// If[NumericQ[v],
		// PosQ[v],
		// If[PowerQ[v] && IntegerQ[v[[2]]],
		// PosQ[v[[1]]],
		// If[ProductQ[v],
		// If[RationalQ[First[v]],
		// If[First[v]>0,
		// PosQ[Rest[v]],
		// NegQ[Rest[v]]],
		// PosQ[First[v]]],
		// If[SumQ[v],
		// PosQ[First[v]],
		// Not[MatchQ[v,-_]]]]]]]]]
		//
		//
		// NegQ[u_] :=
		// If[PossibleZeroQ[u],
		// False,
		// Not[PosQ[u]]]

		// Dist[0,v_] := 0,
		// Dist[1,v_] := v,
		// Dist[u_,v_] := -Dist[-u,v] /;NumericFactor[u]<0,
		// Dist[u_,v_] := Map[Function[Dist[u,#]],v] /; SumQ[v],
		// Dist[u_,v_] := u*v /; FreeQ[v,Int],
		// Dist[u_,v_*w_] := Dist[u*v,w] /; FreeQ[v,Int]

		// TODO add the following rules - at the moment these rules will slow down
		// evaluation too much!!!
		// Dist[u_,v_]+Dist[w_,v_] := If[ZeroQ[u+w], 0, Dist[u+w,v]],
		// Dist[u_,v_]-Dist[w_,v_] := If[ZeroQ[u-w], 0, Dist[u-w,v]],
		// w_*Dist[u_,v_] := Dist[w*u,v] /; w=!=-1,
		// Dist[u_,Dist[v_,w_]] := Dist[u*v,w],
		IAST DIST_RULES = List(SetDelayed(Dist(C0, $p("v")), C0), SetDelayed(Dist(C1, $p("v")), $s("v")), SetDelayed(Dist($p("u"),
				$p("v")), Condition(Times(CN1, Dist(Times(CN1, $s("u")), $s("v"))), Less(NumericFactor($s("u")), C0))), SetDelayed(Dist(
				$p("u"), Dist($p("v"), $p("w"))), Dist(Times($s("u"), $s("v")), $s("w"))), SetDelayed(Dist($p("u"), $p("v")), Condition(
				Map(Function(Dist($s("u"), Slot1)), $s("v")), SumQ($s("v")))), SetDelayed(Dist($p("u"), $p("v")), Condition(Times($s("u"),
				$s("v")), FreeQ($s("v"), $s("Int")))), SetDelayed(Dist($p("u"), Times($p("v"), $p("w"))), Condition(Dist(Times($s("u"),
				$s("v")), $s("w")), FreeQ($s("v"), $s("Int")))));

		IAST NEG_RULES = List(SetDelayed(NegQ($p("u")), If(PossibleZeroQ($s("u")), $s("False"), Not(PosQ($s("u"))))));

		IAST NUMERIC_FACTOR_RULES = List(SetDelayed(NumericFactor($p("u")), If(NumberQ($s("u")), If(ZeroQ(Im($s("u"))), $s("u"), If(
				ZeroQ(Re($s("u"))), Im($s("u")), C1)), If(PowerQ($s("u")), If(And(RationalQ(Part($s("u"), C1)),
				FractionQ(Part($s("u"), C2))), If(Greater(Part($s("u"), C2), C0), Times(C1, Power(Denominator(Part($s("u"), C1)), CN1)),
				Times(C1, Power(Denominator(Times(C1, Power(Part($s("u"), C1), CN1))), CN1))), C1), If(ProductQ($s("u")), Times(
				NumericFactor(First($s("u"))), NumericFactor(Rest($s("u")))), C1)))));

		IAST POSQ_RULES = List(SetDelayed(PosQ($p("u")), If(PossibleZeroQ($s("u")), $s("False"), If(NumericQ($s("u")), If(
				NumberQ($s("u")), If(PossibleZeroQ(Re($s("u"))), Greater(Im($s("u")), C0), Greater(Re($s("u")), C0)), Module(List(Set(
						$s("v"), N($s("u")))), If(PossibleZeroQ(Re($s("v"))), Greater(Im($s("v")), C0), Greater(Re($s("v")), C0)))), Module(
				List(Set($s("v"), Simplify($s("u")))), If(NumericQ($s("v")), PosQ($s("v")), If(And(PowerQ($s("v")), IntegerQ(Part($s("v"),
						C2))), PosQ(Part($s("v"), C1)), If(ProductQ($s("v")), If(RationalQ(First($s("v"))), If(Greater(First($s("v")), C0),
						PosQ(Rest($s("v"))), NegQ(Rest($s("v")))), PosQ(First($s("v")))), If(SumQ($s("v")), PosQ(First($s("v"))), Not(MatchQ(
						$s("v"), Times(CN1, $p((ISymbol) null)))))))))))));

		IAST SUBST_RULES = List(SetDelayed(Subst($p("u"), $p("v"), $p("w")), Condition(If(SameQ($s("u"), $s("v")), $s("w"), If(
				AtomQ($s("u")), $s("u"), If(PowerQ($s("u")), If(And(And(PowerQ($s("v")), SameQ(Part($s("u"), C1), Part($s("v"), C1))),
						SumQ(Part($s("u"), C2))), Times(Subst(Power(Part($s("u"), C1), First(Part($s("u"), C2))), $s("v"), $s("w")), Subst(
						Power(Part($s("u"), C1), Rest(Part($s("u"), C2))), $s("v"), $s("w"))), Power(
						Subst(Part($s("u"), C1), $s("v"), $s("w")), Subst(Part($s("u"), C2), $s("v"), $s("w")))), If(And(SubstQ($s("u")), Or(
						SameQ(Part($s("u"), C2), $s("v")), FreeQ(Part($s("u"), C1), $s("v")))), Subst(Part($s("u"), C1), Part($s("u"), C2),
						Subst(Part($s("u"), C3), $s("v"), $s("w"))), Map(Function(Subst(Slot1, $s("v"), $s("w"))), $s("u")))))), Or(Or(
				AtomQ($s("u")), And(SubstQ($s("u")), Or(SameQ(Part($s("u"), C2), $s("v")), FreeQ(Part($s("u"), C1), $s("v"))))), Not(Or(
				And(CalculusQ($s("u")), Not(FreeQ($s("v"), Part($s("u"), C2)))), MemberQ(List($s("Pattern"), $s("Defer"), $s("Hold"),
						$s("HoldForm")), Head($s("u")))))))));

		EvalEngine.get().addRules(DIST_RULES);
		EvalEngine.get().addRules(NEG_RULES);
		EvalEngine.get().addRules(NUMERIC_FACTOR_RULES);
		EvalEngine.get().addRules(POSQ_RULES);
		EvalEngine.get().addRules(SUBST_RULES);
	}

	public static IAST NumericFactor(final IExpr a0) {
		return unary(NumericFactor, a0);
	}

	public static IAST FractionQ(final IExpr a) {
		return unary(FractionQ, a);
	}
	
	public static IAST PowerQ(final IExpr a0) {
		return unary(PowerQ, a0);
	}

	public static IAST ProductQ(final IExpr a0) {
		return unary(ProductQ, a0);
	}

	public static IAST SumQ(final IExpr a0) {
		return unary(SumQ, a0);
	}

	public static IAST Re(final IExpr a0) {
		return unary($s("Re"), a0);
	}

	public static IAST Im(final IExpr a0) {
		return unary($s("Im"), a0);
	}

	public static IAST First(final IExpr a0) {
		return unary($s("First"), a0);
	}

	public static IAST Rest(final IExpr a0) {
		return unary($s("Rest"), a0);
	}

	public static IAST Dist(final IExpr a0, final IExpr a1) {
		return binary(Dist, a0, a1);
	}

	public static IAST ExpnExpand(final IExpr a0, final IExpr a1) {
		return binary($s("ExpnExpand"), a0, a1);
	}

	public static IAST EllipticE(final IExpr a0, final IExpr a1) {
		return binary($s("EllipticE"), a0, a1);
	}

	public static IAST EllipticF(final IExpr a0, final IExpr a1) {
		return binary($s("EllipticF"), a0, a1);
	}

	public static IAST Exponent(final IExpr a0, final IExpr a1) {
		return binary($s("Exponent"), a0, a1);
	}

	public static IAST FractionOrNegativeQ(final IExpr a) {
		// TODO fix this
		return unary($s("FractionOrNegativeQ"), a);
	}

	public static IAST FresnelC(final IExpr a) {
		// TODO fix this
		return unary($s("FresnelC"), a);
	}

	public static IAST FresnelS(final IExpr a) {
		// TODO fix this
		return unary($s("FresnelS"), a);
	}

	public static IAST FunctionOfQ(final IExpr a0, final IExpr a1, final IExpr a2, final IExpr a3) {
		return quad($s("FunctionOfQ"), a0, a1, a2, a3);
	}

	public static IAST FunctionOfTrig(final IExpr a0, final IExpr a1) {
		return binary($s("FunctionOfTrig"), a0, a1);
	}

	public static IAST FunctionOfTrigQ(final IExpr a0, final IExpr a1, final IExpr a2) {
		return ternary($s("FunctionOfTrigQ"), a0, a1, a2);
	}

	public static IAST HalfIntegerQ(final IExpr a) {
		// TODO fix this
		return unary($s("HalfIntegerQ"), a);
	}

	public static IAST IndependentQ(final IExpr a0, final IExpr a1) {
		return binary($s("IndependentQ"), a0, a1);
	}

	public static IAST IntegerQ(final IExpr a) {
		// TODO fix this
		return unary($s("IntegerQ"), a);
	}

	public static IAST MatchQ(final IExpr a0, final IExpr a1) {
		// TODO fix this
		return binary($s("MatchQ"), a0, a1);
	}

	public static IAST NegQ(final IExpr a) {
		// TODO fix this
		return unary(NegQ, a);
	}

	public static IAST NonzeroQ(final IExpr a) {
		// TODO fix this
		return unary($s("NonzeroQ"), a);
	}

	public static IAST Not(final IExpr a) {
		// TODO fix this
		return unary(Not, a);
	}

	public static IAST NotFalseQ(final IExpr u) {
		return binary($s("UnsameQ"), u, False);
	}

	public static IAST PosQ(final IExpr a) {
		// TODO fix this
		return unary(PosQ, a);
	}

	public static IAST PolynomialQ(final IExpr a0, final IExpr a1) {
		return binary($s("PolynomialQ"), a0, a1);
	}

	public static IAST PositiveQ(final IExpr a) {
		// TODO fix this
		return unary($s("PositiveQ"), a);
	}

	public static IAST RationalQ(final IExpr a) {
		// TODO fix this
		return unary($s("RationalQ"), a);
	}

	public static IExpr Regularize(final IExpr u, final IExpr x) {
		// TODO fix this
		return u;
	}

	public static IAST Rt(final IExpr a0, final IExpr a1) {
		return binary($s("Rt"), a0, a1);
	}

	public static IAST Rt(final IExpr a0, final IExpr a1, final IExpr a2) {
		return ternary($s("Rt"), a0, a1, a2);
	}

	public static IAST SameQ(final IExpr a0, final IExpr a1) {
		return binary($s("SameQ"), a0, a1);
	}

	public static IAST UnsameQ(final IExpr a0, final IExpr a1) {
		return binary($s("UnsameQ"), a0, a1);
	}

	public static IAST SinIntegral(final IExpr a) {
		// TODO fix this
		return unary($s("SinIntegral"), a);
	}

	public static IAST AtomQ(final IExpr a) {
		// TODO fix this
		return unary($s("AtomQ"), a);
	}

	public static IAST CalculusQ(final IExpr a) {
		// TODO fix this
		return unary(CalculusQ, a);
	}

	public static IAST SubstQ(final IExpr a) {
		// TODO fix this
		return unary(SubstQ, a);
	}

	public static IAST Head(final IExpr a) {
		// TODO fix this
		return unary($s("Head"), a);
	}

	public static IAST Map(final IExpr a0, final IExpr a1) {
		return binary($s("Map"), a0, a1);
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
		return ternary($s("SubstFor"), v, u, x);
	}

	public static IAST TryPureTanSubst(final IExpr a0, final IExpr a1) {
		return binary($s("TryPureTanSubst"), a0, a1);
	}

	public static IAST Unequal(final IExpr a0, final IExpr a1) {
		return binary($s("Unequal"), a0, a1);
	}

	public static IAST ZeroQ(final IExpr a) {
		// TODO fix this
		return unary($s("ZeroQ"), a);
	}
}