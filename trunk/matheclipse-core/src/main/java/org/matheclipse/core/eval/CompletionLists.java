package org.matheclipse.core.eval;

import java.util.List;

public class CompletionLists {
	private final static String[] COMPLETIONS = { "True", "False", "Abs[]", "AddTo[]", "And[]", "Append[]", "Apply[]", "ArcCos[]",
			"ArcSin[]", "ArcTan[]", "Arg[]", "Array[]", "AtomQ[]", "Binomial[]", "Blank[]", "Block[]", "Break[]", "Cases[]", "Catalan",
			"CatalanNumber[]", "Ceiling[]", "Clear[]", "ClearAll[]", "Complement[]", "Complex[]", "ComplexInfinity", "ComposeList[]",
			"CompoundExpression[]", "Condition[]", "Conjugate[]", "Continue[]", "Cos[]", "Cosh[]", "Cross[]", "Curl[]", "D[]",
			"Decrement[]", "Definition[]", "Degree", "Denominator[]", "Depth[]", "Det[]", "DiagonalMatrix[]", "DigitQ[]", "Dimensions[]",
			"Divergence[]", "DivideBy[]", "Dot[]", "Drop[]", "E", "Equal[]", "Erf[]", "EulerGamma", "EulerPhi[]", "EvenQ[]", "Exp[]",
			"Expand[]", "ExpandAll[]", "ExtendedGCD[]", "Extract[]", "Factor[]", "Factorial[]", "Factorial2[]", "FactorInteger[]",
			"Fibonacci[]", "FindRoot[]", "First[]", "FixedPoint[]", "Floor[]", "Fold[]", "FoldList[]", "For[]", "FreeQ[]",
			"FromCharacterCode[]", "FromContinuedFraction[]", "FullForm[]", "Function[]", "GCD[]", "Glaisher", "GoldenRatio",
			"Greater[]", "GreaterEqual[]", "Head[]", "Hold[]", "Horner[]", "I", "IdentityMatrix[]", "If[]", "Im[]", "Increment[]",
			"Infinity", "Inner[]", "IntegerQ[]", "Integrate[]", "Intersection[]", "Inverse[]", "JacobiMatrix[]", "JacobiSymbol[]",
			"Join[]", "Khinchin", "KOrderlessPartitions[]", "KPartitions[]", "KSubsets[]", "Last[]", "LeafCount$LeafCountVisitor[]",
			"LeafCount[]", "Length[]", "Less[]", "LessEqual[]", "LetterQ[]", "Level[]", "Log[]", "LowerCaseQ[]", "LUDecomposition[]",
			"Map[]", "MapAll[]", "MatrixPower[]", "MatrixQ[]", "Max[]", "Mean[]", "Median[]", "MemberQ[]", "Min[]", "Mod[]",
			"MoebiusMu[]", "Most[]", "Multinomial[]", "N[]", "Negative[]", "Nest[]", "NestList[]", "NextPrime[]",
			"NonCommutativeMultiply[]", "NonNegative[]", "Norm[]", "Not[]", "NumberPartitions[]", "NumberQ[]", "Numerator[]", "OddQ[]",
			"Or[]", "Order[]", "OrderedQ[]", "Out[]", "Outer[]", "Package[]", "ParametricPlot[]", "Part[]", "Partition[]", "Pattern[]",
			"Permutations[]", "Pi", "Plot[]", "Plot3D[]", "Plus[]", "PolynomialQ[]", "Position[]", "Positive[]", "Power[]", "PowerMod[]",
			"PreDecrement[]", "PreIncrement[]", "Prepend[]", "PrimeQ[]", "PrimitiveRoots[]", "Print[]", "Product[]", "Quotient[]",
			"Range[]", "Rational[]", "Re[]", "ReplaceAll[]", "Rest[]", "Return[]", "Reverse[]", "RotateLeft[]", "RotateRight[]",
			"Rule[]", "SameQ[]", "Select[]", "Set[]", "SetAttributes[]", "SetDelayed[]", "Sign[]", "SignCmp[]", "Sin[]", "Sinh[]",
			"Sort[]", "Sqrt[]", "StringDrop[]", "StringJoin[]", "StringLength[]", "StringTake[]", "SubtractFrom[]", "Sum[]",
			"SyntaxLength[]", "SyntaxQ[]", "Table[]", "Take[]", "Tan[]", "Tanh[]", "Taylor[]", "Through[]", "Times[]", "TimesBy[]",
			"Timing[]", "ToCharacterCode[]", "ToString[]", "ToUnicode[]", "Tr[]", "Trace[]", "Transpose[]", "TrueQ[]", "Trunc[]",
			"Unequal[]", "Union[]", "UnsameQ[]", "UpperCaseQ[]", "VandermondeMatrix[]", "Variables[]", "VectorQ[]", "While[]" };
	List<String> fReplaceWords;
	List<String> fWords;

	public CompletionLists(List<String> words, List<String> replaceWords) {
		fWords = words;
		fReplaceWords = replaceWords;
		for (int i = 0; i < COMPLETIONS.length; i++) {
			fWords.add(COMPLETIONS[i].toLowerCase());
			fReplaceWords.add(COMPLETIONS[i]);
		}
	}

}
