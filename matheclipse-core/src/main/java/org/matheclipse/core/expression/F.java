package org.matheclipse.core.expression;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.convert.Converter;
import org.matheclipse.core.convert.Object2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.Namespace;
import org.matheclipse.core.eval.SystemNamespace;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.reflection.system.Package;

import apache.harmony.math.BigInteger;
import apache.harmony.math.Rational;

/**
 * Factory for creating MathEclipse expression objects.
 * 
 * See <a
 * href="http://code.google.com/p/symja/wiki/AddNewFunctions">AddNewFunctions
 * </a>
 */
public class F {
	transient private final static HashMap<String, ISymbol> fSymbolMap = new HashMap<String, ISymbol>();

	transient private static ISymbolObserver SYMBOL_OBSERVER = new ISymbolObserver() {
		@Override
		public final boolean createSymbol(String symbol) {
			return false;
		}
	};

	public static ISymbol Abs;

	public static ISymbol And;

	public static ISymbol Append;

	public static ISymbol Apply;

	public static ISymbol ArcCos;

	public static ISymbol ArcSin;

	public static ISymbol ArcTan;

	public static ISymbol AtomQ;

	public static ISymbol Binomial;

	public static ISymbol Break;

	public static ISymbol Ceil;

	public static ISymbol Complex;

	public static ISymbol CompoundExpression;

	public static ISymbol Condition;

	public static ISymbol Continue;

	public static ISymbol Cos;

	public static ISymbol Cosh;

	public static ISymbol Cross;

	public static ISymbol D;

	public static ISymbol Denominator;

	public static ISymbol Depth;

	public static ISymbol Det;

	public static ISymbol Dot;

	public static ISymbol Equal;

	public static ISymbol EvenQ;

	public static ISymbol Expand;

	public static ISymbol ExpandAll;

	public static ISymbol Factor;

	public static ISymbol Factorial;

	public static ISymbol FactorInteger;

	public static ISymbol Fibonacci;

	public static ISymbol FindRoot;

	public static ISymbol Floor;

	public static ISymbol FreeQ;

	public static ISymbol FullForm;

	public static ISymbol Function;

	public static ISymbol GCD;

	public static ISymbol Greater;

	public static ISymbol GreaterEqual;

	public static ISymbol GroebnerBasis;

	public static ISymbol Head;

	public static ISymbol Hold;

	public static ISymbol If;

	public static ISymbol IntegerQ;

	public static ISymbol Integrate;

	public static ISymbol Inverse;

	public static ISymbol KOrderlessPartitions;

	public static ISymbol KPartitions;

	public static ISymbol KSubsets;

	public static ISymbol LeafCount;

	public static ISymbol Length;

	public static ISymbol Less;

	public static ISymbol LessEqual;

	public static ISymbol Level;

	public static ISymbol Map;

	public static ISymbol MapAll;

	public static ISymbol MatrixPower;

	public static ISymbol Max;

	public static ISymbol MemberQ;

	public static ISymbol Min;

	public static ISymbol Mod;

	public static ISymbol N;

	public static ISymbol Numerator;

	public static ISymbol Negative;

	public static ISymbol NonNegative;

	public static ISymbol Not;

	public static ISymbol NumberPartitions;

	public static ISymbol NumberQ;

	public static ISymbol OddQ;

	public static ISymbol Or;

	public static ISymbol Order;

	public static ISymbol OrderedQ;

	public static ISymbol Part;

	public static ISymbol Partition;

	public static ISymbol Permutations;

	public static ISymbol Plot;

	public static ISymbol Plot3D;

	public static ISymbol Plus;

	public static ISymbol Positive;

	public static ISymbol Power;

	public static ISymbol Prepend;

	public static ISymbol PrimeQ;

	public static ISymbol Print;

	public static ISymbol Product;

	public static ISymbol Quotient;

	public static ISymbol Rational;

	public static ISymbol RootOf;

	public static ISymbol ReplaceAll;

	public static ISymbol Reverse;

	public static ISymbol RotateLeft;

	public static ISymbol RotateRight;

	public static ISymbol Rule;

	public static ISymbol Set;

	public static ISymbol SetAttributes;

	public static ISymbol SetDelayed;

	public static ISymbol Sign;

	public static ISymbol SignCmp;

	public static ISymbol Sin;

	public static ISymbol Sinh;

	public static ISymbol Sort;

	public static ISymbol Sum;

	public static ISymbol Tan;

	public static ISymbol Tanh;

	public static ISymbol Times;

	public static ISymbol Timing;

	/**
	 * Trace of a matrix
	 */
	public static ISymbol Tr;

	/**
	 * Trace of the execution in the evaluation engine
	 */
	public static ISymbol Trace;

	/**
	 * Transpose a matrix
	 */
	public static ISymbol Transpose;

	public static ISymbol TrueQ;

	public static ISymbol Trunc;

	public static ISymbol Unequal;

	public static ISymbol While;

	/**
	 * Constant integer &quot;0&quot;
	 */
	public static IInteger C0;

	/**
	 * Constant integer &quot;1&quot;
	 */
	public static IInteger C1;

	/**
	 * Constant integer &quot;2&quot;
	 */
	public static IInteger C2;

	/**
	 * Constant integer &quot;3&quot;
	 */
	public static IInteger C3;

	/**
	 * Constant integer &quot;4&quot;
	 */
	public static IInteger C4;

	/**
	 * Constant integer &quot;5&quot;
	 */
	public static IInteger C5;

	/**
	 * Complex imaginary unit. The parsed symbol &quot;I&quot; is converted on
	 * input to this constant.
	 */
	public static IComplex CI;

	/**
	 * Complex negative imaginary unit.
	 */
	public static IComplex CNI;

	/**
	 * Constant fraction &quot;1/2&quot;
	 */
	public static IFraction C1D2;

	/**
	 * Constant fraction &quot;-1/2&quot;
	 */
	public static IFraction CN1D2;

	/**
	 * Constant fraction &quot;1/3&quot;
	 */
	public static IFraction C1D3;

	/**
	 * Constant fraction &quot;-1/3&quot;
	 */
	public static IFraction CN1D3;

	/**
	 * Constant fraction &quot;1/4&quot;
	 */
	public static IFraction C1D4;

	/**
	 * Constant fraction &quot;-1/4&quot;
	 */
	public static IFraction CN1D4;

	/**
	 * Constant double &quot;0.0&quot;
	 */
	public static INum CD0;

	/**
	 * Constant double &quot;1.0&quot;
	 */
	public static INum CD1;

	public static IAST CInfinity;
	public static IAST CNInfinity;

	/**
	 * Constant integer &quot;-1&quot;
	 */
	public static IInteger CN1;

	public static ISymbol ComplexInfinity;

	public static ISymbol Constant;

	public static ISymbol DirectedInfinity;

	public static ISymbol False;

	/**
	 * Attribute for associative functions (i.e. Dot, Times, Plus,...)
	 */
	public static ISymbol Flat;

	/**
	 * Attribute for <i><b>don't evaluate the arguments</b></i> of a function
	 */
	public static ISymbol HoldAll;

	/**
	 * Attribute for <i><b>don't evaluate the first argument</b></i> of a function
	 */
	public static ISymbol HoldFirst;

