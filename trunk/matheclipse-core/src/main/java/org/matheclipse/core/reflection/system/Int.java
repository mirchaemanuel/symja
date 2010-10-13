package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.*;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * 
 */
public class Int extends AbstractFunctionEvaluator {
	public static IAST ArcCot(final IExpr a0) {
		return unary(symbol("ArcCot"), a0);
	}

	public static IAST ArcTanh(final IExpr a0) {
		return unary(symbol("ArcTanh"), a0);
	}

	public static IAST Csc(final IExpr a0) {
		return unary(symbol("Csc"), a0);
	}

	public static IAST Cot(final IExpr a0) {
		return unary(symbol("Cot"), a0);
	}

	public static IAST Sec(final IExpr a0) {
		return unary(symbol("Sec"), a0);
	}

	public static IAST EvenQ(final IExpr a0) {
		return unary(symbol("EvenQ"), a0);
	}

	public static IAST IntegerQ(final IExpr a0) {
		return unary(symbol("IntegerQ"), a0);
	}

	public static IAST NegQ(final IExpr a0) {
		return unary(symbol("NegQ"), a0);
	}

	public static IAST NotFalseQ(final IExpr a0) {
		return unary(symbol("NotFalseQ"), a0);
	}

	public static IAST OddQ(final IExpr a0) {
		return unary(symbol("OddQ"), a0);
	}

	public static IAST PosQ(final IExpr a0) {
		return unary(symbol("PosQ"), a0);
	}

	public static IAST RationalQ(final IExpr a0) {
		return unary(symbol("RationalQ"), a0);
	}

	public static IAST PossibleZeroQ(final IExpr a0) {
		return unary(symbol("PossibleZeroQ"), a0);
	}

	/**
	 * ZeroQ[u] returns True if u is any 0; else returns False
	 * 
	 * @param a0
	 * @return
	 */
	public static IAST ZeroQ(final IExpr a0) {
		return PossibleZeroQ(a0);
	}

	public static IAST NonzeroQ(final IExpr a0) {
		return Not(PossibleZeroQ(a0));
	}

	public static IAST Not(final IExpr a0) {
		return unary(symbol("Not"), a0);
	}

