package org.matheclipse.core.integrate.rubi;


import static org.matheclipse.core.expression.F.$;
import static org.matheclipse.core.expression.F.$p;
import static org.matheclipse.core.expression.F.$s;
import static org.matheclipse.core.expression.F.And;
import static org.matheclipse.core.expression.F.Append;
import static org.matheclipse.core.expression.F.Apply;
import static org.matheclipse.core.expression.F.C0;
import static org.matheclipse.core.expression.F.C1;
import static org.matheclipse.core.expression.F.C1D2;
import static org.matheclipse.core.expression.F.C2;
import static org.matheclipse.core.expression.F.C3;
import static org.matheclipse.core.expression.F.CI;
import static org.matheclipse.core.expression.F.CN1;
import static org.matheclipse.core.expression.F.CompoundExpression;
import static org.matheclipse.core.expression.F.Condition;
import static org.matheclipse.core.expression.F.Denominator;
import static org.matheclipse.core.expression.F.E;
import static org.matheclipse.core.expression.F.Equal;
import static org.matheclipse.core.expression.F.False;
import static org.matheclipse.core.expression.F.First;
import static org.matheclipse.core.expression.F.FreeQ;
import static org.matheclipse.core.expression.F.Function;
import static org.matheclipse.core.expression.F.Greater;
import static org.matheclipse.core.expression.F.GreaterEqual;
import static org.matheclipse.core.expression.F.If;
import static org.matheclipse.core.expression.F.Im;
import static org.matheclipse.core.expression.F.IntegerQ;
import static org.matheclipse.core.expression.F.Less;
import static org.matheclipse.core.expression.F.LessEqual;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Log;
import static org.matheclipse.core.expression.F.MatchQ;
import static org.matheclipse.core.expression.F.MemberQ;
import static org.matheclipse.core.expression.F.Module;
import static org.matheclipse.core.expression.F.N;
import static org.matheclipse.core.expression.F.Not;
import static org.matheclipse.core.expression.F.NumberQ;
import static org.matheclipse.core.expression.F.NumericQ;
import static org.matheclipse.core.expression.F.Or;
import static org.matheclipse.core.expression.F.Part;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.PossibleZeroQ;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Prepend;
import static org.matheclipse.core.expression.F.Re;
import static org.matheclipse.core.expression.F.Rest;
import static org.matheclipse.core.expression.F.Set;
import static org.matheclipse.core.expression.F.SetDelayed;
import static org.matheclipse.core.expression.F.Simplify;
import static org.matheclipse.core.expression.F.Slot1;
import static org.matheclipse.core.expression.F.Times;
import static org.matheclipse.core.expression.F.True;
import static org.matheclipse.core.expression.F.stringx;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CalculusFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CalculusQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ComplexFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CosQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CoshQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CotQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CothQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CscQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.CschQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ExpQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FalseQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FractionOrNegativeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FractionQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FractionalPowerFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.FractionalPowerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.GE;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.GT;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.HalfIntegerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.HyperbolicQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ImaginaryNumericQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ImaginaryQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.IndentedPrint;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.IndependentQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.IntegerPowerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.InverseFunctionFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.InverseFunctionQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.InverseHyperbolicQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.InverseTrigQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LE;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LT;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LeadBase;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LeadDegree;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LeadFactor;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LeadTerm;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.LogQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.MakeList;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Map2;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.MapAnd;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.MapOr;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NegQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NegativeCoefficientQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NegativeOrZeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NegativeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NonsumQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NonzeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NotFalseQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.NotIntegrableQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PosQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PositiveIntegerPowerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PositiveOrZeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PositiveQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.PowerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ProductLogQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ProductQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RationalPowerQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RationalQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RealNumericQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RealQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ReapList;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Regularize;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RemainingFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.RemainingTerms;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SecQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SechQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.Second;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SinCosQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SinQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SinhCoshQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SinhQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SplitFactorsOfTerms;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SplitFreeFactors;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SplitFreeTerms;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SplitMonomialTerms;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SqrtNumberQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SqrtNumberSumQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SqrtQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SubstQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.SumQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.TanQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.TanhQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.TrigHyperbolicFreeQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.TrigQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.ZeroQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.AtomQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Catch;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Do;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Head;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Length;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.ListQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Map;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Print;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Reap;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.SameQ;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Scan;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Sow;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.StringJoin;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.Throw;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.UnsameQ;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.ISymbol;
/** 
 * UtilityFunctions rules from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 *  
 */