	/**
	 * Attribute for <i><b>only evaluate the first argument</b></i> of a function
	 */
	public static ISymbol HoldRest;

	public static ISymbol Indeterminate;

	public static ISymbol Infinity;

	public static ISymbol Line;

	public static ISymbol Limit;
	/**
	 * Head-Symbol for lists (i.e. <code>{a,b,c} ~~ List[a,b,c]</code>)
	 */
	public static ISymbol List;

	/**
	 * Attribute for listable functions (i.e. Sin, Cos,...)
	 */
	public static ISymbol Listable;

	public static ISymbol NHoldAll;

	public static ISymbol NHoldFirst;

	public static ISymbol NHoldRest;

	public static ISymbol Null;

	public static ISymbol NumericFunction;

	public static ISymbol OneIdentity;

	/**
	 * Attribute for commutative functions (i.e. Times, Plus,...)
	 */
	public static ISymbol Orderless;

	public static ISymbol E;

	public static ISymbol Pi;

	public static ISymbol Log;

	public static ISymbol True;

	public static ISymbol Second;

	public static ISymbol BoxRatios;

	public static ISymbol MeshRange;

	public static ISymbol PlotRange;

	public static ISymbol AxesStyle;

	public static ISymbol Automatic;

	public static ISymbol AxesOrigin;

	public static ISymbol Axes;

	public static ISymbol Background;

	public static ISymbol White;

	public static ISymbol Slot;

	public static ISymbol Options;

	public static ISymbol Graphics;

	public static ISymbol SurfaceGraphics;

	public static ISymbol Show;

	public static ISymbol IntegerHead;

	public static ISymbol RationalHead;

	public static ISymbol SymbolHead;

	public static ISymbol RealHead;

	public static ISymbol ComplexHead;

	public static ISymbol PatternHead;

	public static ISymbol BlankHead;

	public static ISymbol StringHead;

	//
	// public static Generic JSCL_LEXICOGRAPHIC;
	//
	// public static Generic JSCL_DEGREE_LEXICOGRAPHIC;
	//
	// public static Generic JSCL_DEGREE_REVERSE_LEXICOGRAPHIC;

	// --- generated function symbols

	// --- generated source codes:
	public static IAST Abs(final IExpr a0) {

		return unary(Abs, a0);
	}

	public static IAST ACos(final IExpr a0) {

		return unary(ArcCos, a0);
	}

	public static IAST Append(final IExpr a0) {

		return unary(Append, a0);
	}

	public static IAST Apply(final IExpr a0, final IExpr a1) {
		return binary(Apply, a0, a1);
	}

	public static IAST ASin(final IExpr a0) {

		return unary(ArcSin, a0);
	}

	public static IAST ATan(final IExpr a0) {

		return unary(ArcTan, a0);
	}

	public static IAST CNInfinity() {
		return binary(Times, CN1, Infinity);
	}

	public static IAST Condition(final IExpr a0, final IExpr a1) {

		return binary(Condition, a0, a1);
	}

	public static IAST Cos(final IExpr a0) {

		return unary(Cos, a0);
	}

	public static IAST Cosh(final IExpr a0) {

		return unary(Cosh, a0);
	}

	public static IAST Cross(final IExpr a0, final IExpr a1) {

		return binary(Cross, a0, a1);
	}

	public static IAST D() {

		return function(D);
	}

	public static IAST D(final IExpr a0, final IExpr a1) {

		return binary(D, a0, a1);
	}

	public static IAST Denominator(final IExpr a0) {

		return unary(Denominator, a0);
	}

	public static IAST Depth(final IExpr a0) {

		return unary(Depth, a0);
	}

	public static IAST Det(final IExpr a0) {

		return unary(Det, a0);
	}

	public static IAST Dot(final IExpr a0, final IExpr a1) {

		return binary(Dot, a0, a1);
	}

	public static IAST Dot(final IExpr... a) {// 0, final IExpr a1, final IExpr
		// a2) {
		return ast(a, Dot);
		// return ternary(Dot, a0, a1, a2);
	}

	public static IAST Equal(final IExpr a0, final IExpr a1) {

		return binary(Equal, a0, a1);
	}

	public static IAST Equal(final IExpr... a) {// , final IExpr a1, final IExpr
		// a2) {
		return ast(a, Equal);
		// return ternary(Equal, a0, a1, a2);
	}

	public static IAST Expand(final IExpr a0) {

		return unary(Expand, a0);
	}

	public static IAST Factor(final IExpr a0) {

		return unary(Factor, a0);
	}

	public static IAST Factorial(final IExpr a0) {

		return unary(Factorial, a0);
	}

	public static IAST Fibonacci(final IExpr a0) {

		return unary(Fibonacci, a0);
	}

	public static IAST FreeQ(final IExpr a0) {

		return unary(FreeQ, a0);
	}

	public static IAST FullForm(final IExpr a0) {

		return unary(FullForm, a0);
	}

	public static IAST GCD(final IExpr a0, final IExpr a1) {

		return binary(GCD, a0, a1);
	}

	public static IAST Graphics() {

		return function(Graphics);
	}

	public static IAST Hold(final IExpr a0) {
		return unary(Hold, a0);
	}

	public static IAST Numerator(final IExpr a0) {
		return unary(Numerator, a0);
	}

	private static boolean isSystemInitialized = false;

	/**
	 * Initialize the complete System. Calls
	 * {@link #initSymbols(String, ISymbolObserver)} with parameters
	 * <code>null, null</code>.
	 */
	public synchronized static void initSymbols() {
		initSymbols(null, null);
	}

	/**
	 * Initialize the complete System. Calls
	 * {@link #initSymbols(String, ISymbolObserver)} with parameters
	 * <code>fileName, null</code>.
	 * 
	 * @param fileName
	 */
	public synchronized static void initSymbols(String fileName) {
		initSymbols(fileName, null);
	}