	public static IAST FreeQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("FreeQ"), a0, a1);
	}

	public static IAST MatchQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("MatchQ"), a0, a1);
	}

	public static IAST PolynomialQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("PolynomialQ"), a0, a1);
	}

	public static IAST SameQ(final IExpr a0, final IExpr a1) {
		return binary(symbol("SameQ"), a0, a1);
	}

	public static IAST EllipticE(final IExpr a0, final IExpr a1) {
		return binary(symbol("EllipticE"), a0, a1);
	}

	public static IAST EllipticF(final IExpr a0, final IExpr a1) {
		return binary(symbol("EllipticF"), a0, a1);
	}

	public static IAST ExpnExpand(final IExpr a0, final IExpr a1) {
		return binary(symbol("ExpnExpand"), a0, a1);
	}

	public static IAST Exponent(final IExpr a0, final IExpr a1) {
		return binary(symbol("Exponent"), a0, a1);
	}

	public static IAST FunctionOfTrig(final IExpr a0, final IExpr a1) {
		return binary(symbol("FunctionOfTrig"), a0, a1);
	}

	public static IAST Greater(final IExpr a0, final IExpr a1) {
		return binary(symbol("Greater"), a0, a1);
	}

	public static IAST Unequal(final IExpr a0, final IExpr a1) {
		return binary(symbol("Unequal"), a0, a1);
	}

	public static IAST GreaterEqual(final IExpr a0, final IExpr a1) {
		return binary(symbol("GreaterEqual"), a0, a1);
	}

	public static IAST Regularize(final IExpr a0, final IExpr a1) {
		return binary(symbol("Regularize"), a0, a1);
	}

	public static IAST Int(final IExpr a0, final IExpr a1) {
		return binary(symbol("Int"), a0, a1);
	}

	public static IAST Module(final IExpr a0, final IExpr a1) {
		return binary(symbol("Module"), a0, a1);
	}

	public static IAST Dist(final IExpr a0, final IExpr a1) {
		return binary(symbol("Dist"), a0, a1);
	}

	public static IAST Rt(final IExpr a0, final IExpr a1) {
		return binary(symbol("Rt"), a0, a1);
	}

	public static IAST Subst(final IExpr... a) {
		return ast(a, symbol("Subst"));
	}

	public static IAST SubstFor(final IExpr... a) {
		return ast(a, symbol("SubstFor"));
	}

	public static IAST TryPureTanSubst(final IExpr... a) {
		return ast(a, symbol("TryPureTanSubst"));
	}

	public static IAST And(final IExpr... a) {
		return ast(a, And);
	}

	public static IAST Block(final IExpr... a) {
		return ast(a, symbol("Block"));
	}

	public static IAST Coefficient(final IExpr... a) {
		return ast(a, symbol("Coefficient"));
	}

	public static IAST Or(final IExpr... a) {
		return ast(a, Or);
	}

	public static IAST FunctionOfQ(final IExpr... a) {
		return ast(a, symbol("FunctionOfQ"));
	}

	IExpr[] RULES = {
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m")), Power(
					Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n", null, true))), pattern(
					"x", symbol("Symbol"))),
			Condition(Dist(Times(CN1, Power(symbol("b"), CN1)), Subst(Int(Regularize(Times(Power(symbol("x"), symbol("n")), Power(Plus(
					C1, Power(symbol("x"), C2)), Times(Plus(symbol("m"), Times(CN1, C2)), C1D2))), symbol("x")), symbol("x")), symbol("x"),
					Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("n")),
					symbol("x")), EvenQ(symbol("m"))), Greater(symbol("m"), C2)), Not(And(OddQ(symbol("n")), Less(Less(C0, symbol("n")),
					Plus(symbol("m"), Times(CN1, C1))))))),
			Int(Times(
					Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m", null, true)),
					Power(Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n", null, true))),
					pattern("x", symbol("Symbol"))),
			Condition(Dist(Times(CN1, Power(symbol("b"), CN1)), Subst(Int(Regularize(Times(Power(symbol("x"), Plus(symbol("m"), Times(
					CN1, C1))), Power(Plus(CN1, Power(symbol("x"), C2)), Times(Plus(symbol("n"), Times(CN1, C1)), C1D2))), symbol("x")),
					symbol("x")), symbol("x"), Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))))), And(And(FreeQ(List(symbol("a"),
					symbol("b"), symbol("m")), symbol("x")), OddQ(symbol("n"))), Not(And(EvenQ(symbol("m")), LessEqual(Less(C0, symbol("m")),
					Plus(symbol("n"), C1)))))),
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m")), Power(
					Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(
					Plus(Times(Times(CN1, Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("m"), Times(CN1, C2)))),
							Times(Power(Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"), C1)), Power(Times(symbol("b"),
									Plus(symbol("n"), C1)), CN1))), Times(CN1, Dist(Times(Plus(symbol("m"), Times(CN1, C2)), Power(Plus(symbol("n"),
							C1), CN1)), Int(Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))),
							Plus(symbol("m"), Times(CN1, C2))), Power(Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"),
							C2))), symbol("x"))))), And(And(And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), RationalQ(List(
							symbol("m"), symbol("n")))), Greater(symbol("m"), C1)), Less(symbol("n"), CN1)), Not(EvenQ(symbol("m"))))),
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m")), Power(
					Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), symbol("m"))), Times(Power(
					Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"), Times(CN1, C1))), Power(Times(symbol("b"),
					symbol("m")), CN1))), Times(CN1, Dist(Times(Plus(symbol("n"), Times(CN1, C1)), Power(symbol("m"), CN1)), Int(Times(Power(
					Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("m"), C2)), Power(Cot(Plus(symbol("a"), Times(
					symbol("b"), symbol("x")))), Plus(symbol("n"), Times(CN1, C2)))), symbol("x"))))), And(And(And(And(FreeQ(List(
					symbol("a"), symbol("b")), symbol("x")), RationalQ(List(symbol("m"), symbol("n")))), Less(symbol("m"), CN1)), Greater(
					symbol("n"), C1)), Not(EvenQ(symbol("m"))))),
			Int(Times(
					Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m", null, true)),
					Power(Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), symbol("m")), Times(Power(Cot(Plus(
					symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"), C1)), Power(Times(symbol("b"), symbol("m")), CN1))),
					And(FreeQ(List(symbol("a"), symbol("b"), symbol("m"), symbol("n")), symbol("x")), ZeroQ(Plus(Plus(symbol("m"),
							symbol("n")), C1)))),
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m")), Power(
					Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), symbol("m")), Times(Power(Cot(Plus(
					symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"), C1)), Power(Times(symbol("b"), symbol("m")), CN1))),
					Dist(Times(Plus(Plus(symbol("m"), symbol("n")), C1), Power(symbol("m"), CN1)), Int(Times(Power(Csc(Plus(symbol("a"),
							Times(symbol("b"), symbol("x")))), Plus(symbol("m"), C2)), Power(Cot(Plus(symbol("a"),
							Times(symbol("b"), symbol("x")))), symbol("n"))), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("n")), symbol("x")), RationalQ(symbol("m"))), Less(symbol("m"), CN1)), Not(EvenQ(symbol("m"))))),
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m")), Power(
					Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("m"), Times(CN1,
					C2)))), Times(Power(Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"), C1)), Power(Times(
					symbol("b"), Plus(Plus(symbol("m"), symbol("n")), Times(CN1, C1))), CN1))), Dist(Times(Plus(symbol("m"), Times(CN1, C2)),
					Power(Plus(Plus(symbol("m"), symbol("n")), Times(CN1, C1)), CN1)), Int(Times(Power(Csc(Plus(symbol("a"), Times(
					symbol("b"), symbol("x")))), Plus(symbol("m"), Times(CN1, C2))), Power(Cot(Plus(symbol("a"), Times(symbol("b"),
					symbol("x")))), symbol("n"))), symbol("x")))), And(And(And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("n")),
					symbol("x")), RationalQ(symbol("m"))), Greater(symbol("m"), C1)), NonzeroQ(Plus(Plus(symbol("m"), symbol("n")), Times(
					CN1, C1)))), Not(EvenQ(symbol("m")))), Not(OddQ(symbol("n"))))),
			Int(Times(
					Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m", null, true)),
					Power(Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), symbol("m"))), Times(Power(
					Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"), Times(CN1, C1))), Power(Times(symbol("b"),
					Plus(Plus(symbol("m"), symbol("n")), Times(CN1, C1))), CN1))), Times(CN1, Dist(Times(Plus(symbol("n"), Times(CN1, C1)),
					Power(Plus(Plus(symbol("m"), symbol("n")), Times(CN1, C1)), CN1)), Int(Times(Power(Csc(Plus(symbol("a"), Times(
					symbol("b"), symbol("x")))), symbol("m")), Power(Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(
					symbol("n"), Times(CN1, C2)))), symbol("x"))))), And(And(And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("m")),
					symbol("x")), RationalQ(symbol("n"))), Greater(symbol("n"), C1)), NonzeroQ(Plus(Plus(symbol("m"), symbol("n")), Times(
					CN1, C1)))), Not(EvenQ(symbol("m")))), Not(OddQ(symbol("n"))))),
			Int(Times(
					Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("m", null, true)),
					Power(Cot(Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), symbol("m"))), Times(Power(
					Cot(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Plus(symbol("n"), C1)), Power(Times(symbol("b"), Plus(
					symbol("n"), C1)), CN1))), Times(CN1, Dist(Times(Plus(Plus(symbol("m"), symbol("n")), C1), Power(Plus(symbol("n"), C1),
					CN1)), Int(Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), symbol("x")))), symbol("m")), Power(Cot(Plus(symbol("a"),
					Times(symbol("b"), symbol("x")))), Plus(symbol("n"), C2))), symbol("x"))))), And(And(And(FreeQ(List(symbol("a"),
					symbol("b"), symbol("m")), symbol("x")), RationalQ(symbol("n"))), Less(symbol("n"), CN1)), Not(EvenQ(symbol("m"))))),
			Int(Times(Times(Power(pattern("x"), pattern("m", null, true)), Power(Csc(Plus(pattern("a", null, true), Times(pattern("b",
					null, true), Power(pattern("x"), pattern("n", null, true))))), pattern("p"))), Cos(Plus(pattern("a", null, true), Times(
					pattern("b", null, true), Power(pattern("x"), pattern("n", null, true)))))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Power(symbol("x"), Plus(Plus(symbol("m"), Times(CN1, symbol("n"))), C1))), Times(Power(
					Csc(Plus(symbol("a"), Times(symbol("b"), Power(symbol("x"), symbol("n"))))), Plus(symbol("p"), Times(CN1, C1))), Power(
					Times(Times(symbol("b"), symbol("n")), Plus(symbol("p"), Times(CN1, C1))), CN1))), Dist(Times(Plus(Plus(symbol("m"),
					Times(CN1, symbol("n"))), C1), Power(Times(Times(symbol("b"), symbol("n")), Plus(symbol("p"), Times(CN1, C1))), CN1)),
					Int(Times(Power(symbol("x"), Plus(symbol("m"), Times(CN1, symbol("n")))), Power(Csc(Plus(symbol("a"), Times(symbol("b"),
							Power(symbol("x"), symbol("n"))))), Plus(symbol("p"), Times(CN1, C1)))), symbol("x")))), And(And(And(And(FreeQ(List(
					symbol("a"), symbol("b"), symbol("p")), symbol("x")), RationalQ(symbol("m"))), IntegerQ(symbol("n"))), GreaterEqual(Plus(
					symbol("m"), Times(CN1, symbol("n"))), C0)), NonzeroQ(Plus(symbol("p"), Times(CN1, C1))))),
			Int(
					Times(Times(Power(pattern("x"), pattern("m", null, true)), Power(Csc(Plus(pattern("a", null, true), Times(pattern("b",
							null, true), Power(pattern("x"), pattern("n", null, true))))), pattern("p", null, true))), Power(Cot(Plus(pattern(
							"a", null, true), Times(pattern("b", null, true), Power(pattern("x"), pattern("n", null, true))))), pattern("q",
							null, true))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Power(symbol("x"), Plus(Plus(symbol("m"), Times(CN1, symbol("n"))), C1))), Times(Power(
					Csc(Plus(symbol("a"), Times(symbol("b"), Power(symbol("x"), symbol("n"))))), symbol("p")), Power(Times(Times(symbol("b"),
					symbol("n")), symbol("p")), CN1))), Dist(Times(Plus(Plus(symbol("m"), Times(CN1, symbol("n"))), C1), Power(Times(Times(
					symbol("b"), symbol("n")), symbol("p")), CN1)), Int(Times(Power(symbol("x"), Plus(symbol("m"), Times(CN1, symbol("n")))),
					Power(Csc(Plus(symbol("a"), Times(symbol("b"), Power(symbol("x"), symbol("n"))))), symbol("p"))), symbol("x")))), And(
					And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("p")), symbol("x")), RationalQ(symbol("m"))),
							IntegerQ(symbol("n"))), GreaterEqual(Plus(symbol("m"), Times(CN1, symbol("n"))), C0)), SameQ(symbol("q"), C1))),
			Int(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), Log(Times(pattern("c", null, true), Power(
					pattern("x"), pattern("n", null, true))))))), pattern("p")), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(Times(CN1, symbol("x")), Cot(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"),
					Power(symbol("x"), symbol("n")))))))), Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(
					symbol("x"), symbol("n"))))))), Plus(symbol("p"), Times(CN1, C2))), Power(Times(Times(symbol("b"), symbol("n")), Plus(
					symbol("p"), Times(CN1, C1))), CN1))), Times(CN1, Times(symbol("x"), Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"),
					Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"), Times(CN1, C2))), Power(Times(Times(Times(
					Power(symbol("b"), C2), Power(symbol("n"), C2)), Plus(symbol("p"), Times(CN1, C1))), Plus(symbol("p"), Times(CN1, C2))),
					CN1))))), Dist(Times(Plus(C1, Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Power(Plus(symbol("p"), Times(
					CN1, C2)), C2))), Power(Times(Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Plus(symbol("p"), Times(CN1,
					C1))), Plus(symbol("p"), Times(CN1, C2))), CN1)), Int(Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(
					symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"), Times(CN1, C2))), symbol("x")))), And(
					And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("n")), symbol("x")), RationalQ(symbol("p"))), Greater(
							symbol("p"), C1)), Unequal(symbol("p"), C2))),
			Int(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), Log(Times(pattern("c", null, true), Power(
					pattern("x"), pattern("n", null, true))))))), pattern("p")), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(symbol("x"), Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(
					symbol("x"), symbol("n"))))))), symbol("p")), Power(Plus(C1, Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)),
					Power(symbol("p"), C2))), CN1))), Times(Times(Times(Times(Times(symbol("b"), symbol("n")), symbol("p")), symbol("x")),
					Cos(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n")))))))), Times(Power(
					Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"),
							C1)), Power(Plus(C1, Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Power(symbol("p"), C2))), CN1)))),
					Dist(Times(Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), symbol("p")), Times(Plus(symbol("p"), C1), Power(
							Plus(C1, Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Power(symbol("p"), C2))), CN1))), Int(Power(
							Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(
									symbol("p"), C2)), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("n")),
					symbol("x")), RationalQ(symbol("p"))), Less(symbol("p"), CN1)), NonzeroQ(Plus(C1, Times(Times(Power(symbol("b"), C2),
					Power(symbol("n"), C2)), Power(symbol("p"), C2)))))),
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), Log(Times(pattern("c", null, true), Power(
					pattern("x"), pattern("n", null, true))))))), C2), Power(pattern("x"), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(Times(CN1,
					Cot(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n")))))))), Power(Times(
					symbol("b"), symbol("n")), CN1)), FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("n")), symbol("x"))),
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), Log(Times(pattern("c", null, true), Power(
					pattern("x"), pattern("n", null, true))))))), pattern("p")), Power(pattern("x"), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Cot(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"),
					symbol("n")))))))), Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"),
					symbol("n"))))))), Plus(symbol("p"), Times(CN1, C2))), Power(Times(Times(symbol("b"), symbol("n")), Plus(symbol("p"),
					Times(CN1, C1))), CN1))), Dist(Times(Plus(symbol("p"), Times(CN1, C2)), Power(Plus(symbol("p"), Times(CN1, C1)), CN1)),
					Int(Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))),
							Plus(symbol("p"), Times(CN1, C2))), Power(symbol("x"), CN1)), symbol("x")))), And(And(FreeQ(List(symbol("a"),
					symbol("b"), symbol("c"), symbol("n")), symbol("x")), RationalQ(symbol("p"))), Greater(symbol("p"), C1))),
			Int(Times(Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null, true), Log(Times(pattern("c", null, true), Power(
					pattern("x"), pattern("n", null, true))))))), pattern("p")), Power(pattern("x"), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Cos(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))),
					Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(
							symbol("p"), C1)), Power(Times(Times(symbol("b"), symbol("n")), symbol("p")), CN1))), Dist(Times(
					Plus(symbol("p"), C1), Power(symbol("p"), CN1)), Int(Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(
					symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"), C2)), Power(symbol("x"), CN1)), symbol("x")))), And(
					And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("n")), symbol("x")), RationalQ(symbol("p"))), Less(
							symbol("p"), CN1))),
			Int(Times(Power(pattern("x"), pattern("m", null, true)), Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null,
					true), Log(Times(pattern("c", null, true), Power(pattern("x"), pattern("n", null, true))))))), pattern("p"))), pattern(
					"x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(Times(CN1, Power(symbol("x"), Plus(symbol("m"), C1))), Cot(Plus(symbol("a"), Times(
					symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n")))))))), Times(Power(Csc(Plus(symbol("a"), Times(
					symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"), Times(CN1, C2))), Power(
					Times(Times(symbol("b"), symbol("n")), Plus(symbol("p"), Times(CN1, C1))), CN1))), Times(CN1, Times(Times(Plus(
					symbol("m"), C1), Power(symbol("x"), Plus(symbol("m"), C1))), Times(Power(Csc(Plus(symbol("a"), Times(symbol("b"),
					Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"), Times(CN1, C2))), Power(Times(Times(Times(
					Power(symbol("b"), C2), Power(symbol("n"), C2)), Plus(symbol("p"), Times(CN1, C1))), Plus(symbol("p"), Times(CN1, C2))),
					CN1))))), Dist(Times(Plus(Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Power(Plus(symbol("p"), Times(CN1,
					C2)), C2)), Power(Plus(symbol("m"), C1), C2)), Power(Times(Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)),
					Plus(symbol("p"), Times(CN1, C1))), Plus(symbol("p"), Times(CN1, C2))), CN1)), Int(Times(Power(symbol("x"), symbol("m")),
					Power(Csc(Plus(symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(
							symbol("p"), Times(CN1, C2)))), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"),
					symbol("m"), symbol("n")), symbol("x")), RationalQ(symbol("p"))), Greater(symbol("p"), C1)), Unequal(symbol("p"), C2))),
			Int(Times(Power(pattern("x"), pattern("m", null, true)), Power(Csc(Plus(pattern("a", null, true), Times(pattern("b", null,
					true), Log(Times(pattern("c", null, true), Power(pattern("x"), pattern("n", null, true))))))), pattern("p"))), pattern(
					"x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(Plus(symbol("m"), C1), Power(symbol("x"), Plus(symbol("m"), C1))), Times(Power(Csc(Plus(
					symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), symbol("p")), Power(Plus(
					Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Power(symbol("p"), C2)), Power(Plus(symbol("m"), C1), C2)),
					CN1))), Times(Times(
					Times(Times(Times(symbol("b"), symbol("n")), symbol("p")), Power(symbol("x"), Plus(symbol("m"), C1))), Cos(Plus(
							symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n")))))))), Times(Power(Csc(Plus(
					symbol("a"), Times(symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"), C1)),
					Power(Plus(Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Power(symbol("p"), C2)), Power(Plus(symbol("m"),
							C1), C2)), CN1)))), Dist(Times(Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), symbol("p")), Times(Plus(
					symbol("p"), C1), Power(Plus(Times(Times(Power(symbol("b"), C2), Power(symbol("n"), C2)), Power(symbol("p"), C2)), Power(
					Plus(symbol("m"), C1), C2)), CN1))), Int(Times(Power(symbol("x"), symbol("m")), Power(Csc(Plus(symbol("a"), Times(
					symbol("b"), Log(Times(symbol("c"), Power(symbol("x"), symbol("n"))))))), Plus(symbol("p"), C2))), symbol("x")))),
					And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("m"), symbol("n")), symbol("x")),
							RationalQ(symbol("p"))), Less(symbol("p"), CN1)), NonzeroQ(Plus(Times(Times(Power(symbol("b"), C2), Power(
							symbol("n"), C2)), Power(symbol("p"), C2)), Power(Plus(symbol("m"), C1), C2))))),
			Int(Power(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Times(symbol("a"), Times(Power(Plus(Times(symbol("a"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))),
					Times(symbol("b"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))))), symbol("n")), Power(Times(Times(
					symbol("b"), symbol("d")), symbol("n")), CN1))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"),
					symbol("n")), symbol("x")), ZeroQ(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2))))),
			Int(Times(C1, Power(Power(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true), Times(pattern("d", null,
					true), pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true), Times(
					pattern("d", null, true), pattern("x")))))), C2), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Power(Times(Times(symbol("a"), symbol("d")), Plus(
					Times(symbol("a"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"),
							Times(symbol("d"), symbol("x"))))))), CN1)), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")),
					symbol("x")), NonzeroQ(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2))))),
			Int(Sqrt(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true),
					Times(pattern("d", null, true), pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true),
					Times(pattern("d", null, true), pattern("x"))))))), pattern("x", symbol("Symbol"))),
			Condition(Times(Times(C2, EllipticE(Times(Plus(Plus(symbol("c"), Times(symbol("d"), symbol("x"))), Times(CN1, ArcTan(
					symbol("a"), symbol("b")))), C1D2), C2)), Times(Sqrt(Plus(Times(symbol("a"), Cos(Plus(symbol("c"), Times(symbol("d"),
					symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))))), Power(Times(symbol("d"),
					Sqrt(Times(Plus(Times(symbol("a"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(
							symbol("c"), Times(symbol("d"), symbol("x")))))), Power(Sqrt(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2))),
							CN1)))), CN1))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")), symbol("x")), NonzeroQ(Plus(
					Power(symbol("a"), C2), Power(symbol("b"), C2))))),
			Int(Times(C1, Power(Sqrt(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true), Times(pattern("d", null,
					true), pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true), Times(
					pattern("d", null, true), pattern("x"))))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(Times(C2, EllipticF(Times(Plus(Plus(symbol("c"), Times(symbol("d"), symbol("x"))), Times(CN1, ArcTan(
					symbol("a"), symbol("b")))), C1D2), C2)), Times(Sqrt(Times(Plus(Times(symbol("a"), Cos(Plus(symbol("c"), Times(
					symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))))), Power(
					Sqrt(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2))), CN1))), Power(Times(symbol("d"), Sqrt(Plus(Times(symbol("a"),
					Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"), Times(symbol("d"),
					symbol("x")))))))), CN1))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")), symbol("x")),
					NonzeroQ(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2))))),
			Int(Power(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Dist(Times(C1, Power(symbol("d"), CN1)), Subst(Int(Regularize(Power(Plus(Plus(Power(symbol("a"), C2), Power(
					symbol("b"), C2)), Times(CN1, Power(symbol("x"), C2))), Times(Plus(symbol("n"), Times(CN1, C1)), C1D2)), symbol("x")),
					symbol("x")), symbol("x"), Plus(Times(Times(CN1, symbol("b")), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))),
					Times(symbol("a"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))))))), And(And(And(FreeQ(List(symbol("a"),
					symbol("b")), symbol("x")), OddQ(symbol("n"))), GreaterEqual(symbol("n"), CN1)), NonzeroQ(Plus(Power(symbol("a"), C2),
					Power(symbol("b"), C2))))),
			Int(Power(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Plus(Times(symbol("b"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(CN1,
					Times(symbol("a"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))))))), Times(Power(Plus(Times(symbol("a"),
					Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"), Times(symbol("d"),
					symbol("x")))))), Plus(symbol("n"), Times(CN1, C1))), Power(Times(symbol("d"), symbol("n")), CN1))), Dist(Times(Plus(
					symbol("n"), Times(CN1, C1)), Times(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2)), Power(symbol("n"), CN1))), Int(
					Power(Plus(Times(symbol("a"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(
							symbol("c"), Times(symbol("d"), symbol("x")))))), Plus(symbol("n"), Times(CN1, C2))), symbol("x")))), And(And(And(
					And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")), symbol("x")), RationalQ(symbol("n"))), Greater(
							symbol("n"), C1)), NonzeroQ(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2)))), Not(OddQ(symbol("n"))))),
			Int(Power(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Plus(Times(symbol("b"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(CN1, Times(
					symbol("a"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))))), Times(Power(Plus(Times(symbol("a"), Cos(Plus(
					symbol("c"), Times(symbol("d"), symbol("x"))))), Times(symbol("b"),
					Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))))), Plus(symbol("n"), C1)), Power(Times(Times(symbol("d"), Plus(
					symbol("n"), C1)), Plus(Power(symbol("a"), C2), Power(symbol("b"), C2))), CN1))), Dist(Times(Plus(symbol("n"), C2),
					Power(Times(Plus(symbol("n"), C1), Plus(Power(symbol("a"), C2), Power(symbol("b"), C2))), CN1)), Int(Power(Plus(Times(
					symbol("a"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"), Times(
					symbol("d"), symbol("x")))))), Plus(symbol("n"), C2)), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d")), symbol("x")), RationalQ(symbol("n"))), Less(symbol("n"), CN1)), NonzeroQ(Plus(Power(
					symbol("a"), C2), Power(symbol("b"), C2))))),
			Int(Power(Plus(Times(pattern("a", null, true), Csc(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Int(Power(Times(Times(symbol("a"), Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Cot(Plus(symbol("c"),
					Times(symbol("d"), symbol("x"))))), symbol("n")), symbol("x")), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"),
					symbol("d"), symbol("n")), symbol("x")), ZeroQ(Plus(symbol("a"), symbol("b"))))),
			Int(Power(Plus(Times(pattern("a", null, true), Sec(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x"))))), Times(pattern("b", null, true), Cos(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Int(Power(Times(Times(symbol("a"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x"))))), Tan(Plus(symbol("c"),
					Times(symbol("d"), symbol("x"))))), symbol("n")), symbol("x")), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"),
					symbol("d"), symbol("n")), symbol("x")), ZeroQ(Plus(symbol("a"), symbol("b"))))),
			Int(Times(pattern("u", null, true), Times(Power(Sin(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))), pattern("n", null, true)), Power(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true),
					Times(pattern("d", null, true), pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true),
					Times(pattern("d", null, true), pattern("x")))))), CN1))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Dist(Times(symbol("b"), Power(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2)), CN1)), Int(Times(
					symbol("u"), Power(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C1)))),
					symbol("x"))), Times(CN1, Dist(Times(symbol("a"), Power(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2)), CN1)), Int(
					Times(Times(symbol("u"),
							Power(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C2)))), Cos(Plus(
							symbol("c"), Times(symbol("d"), symbol("x"))))), symbol("x"))))), Dist(Times(Power(symbol("a"), C2), Power(Plus(
					Power(symbol("a"), C2), Power(symbol("b"), C2)), CN1)), Int(Times(symbol("u"), Times(Power(Sin(Plus(symbol("c"), Times(
					symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C2))), Power(Plus(Times(symbol("a"), Cos(Plus(symbol("c"),
					Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))))), CN1))),
					symbol("x")))),
					And(And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")), symbol("x")), NonzeroQ(Plus(Power(
							symbol("a"), C2), Power(symbol("b"), C2)))), IntegerQ(symbol("n"))), Greater(symbol("n"), C0)), Or(Greater(
							symbol("n"), C1), MatchQ(symbol("u"), Condition(Times(pattern("v", null, true), Power(Tan(Plus(symbol("c"), Times(
							symbol("d"), symbol("x")))), pattern("m", null, true))), And(IntegerQ(symbol("m")), Greater(symbol("m"), C0))))))),
			Int(Times(pattern("u", null, true), Times(Power(Cos(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))), pattern("n", null, true)), Power(Plus(Times(pattern("a", null, true), Cos(Plus(pattern("c", null, true),
					Times(pattern("d", null, true), pattern("x"))))), Times(pattern("b", null, true), Sin(Plus(pattern("c", null, true),
					Times(pattern("d", null, true), pattern("x")))))), CN1))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Dist(Times(symbol("a"), Power(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2)), CN1)), Int(Times(
					symbol("u"), Power(Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C1)))),
					symbol("x"))), Times(CN1, Dist(Times(symbol("b"), Power(Plus(Power(symbol("a"), C2), Power(symbol("b"), C2)), CN1)), Int(
					Times(Times(symbol("u"),
							Power(Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C2)))), Sin(Plus(
							symbol("c"), Times(symbol("d"), symbol("x"))))), symbol("x"))))), Dist(Times(Power(symbol("b"), C2), Power(Plus(
					Power(symbol("a"), C2), Power(symbol("b"), C2)), CN1)), Int(Times(symbol("u"), Times(Power(Cos(Plus(symbol("c"), Times(
					symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C2))), Power(Plus(Times(symbol("a"), Cos(Plus(symbol("c"),
					Times(symbol("d"), symbol("x"))))), Times(symbol("b"), Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))))), CN1))),
					symbol("x")))),
					And(And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")), symbol("x")), NonzeroQ(Plus(Power(
							symbol("a"), C2), Power(symbol("b"), C2)))), IntegerQ(symbol("n"))), Greater(symbol("n"), C0)), Or(Greater(
							symbol("n"), C1), MatchQ(symbol("u"), Condition(Times(pattern("v", null, true), Power(Cot(Plus(symbol("c"), Times(
							symbol("d"), symbol("x")))), pattern("m", null, true))), And(IntegerQ(symbol("m")), Greater(symbol("m"), C0))))))),
			Int(Times(C1, Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(integer(-2L), Power(Times(symbol("e"), Plus(symbol("c"), Times(Plus(symbol("a"), Times(CN1, symbol("b"))),
					Tan(Times(Plus(symbol("d"), Times(symbol("e"), symbol("x"))), C1D2))))), CN1)), And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d"), symbol("e")), symbol("x")), ZeroQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(
					symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Sqrt(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x"))))))), pattern("x", symbol("Symbol"))),
			Condition(Times(integer(-2L), Times(Plus(Times(symbol("c"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))), Times(
					CN1, Times(symbol("b"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Power(Times(symbol("e"), Sqrt(Plus(
					Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"),
							Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))))), CN1))), And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d"), symbol("e")), symbol("x")), ZeroQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(
					symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Plus(Times(Times(CN1, symbol("c")), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))), Times(
					symbol("b"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(Power(Plus(Plus(symbol("a"), Times(
					symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(
					symbol("e"), symbol("x")))))), Plus(symbol("n"), Times(CN1, C1))), Power(Times(symbol("e"), symbol("n")), CN1))), Dist(
					Times(symbol("a"), Times(Plus(Times(C2, symbol("n")), Times(CN1, C1)), Power(symbol("n"), CN1))), Int(Power(Plus(Plus(
							symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"),
							Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(symbol("n"), Times(CN1, C1))), symbol("x")))),
					And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")),
							RationalQ(symbol("n"))), Greater(symbol("n"), C1)), ZeroQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(
							symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Plus(Times(symbol("c"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))), Times(CN1, Times(
					symbol("b"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Times(Power(Plus(Plus(symbol("a"), Times(
					symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(
					symbol("e"), symbol("x")))))), symbol("n")), Power(Times(Times(symbol("a"), symbol("e")),
					Plus(Times(C2, symbol("n")), C1)), CN1))), Dist(Times(Plus(symbol("n"), C1), Power(Times(symbol("a"), Plus(Times(C2,
					symbol("n")), C1)), CN1)), Int(Power(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"),
					symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(symbol("n"), C1)),
					symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")),
					RationalQ(symbol("n"))), Less(symbol("n"), CN1)), ZeroQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"),
					C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Times(C1, Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(Log(Plus(symbol("a"),
					Times(symbol("c"), Tan(Times(Plus(symbol("d"), Times(symbol("e"), symbol("x"))), C1D2))))), Power(Times(symbol("c"),
					symbol("e")), CN1)), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")),
					ZeroQ(Plus(symbol("a"), Times(CN1, symbol("b")))))),
			Int(Times(C1, Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(Times(CN1, Log(Plus(symbol("a"), Times(symbol("c"), Cot(Times(Plus(symbol("d"), Times(symbol("e"),
					symbol("x"))), C1D2)))))), Power(Times(symbol("c"), symbol("e")), CN1)), And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d"), symbol("e")), symbol("x")), ZeroQ(Plus(symbol("a"), symbol("b"))))),
			Int(Times(C1, Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(C2, Times(ArcTan(Times(Plus(symbol("c"), Times(Plus(symbol("a"), Times(CN1, symbol("b"))), Tan(Times(Plus(
					symbol("d"), Times(symbol("e"), symbol("x"))), C1D2)))), Power(Rt(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(
					symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2))), C2), CN1))), Power(Times(symbol("e"), Rt(Plus(Plus(Power(
					symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2))), C2)), CN1))), And(And(FreeQ(
					List(symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")), NonzeroQ(Plus(
					Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))))), PosQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(
					symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Times(C1, Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(integer(-2L), Times(ArcTanh(Times(Plus(symbol("c"), Times(Plus(symbol("a"), Times(CN1, symbol("b"))),
					Tan(Times(Plus(symbol("d"), Times(symbol("e"), symbol("x"))), C1D2)))), Power(Rt(Plus(Plus(Times(CN1, Power(symbol("a"),
					C2)), Power(symbol("b"), C2)), Power(symbol("c"), C2)), C2), CN1))), Power(Times(symbol("e"), Rt(Plus(Plus(Times(CN1,
					Power(symbol("a"), C2)), Power(symbol("b"), C2)), Power(symbol("c"), C2)), C2)), CN1))), And(And(FreeQ(List(symbol("a"),
					symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")), NonzeroQ(Plus(Power(symbol("a"), C2), Times(CN1,
					Power(symbol("b"), C2))))), NegQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(
					symbol("c"), C2)))))),
			Int(Sqrt(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(
					pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(
					pattern("e", null, true), pattern("x"))))))), pattern("x", symbol("Symbol"))),
			Condition(Times(Times(C2, EllipticE(Times(Plus(Plus(symbol("d"), Times(symbol("e"), symbol("x"))), Times(CN1, ArcTan(
					symbol("b"), symbol("c")))), C1D2), Times(C2, Power(Plus(C1, Times(symbol("a"), Power(Sqrt(Plus(Power(symbol("b"), C2),
					Power(symbol("c"), C2))), CN1))), CN1)))), Times(Sqrt(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"),
					Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))),
					Power(Times(symbol("e"), Sqrt(Times(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"),
							symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Power(Plus(
							symbol("a"), Sqrt(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)))), CN1)))), CN1))), And(FreeQ(List(
					symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")), NonzeroQ(Plus(Plus(
					Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Times(C1, Power(Sqrt(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true), Cos(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x"))))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Times(Times(C2, EllipticF(Times(Plus(Plus(symbol("d"), Times(symbol("e"), symbol("x"))), Times(CN1, ArcTan(
					symbol("b"), symbol("c")))), C1D2), Times(C2, Power(Plus(C1, Times(symbol("a"), Power(Sqrt(Plus(Power(symbol("b"), C2),
					Power(symbol("c"), C2))), CN1))), CN1)))), Times(Sqrt(Times(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"),
					Times(symbol("e"), symbol("x")))))), Power(Plus(symbol("a"), Sqrt(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)))),
					CN1))), Power(Times(symbol("e"), Sqrt(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"),
					symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))))), CN1))), And(FreeQ(List(
					symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")), NonzeroQ(Plus(Plus(
					Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Power(Plus(Plus(pattern("a"), Times(pattern("b", null, true), Cos(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(pattern("d", null, true), Times(pattern("e", null,
					true), pattern("x")))))), pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Plus(Times(Times(CN1, symbol("c")), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))), Times(
					symbol("b"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(Power(Plus(Plus(symbol("a"), Times(
					symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(
					symbol("e"), symbol("x")))))), Plus(symbol("n"), C1)), Power(Times(Times(symbol("e"), Plus(symbol("n"), C1)), Plus(Plus(
					Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))), CN1))), Dist(Times(C1,
					Power(Times(Plus(symbol("n"), C1), Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1,
							Power(symbol("c"), C2)))), CN1)), Int(Times(Plus(Plus(Times(Plus(symbol("n"), C1), symbol("a")), Times(CN1, Times(
					Times(Plus(symbol("n"), C2), symbol("b")), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Times(CN1, Times(
					Times(Plus(symbol("n"), C2), symbol("c")), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Power(Plus(Plus(
					symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(symbol("n"), C1))), symbol("x")))), And(And(And(FreeQ(List(
					symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e")), symbol("x")), RationalQ(symbol("n"))), Less(
					symbol("n"), CN1)), NonzeroQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(
					symbol("c"), C2)))))),
			Int(Times(Plus(pattern("A", null, true), Times(pattern("C", null, true), Sin(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Power(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true), Cos(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(Times(CN1, symbol("b")), symbol("C")), Times(Log(Plus(Plus(symbol("a"), Times(symbol("b"),
					Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"),
					symbol("x"))))))), Power(Times(symbol("e"), Plus(Power(symbol("b"), C2), Power(symbol("c"), C2))), CN1))), Times(Times(
					symbol("c"), symbol("C")), Times(Plus(symbol("d"), Times(symbol("e"), symbol("x"))), Power(Times(symbol("e"), Plus(Power(
					symbol("b"), C2), Power(symbol("c"), C2))), CN1)))), Dist(Plus(symbol("A"), Times(CN1, Times(Times(symbol("a"),
					symbol("c")), Times(symbol("C"), Power(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)), CN1))))), Int(Times(C1,
					Power(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(
							symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), CN1)), symbol("x")))), And(And(FreeQ(List(
					symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e"), symbol("A"), symbol("C")), symbol("x")), NonzeroQ(Plus(
					Power(symbol("b"), C2), Power(symbol("c"), C2)))), NonzeroQ(Plus(symbol("A"), Times(CN1, Times(Times(symbol("a"),
					symbol("c")), Times(symbol("C"), Power(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)), CN1)))))))),
			Int(Times(Plus(pattern("A", null, true), Times(pattern("B", null, true), Cos(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Power(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true), Cos(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), CN1)), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(symbol("c"), symbol("B")), Times(Log(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"),
					Times(symbol("e"), symbol("x"))))))),
					Power(Times(symbol("e"), Plus(Power(symbol("b"), C2), Power(symbol("c"), C2))), CN1))), Times(Times(symbol("b"),
					symbol("B")), Times(Plus(symbol("d"), Times(symbol("e"), symbol("x"))), Power(Times(symbol("e"), Plus(Power(symbol("b"),
					C2), Power(symbol("c"), C2))), CN1)))), Dist(Plus(symbol("A"), Times(CN1, Times(Times(symbol("a"), symbol("b")), Times(
					symbol("B"), Power(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)), CN1))))), Int(Times(C1, Power(Plus(Plus(
					symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), CN1)), symbol("x")))), And(And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d"), symbol("e"), symbol("A"), symbol("B")), symbol("x")), NonzeroQ(Plus(Power(symbol("b"), C2),
					Power(symbol("c"), C2)))), NonzeroQ(Plus(symbol("A"), Times(CN1, Times(Times(symbol("a"), symbol("b")), Times(
					symbol("B"), Power(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)), CN1)))))))),
			Int(Times(Plus(Plus(pattern("A", null, true), Times(pattern("B", null, true), Cos(Plus(pattern("d", null, true), Times(
					pattern("e", null, true), pattern("x")))))), Times(pattern("C", null, true), Sin(Plus(pattern("d", null, true), Times(
					pattern("e", null, true), pattern("x")))))), Power(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true),
					Cos(Plus(pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true),
					Sin(Plus(pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), CN1)), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Plus(Times(Plus(Times(symbol("c"), symbol("B")), Times(CN1, Times(symbol("b"), symbol("C")))), Times(Log(Plus(
					Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"),
							Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Power(Times(symbol("e"), Plus(Power(symbol("b"), C2),
					Power(symbol("c"), C2))), CN1))), Times(Plus(Times(symbol("b"), symbol("B")), Times(symbol("c"), symbol("C"))), Times(
					Plus(symbol("d"), Times(symbol("e"), symbol("x"))), Power(Times(symbol("e"), Plus(Power(symbol("b"), C2), Power(
							symbol("c"), C2))), CN1)))), Dist(Plus(symbol("A"), Times(CN1, Times(symbol("a"), Times(Plus(Times(symbol("b"),
					symbol("B")), Times(symbol("c"), symbol("C"))), Power(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)), CN1))))),
					Int(Times(C1, Power(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))),
							Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), CN1)), symbol("x")))), And(And(
					FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"), symbol("e"), symbol("A"), symbol("B"), symbol("C")),
							symbol("x")), NonzeroQ(Plus(Power(symbol("b"), C2), Power(symbol("c"), C2)))), NonzeroQ(Plus(symbol("A"), Times(CN1,
					Times(symbol("a"), Times(Plus(Times(symbol("b"), symbol("B")), Times(symbol("c"), symbol("C"))), Power(Plus(Power(
							symbol("b"), C2), Power(symbol("c"), C2)), CN1)))))))),
			Int(Times(Plus(pattern("A", null, true), Times(pattern("C", null, true), Sin(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Power(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true), Cos(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Plus(Plus(Times(symbol("b"), symbol("C")), Times(Plus(Times(symbol("a"), symbol("C")), Times(CN1, Times(
					symbol("c"), symbol("A")))), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(Times(symbol("b"),
					symbol("A")), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(Power(Plus(Plus(symbol("a"), Times(
					symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(
					symbol("e"), symbol("x")))))), Plus(symbol("n"), C1)), Power(Times(Times(symbol("e"), Plus(symbol("n"), C1)), Plus(Plus(
					Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))), CN1))), Dist(Times(C1,
					Power(Times(Plus(symbol("n"), C1), Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1,
							Power(symbol("c"), C2)))), CN1)), Int(Times(Plus(Plus(Times(Plus(symbol("n"), C1), Plus(Times(symbol("a"),
					symbol("A")), Times(CN1, Times(symbol("c"), symbol("C"))))), Times(CN1, Times(Times(Times(Plus(symbol("n"), C2),
					symbol("b")), symbol("A")), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Times(Times(
					Plus(symbol("n"), C2), Plus(Times(symbol("a"), symbol("C")), Times(CN1, Times(symbol("c"), symbol("A"))))), Sin(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Power(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"),
					Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(
					symbol("n"), C1))), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"),
					symbol("e"), symbol("A"), symbol("C")), symbol("x")), RationalQ(symbol("n"))), Less(symbol("n"), CN1)), NonzeroQ(Plus(
					Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Times(Plus(pattern("A", null, true), Times(pattern("B", null, true), Cos(Plus(pattern("d", null, true), Times(pattern(
					"e", null, true), pattern("x")))))), Power(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true), Cos(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true), Sin(Plus(
					pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Plus(Plus(Times(symbol("c"), symbol("B")), Times(Times(symbol("c"), symbol("A")), Cos(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Times(Plus(Times(symbol("a"), symbol("B")), Times(CN1, Times(
					symbol("b"), symbol("A")))), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Times(Power(Plus(Plus(
					symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(symbol("n"), C1)), Power(Times(Times(symbol("e"), Plus(
					symbol("n"), C1)), Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"),
					C2)))), CN1))), Dist(Times(C1, Power(Times(Plus(symbol("n"), C1), Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(
					symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))), CN1)), Int(Times(Plus(Plus(Times(Plus(symbol("n"), C1), Plus(
					Times(symbol("a"), symbol("A")), Times(CN1, Times(symbol("b"), symbol("B"))))), Times(Times(Plus(symbol("n"), C2), Plus(
					Times(symbol("a"), symbol("B")), Times(CN1, Times(symbol("b"), symbol("A"))))), Cos(Plus(symbol("d"), Times(symbol("e"),
					symbol("x")))))), Times(CN1, Times(Times(Times(Plus(symbol("n"), C2), symbol("c")), symbol("A")), Sin(Plus(symbol("d"),
					Times(symbol("e"), symbol("x"))))))), Power(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(
					symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(
					symbol("n"), C1))), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"),
					symbol("e"), symbol("A"), symbol("B")), symbol("x")), RationalQ(symbol("n"))), Less(symbol("n"), CN1)), NonzeroQ(Plus(
					Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Times(Plus(Plus(pattern("A", null, true), Times(pattern("B", null, true), Cos(Plus(pattern("d", null, true), Times(
					pattern("e", null, true), pattern("x")))))), Times(pattern("C", null, true), Sin(Plus(pattern("d", null, true), Times(
					pattern("e", null, true), pattern("x")))))), Power(Plus(Plus(pattern("a", null, true), Times(pattern("b", null, true),
					Cos(Plus(pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), Times(pattern("c", null, true),
					Sin(Plus(pattern("d", null, true), Times(pattern("e", null, true), pattern("x")))))), pattern("n"))), pattern("x",
					symbol("Symbol"))),
			Condition(Plus(Times(Times(CN1, Plus(Plus(Plus(Times(symbol("c"), symbol("B")), Times(CN1, Times(symbol("b"), symbol("C")))),
					Times(CN1, Times(Plus(Times(symbol("a"), symbol("C")), Times(CN1, Times(symbol("c"), symbol("A")))), Cos(Plus(
							symbol("d"), Times(symbol("e"), symbol("x"))))))), Times(Plus(Times(symbol("a"), symbol("B")), Times(CN1, Times(
					symbol("b"), symbol("A")))), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x"))))))), Times(Power(Plus(Plus(
					symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(symbol("n"), C1)), Power(Times(Times(symbol("e"), Plus(
					symbol("n"), C1)), Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"),
					C2)))), CN1))), Dist(Times(C1, Power(Times(Plus(symbol("n"), C1), Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(
					symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))), CN1)), Int(Times(Plus(Plus(Times(Plus(symbol("n"), C1), Plus(
					Plus(Times(symbol("a"), symbol("A")), Times(CN1, Times(symbol("b"), symbol("B")))), Times(CN1, Times(symbol("c"),
							symbol("C"))))), Times(Times(Plus(symbol("n"), C2), Plus(Times(symbol("a"), symbol("B")), Times(CN1, Times(
					symbol("b"), symbol("A"))))), Cos(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Times(Times(
					Plus(symbol("n"), C2), Plus(Times(symbol("a"), symbol("C")), Times(CN1, Times(symbol("c"), symbol("A"))))), Sin(Plus(
					symbol("d"), Times(symbol("e"), symbol("x")))))), Power(Plus(Plus(symbol("a"), Times(symbol("b"), Cos(Plus(symbol("d"),
					Times(symbol("e"), symbol("x")))))), Times(symbol("c"), Sin(Plus(symbol("d"), Times(symbol("e"), symbol("x")))))), Plus(
					symbol("n"), C1))), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d"),
					symbol("e"), symbol("A"), symbol("B"), symbol("C")), symbol("x")), RationalQ(symbol("n"))), Less(symbol("n"), CN1)),
					NonzeroQ(Plus(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))), Times(CN1, Power(symbol("c"), C2)))))),
			Int(Times(Power(Sec(pattern("v")), pattern("m", null, true)), Power(Plus(pattern("a"), Times(pattern("b", null, true),
					Tan(pattern("v")))), pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Int(Power(Plus(Times(symbol("a"), Cos(symbol("v"))), Times(symbol("b"), Sin(symbol("v")))), symbol("n")),
					symbol("x")), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(List(symbol("m"), symbol("n")))),
					Equal(Plus(symbol("m"), symbol("n")), C0))),
			Int(Times(Power(Csc(pattern("v")), pattern("m", null, true)), Power(Plus(pattern("a"), Times(pattern("b", null, true),
					Cot(pattern("v")))), pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Int(Power(Plus(Times(symbol("b"), Cos(symbol("v"))), Times(symbol("a"), Sin(symbol("v")))), symbol("n")),
					symbol("x")), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(List(symbol("m"), symbol("n")))),
					Equal(Plus(symbol("m"), symbol("n")), C0))),
			Int(Times(Power(symbol("E"), Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), Sin(Plus(
					pattern("c", null, true), Times(pattern("d", null, true), pattern("x"))))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Times(Times(CN1, symbol("d")), Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))),
					Times(Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Power(
							Plus(Power(symbol("b"), C2), Power(symbol("d"), C2)), CN1))), Times(Times(symbol("b"), Power(symbol("E"), Plus(
					symbol("a"), Times(symbol("b"), symbol("x"))))), Times(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Power(
					Plus(Power(symbol("b"), C2), Power(symbol("d"), C2)), CN1)))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"),
					symbol("d")), symbol("x")), NonzeroQ(Plus(Power(symbol("b"), C2), Power(symbol("d"), C2))))),
			Int(Times(Power(symbol("E"), Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), Power(Sin(Plus(
					pattern("c", null, true), Times(pattern("d", null, true), pattern("x")))), pattern("n"))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(symbol("b"), Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), Times(
					Power(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), symbol("n")), Power(Plus(Power(symbol("b"), C2), Times(
							Power(symbol("d"), C2), Power(symbol("n"), C2))), CN1))), Times(CN1, Times(Times(Times(
					Times(symbol("d"), symbol("n")), Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), Cos(Plus(
					symbol("c"), Times(symbol("d"), symbol("x"))))), Times(Power(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))),
					Plus(symbol("n"), Times(CN1, C1))), Power(Plus(Power(symbol("b"), C2), Times(Power(symbol("d"), C2), Power(symbol("n"),
					C2))), CN1))))), Dist(Times(Times(symbol("n"), Plus(symbol("n"), Times(CN1, C1))), Times(Power(symbol("d"), C2), Power(
					Plus(Power(symbol("b"), C2), Times(Power(symbol("d"), C2), Power(symbol("n"), C2))), CN1))), Int(Times(Power(symbol("E"),
					Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Power(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(
					symbol("n"), Times(CN1, C2)))), symbol("x")))), And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")),
					symbol("x")), RationalQ(symbol("n"))), Greater(symbol("n"), C1))),
			Int(Times(Power(symbol("E"), Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), Cos(Plus(
					pattern("c", null, true), Times(pattern("d", null, true), pattern("x"))))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Times(Times(symbol("b"), Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))),
					Times(Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Power(
							Plus(Power(symbol("b"), C2), Power(symbol("d"), C2)), CN1))), Times(Times(symbol("d"), Power(symbol("E"), Plus(
					symbol("a"), Times(symbol("b"), symbol("x"))))), Times(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Power(
					Plus(Power(symbol("b"), C2), Power(symbol("d"), C2)), CN1)))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"),
					symbol("d")), symbol("x")), NonzeroQ(Plus(Power(symbol("b"), C2), Power(symbol("d"), C2))))),
			Int(Times(Power(symbol("E"), Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), Power(Cos(Plus(
					pattern("c", null, true), Times(pattern("d", null, true), pattern("x")))), pattern("n"))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(symbol("b"), Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), Times(
					Power(Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), symbol("n")), Power(Plus(Power(symbol("b"), C2), Times(
							Power(symbol("d"), C2), Power(symbol("n"), C2))), CN1))), Times(Times(Times(Times(symbol("d"), symbol("n")), Power(
					symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), Power(Cos(Plus(symbol("c"), Times(symbol("d"),
					symbol("x")))), Plus(symbol("n"), Times(CN1, C1)))), Times(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))),
					Power(Plus(Power(symbol("b"), C2), Times(Power(symbol("d"), C2), Power(symbol("n"), C2))), CN1)))), Dist(Times(Times(
					symbol("n"), Plus(symbol("n"), Times(CN1, C1))), Times(Power(symbol("d"), C2), Power(Plus(Power(symbol("b"), C2), Times(
					Power(symbol("d"), C2), Power(symbol("n"), C2))), CN1))), Int(Times(Power(symbol("E"), Plus(symbol("a"), Times(
					symbol("b"), symbol("x")))), Power(Cos(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1,
					C2)))), symbol("x")))), And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")), symbol("x")),
					RationalQ(symbol("n"))), Greater(symbol("n"), C1))),
			Int(Times(Power(symbol("E"), Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), Power(Sec(Plus(
					pattern("c", null, true), Times(pattern("d", null, true), pattern("x")))), pattern("n"))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(Times(CN1, symbol("b")), Power(symbol("E"),
					Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), Times(Power(
					Sec(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C2))), Power(Times(Times(Power(
					symbol("d"), C2), Plus(symbol("n"), Times(CN1, C1))), Plus(symbol("n"), Times(CN1, C2))), CN1))), Times(Times(Power(
					symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Power(Sec(Plus(symbol("c"), Times(symbol("d"),
					symbol("x")))), Plus(symbol("n"), Times(CN1, C1)))), Times(Sin(Plus(symbol("c"), Times(symbol("d"), symbol("x")))),
					Power(Times(symbol("d"), Plus(symbol("n"), Times(CN1, C1))), CN1)))), Dist(Times(Plus(Power(symbol("b"), C2), Times(
					Power(symbol("d"), C2), Power(Plus(symbol("n"), Times(CN1, C2)), C2))), Power(Times(Times(Power(symbol("d"), C2), Plus(
					symbol("n"), Times(CN1, C1))), Plus(symbol("n"), Times(CN1, C2))), CN1)), Int(Times(Power(symbol("E"), Plus(symbol("a"),
					Times(symbol("b"), symbol("x")))), Power(Sec(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"),
					Times(CN1, C2)))), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"), symbol("c"), symbol("d")),
					symbol("x")), RationalQ(symbol("n"))), Greater(symbol("n"), C1)), Unequal(symbol("n"), C2))),
			Int(Times(Power(symbol("E"), Plus(pattern("a", null, true), Times(pattern("b", null, true), pattern("x")))), Power(Csc(Plus(
					pattern("c", null, true), Times(pattern("d", null, true), pattern("x")))), pattern("n"))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Plus(Times(Times(Times(CN1, symbol("b")), Power(symbol("E"),
					Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), Times(Power(
					Csc(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"), Times(CN1, C2))), Power(Times(Times(Power(
					symbol("d"), C2), Plus(symbol("n"), Times(CN1, C1))), Plus(symbol("n"), Times(CN1, C2))), CN1))), Times(CN1, Times(Times(
					Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Cos(Plus(symbol("c"), Times(symbol("d"),
							symbol("x"))))), Times(Power(Csc(Plus(symbol("c"), Times(symbol("d"), symbol("x")))), Plus(symbol("n"),
					Times(CN1, C1))), Power(Times(symbol("d"), Plus(symbol("n"), Times(CN1, C1))), CN1))))), Dist(Times(Plus(Power(
					symbol("b"), C2), Times(Power(symbol("d"), C2), Power(Plus(symbol("n"), Times(CN1, C2)), C2))), Power(Times(Times(Power(
					symbol("d"), C2), Plus(symbol("n"), Times(CN1, C1))), Plus(symbol("n"), Times(CN1, C2))), CN1)), Int(Times(Power(
					symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Power(Csc(Plus(symbol("c"), Times(symbol("d"),
					symbol("x")))), Plus(symbol("n"), Times(CN1, C2)))), symbol("x")))), And(And(And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d")), symbol("x")), RationalQ(symbol("n"))), Greater(symbol("n"), C1)), Unequal(symbol("n"), C2))),
			Int(Times(Times(Power(pattern("x"), pattern("m", null, true)), Power(symbol("E"), Plus(pattern("a", null, true), Times(
					pattern("b", null, true), pattern("x"))))), Power(Sin(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))), pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Module(List(Set(symbol("u"), Block(List(Set(symbol("ShowSteps"), symbol("False")), Set(symbol("StepCounter"),
					symbol("Null"))), Int(Times(Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Power(Sin(Plus(
					symbol("c"), Times(symbol("d"), symbol("x")))), symbol("n"))), symbol("x"))))), Plus(Times(
					Power(symbol("x"), symbol("m")), symbol("u")), Times(CN1, Dist(symbol("m"), Int(Times(Power(symbol("x"), Plus(
					symbol("m"), Times(CN1, C1))), symbol("u")), symbol("x")))))), And(And(And(And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d")), symbol("x")), RationalQ(symbol("m"))), IntegerQ(symbol("n"))), Greater(symbol("m"), C0)),
					Greater(symbol("n"), C0))),
			Int(Times(Times(Power(pattern("x"), pattern("m", null, true)), Power(symbol("E"), Plus(pattern("a", null, true), Times(
					pattern("b", null, true), pattern("x"))))), Power(Cos(Plus(pattern("c", null, true), Times(pattern("d", null, true),
					pattern("x")))), pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Module(List(Set(symbol("u"), Block(List(Set(symbol("ShowSteps"), symbol("False")), Set(symbol("StepCounter"),
					symbol("Null"))), Int(Times(Power(symbol("E"), Plus(symbol("a"), Times(symbol("b"), symbol("x")))), Power(Cos(Plus(
					symbol("c"), Times(symbol("d"), symbol("x")))), symbol("n"))), symbol("x"))))), Plus(Times(
					Power(symbol("x"), symbol("m")), symbol("u")), Times(CN1, Dist(symbol("m"), Int(Times(Power(symbol("x"), Plus(
					symbol("m"), Times(CN1, C1))), symbol("u")), symbol("x")))))), And(And(And(And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c"), symbol("d")), symbol("x")), RationalQ(symbol("m"))), IntegerQ(symbol("n"))), Greater(symbol("m"), C0)),
					Greater(symbol("n"), C0))),
			Int(Times(Power(pattern("f"), pattern("v")), Sin(pattern("w"))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Dist(Times(CI, C1D2), Int(Times(Power(symbol("f"), symbol("v")), Power(Power(symbol("E"), Times(CI,
					symbol("w"))), CN1)), symbol("x"))), Times(CN1, Dist(Times(CI, C1D2), Int(Times(Power(symbol("f"), symbol("v")), Power(
					symbol("E"), Times(CI, symbol("w")))), symbol("x"))))), And(And(And(And(FreeQ(symbol("f"), symbol("x")), PolynomialQ(
					symbol("v"), symbol("x"))), LessEqual(Exponent(symbol("v"), symbol("x")), C2)), PolynomialQ(symbol("w"), symbol("x"))),
					LessEqual(Exponent(symbol("w"), symbol("x")), C2))),
			Int(Times(Power(pattern("f"), pattern("v")), Power(Sin(pattern("w")), pattern("n"))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(Times(CI, C1D2), symbol("n")), Int(
					Times(Power(symbol("f"), symbol("v")), Power(Plus(Times(C1, Power(Power(symbol("E"), Times(CI, symbol("w"))), CN1)),
							Times(CN1, Power(symbol("E"), Times(CI, symbol("w"))))), symbol("n"))), symbol("x"))), And(And(And(And(And(And(FreeQ(
					symbol("f"), symbol("x")), IntegerQ(symbol("n"))), Greater(symbol("n"), C0)), PolynomialQ(symbol("v"), symbol("x"))),
					LessEqual(Exponent(symbol("v"), symbol("x")), C2)), PolynomialQ(symbol("w"), symbol("x"))), LessEqual(Exponent(
					symbol("w"), symbol("x")), C2))),
			Int(Times(Power(pattern("f"), pattern("v")), Cos(pattern("w"))), pattern("x", symbol("Symbol"))),
			Condition(Plus(Dist(C1D2,
					Int(Times(Power(symbol("f"), symbol("v")), Power(symbol("E"), Times(CI, symbol("w")))), symbol("x"))), Dist(C1D2, Int(
					Times(Power(symbol("f"), symbol("v")), Power(Power(symbol("E"), Times(CI, symbol("w"))), CN1)), symbol("x")))), And(And(
					And(And(FreeQ(symbol("f"), symbol("x")), PolynomialQ(symbol("v"), symbol("x"))), LessEqual(Exponent(symbol("v"),
							symbol("x")), C2)), PolynomialQ(symbol("w"), symbol("x"))), LessEqual(Exponent(symbol("w"), symbol("x")), C2))),
			Int(Times(Power(pattern("f"), pattern("v")), Power(Cos(pattern("w")), pattern("n"))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Times(C1, Power(Power(C2, symbol("n")), CN1)), Int(Times(Power(symbol("f"), symbol("v")), Power(Plus(Power(
					symbol("E"), Times(CI, symbol("w"))), Times(C1, Power(Power(symbol("E"), Times(CI, symbol("w"))), CN1))), symbol("n"))),
					symbol("x"))), And(And(And(And(
					And(And(FreeQ(symbol("f"), symbol("x")), IntegerQ(symbol("n"))), Greater(symbol("n"), C0)), PolynomialQ(symbol("v"),
							symbol("x"))), LessEqual(Exponent(symbol("v"), symbol("x")), C2)), PolynomialQ(symbol("w"), symbol("x"))), LessEqual(
					Exponent(symbol("w"), symbol("x")), C2))),
			Int(Times(pattern("u", null, true), Power(Plus(pattern("a"), Times(pattern("b", null, true), Power(Sin(pattern("v")), C2))),
					pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("a"), symbol("n")), Int(Times(symbol("u"), Power(Cos(symbol("v")), Times(C2, symbol("n")))),
					symbol("x"))), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(symbol("n"))), ZeroQ(Plus(
					symbol("a"), symbol("b"))))),
			Int(Times(pattern("u", null, true), Power(Plus(pattern("a"), Times(pattern("b", null, true), Power(Cos(pattern("v")), C2))),
					pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("a"), symbol("n")), Int(Times(symbol("u"), Power(Sin(symbol("v")), Times(C2, symbol("n")))),
					symbol("x"))), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(symbol("n"))), ZeroQ(Plus(
					symbol("a"), symbol("b"))))),
			Int(Times(pattern("u", null, true), Power(Plus(pattern("a"), Times(pattern("b", null, true), Power(Tan(pattern("v")), C2))),
					pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("a"), symbol("n")), Int(Times(symbol("u"), Power(Sec(symbol("v")), Times(C2, symbol("n")))),
					symbol("x"))), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(symbol("n"))), ZeroQ(Plus(
					symbol("a"), Times(CN1, symbol("b")))))),
			Int(Times(pattern("u", null, true), Power(Plus(pattern("a"), Times(pattern("b", null, true), Power(Cot(pattern("v")), C2))),
					pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("a"), symbol("n")), Int(Times(symbol("u"), Power(Csc(symbol("v")), Times(C2, symbol("n")))),
					symbol("x"))), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(symbol("n"))), ZeroQ(Plus(
					symbol("a"), Times(CN1, symbol("b")))))),
			Int(Times(pattern("u", null, true), Power(Plus(pattern("a"), Times(pattern("b", null, true), Power(Sec(pattern("v")), C2))),
					pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("b"), symbol("n")), Int(Times(symbol("u"), Power(Tan(symbol("v")), Times(C2, symbol("n")))),
					symbol("x"))), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(symbol("n"))), ZeroQ(Plus(
					symbol("a"), symbol("b"))))),
			Int(Times(pattern("u", null, true), Power(Plus(pattern("a"), Times(pattern("b", null, true), Power(Csc(pattern("v")), C2))),
					pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("b"), symbol("n")), Int(Times(symbol("u"), Power(Cot(symbol("v")), Times(C2, symbol("n")))),
					symbol("x"))), And(And(FreeQ(List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(symbol("n"))), ZeroQ(Plus(
					symbol("a"), symbol("b"))))),
			Int(Power(Plus(Times(pattern("a", null, true), Tan(pattern("v"))), Times(pattern("b", null, true), Sec(pattern("v")))),
					pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("a"), symbol("n")), Int(Power(Tan(Plus(Times(symbol("v"), C1D2), Times(Times(symbol("a"), Power(
					symbol("b"), CN1)), Times(symbol("Pi"), C1D4)))), symbol("n")), symbol("x"))), And(And(FreeQ(List(symbol("a"),
					symbol("b")), symbol("x")), ZeroQ(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))))), EvenQ(symbol("n")))),
			Int(
					Times(pattern("u", null, true), Power(Plus(Times(pattern("a", null, true), Power(Sec(pattern("v")), pattern("m", null,
							true))), Times(pattern("b", null, true), Power(Tan(pattern("v")), pattern("m", null, true)))), pattern("n", null,
							true))), pattern("x", symbol("Symbol"))),
			Condition(Int(Times(symbol("u"), Times(Power(Plus(symbol("a"), Times(symbol("b"), Power(Sin(symbol("v")), symbol("m")))),
					symbol("n")), Power(Power(Cos(symbol("v")), Times(symbol("m"), symbol("n"))), CN1))), symbol("x")), And(And(And(FreeQ(
					List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(List(symbol("m"), symbol("n")))), Or(OddQ(Times(symbol("m"),
					symbol("n"))), Less(Times(symbol("m"), symbol("n")), C0))), Not(And(Equal(symbol("m"), C2), ZeroQ(Plus(symbol("a"),
					symbol("b"))))))),
			Int(Power(Plus(Times(pattern("a", null, true), Cot(pattern("v"))), Times(pattern("b", null, true), Csc(pattern("v")))),
					pattern("n")), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(symbol("a"), symbol("n")), Int(Power(Cot(Plus(Times(symbol("v"), C1D2), Times(Plus(Times(symbol("a"),
					Power(symbol("b"), CN1)), Times(CN1, C1)), Times(symbol("Pi"), C1D4)))), symbol("n")), symbol("x"))), And(And(FreeQ(List(
					symbol("a"), symbol("b")), symbol("x")), ZeroQ(Plus(Power(symbol("a"), C2), Times(CN1, Power(symbol("b"), C2))))),
					EvenQ(symbol("n")))),
			Int(
					Times(pattern("u", null, true), Power(Plus(Times(pattern("a", null, true), Power(Csc(pattern("v")), pattern("m", null,
							true))), Times(pattern("b", null, true), Power(Cot(pattern("v")), pattern("m", null, true)))), pattern("n", null,
							true))), pattern("x", symbol("Symbol"))),
			Condition(Int(Times(symbol("u"), Times(Power(Plus(symbol("a"), Times(symbol("b"), Power(Cos(symbol("v")), symbol("m")))),
					symbol("n")), Power(Power(Sin(symbol("v")), Times(symbol("m"), symbol("n"))), CN1))), symbol("x")), And(And(And(FreeQ(
					List(symbol("a"), symbol("b")), symbol("x")), IntegerQ(List(symbol("m"), symbol("n")))), Or(OddQ(Times(symbol("m"),
					symbol("n"))), Less(Times(symbol("m"), symbol("n")), C0))), Not(And(Equal(symbol("m"), C2), ZeroQ(Plus(symbol("a"),
					symbol("b"))))))),
			Int(Times(Times(Power(pattern("x"), pattern("m", null, true)), Power(Sec(pattern("v")), pattern("n", null, true))), Power(
					Csc(pattern("v")), pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Power(C2, symbol("n")), Int(Times(Power(symbol("x"), symbol("m")), Power(Csc(Dist(C2, symbol("v"))),
					symbol("n"))), symbol("x"))), And(IntegerQ(List(symbol("m"), symbol("n"))), Greater(symbol("m"), C0))),
			Int(Times(pattern("u"), Cos(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null, true),
					pattern("x")))))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Times(C1, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(SubstFor(Sin(Times(symbol("c"),
					Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x")), symbol("x")), symbol("x")), symbol("x"),
					Sin(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))))), And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c")), symbol("x")), FunctionOfQ(Sin(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))),
					symbol("u"), symbol("x"), symbol("True")))),
			Int(Times(pattern("u"), Cot(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null, true),
					pattern("x")))))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Times(C1, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(Times(SubstFor(Sin(Times(
					symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x")), Power(symbol("x"), CN1)),
					symbol("x")), symbol("x")), symbol("x"), Sin(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))))),
					And(FreeQ(List(symbol("a"), symbol("b"), symbol("c")), symbol("x")), FunctionOfQ(Sin(Times(symbol("c"), Plus(symbol("a"),
							Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x"), symbol("True")))),
			Int(Times(pattern("u"), Sin(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null, true),
					pattern("x")))))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Times(C4, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(Times(symbol("x"), SubstFor(
					Sin(Times(symbol("c"), Times(Plus(symbol("a"), Times(symbol("b"), symbol("x"))), C1D2))), symbol("u"), symbol("x"))),
					symbol("x")), symbol("x")), symbol("x"), Sin(Times(symbol("c"), Times(Plus(symbol("a"), Times(symbol("b"), symbol("x"))),
					C1D2))))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c")), symbol("x")), FunctionOfQ(Sin(Times(symbol("c"), Times(
					Plus(symbol("a"), Times(symbol("b"), symbol("x"))), C1D2))), symbol("u"), symbol("x"), symbol("True")))),
			Int(Times(pattern("u"), Sin(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null, true),
					pattern("x")))))), pattern("x", symbol("Symbol"))),
			Condition(Times(CN1, Dist(Times(C1, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(SubstFor(Cos(Times(
					symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x")), symbol("x")), symbol("x")),
					symbol("x"), Cos(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x")))))))), And(FreeQ(List(symbol("a"),
					symbol("b"), symbol("c")), symbol("x")), FunctionOfQ(Cos(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"),
					symbol("x"))))), symbol("u"), symbol("x"), symbol("True")))),
			Int(Times(pattern("u"), Tan(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null, true),
					pattern("x")))))), pattern("x", symbol("Symbol"))),
			Condition(Times(CN1, Dist(Times(C1, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(Times(SubstFor(
					Cos(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x")), Power(
					symbol("x"), CN1)), symbol("x")), symbol("x")), symbol("x"), Cos(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"),
					symbol("x")))))))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c")), symbol("x")), FunctionOfQ(Cos(Times(
					symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x"), symbol("True")))),
			Int(Times(pattern("u"), Sin(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null, true),
					pattern("x")))))), pattern("x", symbol("Symbol"))),
			Condition(Times(CN1, Dist(Times(C4, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(Times(symbol("x"),
					SubstFor(Cos(Times(symbol("c"), Times(Plus(symbol("a"), Times(symbol("b"), symbol("x"))), C1D2))), symbol("u"),
							symbol("x"))), symbol("x")), symbol("x")), symbol("x"), Cos(Times(symbol("c"), Times(Plus(symbol("a"), Times(
					symbol("b"), symbol("x"))), C1D2)))))), And(FreeQ(List(symbol("a"), symbol("b"), symbol("c")), symbol("x")), FunctionOfQ(
					Cos(Times(symbol("c"), Times(Plus(symbol("a"), Times(symbol("b"), symbol("x"))), C1D2))), symbol("u"), symbol("x"),
					symbol("True")))),
			Int(Times(pattern("u"), Power(Tan(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null,
					true), pattern("x"))))), pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Times(CN1, Dist(Times(C1, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(Times(SubstFor(
					Cot(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x")), Power(Times(
					Power(symbol("x"), symbol("n")), Plus(C1, Power(symbol("x"), C2))), CN1)), symbol("x")), symbol("x")), symbol("x"),
					Cot(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x")))))))), And(And(And(FreeQ(List(symbol("a"),
					symbol("b"), symbol("c")), symbol("x")), IntegerQ(symbol("n"))), FunctionOfQ(Cot(Times(symbol("c"), Plus(symbol("a"),
					Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x"), symbol("True"))), TryPureTanSubst(Times(symbol("u"), Power(
					Tan(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("n"))), symbol("x")))),
			Int(pattern("u"), pattern("x", symbol("Symbol"))),
			Module(List(Set(symbol("v"), FunctionOfTrig(symbol("u"), symbol("x")))), Condition(Dist(Times(CN1, Power(Coefficient(
					symbol("v"), symbol("x"), C1), CN1)), Subst(Int(Regularize(Times(SubstFor(Cot(symbol("v")), symbol("u"), symbol("x")),
					Power(Plus(C1, Power(symbol("x"), C2)), CN1)), symbol("x")), symbol("x")), symbol("x"), Cot(symbol("v")))), And(And(
					NotFalseQ(symbol("v")), FunctionOfQ(Cot(symbol("v")), symbol("u"), symbol("x"), symbol("True"))), TryPureTanSubst(
					symbol("u"), symbol("x"))))),
			Int(Times(pattern("u"), Power(Cot(Times(pattern("c", null, true), Plus(pattern("a", null, true), Times(pattern("b", null,
					true), pattern("x"))))), pattern("n", null, true))), pattern("x", symbol("Symbol"))),
			Condition(Dist(Times(C1, Power(Times(symbol("b"), symbol("c")), CN1)), Subst(Int(Regularize(Times(SubstFor(Tan(Times(
					symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("u"), symbol("x")), Power(Times(Power(
					symbol("x"), symbol("n")), Plus(C1, Power(symbol("x"), C2))), CN1)), symbol("x")), symbol("x")), symbol("x"), Tan(Times(
					symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))))), And(And(And(FreeQ(List(symbol("a"), symbol("b"),
					symbol("c")), symbol("x")), IntegerQ(symbol("n"))), FunctionOfQ(Tan(Times(symbol("c"), Plus(symbol("a"), Times(
					symbol("b"), symbol("x"))))), symbol("u"), symbol("x"), symbol("True"))), TryPureTanSubst(Times(symbol("u"), Power(
					Cot(Times(symbol("c"), Plus(symbol("a"), Times(symbol("b"), symbol("x"))))), symbol("n"))), symbol("x")))),
			Int(pattern("u"), pattern("x", symbol("Symbol"))),
			Module(List(Set(symbol("v"), FunctionOfTrig(symbol("u"), symbol("x")))), Condition(Dist(Times(C1, Power(Coefficient(
					symbol("v"), symbol("x"), C1), CN1)), Subst(Int(Regularize(Times(SubstFor(Tan(symbol("v")), symbol("u"), symbol("x")),
					Power(Plus(C1, Power(symbol("x"), C2)), CN1)), symbol("x")), symbol("x")), symbol("x"), Tan(symbol("v")))), And(And(
					NotFalseQ(symbol("v")), FunctionOfQ(Tan(symbol("v")), symbol("u"), symbol("x"), symbol("True"))), TryPureTanSubst(
					symbol("u"), symbol("x"))))),
			TryPureTanSubst(pattern("u"), pattern("x", symbol("Symbol"))),
			And(And(And(And(Not(MatchQ(symbol("u"), Condition(ArcTan(Times(pattern("a", null, true), Tan(pattern("v")))), FreeQ(
					symbol("a"), symbol("x"))))), Not(MatchQ(symbol("u"), Condition(
					ArcTan(Times(pattern("a", null, true), Cot(pattern("v")))), FreeQ(symbol("a"), symbol("x")))))), Not(MatchQ(symbol("u"),
					Condition(ArcCot(Times(pattern("a", null, true), Tan(pattern("v")))), FreeQ(symbol("a"), symbol("x")))))), Not(MatchQ(
					symbol("u"), Condition(ArcCot(Times(pattern("a", null, true), Cot(pattern("v")))), FreeQ(symbol("a"), symbol("x")))))),
					SameQ(symbol("u"), ExpnExpand(symbol("u"), symbol("x")))),

	};

	public Int() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() == 3 && (ast.get(2).isVector() == 3)) {
		}
		return null;
	}

	public IExpr[] getExprRules() {
		return RULES;
	}

	public void setUp(final ISymbol symbol) throws SyntaxError {
		IExpr[] rules;
		if ((rules = getExprRules()) != null) {
			final EvalEngine engine = EvalEngine.get();
			boolean oldPackageMode = engine.isPackageMode();
			try {
				engine.setPackageMode(true);
				// if (session != null) {
				// parser.setFactory(ExpressionFactory.get());
				// }
				if (Config.DEBUG) {
					try {
						setUpRules(rules, engine);
					} catch (final Throwable th) {
						th.printStackTrace();
					}
				} else {
					setUpRules(rules, engine);
				}
			} finally {
				engine.setPackageMode(oldPackageMode);
			}
		}
		F.SYMBOL_OBSERVER.createPredefinedSymbol(symbol.toString());
	}

	private void setUpRules(final IExpr[] rules, final EvalEngine engine) {
		for (int i = 0; i < rules.length; i += 2) {
			engine.evaluate(F.SetDelayed(rules[i], rules[i + 1]));
		}

	}
}