public class UtilityFunctions0 { 
  public static IAST RULES = List( 
SetDelayed(IndentedPrint($p("n",$s("Integer")),$p("u")),
    CompoundExpression(Print(StringJoin(MakeList($s("n"),stringx(" "))),$s("u")),$s("u"))),
SetDelayed(MakeList($p("n",$s("Integer")),$p("u")),
    If(Equal($s("n"),C0),List(),Append(MakeList(Plus($s("n"),Times(CN1,C1)),$s("u")),$s("u")))),
SetDelayed(Second($p("u")),
    Part($s("u"),C2)),
//SetDelayed(ClearDownValues($p("func",$s("Symbol"))),
//    CompoundExpression(CompoundExpression(Unprotect($s("func")),Set(DownValues($s("func")),List())),Protect($s("func")))),
//SetDelayed(SetDownValues($p("func",$s("Symbol")),$p("lst",$s("List"))),
//    CompoundExpression(CompoundExpression(CompoundExpression(Unprotect($s("func")),Set(DownValues($s("func")),Take($s("lst"),Min(integer(529L),Length($s("lst")))))),Scan(Function(ReplacePart(ReplacePart(Slot1,Part(Part(Slot1,C1),C1),C1),$s("SetDelayed"),C0)),Drop($s("lst"),Min(integer(529L),Length($s("lst")))))),Protect($s("func")))),
//SetDelayed(MoveDownValues($p("func1",$s("Symbol")),$p("func2",$s("Symbol"))),
//    Module(List($s("lst")),CompoundExpression(SetDownValues($s("func2"),ReplaceAll(DownValues($s("func1")),List(Rule($s("func1"),$s("func2"))))),ClearDownValues($s("func1"))))),
SetDelayed(Map2($p("func"),$p("lst1"),$p("lst2")),
    ReapList(Do(Sow($($s("func"),Part($s("lst1"),$s("i")),Part($s("lst2"),$s("i")))),List($s("i"),Length($s("lst1")))))),
SetDelayed(ReapList($p("u")),
    Module(List(Set($s("lst"),Part(Reap($s("u")),C2))),If(SameQ($s("lst"),List()),$s("lst"),Part($s("lst"),C1)))),
SetDelayed(MapAnd($p("f"),$p("lst")),
    Catch(CompoundExpression(Scan(Function(If($($s("f"),Slot1),$s("Null"),Throw(False))),$s("lst")),True))),
SetDelayed(MapAnd($p("f"),$p("lst"),$p("x")),
    Catch(CompoundExpression(Scan(Function(If($($s("f"),Slot1,$s("x")),$s("Null"),Throw(False))),$s("lst")),True))),
SetDelayed(MapOr($p("f"),$p("lst")),
    Catch(CompoundExpression(Scan(Function(If($($s("f"),Slot1),Throw(True),$s("Null"))),$s("lst")),False))),
SetDelayed(NotIntegrableQ($p("u"),$p("x",$s("Symbol"))),
    Or(MatchQ($s("u"),Condition(Times(Power($s("x"),$p("m")),Power(Log(Plus($p("a"),Times($p("b",true),$s("x")))),$p("n"))),And(And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ(List($s("m"),$s("n")))),Less($s("m"),C0)),Less($s("n"),C0)))),MatchQ($s("u"),Condition($($p("f"),Times(Power($s("x"),$p("m",true)),Log(Plus($p("a",true),Times($p("b",true),$s("x")))))),And(And(FreeQ(List($s("a"),$s("b")),$s("x")),IntegerQ($s("m"))),Or(TrigQ($s("f")),HyperbolicQ($s("f")))))))),
SetDelayed(ZeroQ($p("u")),
    PossibleZeroQ($s("u"))),
SetDelayed(NonzeroQ($p("u")),
    Not(PossibleZeroQ($s("u")))),
SetDelayed(RealNumericQ($p("u")),
    And(NumericQ($s("u")),PossibleZeroQ(Im(N($s("u")))))),
