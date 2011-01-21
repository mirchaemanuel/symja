/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *       at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jsyntaxpane.lexers;


import jsyntaxpane.Token;
import jsyntaxpane.TokenType;
 
%% 

%public
%class SymjaLexer
%extends DefaultJFlexLexer
%final
%unicode
%char
%type Token


%{
    /**
     * Create an empty lexer, yyrset will be called later to reset and assign
     * the reader
     */
    public SymjaLexer() {
        super();
    }

    @Override
    public int yychar() {
        return yychar;
    }

    private static final byte PARAN     = 1;
    private static final byte BRACKET   = 2;
    private static final byte CURLY     = 3;

%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]+

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} 

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
OctLongLiteral    = 0+ 1? {OctDigit} {1,21} [lL]
OctDigit          = [0-7]
    
/* floating point literals */        
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

%state STRING

%%

<YYINITIAL> {

  /* keywords */
  "True |"
  "False |"
  "Null |"
  "Abs" |
  "AddTo" |
  "And" |
  "Apart" |
  "Append" |
  "Apply" |
  "ArcCos" |
  "ArcSin" |
  "ArcTan" |
  "Arg" |
  "Array" |
  "AtomQ" |
  "Binomial" |
  "Blank" |
  "Block" |
  "Break" |
  "CartesianProduct" |
  "Cases" |
  "Catalan" |
  "CatalanNumber" |
  "Ceiling" |
  "CharacteristicPolynomial" |
  "ChessboardDistance" |
  "Chop" |
  "Clear" |
  "ClearAll" |
  "Complement" |
  "Complex" |
  "ComplexInfinity" |
  "ComposeList" |
  "CompoundExpression" |
  "Condition" |
  "Conjugate" |
  "ConstantArray" |
  "Continue" |
  "ContinuedFraction" |
  "CoprimeQ" |
  "Cos" |
  "Cosh" |
  "Cot" |
  "Cross" |
  "Csc" |
  "Curl" |
  "D" |
  "Decrement" |
  "Default" |
  "Definition" |
  "Degree" |
  "Denominator" |
  "Depth" |
  "Det" |
  "DiagonalMatrix" |
  "DigitQ" |
  "Dimensions" |
  "Divergence" |
  "DivideBy" |
  "Dot" |
  "Drop" |
  "E" |
  "Eigenvalues" |
  "Eigenvectors" |
  "Equal" |
  "Erf" |
  "EuclidianDistance" |
  "EulerGamma" |
  "EulerPhi" |
  "EvenQ" |
  "Exp" |
  "Expand" |
  "ExpandAll" |
  "ExtendedGCD" |
  "Extract" |
  "Factor" |
  "Factorial" |
  "Factorial2" |
  "FactorInteger" |
  "FactorSquareFree" |
  "FactorTerms" |
  "Fibonacci" |
  "FindRoot" |
  "First" |
  "Fit" |
  "FixedPoint" |
  "Floor" |
  "Fold" |
  "FoldList" |
  "For" |
  "FreeQ" |
  "FromCharacterCode" |
  "FromContinuedFraction" |
  "FullForm" |
  "Function" |
  "Gamma" |
  "GCD" |
  "Glaisher" |
  "GoldenRatio" |
  "Greater" |
  "GreaterEqual" |
  "GroebnerBasis" |
  "HarmonicNumber" |
  "Head" |
  "HilbertMatrix" |
  "Hold" |
  "Horner" |
  "I" |
  "IdentityMatrix" |
  "If" |
  "Im" |
  "Increment" |
  "Infinity" |
  "Inner" |
  "IntegerPartitions" |
  "IntegerQ" |
  "Integrate" |
  "Intersection" |
  "Inverse" |
  "JacobiMatrix" |
  "JacobiSymbol" |
  "JavaForm" |
  "Join" |
  "Khinchin" |
  "KOrderlessPartitions" |
  "KPartitions" |
  "Last" |
  "LCM" |
  "LeafCount" |
  "Length" |
  "Less" |
  "LessEqual" |
  "LetterQ" |
  "Level" |
  "Limit" |
  "LinearProgramming" |
  "LinearSolve" |
  "Log" |
  "LowerCaseQ" |
  "LUDecomposition" |
  "ManhattanDistance" |
  "Map" |
  "MapAll" |
  "MapThread" |
  "MatchQ" |
  "MatrixPower" |
  "MatrixQ" |
  "Max" |
  "Mean" |
  "Median" |
  "MemberQ" |
  "Min" |
  "Mod" |
  "Module" |
  "MoebiusMu" |
  "Most" |
  "Multinomial" |
  "N" |
  "Negative" |
  "Nest" |
  "NestList" |
  "NextPrime" |
  "NIntegrate" |
  "NonCommutativeMultiply" |
  "NonNegative" |
  "Norm" |
  "Not" |
  "NRoots" |
  "NumberQ" |
  "Numerator" |
  "NumericQ" |
  "OddQ" |
  "Or" |
  "Order" |
  "OrderedQ" |
  "Out" |
  "Outer" |
  "Package" |
  "ParametricPlot" |
  "Part" |
  "Partition" |
  "Pattern" |
  "Permutations" |
  "Pi" |
  "Plot" |
  "Plot3D" |
  "Plus" |
  "PolynomialExtendedGCD" |
  "PolynomialGCD" |
  "PolynomialLCM" |
  "PolynomialQ" |
  "PolynomialQuotient" |
  "PolynomialQuotientRemainder" |
  "PolynomialRemainder" |
  "Position" |
  "Positive" |
  "PossibleZeroQ" |
  "Power" |
  "PowerExpand" |
  "PowerMod" |
  "PreDecrement" |
  "PreIncrement" |
  "Prepend" |
  "PrimeQ" |
  "PrimitiveRoots" |
  "Print" |
  "Product" |
  "Quotient" |
  "RandomInteger" |
  "RandomReal" |
  "Range" |
  "Rational" |
  "Rationalize" |
  "Re" |
  "ReplaceAll" |
  "ReplaceRepeated" |
  "Rest" |
  "Return" |
  "Reverse" |
  "RootIntervals" |
  "Roots" |
  "RotateLeft" |
  "RotateRight" |
  "Rule" |
  "RuleDelayed" |
  "SameQ" |
  "Sec" |
  "Select" |
  "Set" |
  "SetAttributes" |
  "SetDelayed" |
  "Sign" |
  "SignCmp" |
  "Simplify" |
  "Sin" |
  "SingularValueDecomposition" |
  "Sinh" |
  "Solve" |
  "Sort" |
  "Sqrt" |
  "SquaredEuclidianDistance" |
  "SquareFreeQ" |
  "StringDrop" |
  "StringJoin" |
  "StringLength" |
  "StringTake" |
  "Subsets" |
  "SubtractFrom" |
  "Sum" |
  "SyntaxLength" |
  "SyntaxQ" |
  "Table" |
  "Take" |
  "Tan" |
  "Tanh" |
  "Taylor" |
  "Thread" |
  "Through" |
  "Times" |
  "TimesBy" |
  "Timing" |
  "ToCharacterCode" |
  "Together" |
  "ToString" |
  "Total" |
  "ToUnicode" |
  "Tr" |
  "Trace" |
  "Transpose" |
  "TrigReduce" |
  "TrigToExp" |
  "TrueQ" |
  "Trunc" |
  "Unequal" |
  "Union" |
  "UnsameQ" |
  "UpperCaseQ" |
  "ValueQ" |
  "VandermondeMatrix" |
  "Variables" |
  "VectorQ" |
  "While"                        { return token(TokenType.KEYWORD); }

  /* operators */

  "("                            { return token(TokenType.OPERATOR,  PARAN); }
  ")"                            { return token(TokenType.OPERATOR, -PARAN); }
  "{"                            { return token(TokenType.OPERATOR,  CURLY); }
  "}"                            { return token(TokenType.OPERATOR, -CURLY); }
  "["                            { return token(TokenType.OPERATOR,  BRACKET); }
  "]"                            { return token(TokenType.OPERATOR, -BRACKET); }
  ";"                            | 
  "'"                            |
  ","                            | 
  "."                            | 
  "="                            | 
  ">"                            | 
  "<"                            |
  "!"                            | 
  "~"                            | 
  "?"                            | 
  ":"                            | 
  "=="                           | 
  "<="                           | 
  ">="                           | 
  "!="                           | 
  "&&"                           | 
  "||"                           | 
  "++"                           | 
  "--"                           | 
  "+"                            | 
  "-"                            | 
  "*"                            | 
  "/"                            | 
  "%"                            | 
  "+="                           | 
  "-="                           | 
  "*="                           | 
  "/="                           | 
  "&="                           | 
  "|="                           | 
  "^="                           { return token(TokenType.OPERATOR); } 
  
  /* string literal */
  \"                             {  
                                    yybegin(STRING); 
                                    tokenStart = yychar; 
                                    tokenLength = 1; 
                                 }

  /* numeric literals */

  {DecIntegerLiteral}            |
  {DecLongLiteral}               |
  
  {HexIntegerLiteral}            |
  {HexLongLiteral}               |
 
  {OctIntegerLiteral}            |
  {OctLongLiteral}               |
  
  {FloatLiteral}                 |
  {DoubleLiteral}                |
  {DoubleLiteral}[dD]            { return token(TokenType.NUMBER); }

  /* comments */
  {Comment}                      { return token(TokenType.COMMENT); }

  /* whitespace */
  {WhiteSpace}                   { }

  /* identifiers */ 
  {Identifier}                   { return token(TokenType.IDENTIFIER); }
}


<STRING> {
  \"                             { 
                                     yybegin(YYINITIAL); 
                                     // length also includes the trailing quote
                                     return token(TokenType.STRING, tokenStart, tokenLength + 1);
                                 }
  
  {StringCharacter}+             { tokenLength += yylength(); }

  \\[0-3]?{OctDigit}?{OctDigit}  { tokenLength += yylength(); }
  
  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

