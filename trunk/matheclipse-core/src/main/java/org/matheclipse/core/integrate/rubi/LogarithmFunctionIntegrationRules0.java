package org.matheclipse.core.integrate.rubi;


import static org.matheclipse.core.expression.F.*;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.*;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * LogarithmFunctionIntegrationRules from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 * 
 */
public class LogarithmFunctionIntegrationRules0 { 
	public static IAST RULES = List( 
			SetDelayed(Int(Log(Times($p("c",true),Power(Plus($p("a",true),Times($p("b",true),$p("x"))),$p("n",true)))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Plus($s("a"),Times($s("b"),$s("x"))),Times(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Power($s("b"),CN1))),Times(CN1,Times($s("n"),$s("x")))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")))),
			SetDelayed(Int(Times(Log(Times($p("c",true),Plus($p("a",true),Times($p("b",true),$p("x"))))),Power(Plus($p("d"),Times($p("e",true),$p("x"))),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Times(CN1,PolyLog(C2,Plus(Plus(C1,Times(CN1,Times($s("a"),$s("c")))),Times(CN1,Times(Times($s("b"),$s("c")),$s("x")))))),Power($s("e"),CN1)),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e")),$s("x")),ZeroQ(Plus(Plus(Times(Times($s("a"),$s("c")),$s("e")),Times(CN1,Times(Times($s("b"),$s("c")),$s("d")))),Times(CN1,$s("e"))))))),
			SetDelayed(Int(Times(Log(Times($p("c",true),Power(Plus($p("a",true),Times($p("b",true),$p("x"))),$p("n",true)))),Power(Plus($p("d"),Times($p("e",true),$p("x"))),CN1)),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Times(Log(Times($s("b"),Times(Plus($s("d"),Times($s("e"),$s("x"))),Power(Plus(Times($s("b"),$s("d")),Times(CN1,Times($s("a"),$s("e")))),CN1)))),Power($s("e"),CN1))),Times($s("n"),Times(PolyLog(C2,Times(Times(CN1,$s("e")),Times(Plus($s("a"),Times($s("b"),$s("x"))),Power(Plus(Times($s("b"),$s("d")),Times(CN1,Times($s("a"),$s("e")))),CN1)))),Power($s("e"),CN1)))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("n")),$s("x")),NonzeroQ(Plus(Times($s("b"),$s("d")),Times(CN1,Times($s("a"),$s("e")))))))),
			SetDelayed(Int(Log(Times($p("c",true),Power(Times($p("b",true),Power($p("x"),$p("n"))),$p("p",true)))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times($s("x"),Log(Times($s("c"),Power(Times($s("b"),Power($s("x"),$s("n"))),$s("p"))))),Times(CN1,Times(Times($s("n"),$s("p")),$s("x")))),FreeQ(List($s("b"),$s("c"),$s("n"),$s("p")),$s("x")))),
			SetDelayed(Int(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n")))),$p("p",true)))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times($s("x"),Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),$s("p"))))),Times(CN1,Dist(Times(Times($s("b"),$s("n")),$s("p")),Int(Times(C1,Power(Plus($s("b"),Times($s("a"),Power($s("x"),Times(CN1,$s("n"))))),CN1)),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("p")),$s("x")),RationalQ($s("n"))),Less($s("n"),C0)))),
			SetDelayed(Int(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n")))),$p("p",true)))),$p("x",$s("Symbol"))),
			    Condition(Plus(Plus(Times($s("x"),Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),$s("p"))))),Times(CN1,Times(Times($s("n"),$s("p")),$s("x")))),Dist(Times(Times($s("a"),$s("n")),$s("p")),Int(Times(C1,Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),CN1)),$s("x")))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("p")),$s("x")))),
			SetDelayed(Int(Times(Log(Plus(C1,Times($p("b",true),Power($p("x"),$p("n",true))))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Times(CN1,PolyLog(C2,Times(Times(CN1,$s("b")),Power($s("x"),$s("n"))))),Power($s("n"),CN1)),FreeQ(List($s("b"),$s("n")),$s("x")))),
			SetDelayed(Int(Times(Log(Times($p("c",true),Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n",true)))))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Log(Times($s("a"),$s("c"))),Log($s("x"))),Int(Times(Log(Plus(C1,Times($s("b"),Times(Power($s("x"),$s("n")),Power($s("a"),CN1))))),Power($s("x"),CN1)),$s("x"))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")),PositiveQ(Times($s("a"),$s("c")))))),
			SetDelayed(Int(Times(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n",true)))),$p("p",true)))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),$s("p")))),Times(Log(Times(Times(CN1,$s("b")),Times(Power($s("x"),$s("n")),Power($s("a"),CN1)))),Power($s("n"),CN1))),Times(CN1,Dist(Times($s("b"),$s("p")),Int(Times(Power($s("x"),Plus($s("n"),Times(CN1,C1))),Times(Log(Times(Times(CN1,$s("b")),Times(Power($s("x"),$s("n")),Power($s("a"),CN1)))),Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),CN1))),$s("x"))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("p")),$s("x")))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Log(Times($p("c",true),Power(Times($p("b",true),Power($p("x"),$p("n",true))),$p("p",true))))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(Log(Times($s("c"),Power(Times($s("b"),Power($s("x"),$s("n"))),$s("p")))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Times(Times($s("n"),$s("p")),Times(Power($s("x"),Plus($s("m"),C1)),Power(Power(Plus($s("m"),C1),C2),CN1))))),And(FreeQ(List($s("b"),$s("c"),$s("m"),$s("n"),$s("p")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),$p("n",true)))),$p("p",true))))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),$s("p")))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times(Times($s("b"),$s("n")),Times($s("p"),Power(Plus($s("m"),C1),CN1))),Int(Times(Power($s("x"),Plus($s("m"),$s("n"))),Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("n")))),CN1)),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n"),$s("p")),$s("x")),NonzeroQ(Plus($s("m"),C1))),NonzeroQ(Plus(Plus($s("m"),Times(CN1,$s("n"))),C1))))),
			SetDelayed(Int(Times(C1,Power(Log(Times($p("c",true),Plus($p("d",true),Times($p("e",true),$p("x"))))),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(LogIntegral(Times($s("c"),Plus($s("d"),Times($s("e"),$s("x"))))),Power(Times($s("c"),$s("e")),CN1)),FreeQ(List($s("c"),$s("d"),$s("e")),$s("x")))),
			SetDelayed(Int(Times(C1,Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),$p("n",true)))))),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Plus($s("d"),Times($s("e"),$s("x"))),Times(ExpIntegralEi(Times(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))))),Power(Times($s("b"),$s("n")),CN1))),Power(Times(Times(Times(Times($s("b"),$s("e")),$s("n")),Power(E,Times($s("a"),Power(Times($s("b"),$s("n")),CN1)))),Power(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n"))),Times(C1,Power($s("n"),CN1)))),CN1))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("n")),$s("x")))),
			SetDelayed(Int(Times(C1,Power(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),$p("n",true)))))),C1D2),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Times(Power(Pi,C1D2),Plus($s("d"),Times($s("e"),$s("x")))),Times(Erfi(Times(Rt(Times(C1,Power(Times($s("b"),$s("n")),CN1)),C2),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))))),C1D2))),Power(Times(Times(Times(Times(Times($s("b"),$s("e")),$s("n")),Rt(Times(C1,Power(Times($s("b"),$s("n")),CN1)),C2)),Power(E,Times($s("a"),Power(Times($s("b"),$s("n")),CN1)))),Power(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n"))),Times(C1,Power($s("n"),CN1)))),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("n")),$s("x")),PosQ(Times(C1,Power(Times($s("b"),$s("n")),CN1)))))),
			SetDelayed(Int(Times(C1,Power(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),$p("n",true)))))),C1D2),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Times(Power(Pi,C1D2),Plus($s("d"),Times($s("e"),$s("x")))),Times(Erf(Times(Rt(Times(CN1,Power(Times($s("b"),$s("n")),CN1)),C2),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))))),C1D2))),Power(Times(Times(Times(Times(Times($s("b"),$s("e")),$s("n")),Rt(Times(CN1,Power(Times($s("b"),$s("n")),CN1)),C2)),Power(E,Times($s("a"),Power(Times($s("b"),$s("n")),CN1)))),Power(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n"))),Times(C1,Power($s("n"),CN1)))),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("n")),$s("x")),NegQ(Times(C1,Power(Times($s("b"),$s("n")),CN1)))))),
			SetDelayed(Int(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),$p("p")),$p("x",$s("Symbol"))),
			    Condition(Plus(Times($s("x"),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),$s("p"))),Times(CN1,Dist(Times(Times($s("b"),$s("n")),$s("p")),Int(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Plus($s("p"),Times(CN1,C1))),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)))),
			SetDelayed(Int(Power(Log(Times($p("c",true),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),$p("n",true)))),$p("p")),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Plus($s("d"),Times($s("e"),$s("x"))),Times(Power(Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))),$s("p")),Power($s("e"),CN1))),Times(CN1,Dist(Times($s("n"),$s("p")),Int(Power(Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))),Plus($s("p"),Times(CN1,C1))),$s("x"))))),And(And(FreeQ(List($s("c"),$s("d"),$s("e"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)))),
			SetDelayed(Int(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),$p("n",true)))))),$p("p")),$p("x",$s("Symbol"))),
			    Condition(Plus(Times($s("x"),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))))),$s("p"))),Times(CN1,Dist(Times(Times(Times($s("b"),$s("e")),$s("n")),$s("p")),Int(Times($s("x"),Times(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))))),Plus($s("p"),Times(CN1,C1))),Power(Plus($s("d"),Times($s("e"),$s("x"))),CN1))),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)))),
			SetDelayed(Int(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),$p("p")),$p("x",$s("Symbol"))),
			    Condition(Plus(Times($s("x"),Times(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Plus($s("p"),C1)),Power(Times(Times($s("b"),$s("n")),Plus($s("p"),C1)),CN1))),Times(CN1,Dist(Times(C1,Power(Times(Times($s("b"),$s("n")),Plus($s("p"),C1)),CN1)),Int(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Plus($s("p"),C1)),$s("x"))))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")),RationalQ($s("p"))),Less($s("p"),CN1)))),
			SetDelayed(Int(Power(Log(Times($p("c",true),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),$p("n",true)))),$p("p")),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Plus($s("d"),Times($s("e"),$s("x"))),Times(Power(Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))),Plus($s("p"),C1)),Power(Times(Times($s("e"),$s("n")),Plus($s("p"),C1)),CN1))),Times(CN1,Dist(Times(C1,Power(Times($s("n"),Plus($s("p"),C1)),CN1)),Int(Power(Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))),Plus($s("p"),C1)),$s("x"))))),And(And(FreeQ(List($s("c"),$s("d"),$s("e"),$s("n")),$s("x")),RationalQ($s("p"))),Less($s("p"),CN1)))),
			SetDelayed(Int(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),$p("n",true)))))),$p("p")),$p("x",$s("Symbol"))),
			    Condition(Times(Times(Plus($s("d"),Times($s("e"),$s("x"))),Gamma(Plus($s("p"),C1),Times(Times(CN1,Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n"))))))),Power(Times($s("b"),$s("n")),CN1)))),Times(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n")))))),$s("p")),Power(Times(Times(Times($s("e"),Power(Times(Times(CN1,Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n"))))))),Power(Times($s("b"),$s("n")),CN1)),$s("p"))),Power(E,Times($s("a"),Power(Times($s("b"),$s("n")),CN1)))),Power(Times($s("c"),Power(Plus($s("d"),Times($s("e"),$s("x"))),$s("n"))),Times(C1,Power($s("n"),CN1)))),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("n"),$s("p")),$s("x")),NonzeroQ(Plus($s("p"),C1))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Power($s("x"),Plus($s("m"),C1)),Times(ExpIntegralEi(Times(Plus($s("m"),C1),Times(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Power(Times($s("b"),$s("n")),CN1)))),Power(Times(Times(Times($s("b"),$s("n")),Power(E,Times($s("a"),Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1))))),Power(Times($s("c"),Power($s("x"),$s("n"))),Times(Plus($s("m"),C1),Power($s("n"),CN1)))),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),C1D2),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Times(Power(Pi,C1D2),Power($s("x"),Plus($s("m"),C1))),Times(Erfi(Times(Rt(Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1)),C2),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),C1D2))),Power(Times(Times(Times(Times($s("b"),$s("n")),Rt(Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1)),C2)),Power(E,Times($s("a"),Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1))))),Power(Times($s("c"),Power($s("x"),$s("n"))),Times(Plus($s("m"),C1),Power($s("n"),CN1)))),CN1))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n")),$s("x")),NonzeroQ(Plus($s("m"),C1))),PosQ(Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1)))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),C1D2),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Times(Power(Pi,C1D2),Power($s("x"),Plus($s("m"),C1))),Times(Erf(Times(Rt(Times(Times(CN1,Plus($s("m"),C1)),Power(Times($s("b"),$s("n")),CN1)),C2),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),C1D2))),Power(Times(Times(Times(Times($s("b"),$s("n")),Rt(Times(Times(CN1,Plus($s("m"),C1)),Power(Times($s("b"),$s("n")),CN1)),C2)),Power(E,Times($s("a"),Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1))))),Power(Times($s("c"),Power($s("x"),$s("n"))),Times(Plus($s("m"),C1),Power($s("n"),CN1)))),CN1))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n")),$s("x")),NonzeroQ(Plus($s("m"),C1))),NegQ(Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1)))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),$p("p"))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),$s("p")),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times(Times($s("b"),$s("n")),Times($s("p"),Power(Plus($s("m"),C1),CN1))),Int(Times(Power($s("x"),$s("m")),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Plus($s("p"),Times(CN1,C1)))),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)),NonzeroQ(Plus($s("m"),C1))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),$p("p"))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Plus($s("p"),C1)),Power(Times(Times($s("b"),$s("n")),Plus($s("p"),C1)),CN1))),Times(CN1,Dist(Times(Plus($s("m"),C1),Power(Times(Times($s("b"),$s("n")),Plus($s("p"),C1)),CN1)),Int(Times(Power($s("x"),$s("m")),Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Plus($s("p"),C1))),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n")),$s("x")),RationalQ($s("p"))),Less($s("p"),CN1)),NonzeroQ(Plus($s("m"),C1))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Plus($p("a",true),Times($p("b",true),Log(Times($p("c",true),Power($p("x"),$p("n",true)))))),$p("p"))),$p("x",$s("Symbol"))),
			    Condition(Times(Times(Power($s("x"),Plus($s("m"),C1)),Gamma(Plus($s("p"),C1),Times(Times(CN1,Plus($s("m"),C1)),Times(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Power(Times($s("b"),$s("n")),CN1))))),Times(Power(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),$s("p")),Power(Times(Times(Times(Plus($s("m"),C1),Power(E,Times($s("a"),Times(Plus($s("m"),C1),Power(Times($s("b"),$s("n")),CN1))))),Power(Times($s("c"),Power($s("x"),$s("n"))),Times(Plus($s("m"),C1),Power($s("n"),CN1)))),Power(Times(Times(CN1,Plus($s("m"),C1)),Times(Plus($s("a"),Times($s("b"),Log(Times($s("c"),Power($s("x"),$s("n")))))),Power(Times($s("b"),$s("n")),CN1))),$s("p"))),CN1))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n"),$s("p")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
			SetDelayed(Int(Power(Log(Times($p("a",true),Power(Times($p("b",true),Power($p("x"),$p("n",true))),$p("p")))),$p("q",true)),$p("x",$s("Symbol"))),
			    Condition(Subst(Int(Power(Log(Power($s("x"),Times($s("n"),$s("p")))),$s("q")),$s("x")),Power($s("x"),Times($s("n"),$s("p"))),Times($s("a"),Power(Times($s("b"),Power($s("x"),$s("n"))),$s("p")))),FreeQ(List($s("a"),$s("b"),$s("n"),$s("p"),$s("q")),$s("x")))),
			SetDelayed(Int(Power(Log(Times($p("a",true),Power(Times($p("b",true),Power(Times($p("c",true),Power($p("x"),$p("n",true))),$p("p"))),$p("q")))),$p("r",true)),$p("x",$s("Symbol"))),
			    Condition(Subst(Int(Power(Log(Power($s("x"),Times(Times($s("n"),$s("p")),$s("q")))),$s("r")),$s("x")),Power($s("x"),Times(Times($s("n"),$s("p")),$s("q"))),Times($s("a"),Power(Times($s("b"),Power(Times($s("c"),Power($s("x"),$s("n"))),$s("p"))),$s("q")))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("p"),$s("q"),$s("r")),$s("x")))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Log(Times($p("a",true),Power(Times($p("b",true),Power($p("x"),$p("n",true))),$p("p")))),$p("q",true))),$p("x",$s("Symbol"))),
			    Condition(Subst(Int(Times(Power($s("x"),$s("m")),Power(Log(Power($s("x"),Times($s("n"),$s("p")))),$s("q"))),$s("x")),Power($s("x"),Times($s("n"),$s("p"))),Times($s("a"),Power(Times($s("b"),Power($s("x"),$s("n"))),$s("p")))),And(And(FreeQ(List($s("a"),$s("b"),$s("m"),$s("n"),$s("p"),$s("q")),$s("x")),NonzeroQ(Plus($s("m"),C1))),Not(SameQ(Power($s("x"),Times($s("n"),$s("p"))),Times($s("a"),Power(Times($s("b"),Power($s("x"),$s("n"))),$s("p")))))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Log(Times($p("a",true),Power(Times($p("b",true),Power(Times($p("c",true),Power($p("x"),$p("n",true))),$p("p"))),$p("q")))),$p("r",true))),$p("x",$s("Symbol"))),
			    Condition(Subst(Int(Times(Power($s("x"),$s("m")),Power(Log(Power($s("x"),Times(Times($s("n"),$s("p")),$s("q")))),$s("r"))),$s("x")),Power($s("x"),Times(Times($s("n"),$s("p")),$s("q"))),Times($s("a"),Power(Times($s("b"),Power(Times($s("c"),Power($s("x"),$s("n"))),$s("p"))),$s("q")))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n"),$s("p"),$s("q"),$s("r")),$s("x")),NonzeroQ(Plus($s("m"),C1))),Not(SameQ(Power($s("x"),Times(Times($s("n"),$s("p")),$s("q"))),Times($s("a"),Power(Times($s("b"),Power(Times($s("c"),Power($s("x"),$s("n"))),$s("p"))),$s("q")))))))),
			SetDelayed(Int(Times(Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),$p("n",true)))),$p("p",true)),Power(Plus($p("d",true),Times($p("e",true),$p("x"))),CN1)),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p")),Times(Log(Times($s("b"),Times(Plus($s("d"),Times($s("e"),$s("x"))),Power(Plus(Times($s("b"),$s("d")),Times(CN1,Times($s("a"),$s("e")))),CN1)))),Power($s("e"),CN1))),Times(CN1,Dist(Times(Times($s("b"),$s("n")),Times($s("p"),Power($s("e"),CN1))),Int(Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Plus($s("p"),Times(CN1,C1))),Times(Log(Times($s("b"),Times(Plus($s("d"),Times($s("e"),$s("x"))),Power(Plus(Times($s("b"),$s("d")),Times(CN1,Times($s("a"),$s("e")))),CN1)))),Power(Plus($s("a"),Times($s("b"),$s("x"))),CN1))),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)),NonzeroQ(Plus(Times($s("b"),$s("d")),Times(CN1,Times($s("a"),$s("e")))))))),
			SetDelayed(Int(Times(Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),$p("n",true)))),$p("p",true)),Times(Log(Times($p("h",true),Plus($p("f",true),Times($p("g",true),$p("x"))))),Power(Plus($p("d"),Times($p("e",true),$p("x"))),CN1))),$p("x",$s("Symbol"))),
			    Condition(Module(List(Set($s("q"),Simplify(Plus(C1,Times(CN1,Times($s("h"),Plus($s("f"),Times($s("g"),$s("x"))))))))),Plus(Times(Times(CN1,Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p"))),Times(PolyLog(C2,$s("q")),Power($s("e"),CN1))),Dist(Times(Times($s("b"),$s("n")),Times($s("p"),Power($s("e"),CN1))),Int(Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Plus($s("p"),Times(CN1,C1))),Times(PolyLog(C2,$s("q")),Power(Plus($s("a"),Times($s("b"),$s("x"))),CN1))),$s("x"))))),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("f"),$s("g"),$s("h"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)),ZeroQ(Plus(Times($s("a"),$s("e")),Times(CN1,Times($s("b"),$s("d")))))),ZeroQ(Plus(Times(Times($s("a"),$s("g")),$s("h")),Times(CN1,Times($s("b"),Plus(Times($s("f"),$s("h")),Times(CN1,C1))))))))),
			SetDelayed(Int(Times(Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),$p("n",true)))),$p("p",true)),Times(PolyLog($p("m"),Times($p("h",true),Plus($p("f",true),Times($p("g",true),$p("x"))))),Power(Plus($p("d"),Times($p("e",true),$p("x"))),CN1))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p")),Times(PolyLog(Plus($s("m"),C1),Times($s("h"),Plus($s("f"),Times($s("g"),$s("x"))))),Power($s("e"),CN1))),Times(CN1,Dist(Times(Times($s("b"),$s("n")),Times($s("p"),Power($s("e"),CN1))),Int(Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Plus($s("p"),Times(CN1,C1))),Times(PolyLog(Plus($s("m"),C1),Times($s("h"),Plus($s("f"),Times($s("g"),$s("x"))))),Power(Plus($s("a"),Times($s("b"),$s("x"))),CN1))),$s("x"))))),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("e"),$s("f"),$s("g"),$s("h"),$s("m"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)),ZeroQ(Plus(Times($s("a"),$s("e")),Times(CN1,Times($s("b"),$s("d")))))),ZeroQ(Plus(Times($s("a"),$s("g")),Times(CN1,Times($s("b"),$s("f")))))))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),$p("n",true)))),$p("p"))),$p("x",$s("Symbol"))),
			    Condition(Plus(Plus(Times(Times(Power($s("x"),$s("m")),Plus($s("a"),Times($s("b"),$s("x")))),Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p")),Power(Times($s("b"),Plus($s("m"),C1)),CN1))),Times(CN1,Dist(Times($s("a"),Times($s("m"),Power(Times($s("b"),Plus($s("m"),C1)),CN1))),Int(Times(Power($s("x"),Plus($s("m"),Times(CN1,C1))),Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p"))),$s("x"))))),Times(CN1,Dist(Times($s("n"),Times($s("p"),Power(Plus($s("m"),C1),CN1))),Int(Times(Power($s("x"),$s("m")),Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Plus($s("p"),Times(CN1,C1)))),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")),RationalQ(List($s("m"),$s("p")))),Greater($s("m"),C0)),Greater($s("p"),C0)))),
			SetDelayed(Int(Times(Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),$p("n",true)))),$p("p")),Power(Power($p("x"),C2),CN1)),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Times(CN1,Plus($s("a"),Times($s("b"),$s("x")))),Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p")),Power(Times($s("a"),$s("x")),CN1))),Dist(Times(Times($s("b"),$s("n")),Times($s("p"),Power($s("a"),CN1))),Int(Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Plus($s("p"),Times(CN1,C1))),Power($s("x"),CN1)),$s("x")))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")),RationalQ($s("p"))),Greater($s("p"),C0)))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),$p("n",true)))),$p("p"))),$p("x",$s("Symbol"))),
			    Condition(Plus(Plus(Times(Times(Power($s("x"),Plus($s("m"),C1)),Plus($s("a"),Times($s("b"),$s("x")))),Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p")),Power(Times($s("a"),Plus($s("m"),C1)),CN1))),Times(CN1,Dist(Times(Times($s("b"),Plus($s("m"),C2)),Power(Times($s("a"),Plus($s("m"),C1)),CN1)),Int(Times(Power($s("x"),Plus($s("m"),C1)),Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),$s("p"))),$s("x"))))),Times(CN1,Dist(Times(Times($s("b"),$s("n")),Times($s("p"),Power(Times($s("a"),Plus($s("m"),C1)),CN1))),Int(Times(Power($s("x"),Plus($s("m"),C1)),Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),$s("x"))),$s("n")))),Plus($s("p"),Times(CN1,C1)))),$s("x"))))),And(And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")),RationalQ(List($s("m"),$s("p")))),Less($s("m"),CN1)),Unequal($s("m"),integer(-2L))),Greater($s("p"),C0)))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),$p("x"))),$p("n",true)))),$p("p"))),$p("x",$s("Symbol"))),
			    Condition(Dist(Times(C1,Power($s("b"),CN1)),Subst(Int(Times(Power(Plus(Times(Times(CN1,$s("a")),Power($s("b"),CN1)),Times($s("x"),Power($s("b"),CN1))),$s("m")),Power(Log(Times($s("c"),Power($s("x"),$s("n")))),$s("p"))),$s("x")),$s("x"),Plus($s("a"),Times($s("b"),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n"),$s("p")),$s("x")),IntegerQ($s("m"))),Greater($s("m"),C0)),Not(And(RationalQ($s("p")),Greater($s("p"),C0)))))),
			SetDelayed(Int(Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),CN1))),$p("n",true)))),$p("p")),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Plus($s("b"),Times($s("a"),$s("x"))),Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),CN1))),$s("n")))),$s("p")),Power($s("a"),CN1))),Dist(Times(Times(Times($s("b"),Power($s("a"),CN1)),$s("n")),$s("p")),Int(Times(Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),CN1))),$s("n")))),Plus($s("p"),Times(CN1,C1))),Power($s("x"),CN1)),$s("x")))),And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")),IntegerQ($s("p"))),Greater($s("p"),C0)))),
			SetDelayed(Int(Power(Log(Times($p("c",true),Power(Plus($p("a"),Times($p("b",true),Power($p("x"),C2))),$p("n",true)))),C2),$p("x",$s("Symbol"))),
			    Condition(Plus(Plus(Plus(Times($s("x"),Power(Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),C2))),$s("n")))),C2)),Times(Times(integer(8L),Power($s("n"),C2)),$s("x"))),Times(CN1,Times(Times(Times(C4,$s("n")),$s("x")),Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),C2))),$s("n"))))))),Times(Times($s("n"),Times(Power($s("a"),C1D2),Power(Power(Times(CN1,$s("b")),C1D2),CN1))),Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Times(Times(C4,$s("n")),Log(Times(Plus(Times(CN1,Power($s("a"),C1D2)),Times(Power(Times(CN1,$s("b")),C1D2),$s("x"))),Power(Plus(Power($s("a"),C1D2),Times(Power(Times(CN1,$s("b")),C1D2),$s("x"))),CN1)))),Times(CN1,Times(Times(Times(C4,$s("n")),ArcTanh(Times(Power(Times(CN1,$s("b")),C1D2),Times($s("x"),Power(Power($s("a"),C1D2),CN1))))),Plus(Log(Plus(Times(Times(CN1,Power($s("a"),C1D2)),Power(Power(Times(CN1,$s("b")),C1D2),CN1)),$s("x"))),Log(Plus(Times(Power($s("a"),C1D2),Power(Power(Times(CN1,$s("b")),C1D2),CN1)),$s("x"))))))),Times(CN1,Times($s("n"),Power(Log(Plus(Times(Times(CN1,Power($s("a"),C1D2)),Power(Power(Times(CN1,$s("b")),C1D2),CN1)),$s("x"))),C2)))),Times($s("n"),Power(Log(Plus(Times(Power($s("a"),C1D2),Power(Power(Times(CN1,$s("b")),C1D2),CN1)),$s("x"))),C2))),Times(CN1,Times(Times(Times(C2,$s("n")),Log(Plus(Times(Power($s("a"),C1D2),Power(Power(Times(CN1,$s("b")),C1D2),CN1)),$s("x")))),Log(Plus(C1D2,Times(CN1,Times(Power(Times(CN1,$s("b")),C1D2),Times($s("x"),Power(Times(C2,Power($s("a"),C1D2)),CN1))))))))),Times(Times(Times(C2,$s("n")),Log(Plus(Times(Times(CN1,Power($s("a"),C1D2)),Power(Power(Times(CN1,$s("b")),C1D2),CN1)),$s("x")))),Log(Times(Plus(C1,Times(Power(Times(CN1,$s("b")),C1D2),Times($s("x"),Power(Power($s("a"),C1D2),CN1)))),C1D2)))),Times(Times(C4,ArcTanh(Times(Power(Times(CN1,$s("b")),C1D2),Times($s("x"),Power(Power($s("a"),C1D2),CN1))))),Log(Times($s("c"),Power(Plus($s("a"),Times($s("b"),Power($s("x"),C2))),$s("n")))))),Times(Times(C2,$s("n")),PolyLog(C2,Plus(C1D2,Times(CN1,Times(Power(Times(CN1,$s("b")),C1D2),Times($s("x"),Power(Times(C2,Power($s("a"),C1D2)),CN1)))))))),Times(CN1,Times(Times(C2,$s("n")),PolyLog(C2,Times(Plus(C1,Times(Power(Times(CN1,$s("b")),C1D2),Times($s("x"),Power(Power($s("a"),C1D2),CN1)))),C1D2))))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("n")),$s("x")))),
			SetDelayed(Int(Power(Log(Times($p("d",true),Power(Plus(Plus($p("a",true),Times($p("b",true),$p("x"))),Times($p("c",true),Power($p("x"),C2))),$p("n",true)))),C2),$p("x",$s("Symbol"))),
			    Condition(Plus(Plus(Times($s("x"),Power(Log(Times($s("d"),Power(Plus(Plus($s("a"),Times($s("b"),$s("x"))),Times($s("c"),Power($s("x"),C2))),$s("n")))),C2)),Times(CN1,Dist(Times(Times(C2,$s("b")),$s("n")),Int(Times($s("x"),Times(Log(Times($s("d"),Power(Plus(Plus($s("a"),Times($s("b"),$s("x"))),Times($s("c"),Power($s("x"),C2))),$s("n")))),Power(Plus(Plus($s("a"),Times($s("b"),$s("x"))),Times($s("c"),Power($s("x"),C2))),CN1))),$s("x"))))),Times(CN1,Dist(Times(Times(C4,$s("c")),$s("n")),Int(Times(Power($s("x"),C2),Times(Log(Times($s("d"),Power(Plus(Plus($s("a"),Times($s("b"),$s("x"))),Times($s("c"),Power($s("x"),C2))),$s("n")))),Power(Plus(Plus($s("a"),Times($s("b"),$s("x"))),Times($s("c"),Power($s("x"),C2))),CN1))),$s("x"))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("n")),$s("x")))),
			SetDelayed(Int(Log(Times($p("a",true),Power(Log(Times($p("b",true),Power($p("x"),$p("n",true)))),$p("p",true)))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times($s("x"),Log(Times($s("a"),Power(Log(Times($s("b"),Power($s("x"),$s("n")))),$s("p"))))),Times(CN1,Dist(Times($s("n"),$s("p")),Int(Times(C1,Power(Log(Times($s("b"),Power($s("x"),$s("n")))),CN1)),$s("x"))))),FreeQ(List($s("a"),$s("b"),$s("n"),$s("p")),$s("x")))),
			SetDelayed(Int(Times(Log(Times($p("a",true),Power(Log(Times($p("b",true),Power($p("x"),$p("n",true)))),$p("p",true)))),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
			    Condition(Times(Log(Times($s("b"),Power($s("x"),$s("n")))),Times(Plus(Times(CN1,$s("p")),Log(Times($s("a"),Power(Log(Times($s("b"),Power($s("x"),$s("n")))),$s("p"))))),Power($s("n"),CN1))),FreeQ(List($s("a"),$s("b"),$s("n"),$s("p")),$s("x")))),
			SetDelayed(Int(Times(Power($p("x"),$p("m",true)),Log(Times($p("a",true),Power(Log(Times($p("b",true),Power($p("x"),$p("n",true)))),$p("p",true))))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power($s("x"),Plus($s("m"),C1)),Times(Log(Times($s("a"),Power(Log(Times($s("b"),Power($s("x"),$s("n")))),$s("p")))),Power(Plus($s("m"),C1),CN1))),Times(CN1,Dist(Times($s("n"),Times($s("p"),Power(Plus($s("m"),C1),CN1))),Int(Times(Power($s("x"),$s("m")),Power(Log(Times($s("b"),Power($s("x"),$s("n")))),CN1)),$s("x"))))),And(FreeQ(List($s("a"),$s("b"),$s("m"),$s("n"),$s("p")),$s("x")),NonzeroQ(Plus($s("m"),C1))))),
			SetDelayed(Int(Times(Power(Log(Times(Plus($p("a",true),Times($p("b",true),$p("x"))),Power(Plus($p("c"),Times($p("d",true),$p("x"))),CN1))),$p("m",true)),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
			    Condition(Plus(Subst(Int(Times(Power(Log(Plus(Times($s("a"),Power($s("c"),CN1)),Times($s("x"),Power($s("c"),CN1)))),$s("m")),Power($s("x"),CN1)),$s("x")),$s("x"),Times(Plus(Times($s("b"),$s("c")),Times(CN1,Times($s("a"),$s("d")))),Times($s("x"),Power(Plus($s("c"),Times($s("d"),$s("x"))),CN1)))),Times(CN1,Subst(Int(Times(Power(Log(Plus(Times($s("b"),Power($s("d"),CN1)),Times($s("x"),Power($s("d"),CN1)))),$s("m")),Power($s("x"),CN1)),$s("x")),$s("x"),Times(Times(CN1,Plus(Times($s("b"),$s("c")),Times(CN1,Times($s("a"),$s("d"))))),Power(Plus($s("c"),Times($s("d"),$s("x"))),CN1))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ($s("m"))),Greater($s("m"),C0)),NonzeroQ(Plus(Times($s("b"),$s("c")),Times(CN1,Times($s("a"),$s("d")))))))),
			SetDelayed(Int(Times(Plus($p("A",true),Times($p("B",true),Log(Plus($p("c",true),Times($p("d",true),$p("x")))))),Power(Power(Plus($p("a"),Times($p("b",true),Log(Plus($p("c",true),Times($p("d",true),$p("x")))))),C1D2),CN1)),$p("x",$s("Symbol"))),
			    Condition(Plus(Dist(Times(Plus(Times($s("b"),$s("A")),Times(CN1,Times($s("a"),$s("B")))),Power($s("b"),CN1)),Int(Times(C1,Power(Power(Plus($s("a"),Times($s("b"),Log(Plus($s("c"),Times($s("d"),$s("x")))))),C1D2),CN1)),$s("x"))),Dist(Times($s("B"),Power($s("b"),CN1)),Int(Power(Plus($s("a"),Times($s("b"),Log(Plus($s("c"),Times($s("d"),$s("x")))))),C1D2),$s("x")))),And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d"),$s("A"),$s("B")),$s("x")),NonzeroQ(Plus(Times($s("b"),$s("A")),Times(CN1,Times($s("a"),$s("B")))))))),
			SetDelayed(Int(Times(Power(Plus($p("a",true),Times($p("b",true),$p("x"))),$p("m",true)),Power(Log(Plus($p("c",true),Times($p("d",true),$p("x")))),$p("n"))),$p("x",$s("Symbol"))),
			    Condition(Plus(Times(Power(Plus($s("a"),Times($s("b"),$s("x"))),Plus($s("m"),C1)),Times(Power(Log(Plus($s("c"),Times($s("d"),$s("x")))),$s("n")),Power(Times($s("b"),Plus($s("m"),C1)),CN1))),Times(CN1,Dist(Times($s("d"),Times($s("n"),Power(Times($s("b"),Plus($s("m"),C1)),CN1))),Int(Regularize(Times(Power(Plus($s("a"),Times($s("b"),$s("x"))),Plus($s("m"),C1)),Times(Power(Log(Plus($s("c"),Times($s("d"),$s("x")))),Plus($s("n"),Times(CN1,C1))),Power(Plus($s("c"),Times($s("d"),$s("x"))),CN1))),$s("x")),$s("x"))))),And(And(And(FreeQ(List($s("a"),$s("b"),$s("c"),$s("d")),$s("x")),IntegerQ(List($s("m"),$s("n")))),Less($s("m"),CN1)),Greater($s("n"),C0)))),
			SetDelayed(Int(Power($p("f"),Times($p("a",true),Log($p("u")))),$p("x",$s("Symbol"))),
			    Condition(Int(Power($s("u"),Times($s("a"),Log($s("f")))),$s("x")),FreeQ(List($s("a"),$s("f")),$s("x")))),
			SetDelayed(Int(Times(C1,Power(Plus(Times($p("a",true),$p("x")),Times(Times($p("b",true),$p("x")),Power(Log(Times($p("c",true),Power($p("x"),$p("n",true)))),$p("m",true)))),CN1)),$p("x",$s("Symbol"))),
			    Condition(Dist(Times(C1,Power($s("n"),CN1)),Subst(Int(Times(C1,Power(Plus($s("a"),Times($s("b"),Power($s("x"),$s("m")))),CN1)),$s("x")),$s("x"),Log(Times($s("c"),Power($s("x"),$s("n")))))),FreeQ(List($s("a"),$s("b"),$s("c"),$s("m"),$s("n")),$s("x")))),
			SetDelayed(Int(Times($p("u"),Power($p("x"),CN1)),$p("x",$s("Symbol"))),
			    Condition(Module(List(Set($s("lst"),FunctionOfLog($s("u"),$s("x")))),Condition(Dist(Times(C1,Power(Part($s("lst"),C3),CN1)),Subst(Int(Part($s("lst"),C1),$s("x")),$s("x"),Log(Part($s("lst"),C2)))),Not(FalseQ($s("lst"))))),NonsumQ($s("u")))),
			SetDelayed(Int(Log($p("u")),$p("x",$s("Symbol"))),
			    Condition(Plus(Times($s("x"),Log($s("u"))),Times(CN1,Int(Regularize(Times($s("x"),Times(D($s("u"),$s("x")),Power($s("u"),CN1))),$s("x")),$s("x")))),AlgebraicFunctionQ($s("u"),$s("x"))))
			);
}