	/**
	 * Initialize the complete System
	 * 
	 * @param fileName
	 *          <code>null</code> or optional text filename, which includes the
	 *          preloaded system rules
	 * @param symbolObserver
	 *          TODO
	 */
	public synchronized static void initSymbols(String fileName, ISymbolObserver symbolObserver) {

		if (!isSystemInitialized) {
			isSystemInitialized = true;
			// try {
			// JSCL_LEXICOGRAPHIC = Variable.valueOf("lex").expressionValue();
			// JSCL_DEGREE_LEXICOGRAPHIC =
			// Variable.valueOf("tdl").expressionValue();
			// JSCL_DEGREE_REVERSE_LEXICOGRAPHIC =
			// Variable.valueOf("drl").expressionValue();
			// } catch (NotVariableException e) {
			// e.printStackTrace();
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
			Converter.add(AST2Expr.CONST);
			Converter.add(Object2Expr.CONST);

			C0 = IntegerSym.valueOf(0);
			C1 = IntegerSym.valueOf(1);
			C2 = IntegerSym.valueOf(2);
			C3 = IntegerSym.valueOf(3);
			C4 = IntegerSym.valueOf(4);
			C5 = IntegerSym.valueOf(5);
			CN1 = IntegerSym.valueOf(-1);

			C1D2 = FractionSym.valueOf(1, 2);
			C1D3 = FractionSym.valueOf(1, 3);
			C1D4 = FractionSym.valueOf(1, 4);
			CN1D2 = FractionSym.valueOf(-1, 2);
			CN1D3 = FractionSym.valueOf(-1, 3);
			CN1D4 = FractionSym.valueOf(-1, 4);

			CI = ComplexSym.valueOf(BigInteger.ZERO, BigInteger.ONE);
			CNI = ComplexSym.valueOf(BigInteger.ZERO, BigInteger.MINUS_ONE);

			CD0 = Num.valueOf(0.0);
			CD1 = Num.valueOf(1.0);

			/**
			 * Define the &quot;set symbols&quot; first, because of dependencies in
			 * the predefined rules
			 */
			Set = predefinedSymbol("Set");
			SetDelayed = predefinedSymbol("SetDelayed");

			List = predefinedSymbol(IConstantHeaders.List);
			Log = predefinedSymbol(IConstantHeaders.Log);
			True = predefinedSymbol(IConstantHeaders.True);
			False = predefinedSymbol(IConstantHeaders.False);
			Null = predefinedSymbol(IConstantHeaders.Null);
			E = predefinedSymbol(IConstantHeaders.E);
			Pi = predefinedSymbol(IConstantHeaders.Pi);
			Second = predefinedSymbol(IConstantHeaders.Second);
			Indeterminate = predefinedSymbol("Indeterminate");
			Infinity = predefinedSymbol(IConstantHeaders.Infinity);
			ComplexInfinity = predefinedSymbol(IConstantHeaders.ComplexInfinity);
			DirectedInfinity = predefinedSymbol(IConstantHeaders.DirectedInfinity);

			Listable = predefinedSymbol(IConstantHeaders.Listable);
			Constant = predefinedSymbol(IConstantHeaders.Constant);
			NumericFunction = predefinedSymbol(IConstantHeaders.NumericFunction);
			Orderless = predefinedSymbol(IConstantHeaders.Orderless);
			OneIdentity = predefinedSymbol(IConstantHeaders.OneIdentity);
			Flat = predefinedSymbol(IConstantHeaders.Flat);
			HoldFirst = predefinedSymbol(IConstantHeaders.HoldFirst);
			HoldRest = predefinedSymbol(IConstantHeaders.HoldRest);
			HoldAll = predefinedSymbol(IConstantHeaders.HoldAll);
			NHoldFirst = predefinedSymbol(IConstantHeaders.NHoldFirst);
			NHoldRest = predefinedSymbol(IConstantHeaders.NHoldRest);
			NHoldAll = predefinedSymbol(IConstantHeaders.NHoldAll);

			Line = predefinedSymbol(IConstantHeaders.Line);
			BoxRatios = predefinedSymbol(IConstantHeaders.BoxRatios);
			MeshRange = predefinedSymbol(IConstantHeaders.MeshRange);
			PlotRange = predefinedSymbol(IConstantHeaders.PlotRange);

			AxesStyle = predefinedSymbol(IConstantHeaders.AxesStyle);
			Automatic = predefinedSymbol(IConstantHeaders.Automatic);
			AxesOrigin = predefinedSymbol(IConstantHeaders.AxesOrigin);
			Axes = predefinedSymbol(IConstantHeaders.Axes);
			Background = predefinedSymbol(IConstantHeaders.Background);
			White = predefinedSymbol(IConstantHeaders.White);

			// _Failed = createPredefinedSymbol("$Failed");

			IntegerHead = predefinedSymbol(IConstantHeaders.IntegerHead);
			RationalHead = predefinedSymbol(IConstantHeaders.RationalHead);
			SymbolHead = predefinedSymbol(IConstantHeaders.SymbolHead);
			RealHead = predefinedSymbol(IConstantHeaders.RealHead);
			ComplexHead = predefinedSymbol(IConstantHeaders.ComplexHead);
			PatternHead = predefinedSymbol(IConstantHeaders.PatternHead);
			BlankHead = predefinedSymbol(IConstantHeaders.BlankHead);
			StringHead = predefinedSymbol(IConstantHeaders.StringHead);

			Slot = predefinedSymbol("Slot");
			Options = predefinedSymbol("Options");
			Graphics = predefinedSymbol("Graphics");
			ReplaceAll = predefinedSymbol("ReplaceAll");
			Show = predefinedSymbol("Show");
			SurfaceGraphics = predefinedSymbol("SurfaceGraphics");

			// generated symbols
			Abs = predefinedSymbol("Abs");
			And = predefinedSymbol("And");
			Append = predefinedSymbol("Append");
			Apply = predefinedSymbol("Apply");
			ArcCos = predefinedSymbol("ArcCos");
			ArcSin = predefinedSymbol("ArcSin");
			ArcTan = predefinedSymbol("ArcTan");
			AtomQ = predefinedSymbol("AtomQ");
			Binomial = predefinedSymbol("Binomial");
			Break = predefinedSymbol("Break");
			Ceil = predefinedSymbol("Ceil");
			CompoundExpression = predefinedSymbol("CompoundExpression");
			Condition = predefinedSymbol("Condition");
			Continue = predefinedSymbol("Continue");
			Cos = predefinedSymbol("Cos");
			Cosh = predefinedSymbol("Cosh");
			Cross = predefinedSymbol("Cross");
			D = predefinedSymbol("D");
			Denominator = predefinedSymbol("Denominator");
			Det = predefinedSymbol("Det");
			Dot = predefinedSymbol("Dot");
			Equal = predefinedSymbol("Equal");
			EvenQ = predefinedSymbol("EvenQ");
			Expand = predefinedSymbol("Expand");
			ExpandAll = predefinedSymbol("ExpandAll");
			Factor = predefinedSymbol("Factor");
			Factorial = predefinedSymbol("Factorial");
			FactorInteger = predefinedSymbol("FactorInteger");
			Fibonacci = predefinedSymbol("Fibonacci");
			FindRoot = predefinedSymbol("FindRoot");
			Floor = predefinedSymbol("Floor");
			FreeQ = predefinedSymbol("FreeQ");
			FullForm = predefinedSymbol("FullForm");
			Function = predefinedSymbol("Function");
			GCD = predefinedSymbol("GCD");
			Greater = predefinedSymbol("Greater");
			GreaterEqual = predefinedSymbol("GreaterEqual");
			GroebnerBasis = predefinedSymbol("GroebnerBasis");
			Head = predefinedSymbol("Head");
			Hold = predefinedSymbol("Hold");
			IntegerQ = predefinedSymbol("IntegerQ");
			Integrate = predefinedSymbol("Integrate");
			Inverse = predefinedSymbol("Inverse");
			KOrderlessPartitions = predefinedSymbol("KOrderlessPartitions");
			KPartitions = predefinedSymbol("KPartitions");
			KSubsets = predefinedSymbol("KSubsets");
			LeafCount = predefinedSymbol("LeafCount");
			Length = predefinedSymbol("Length");
			Less = predefinedSymbol("Less");
			LessEqual = predefinedSymbol("LessEqual");
			Level = predefinedSymbol("Level");
			Limit = predefinedSymbol("Limit");
			Map = predefinedSymbol("Map");
			MapAll = predefinedSymbol("MapAll");
			MatrixPower = predefinedSymbol("MatrixPower");
			Max = predefinedSymbol("Max");
			MemberQ = predefinedSymbol("MemberQ");
			Min = predefinedSymbol("Min");
			Mod = predefinedSymbol("Mod");
			N = predefinedSymbol("N");
			Negative = predefinedSymbol("Negative");
			NonNegative = predefinedSymbol("NonNegative");
			Not = predefinedSymbol("Not");
			NumberPartitions = predefinedSymbol("NumberPartitions");
			NumberQ = predefinedSymbol("NumberQ");
			Numerator = predefinedSymbol("Numerator");
			OddQ = predefinedSymbol("OddQ");
			Or = predefinedSymbol("Or");
			Order = predefinedSymbol("Order");
			OrderedQ = predefinedSymbol("OrderedQ");
			Part = predefinedSymbol("Part");
			Partition = predefinedSymbol("Partition");
			Permutations = predefinedSymbol("Permutations");
			Plot = predefinedSymbol("Plot");
			Plot3D = predefinedSymbol("Plot3D");
			Plus = predefinedSymbol("Plus");
			Positive = predefinedSymbol("Positive");
			Power = predefinedSymbol("Power");
			Prepend = predefinedSymbol("Prepend");
			PrimeQ = predefinedSymbol("PrimeQ");
			Print = predefinedSymbol("Print");
			Product = predefinedSymbol("Product");
			Quotient = predefinedSymbol("Quotient");
			Reverse = predefinedSymbol("Reverse");
			RootOf = predefinedSymbol("RootOf");
			RotateLeft = predefinedSymbol("RotateLeft");
			RotateRight = predefinedSymbol("RotateRight");
			Rule = predefinedSymbol("Rule");
			SetAttributes = predefinedSymbol("SetAttributes");
			Sign = predefinedSymbol("Sign");
			SignCmp = predefinedSymbol("SignCmp");
			Sin = predefinedSymbol("Sin");
			Sinh = predefinedSymbol("Sinh");
			Sort = predefinedSymbol("Sort");
			Sum = predefinedSymbol("Sum");
			Tan = predefinedSymbol("Tan");
			Tanh = predefinedSymbol("Tanh");
			Times = predefinedSymbol("Times");
			Timing = predefinedSymbol("Timing");
			Tr = predefinedSymbol("Tr");
			Trace = predefinedSymbol("Trace");
			Transpose = predefinedSymbol("Transpose");
			TrueQ = predefinedSymbol("TrueQ");
			Trunc = predefinedSymbol("Trunc");
			Unequal = predefinedSymbol("Unequal");
			While = predefinedSymbol("While");

			CInfinity = function(DirectedInfinity, C1);
			CNInfinity = function(DirectedInfinity, CN1);

			if (symbolObserver != null) {
				SYMBOL_OBSERVER = symbolObserver;
			}

			Reader reader = null;
			if (fileName != null) {
				try {
					reader = new FileReader(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (reader == null) {
				InputStream systemPackage = F.class.getResourceAsStream("/System.mep");
				if (systemPackage != null) {
					reader = new InputStreamReader(systemPackage);
				}
			}
			if (reader != null) {
				Package.loadPackage(EvalEngine.get(), reader);
			}
		}
	}

	public static IAST Integrate(final IExpr a0, final IExpr a1) {

		return binary(Integrate, a0, a1);
	}

	public static IAST Inverse(final IExpr a0) {

		return unary(Inverse, a0);
	}

	public static IAST KOrderlessPartitions(final IExpr a0) {

		return unary(KOrderlessPartitions, a0);
	}

	public static IAST KPartitions(final IExpr a0) {

		return unary(KPartitions, a0);
	}

	public static IAST KSubsets(final IExpr a0) {

		return unary(KSubsets, a0);
	}

	public static IAST LeafCount(final IExpr a0) {

		return unary(LeafCount, a0);
	}

	public static IAST Less(final IExpr a0, final IExpr a1) {

		return binary(Less, a0, a1);
	}

	public static IAST LessEqual(final IExpr a0, final IExpr a1) {

		return binary(LessEqual, a0, a1);
	}

	public static IAST Line() {
		return function(Line);
	}

	public static IAST Limit(final IExpr a0, final IExpr a1) {

		return binary(Limit, a0, a1);
	}

	public static IAST List() {
		return function(List);
	}

	public static IAST List(final IExpr a0) {

		return unary(List, a0);
	}

	public static IAST List(final IExpr a0, final IExpr a1) {

		return binary(List, a0, a1);
	}

	public static IAST List(final IExpr... a) {// 0, final IExpr a1, final IExpr
		// a2) {
		return ast(a, List);
		// return ternary(List, a0, a1, a2);
	}

	public static IAST Log(final IExpr a0) {

		return unary(Log, a0);
	}

	public static IAST Map(final IExpr a0) {

		return unary(Map, a0);
	}

	public static IAST MapAll(final IExpr a0) {

		return unary(MapAll, a0);
	}

	public static IAST MatrixPower(final IExpr a0) {

		return unary(MatrixPower, a0);
	}

	public static IAST Max() {
		return function(Max);
	}

	public static IAST Max(final IExpr a0, final IExpr a1) {
		return binary(Max, a0, a1);
	}

	public static IAST MemberQ(final IExpr a0) {

		return unary(MemberQ, a0);
	}

	public static IAST Min() {
		return function(Min);
	}

	public static IAST Min(final IExpr a0, final IExpr a1) {
		return binary(Min, a0, a1);
	}

	/**
	 * Evaluate the given expression in numeric mode
	 * 
	 * @param a0
	 * @return
	 */
	public static IAST N(final IExpr a0) {

		return unary(N, a0);
	}

	public static IAST Negate(final IExpr a) {

		return binary(Times, C1, a);
	}

	public static IAST NumberPartitions(final IExpr a0) {

		return unary(NumberPartitions, a0);
	}

	public static IAST NumberQ(final IExpr a0) {

		return unary(NumberQ, a0);
	}

	public static IAST Options(final IExpr a0) {

		return unary(Options, a0);
	}

	public static IAST Partition(final IExpr a0) {

		return unary(Partition, a0);
	}

	public static IAST Permutations(final IExpr a0) {

		return unary(Permutations, a0);
	}

	public static IAST Plus() {
		return function(Plus);
	}

	public static IAST Plus(final IExpr a0) {

		return unary(Plus, a0);
	}

	public static IAST Plus(final IExpr a0, final IExpr a1) {

		return binary(Plus, a0, a1);
	}

	public static IAST Plus(final IExpr... a) {// 0, final IExpr a1, final IExpr
		// a2) {
		return ast(a, Plus);
		// return ternary(Plus, a0, a1, a2);
	}

	public static IAST Power() {

		return function(Power);
	}

	public static IAST Power(final IExpr a0, final IExpr a1) {
		return binary(Power, a0, a1);
	}

	public static IAST Power(final IExpr a0, final long exp) {
		return binary(Power, a0, integer(exp));
	}

	public static IAST Prepend(final IExpr a0) {

		return unary(Prepend, a0);
	}

	public static IAST PrimeQ(final IExpr a0) {

		return unary(PrimeQ, a0);
	}

	public static IAST ReplaceAll(final IExpr a0, final IExpr a1) {

		return binary(ReplaceAll, a0, a1);
	}

	public static IAST Rule(final IExpr a0, final IExpr a1) {

		return binary(Rule, a0, a1);
	}

	public static IAST Set(final IExpr a0, final IExpr a1) {

		return binary(Set, a0, a1);
	}

	public static IAST SetAttributes(final IExpr a0) {

		return unary(SetAttributes, a0);
	}

	public static IAST SetDelayed(final IExpr a0, final IExpr a1) {

		return binary(SetDelayed, a0, a1);
	}

	public static IAST Show(final IExpr a0) {

		return unary(Show, a0);
	}

	public static IAST Sin(final IExpr a0) {

		return unary(Sin, a0);
	}

	public static IAST Sinh(final IExpr a0) {

		return unary(Sinh, a0);
	}

	public static IAST Slot(final IExpr a0) {
		return unary(Slot, a0);
	}

	public static IAST Slot(final int i) {
		return unary(Slot, integer(i));
	}

	public static IAST Sqr(final IExpr a0) {

		return binary(Power, a0, C2);
	}

	public static IAST Sqrt(final IExpr a0) {

		return binary(Power, a0, C1D2);
	}

	public static IAST Subtract(final IExpr a0, final IExpr a1) {
		return binary(Plus, a0, binary(Times, CN1, a1));
	}

	public static IAST SurfaceGraphics() {

		return function(SurfaceGraphics);
	}

	public static IAST Tan(final IExpr a0) {

		return unary(Tan, a0);
	}

	public static IAST Tanh(final IExpr a0) {

		return unary(Tanh, a0);
	}

	public static IAST Times() {
		return function(Times);
	}

	public static IAST Times(final IExpr a0) {
		return unary(Times, a0);
	}

	public static IAST Times(final IExpr a0, final IExpr a1) {
		return binary(Times, a0, a1);
	}

	public static IAST Times(final IExpr... a) {
		return ast(a, Times);
	}

	public static IAST Tr(final IExpr a0) {
		return unary(Tr, a0);
	}

	public static IAST Trace(final IExpr a0) {
		return unary(Trace, a0);
	}

	public static IAST Transpose(final IExpr a0) {
		return unary(Transpose, a0);
	}

	/**
	 * Creates a new list from the given ast and symbol. if <code>include</code>
	 * is set to <code>true </code> all arguments from index first to last-1 are
	 * copied in the new list if <code>include</code> is set to
	 * <code> false </code> all arguments excluded from index first to last-1 are
	 * copied in the new list
	 * 
	 */
	public static IAST ast(final IAST f, final IExpr sym, final boolean include, final int first, final int last) {
		// return new AST(f, sym, incl, first, last);
		final AST ast = AST.newInstance(sym);
		if (include == true) {
			// range include
			for (int i = first; i < last; i++) {
				ast.add(f.get(i));
			}
		} else {
			// range exclude
			for (int i = 1; i < first; i++) {
				ast.add(f.get(i));
			}
			for (int j = last; j < f.size(); j++) {
				ast.add(f.get(j));
			}
		}
		return ast;
	}

	public static IAST ast(final IExpr head) {
		return AST.newInstance(head);
	}

	/**
	 * Short form for creating an AST
	 * 
	 * @param head
	 * @param a
	 * @return
	 */
	public static IAST $(final IExpr head, final IExpr... a) {
		return ast(a, head);
	}

	public static IAST ast(final IExpr head, final int initialCapacity, final boolean setLength) {
		final AST ast = AST.newInstance(head);
		if (setLength) {
			for (int i = 0; i < initialCapacity; i++) {
				ast.add(null);
			}
		}
		return ast;
	}

	public static IAST ast(final IExpr[] arr, final IExpr head) {
		final AST ast = AST.newInstance(head);
		for (final IExpr expr : arr) {
			ast.add(expr);
		}
		return ast;
	}

	/**
	 * Create a function with 2 arguments without evaluation.
	 * 
	 * @param head
	 * @param a0
	 * @param a1
	 * @return
	 */
	public static IAST binary(final IExpr head, final IExpr a0, final IExpr a1) {
		final IAST ast = ast(head);
		ast.add(a0);
		ast.add(a1);
		return ast;
	}

	/**
	 * Gives symbols "True" or "False" (type ISymbol) depending on the boolean
	 * value.
	 * 
	 * @param value
	 * @return
	 */
	public static ISymbol bool(final boolean value) {
		if (value) {
			return True;
		}
		return False;
	}

	/**
	 * Create a symbolic complex number
	 * 
	 * @param re
	 * @return
	 */
	public static IComplex complex(final IFraction re) {
		return complex(re, fraction(0, 1));
	}

	/**
	 * Create a symbolic complex number
	 * 
	 * @param re
	 * @param im
	 * @return
	 */
	public static IComplex complex(final IFraction re, final IFraction im) {
		return ComplexSym.valueOf(re, im);
	}

	/**
	 * Create a symbolic complex number
	 * 
	 * @param re
	 * @param im
	 * @return
	 */
	public static IComplex complex(final long real_numerator, final long real_denominator, final long imag_numerator,
			final long imag_denominator) {
		return ComplexSym.valueOf(real_numerator, real_denominator, imag_numerator, imag_denominator);
	}

	/**
	 * Create a symbolic complex number
	 * 
	 * @param realPart
	 *          the real double value part which should be converted to a complex
	 *          number
	 * @param imagPart
	 *          the imaginary double value part which should be converted to a
	 *          complex number
	 * @return IFraction
	 */
	public static IComplex complex(final double realPart, final double imagPart) {
		return ComplexSym.valueOf(FractionSym.valueOf(realPart), FractionSym.valueOf(imagPart));
	}

	/**
	 * Create a symbolic complex number
	 * 
	 * @param re
	 * @param im
	 * @return
	 */
	public static IComplex complex(final IInteger re, final IInteger im) {
		return ComplexSym.valueOf(re, im);
	}

	/**
	 * Create a complex numeric number with imaginary part = 0.0
	 * 
	 * @param r
	 *          the real part of the number
	 * @return
	 */
	public static IComplexNum complexNum(final double r) {
		return complexNum(r, 0.0);
	}

	/**
	 * Create a complex numeric value
	 * 
	 * @param r
	 *          real part
	 * @param i
	 *          imaginary part
	 * @return
	 */
	public static IComplexNum complexNum(final double r, final double i) {
		return ComplexNum.valueOf(r, i);
	}

	/**
	 * Create a function with 1 argument and evaluate it.
	 * 
	 * @param head
	 * @param a0
	 * @return
	 */
	public static IExpr eval(final ISymbol head, final IExpr a0) {
		final IAST ast = ast(head);
		ast.add(a0);
		return EvalEngine.eval(ast);
	}

	/**
	 * Create a function with 2 arguments and evaluate it.
	 * 
	 * @param head
	 * @param a0
	 * @param a1
	 * @return
	 */
	public static IExpr eval(final ISymbol head, final IExpr a0, final IExpr a1) {
		final IAST ast = ast(head);
		ast.add(a0);
		ast.add(a1);
		return EvalEngine.eval(ast);
	}

	/**
	 * Create a function with 3 arguments and evaluate it.
	 * 
	 * @param head
	 * @param a0
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static IExpr eval(final ISymbol head, final IExpr a0, final IExpr a1, final IExpr a2) {
		final IAST ast = ast(head);
		ast.add(a0);
		ast.add(a1);
		ast.add(a2);
		return EvalEngine.eval(ast);
	}

	/**
	 * Evaluate the given expression in numeric mode
	 * 
	 * @param a0
	 * @return
	 */
	public static IExpr evaln(final IExpr a0) {
		return eval(N, a0);
	}

	/**
	 * Create a "fractional" number
	 * 
	 * @param numerator
	 *          numerator of the fractional number
	 * @param denominator
	 *          denumerator of the fractional number
	 * @return IFraction
	 */
	public static IFraction fraction(final IInteger numerator, final IInteger denominator) {
		return FractionSym.valueOf(numerator, denominator);
	}

	/**
	 * Create a "fractional" number
	 * 
	 * @param numerator
	 *          numerator of the fractional number
	 * @param denominator
	 *          denumerator of the fractional number
	 * @return IFraction
	 */
	public static IFraction fraction(final BigInteger numerator, final BigInteger denominator) {
		return FractionSym.valueOf(numerator, denominator);
	}

	/**
	 * Create a "fractional" number
	 * 
	 * @param numerator
	 *          numerator of the fractional number
	 * @param denominator
	 *          denumerator of the fractional number
	 * @return IFraction
	 */
	public static IFraction fraction(java.math.BigInteger jmNumeratorBig, java.math.BigInteger jmDenominatorBig) {
		return FractionSym.valueOf(new BigInteger(jmNumeratorBig.toByteArray()), new BigInteger(jmDenominatorBig.toByteArray()));
	}

	/**
	 * Create a "fractional" number
	 * 
	 * @param numerator
	 *          numerator of the fractional number
	 * @param denominator
	 *          denumerator of the fractional number
	 * @return IFraction
	 */
	public static IFraction fraction(final long numerator, final long denominator) {
		return FractionSym.valueOf(numerator, denominator);
	}

	/**
	 * Create a "fractional" number
	 * 
	 * @param value
	 *          the rational value which should be converted to a fractional
	 *          number
	 * @return IFraction
	 */
	public static IFraction fraction(final Rational value) {
		return FractionSym.valueOf(value);
	}

	/**
	 * Create a "fractional" number
	 * 
	 * @param value
	 *          the double value which should be converted to a fractional number
	 * @return IFraction
	 */
	public static IFraction fraction(final double value) {
		return FractionSym.valueOf(value);
	}

	/**
	 * Create a function
	 * 
	 * @param head
	 * @return
	 */
	public static IAST function(final IExpr head) {
		final IAST list = ast(head);
		return list;
	}

	/**
	 * Create a function
	 * 
	 * @param head
	 * @param arg0
	 * @return
	 */
	public static IAST function(final IExpr head, final IExpr arg0) {
		final IAST list = ast(head);
		list.add(arg0);
		return list;
	}

	/**
	 * Create a function
	 * 
	 * @param head
	 * @param arg0
	 *          first argument of the function
	 * @param arg1
	 *          second argument of the function
	 * @return
	 */
	public static IAST function(final IExpr head, final IExpr arg0, final IExpr arg1) {
		final IAST list = ast(head);
		list.add(arg0);
		list.add(arg1);
		return list;
	}

	/**
	 * Create a function
	 * 
	 * @param head
	 * @return
	 */
	public static IAST function(final ISymbol head) {
		final IAST list = ast(head);
		return list;
	}

	/**
	 * Create a function
	 * 
	 * @param head
	 * @param arg0
	 * @return
	 */
	public static IAST function(final ISymbol head, final IExpr arg0) {
		final IAST list = ast(head);
		list.add(arg0);
		return list;
	}

	/**
	 * Create a function
	 * 
	 * @param head
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static IAST function(final ISymbol head, final IExpr arg0, final IExpr arg1) {
		final IAST list = ast(head);
		list.add(arg0);
		list.add(arg1);
		return list;
	}

	/**
	 * Create a function
	 * 
	 * @param head
	 *          usually a String which tags the function
	 * @return IAST
	 * @see org.matheclipse.parser.client.IConstantHeaders
	 */
	public static IAST function(final String head) {
		final IAST list = ast(symbol(head));
		return list;
	}

	/**
	 * Create a unary function with 1 argument
	 * 
	 * @param head
	 *          usually a String which tags the function
	 * @param arg0
	 *          the argument of this function
	 * @return IAST
	 */
	public static IAST function(final String head, final IExpr arg0) {
		final IAST list = ast(symbol(head));
		list.add(arg0);
		return list;
	}

	/**
	 * Create a binary function with 2 arguments
	 * 
	 * @param head
	 *          usually a String which tags the function
	 * @param arg0
	 *          the first argument of this function
	 * @param arg1
	 *          the second argument of this function
	 * @return IAST
	 */
	public static IAST function(final String head, final IExpr arg0, final IExpr arg1) {
		final IAST list = ast(symbol(head));
		list.add(arg0);
		list.add(arg1);
		return list;
	}

	/**
	 * Get the thread local instance of the <code>ExprFactory</code>
	 * 
	 * @return
	 */
	// public static ExprFactory get() {
	// return instance.get();
	// }
	/**
	 * Get the namespace
	 * 
	 * @return
	 */
	final public static Namespace getNamespace() {
		return SystemNamespace.DEFAULT;
	}

	/**
	 * Create a large integer number.
	 * 
	 * @param integerValue
	 * @return
	 */
	public static IInteger integer(final BigInteger integerValue) {
		return IntegerSym.valueOf(integerValue);
	}

	/**
	 * Create a large integer number.
	 * 
	 * @param integerValue
	 * @return
	 */
	public static IInteger integer(java.math.BigInteger jmBig) {
		return IntegerSym.valueOf(new BigInteger(jmBig.toByteArray()));
	}

	/**
	 * Create a large integer number.
	 * 
	 * @param integerValue
	 * @return
	 */
	public static IInteger integer(final long integerValue) {
		return IntegerSym.valueOf(integerValue);
	}

	/**
	 * Create a large integer number.
	 * 
	 * @param integerString
	 *          the integer number represented as a String
	 * @param numberFormat
	 *          the format of the number (usually 10)
	 * @return Object
	 */
	public static IInteger integer(final String integerString, final int numberFormat) {
		return IntegerSym.valueOf(integerString, numberFormat);
	}

	/**
	 * Create a numeric value
	 * 
	 * @param d
	 * @return
	 */
	public static Num num(final double d) {
		return Num.valueOf(d);
	}

	/**
	 * Create a numeric value
	 * 
	 * @param d
	 * @return
	 */
	public static Num num(final String doubleString) {
		return Num.valueOf(Double.parseDouble(doubleString));
	}

	/**
	 * Create a pattern for pattern-matching and term rewriting
	 * 
	 * @param symbol
	 * @return IPattern
	 */
	public static IPattern pattern(final ISymbol symbol) {
		if (symbol == null) {
			return Pattern.valueOf(null);
		}
		return Pattern.valueOf((Symbol) symbol);
	}

	/**
	 * Create a pattern for pattern-matching and term rewriting
	 * 
	 * @param symbol
	 * @param check
	 *          additional condition which should be checked in pattern-matching
	 * @return IPattern
	 */
	public static IPattern pattern(final ISymbol symbol, final IExpr check) {
		if (symbol == null) {
			return Pattern.valueOf(null, check);
		}
		return Pattern.valueOf((Symbol) symbol, check);
	}

	/**
	 * Create a pattern for pattern-matching and term rewriting
	 * 
	 * @param symbolName
	 * @return IPattern
	 */
	public static IPattern pattern(final String symbolName) {
		if (symbolName == null) {
			return Pattern.valueOf(null);
		}
		return Pattern.valueOf((Symbol) symbol(symbolName));
	}

	/**
	 * Create a pattern for pattern-matching and term rewriting
	 * 
	 * @param symbolName
	 * @param check
	 *          additional condition which should be checked in pattern-matching
	 * @return IPattern
	 */
	public static IPattern pattern(final String symbolName, final IExpr check) {
		if (symbolName == null) {
			return Pattern.valueOf(null, check);
		}
		return Pattern.valueOf((Symbol) symbol(symbolName), check);
	}

	/**
	 * Create a "predefined" symbol for constants or function names.
	 * 
	 * @param symbolName
	 * @return
	 */
	public static ISymbol predefinedSymbol(final String symbolName) {
		ISymbol temp = fSymbolMap.get(symbolName);
		if (temp != null) {
			return temp;
		}
		temp = new Symbol(symbolName);
		fSymbolMap.put(symbolName, temp);
		if (Character.isUpperCase(symbolName.charAt(0))) {
			// probably a predefined function use reflection to setUp this
			// symbol
			SystemNamespace.DEFAULT.setEvaluator(temp);
		}

		return temp;
	}

	/**
	 * Set the thread local instance.
	 * 
	 * @param factory
	 */
	// public static void set(final ExprFactory factory) {
	// instance.set(factory);
	// }
	/**
	 * Create a string expression
	 * 
	 * @param str
	 * @return
	 */
	final static public IStringX stringx(final String str) {
		return StringX.valueOf(str);
	}

	/**
	 * Create a string expression
	 * 
	 * @param str
	 * @return
	 */
	final static public IStringX stringx(final StringBuffer str) {
		return StringX.valueOf(str);
	}

	/**
	 * Get a global symbol which is retrieved from the global symbols map or
	 * created or retrieved from the thread local variables map.
	 * 
	 * @param symbolName
	 * @return
	 */
	public static ISymbol symbol(final String symbolName) {
		ISymbol temp = fSymbolMap.get(symbolName);
		if (temp != null) {
			return temp;
		}
		if (Config.SERVER_MODE) {
			if (Character.isUpperCase(symbolName.charAt(0))) {
				if (SYMBOL_OBSERVER.createSymbol(symbolName)) {
					// second try, because the symbol may now be added to fSymbolMap
					ISymbol temp2 = fSymbolMap.get(symbolName);
					if (temp2 != null) {
						return temp2;
					}
				}
			}
			EvalEngine engine = EvalEngine.get();
			Map<String, ISymbol> variableMap = engine.getVariableMap();
			temp = variableMap.get(symbolName);
			if (temp != null) {
				return temp;
			}
			temp = new Symbol(symbolName);
			variableMap.put(symbolName, temp);
		} else {
			temp = new Symbol(symbolName);
			fSymbolMap.put(symbolName, temp);
		}

		if (Character.isUpperCase(symbolName.charAt(0))) {
			// probably a predefined function
			// use reflection to setUp this symbol
			SystemNamespace.DEFAULT.setEvaluator(temp);
		}

		return temp;
	}

	/**
	 * Create a local symbol which is created or retrieved from the eval engines
	 * thread local variables map and push a value on the local stack;
	 * 
	 * @param symbolName
	 *          the name of the symbol
	 * @return
	 */
	public static ISymbol local(final String symbolName, IExpr value) {
		// HashMap<String, ISymbol> variableMap = EvalEngine.getVariableMap();
		// ISymbol temp = variableMap.get(symbolName);
		// if (temp != null) {
		// temp.pushLocalVariable(value);
		// return temp;
		// }
		ISymbol temp = new Symbol(symbolName);
		// variableMap.put(symbolName, temp);
		temp.pushLocalVariable(value);
		return temp;
	}

	/**
	 * Create a local symbol which is created or retrieved from the eval engines
	 * thread local variables map and push a <code>null</code> value on the local
	 * stack;
	 * 
	 * @param symbolName
	 *          the name of the symbol
	 * @return
	 */
	public static ISymbol local(final String symbolName) {
		return local(symbolName, null);
	}

	/**
	 * Pop the current top value from the symbols local variable stack.
	 * 
	 * @param temp
	 */
	public static void popLocal(ISymbol temp) {
		temp.popLocalVariable();
	}

	/**
	 * Create a function with 3 arguments without evaluation.
	 * 
	 * @param head
	 * @param a0
	 * @param a1
	 * @param a2
	 * @return
	 */
	// public static IAST ternary(final IExpr head, final IExpr a0, final IExpr
	// a1, final IExpr a2) {
	// final IAST ast = ast(head);
	// ast.add(a0);
	// ast.add(a1);
	// ast.add(a2);
	// return ast;
	// }
	/**
	 * Create a function with 1 argument without evaluation.
	 * 
	 * @param head
	 * @param a0
	 * @return
	 */
	public static IAST unary(final IExpr head, final IExpr a0) {
		final IAST ast = ast(head);
		ast.add(a0);
		return ast;
	}

	public static IExpr plus(Integer i, IExpr b) {
		return function(Plus, integer(i.longValue()), b);
	}

	public static IExpr plus(IExpr a, Integer i) {
		return function(Plus, a, integer(i.longValue()));
	}

	public static IExpr minus(Integer i, IExpr b) {
		return function(Plus, integer(i.longValue()), function(Times, b, CN1));
	}

	public static IExpr minus(IExpr a, Integer i) {
		return function(Plus, a, function(Times, integer(i.longValue()), CN1));
	}

	public static IExpr multiply(Integer i, IExpr b) {
		return function(Times, integer(i.longValue()), b);
	}

	public static IExpr multiply(IExpr a, Integer i) {
		return function(Times, a, integer(i.longValue()));
	}

	public static IExpr div(IExpr a, Integer i) {
		return function(Times, a, function(Power, integer(i.longValue()), CN1));
	}

	public static IExpr div(Integer i, IExpr b) {
		return function(Times, integer(i.longValue()), function(Power, b, CN1));
	}

	public static IExpr mod(IExpr a, Integer i) {
		return function(Mod, a, integer(i.longValue()));
	}

	public static IExpr mod(Integer i, IExpr b) {
		return function(Mod, integer(i.longValue()), b);
	}

	public static IExpr and(IExpr a, Integer i) {
		return function(And, a, integer(i.longValue()));
	}

	public static IExpr and(Integer i, IExpr b) {
		return function(And, integer(i.longValue()), b);
	}

	public static IExpr or(IExpr a, Integer i) {
		return function(Or, a, integer(i.longValue()));
	}

	public static IExpr or(Integer i, IExpr b) {
		return function(Or, integer(i.longValue()), b);
	}

	/**
	 * After a successful <code>isCase()</code> the symbols associated with the
	 * patterns contain the matched values on the local stack.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isCase(IExpr a, IExpr b) {
		if (a instanceof IAST) {
			final PatternMatcher matcher = new PatternMatcher(a);
			if (matcher.apply(b)) {
				matcher.setPatternValue2Local(a);
				return true;
			}
		}
		return equals(a, b);
	}

	public static boolean isCase(IExpr a, Integer i) {
		return isCase(a, integer(i.longValue()));
	}

	public static boolean isCase(Integer i, IExpr b) {
		return equals(i, b);
	}

	public static boolean isCase(IExpr a, java.math.BigInteger i) {
		return isCase(a, integer(i));
	}

	public static boolean isCase(java.math.BigInteger i, IExpr b) {
		return equals(i, b);
	}

	/**
	 * Test if the absolute value is less <code>Config.DOUBLE_EPSILON</code>.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isZero(double value) {
		return isZero(value, Config.DOUBLE_EPSILON);
	}

	/**
	 * Test if the absolute value is less than the given epsilon.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isZero(double value, double epsilon) {
		return Math.abs(value) < epsilon;
	}

	/**
	 * Evaluate an object. If no evaluation was possible this method returns the
	 * given argument.
	 * 
	 * @param a
	 *          the expression which should be evaluated
	 * @return the evaluated expression
	 * @see EvalEngine#eval(IExpr)
	 */
	public static IExpr eval(IExpr a) {
		return EvalEngine.eval(a);
	}

	public static IExpr cast(Object obj) {
		return Object2Expr.CONST.convert(obj);
	}

	public static boolean equals(IExpr a, IExpr b) {
		IExpr tempA = a;
		IExpr tempB = b;
		if (a instanceof AST) {
			tempA = eval(a);
		}
		if (b instanceof AST) {
			tempB = eval(b);
		}
		return tempA.equals(tempB);
	}

	public static boolean equals(IExpr a, java.math.BigInteger i) {
		IExpr tempA = a;
		IExpr tempB = integer(i);
		if (a instanceof AST) {
			tempA = eval(a);
		}
		return tempA.equals(tempB);
	}

	public static boolean equals(java.math.BigInteger i, IExpr b) {
		IExpr tempA = integer(i);
		IExpr tempB = b;
		if (b instanceof AST) {
			tempB = eval(b);
		}
		return tempA.equals(tempB);
	}

	public static boolean equals(IExpr a, Integer i) {
		IExpr tempA = a;
		IExpr tempB = integer(i.longValue());
		if (a instanceof AST) {
			tempA = eval(a);
		}
		return tempA.equals(tempB);
	}

	public static boolean equals(Integer i, IExpr b) {
		IExpr tempA = integer(i.longValue());
		IExpr tempB = b;
		if (b instanceof AST) {
			tempB = eval(b);
		}
		return tempA.equals(tempB);
	}

	public static int compareTo(IExpr a, IExpr b) throws UnsupportedOperationException {
		if (a instanceof ISignedNumber && b instanceof ISignedNumber) {
			return a.compareTo(b);
		}
		IExpr tempA = eval(a);
		IExpr tempB = eval(b);
		if (tempA instanceof ISignedNumber && tempB instanceof ISignedNumber) {
			return tempA.compareTo(tempB);
		}
		throw new UnsupportedOperationException("compareTo() - first or second argument could not be converted into a signed number.");
	}

	public static int compareTo(IExpr a, Integer i) throws UnsupportedOperationException {
		if (a instanceof ISignedNumber) {
			return a.compareTo(integer(i.longValue()));
		}
		IExpr temp = eval(a);
		if (temp instanceof ISignedNumber) {
			return temp.compareTo(integer(i.longValue()));
		}
		throw new UnsupportedOperationException("compareTo() - first argument could not be converted into a signed number.");
	}

	public static int compareTo(Integer i, IExpr b) throws UnsupportedOperationException {
		if (b instanceof ISignedNumber) {
			return integer(i.longValue()).compareTo(b);
		}
		IExpr temp = eval(b);
		if (temp instanceof ISignedNumber) {
			return integer(i.longValue()).compareTo(temp);
		}
		throw new UnsupportedOperationException("compareTo() - second argument could not be converted into a signed number.");
	}

	public static int compareTo(IExpr a, java.math.BigInteger i) throws UnsupportedOperationException {
		if (a instanceof ISignedNumber) {
			return a.compareTo(integer(i));
		}
		IExpr temp = eval(a);
		if (temp instanceof ISignedNumber) {
			return temp.compareTo(integer(i));
		}
		throw new UnsupportedOperationException("compareTo() - first argument could not be converted into a signed number.");
	}

	public static int compareTo(java.math.BigInteger i, IExpr b) throws UnsupportedOperationException {
		if (b instanceof ISignedNumber) {
			return integer(i).compareTo(b);
		}
		IExpr temp = eval(b);
		if (temp instanceof ISignedNumber) {
			return integer(i).compareTo(temp);
		}
		throw new UnsupportedOperationException("compareTo() - second argument could not be converted into a signed number.");
	}

	public static IExpr plus(java.math.BigInteger i, IExpr b) {
		return function(Plus, integer(i), b);
	}

	public static IExpr plus(IExpr a, java.math.BigInteger i) {
		return function(Plus, a, integer(i));
	}

	public static IExpr minus(java.math.BigInteger i, IExpr b) {
		return function(Plus, integer(i), function(Times, b, CN1));
	}

	public static IExpr minus(IExpr a, java.math.BigInteger i) {
		return function(Plus, a, function(Times, integer(i), CN1));
	}

	public static IExpr multiply(java.math.BigInteger i, IExpr b) {
		return function(Times, integer(i), b);
	}

	public static IExpr multiply(IExpr a, java.math.BigInteger i) {
		return function(Times, a, integer(i));
	}

	public static IExpr div(IExpr a, java.math.BigInteger i) {
		return function(Times, a, function(Power, integer(i), CN1));
	}

	public static IExpr div(java.math.BigInteger i, IExpr b) {
		return function(Times, integer(i), function(Power, b, CN1));
	}

	public static IExpr mod(IExpr a, java.math.BigInteger i) {
		return function(Mod, a, integer(i));
	}

	public static IExpr mod(java.math.BigInteger i, IExpr b) {
		return function(Mod, integer(i), b);
	}

	public static IExpr and(IExpr a, java.math.BigInteger i) {
		return function(And, a, integer(i));
	}

	public static IExpr and(java.math.BigInteger i, IExpr b) {
		return function(And, integer(i), b);
	}

	public static IExpr or(IExpr a, java.math.BigInteger i) {
		return function(Or, a, integer(i));
	}

	public static IExpr or(java.math.BigInteger i, IExpr b) {
		return function(Or, integer(i), b);
	}

}