SetDelayed(ImaginaryNumericQ($p("u")),
    And(And(NumericQ($s("u")),PossibleZeroQ(Re(N($s("u"))))),Not(PossibleZeroQ(Im(N($s("u"))))))),
SetDelayed(PositiveQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),Greater(Re(N($s("v"))),C0)))),
SetDelayed(PositiveOrZeroQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),GreaterEqual(Re(N($s("v"))),C0)))),
SetDelayed(NegativeQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),Less(Re(N($s("v"))),C0)))),
SetDelayed(NegativeOrZeroQ($p("u")),
    Module(List(Set($s("v"),Simplify($s("u")))),And(RealNumericQ($s("v")),LessEqual(Re(N($s("v"))),C0)))),
SetDelayed(FractionQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::FractionQ"),$s("u")),SameQ(Head($s("u")),$s("Rational")))),
SetDelayed(RationalQ(Plus($p("u"),Times($p("m"),Plus($p("n"),$p("v"))))),
    And(And(RationalQ($s("m")),RationalQ($s("n"))),RationalQ(Plus($s("u"),Times($s("m"),$s("v")))))),
SetDelayed(RationalQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::RationalQ"),$s("u")),Or(IntegerQ($s("u")),SameQ(Head($s("u")),$s("Rational"))))),
SetDelayed(HalfIntegerQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::HalfIntegerQ"),$s("u")),And(FractionQ($s("u")),Equal(Denominator($s("u")),C2)))),
SetDelayed(FractionOrNegativeQ($p("u")),
    If(ListQ($s("u")),MapAnd($s("Integrate::FractionOrNegativeQ"),$s("u")),Or(FractionQ($s("u")),And(IntegerQ($s("u")),Less($s("u"),C0))))),
SetDelayed(SqrtNumberQ(Power($p("m"),$p("n"))),
    Or(And(IntegerQ($s("n")),SqrtNumberQ($s("m"))),And(HalfIntegerQ($s("n")),RationalQ($s("m"))))),
SetDelayed(SqrtNumberQ(Times($p("u"),$p("v"))),
    And(SqrtNumberQ($s("u")),SqrtNumberQ($s("v")))),
SetDelayed(SqrtNumberQ($p("u")),
    Or(RationalQ($s("u")),SameQ($s("u"),CI))),
SetDelayed(SqrtNumberSumQ($p("u")),
    And(And(SumQ($s("u")),SqrtNumberQ(First($s("u")))),SqrtNumberQ(Rest($s("u"))))),
SetDelayed(FalseQ($p("u")),
    SameQ($s("u"),False)),
SetDelayed(NotFalseQ($p("u")),
    UnsameQ($s("u"),False)),
SetDelayed(SumQ($p("u")),
    SameQ(Head($s("u")),$s("Plus"))),
SetDelayed(NonsumQ($p("u")),
    UnsameQ(Head($s("u")),$s("Plus"))),
SetDelayed(ProductQ($p("u")),
    SameQ(Head($s("u")),$s("Times"))),
SetDelayed(PowerQ($p("u")),
    SameQ(Head($s("u")),$s("Power"))),
SetDelayed(IntegerPowerQ($p("u")),
    And(PowerQ($s("u")),IntegerQ(Part($s("u"),C2)))),
SetDelayed(PositiveIntegerPowerQ($p("u")),
    And(And(PowerQ($s("u")),IntegerQ(Part($s("u"),C2))),Greater(Part($s("u"),C2),C0))),
SetDelayed(FractionalPowerQ($p("u")),
    And(PowerQ($s("u")),FractionQ(Part($s("u"),C2)))),
SetDelayed(RationalPowerQ($p("u")),
    And(PowerQ($s("u")),RationalQ(Part($s("u"),C2)))),
SetDelayed(SqrtQ($p("u")),
    And(PowerQ($s("u")),SameQ(Part($s("u"),C2),C1D2))),
SetDelayed(ExpQ($p("u")),
    And(PowerQ($s("u")),SameQ(Part($s("u"),C1),E))),
SetDelayed(ImaginaryQ($p("u")),
    And(SameQ(Head($s("u")),$s("Complex")),SameQ(Re($s("u")),C0))),
SetDelayed(FractionalPowerFreeQ($p("u")),
    If(AtomQ($s("u")),True,If(And(FractionalPowerQ($s("u")),Not(AtomQ(Part($s("u"),C1)))),False,Catch(CompoundExpression(Scan(Function(If(FractionalPowerFreeQ(Slot1),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(ComplexFreeQ($p("u")),
    If(AtomQ($s("u")),UnsameQ(Head($s("u")),$s("Complex")),Catch(CompoundExpression(Scan(Function(If(ComplexFreeQ(Slot1),$s("Null"),Throw(False))),$s("u")),True)))),
SetDelayed(LogQ($p("u")),
    SameQ(Head($s("u")),$s("Log"))),
SetDelayed(ProductLogQ($p("u")),
    SameQ(Head($s("u")),$s("ProductLog"))),
SetDelayed(SinQ($p("u")),
    SameQ(Head($s("u")),$s("Sin"))),
SetDelayed(CosQ($p("u")),
    SameQ(Head($s("u")),$s("Cos"))),
SetDelayed(TanQ($p("u")),
    SameQ(Head($s("u")),$s("Tan"))),
SetDelayed(CotQ($p("u")),
    SameQ(Head($s("u")),$s("Cot"))),
SetDelayed(SecQ($p("u")),
    SameQ(Head($s("u")),$s("Sec"))),
SetDelayed(CscQ($p("u")),
    SameQ(Head($s("u")),$s("Csc"))),
SetDelayed(SinhQ($p("u")),
    SameQ(Head($s("u")),$s("Sinh"))),
SetDelayed(CoshQ($p("u")),
    SameQ(Head($s("u")),$s("Cosh"))),
SetDelayed(TanhQ($p("u")),
    SameQ(Head($s("u")),$s("Tanh"))),
SetDelayed(CothQ($p("u")),
    SameQ(Head($s("u")),$s("Coth"))),
SetDelayed(SechQ($p("u")),
    SameQ(Head($s("u")),$s("Sech"))),
SetDelayed(CschQ($p("u")),
    SameQ(Head($s("u")),$s("Csch"))),
SetDelayed(TrigQ($p("u")),
    MemberQ(List($s("Sin"),$s("Cos"),$s("Tan"),$s("Cot"),$s("Sec"),$s("Csc")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(InverseTrigQ($p("u")),
    MemberQ(List($s("ArcSin"),$s("ArcCos"),$s("ArcTan"),$s("ArcCot"),$s("ArcSec"),$s("ArcCsc")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(HyperbolicQ($p("u")),
    MemberQ(List($s("Sinh"),$s("Cosh"),$s("Tanh"),$s("Coth"),$s("Sech"),$s("Csch")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(InverseHyperbolicQ($p("u")),
    MemberQ(List($s("ArcSinh"),$s("ArcCosh"),$s("ArcTanh"),$s("ArcCoth"),$s("ArcSech"),$s("ArcCsch")),If(AtomQ($s("u")),$s("u"),Head($s("u"))))),
SetDelayed(SinCosQ($p("f")),
    MemberQ(List($s("Sin"),$s("Cos"),$s("Sec"),$s("Csc")),$s("f"))),
SetDelayed(SinhCoshQ($p("f")),
    MemberQ(List($s("Sinh"),$s("Cosh"),$s("Sech"),$s("Csch")),$s("f"))),
SetDelayed(CalculusQ($p("u")),
    MemberQ(List($s("D"),$s("Integrate"),$s("Sum"),$s("Product"),$s("Int"),$s("Dif"),$s("Integrate::Subst")),Head($s("u")))),
SetDelayed(CalculusFreeQ($p("u"),$p("x")),
    If(AtomQ($s("u")),True,If(Or(Or(And(CalculusQ($s("u")),SameQ(Part($s("u"),C2),$s("x"))),SameQ(Head($s("u")),$s("Pattern"))),SameQ(Head($s("u")),$s("Defer"))),False,Catch(CompoundExpression(Scan(Function(If(CalculusFreeQ(Slot1,$s("x")),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(SubstQ($p("u")),
    SameQ(Head($s("u")),$s("Integrate::Subst"))),
SetDelayed(InverseFunctionQ($p("u")),
    Or(Or(Or(LogQ($s("u")),InverseTrigQ($s("u"))),InverseHyperbolicQ($s("u"))),SameQ(Head($s("u")),$s("Integrate::Mods")))),
SetDelayed(TrigHyperbolicFreeQ($p("u"),$p("x",$s("Symbol"))),
    If(AtomQ($s("u")),True,If(Or(Or(TrigQ($s("u")),HyperbolicQ($s("u"))),CalculusQ($s("u"))),FreeQ($s("u"),$s("x")),Catch(CompoundExpression(Scan(Function(If(TrigHyperbolicFreeQ(Slot1,$s("x")),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(InverseFunctionFreeQ($p("u"),$p("x",$s("Symbol"))),
    If(AtomQ($s("u")),True,If(Or(InverseFunctionQ($s("u")),CalculusQ($s("u"))),FreeQ($s("u"),$s("x")),Catch(CompoundExpression(Scan(Function(If(InverseFunctionFreeQ(Slot1,$s("x")),$s("Null"),Throw(False))),$s("u")),True))))),
SetDelayed(NegativeCoefficientQ($p("u")),
    If(SumQ($s("u")),NegativeCoefficientQ(First($s("u"))),MatchQ($s("u"),Condition(Times($p("m"),$p("v",true)),And(RationalQ($s("m")),Less($s("m"),C0)))))),
SetDelayed(RealQ($p("u")),
    Condition(MapAnd($s("Integrate::RealQ"),$s("u")),ListQ($s("u")))),
SetDelayed(RealQ($p("u")),
    Condition(PossibleZeroQ(Im(N($s("u")))),NumericQ($s("u")))),
SetDelayed(RealQ(Power($p("u"),$p("v"))),
    And(And(RealQ($s("u")),RealQ($s("v"))),Or(IntegerQ($s("v")),PositiveOrZeroQ($s("u"))))),
SetDelayed(RealQ(Times($p("u"),$p("v"))),
    And(RealQ($s("u")),RealQ($s("v")))),
SetDelayed(RealQ(Plus($p("u"),$p("v"))),
    And(RealQ($s("u")),RealQ($s("v")))),
SetDelayed(RealQ($($p("f"),$p("u"))),
    If(MemberQ(List($s("Sin"),$s("Cos"),$s("Tan"),$s("Cot"),$s("Sec"),$s("Csc"),$s("ArcTan"),$s("ArcCot"),$s("Erf")),$s("f")),RealQ($s("u")),If(MemberQ(List($s("ArcSin"),$s("ArcCos")),$s("f")),LE(CN1,$s("u"),C1),If(SameQ($s("f"),$s("Log")),PositiveOrZeroQ($s("u")),False)))),
SetDelayed(RealQ($p("u")),
    False),
SetDelayed(PosQ($p("u")),
    If(PossibleZeroQ($s("u")),False,If(NumericQ($s("u")),If(NumberQ($s("u")),If(PossibleZeroQ(Re($s("u"))),Greater(Im($s("u")),C0),Greater(Re($s("u")),C0)),Module(List(Set($s("v"),N($s("u")))),If(PossibleZeroQ(Re($s("v"))),Greater(Im($s("v")),C0),Greater(Re($s("v")),C0)))),Module(List(Set($s("v"),Simplify($s("u")))),If(NumericQ($s("v")),PosQ($s("v")),If(And(PowerQ($s("v")),IntegerQ(Part($s("v"),C2))),PosQ(Part($s("v"),C1)),If(ProductQ($s("v")),If(RationalQ(First($s("v"))),If(Greater(First($s("v")),C0),PosQ(Rest($s("v"))),NegQ(Rest($s("v")))),PosQ(First($s("v")))),If(SumQ($s("v")),PosQ(First($s("v"))),Not(MatchQ($s("v"),Times(CN1,$p((ISymbol)null)))))))))))),
SetDelayed(NegQ($p("u")),
    If(PossibleZeroQ($s("u")),False,Not(PosQ($s("u"))))),
SetDelayed(LeadTerm($p("u")),
    If(SumQ($s("u")),First($s("u")),$s("u"))),
SetDelayed(RemainingTerms($p("u")),
    If(SumQ($s("u")),Rest($s("u")),C0)),
SetDelayed(LeadFactor($p("u")),
    If(ProductQ($s("u")),LeadFactor(First($s("u"))),If(ImaginaryQ($s("u")),If(SameQ(Im($s("u")),C1),$s("u"),LeadFactor(Im($s("u")))),$s("u")))),
SetDelayed(RemainingFactors($p("u")),
    If(ProductQ($s("u")),Times(RemainingFactors(First($s("u"))),Rest($s("u"))),If(ImaginaryQ($s("u")),If(SameQ(Im($s("u")),C1),C1,Times(CI,RemainingFactors(Im($s("u"))))),C1))),
SetDelayed(LeadBase($p("u")),
    Module(List(Set($s("v"),LeadFactor($s("u")))),If(PowerQ($s("v")),Part($s("v"),C1),$s("v")))),
SetDelayed(LeadDegree($p("u")),
    Module(List(Set($s("v"),LeadFactor($s("u")))),If(PowerQ($s("v")),Part($s("v"),C2),C1))),
SetDelayed(LT($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),Less(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(LT($p("u"),$p("v"),$p("w")),
    And(LT($s("u"),$s("v")),LT($s("v"),$s("w")))),
SetDelayed(LE($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),LessEqual(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(LE($p("u"),$p("v"),$p("w")),
    And(LE($s("u"),$s("v")),LE($s("v"),$s("w")))),
SetDelayed(GT($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),Greater(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(GT($p("u"),$p("v"),$p("w")),
    And(GT($s("u"),$s("v")),GT($s("v"),$s("w")))),
SetDelayed(GE($p("u"),$p("v")),
    And(And(RealNumericQ($s("u")),RealNumericQ($s("v"))),GreaterEqual(Re(N($s("u"))),Re(N($s("v")))))),
SetDelayed(GE($p("u"),$p("v"),$p("w")),
    And(GE($s("u"),$s("v")),GE($s("v"),$s("w")))),
SetDelayed(IndependentQ($p("u"),$p("x",$s("Symbol"))),
    FreeQ($s("u"),$s("x"))),
SetDelayed(SplitFreeFactors($p("u"),$p("x",$s("Symbol"))),
    If(ProductQ($s("u")),Map(Function(If(FreeQ(Slot1,$s("x")),List(Slot1,C1),List(C1,Slot1))),$s("u")),If(FreeQ($s("u"),$s("x")),List($s("u"),C1),List(C1,$s("u"))))),
SetDelayed(SplitFreeTerms($p("u"),$p("x",$s("Symbol"))),
    If(SumQ($s("u")),Map(Function(SplitFreeTerms(Slot1,$s("x"))),$s("u")),If(FreeQ($s("u"),$s("x")),List($s("u"),C0),List(C0,$s("u"))))),
SetDelayed(SplitFactorsOfTerms($p("u"),$p("x",$s("Symbol"))),
    Module(List(Set($s("lst"),SplitFreeTerms($s("u"),$s("x"))),$s("v"),$s("w")),CompoundExpression(CompoundExpression(CompoundExpression(Set($s("v"),Part($s("lst"),C1)),Set($s("w"),Part($s("lst"),C2))),If(ZeroQ($s("w")),Set($s("lst"),List()),If(SumQ($s("w")),CompoundExpression(CompoundExpression(Set($s("lst"),Map(Function(SplitFreeFactors(Slot1,$s("x"))),Apply($s("List"),$s("w")))),Set($s("lst"),Map(Function(Prepend(SplitFreeFactors(Regularize(Part(Slot1,C2),$s("x")),$s("x")),Part(Slot1,C1))),$s("lst")))),Set($s("lst"),Map(Function(List(Times(Part(Slot1,C1),Part(Slot1,C2)),Part(Slot1,C3))),$s("lst")))),CompoundExpression(CompoundExpression(Set($s("lst"),SplitFreeFactors($s("w"),$s("x"))),Set($s("lst"),Prepend(SplitFreeFactors(Regularize(Part($s("lst"),C2),$s("x")),$s("x")),Part($s("lst"),C1)))),Set($s("lst"),List(List(Times(Part($s("lst"),C1),Part($s("lst"),C2)),Part($s("lst"),C3)))))))),If(ZeroQ($s("v")),$s("lst"),Prepend($s("lst"),List(C1,$s("v"))))))),
SetDelayed(SplitMonomialTerms($p("u"),$p("x",$s("Symbol"))),
    Map(Function(If(Or(FreeQ(Slot1,$s("x")),MatchQ(Slot1,Condition(Times($p("a",true),Power($s("x"),$p("n",true))),FreeQ(List($s("a"),$s("n")),$s("x"))))),List(Slot1,C0),List(C0,Slot1))),$s("u")))
  );
}
