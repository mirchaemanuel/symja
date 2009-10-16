package org.matheclipse.core.convert;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.reflection.system.Blank;
import org.matheclipse.core.reflection.system.Complex;
import org.matheclipse.core.reflection.system.Pattern;
import org.matheclipse.core.reflection.system.Rational;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.ast.FloatNode;
import org.matheclipse.parser.client.ast.FractionNode;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.ast.IntegerNode;
import org.matheclipse.parser.client.ast.PatternNode;
import org.matheclipse.parser.client.ast.StringNode;
import org.matheclipse.parser.client.ast.SymbolNode;

/**
 * Converts a parsed org.matheclipse.parser.client.ast.ASTNode expression into
 * an IExpr expression
 * 
 */
public class AST2Expr extends Converter<ASTNode, IExpr> {

  /**
   * Typical instance of an ASTNode to IExpr converter
   */
  public final static AST2Expr CONST = new AST2Expr(ASTNode.class, IExpr.class);

  public AST2Expr(final Class<ASTNode> sType, final Class<IExpr> tType) {
    super(sType, tType);
  }

  /**
   * Converts a parsed FunctionNode expression into an IAST expression
   */
  public IAST convert(IAST ast, FunctionNode functionNode)
      throws ConversionException {
    ast.set(0, convert((ASTNode) functionNode.get(0)));
    for (int i = 1; i < functionNode.size(); i++) {
      checkCanceled();
      ast.add(convert((ASTNode) functionNode.get(i)));
    }
    return ast;
  }

  /**
   * Converts a parsed ASTNode expression into an IExpr expression
   */
  @Override
  public IExpr convert(ASTNode node) throws ConversionException {
    if (node == null) {
      return null;
    }
    if (node instanceof FunctionNode) {
      final FunctionNode functionNode = (FunctionNode) node;
      final IAST ast = F.ast(convert((ASTNode) functionNode.get(0)));
      for (int i = 1; i < functionNode.size(); i++) {
        checkCanceled();
        ast.add(convert((ASTNode) functionNode.get(i)));
      }
      IExpr head = ast.head();
      if (head.equals(F.PatternHead)) {
        final IExpr expr = Pattern.CONST.evaluate(ast);
        if (expr != null) {
          return expr;
        }
      } else if (head.equals(F.BlankHead)) {
        final IExpr expr = Blank.CONST.evaluate(ast);
        if (expr != null) {
          return expr;
        }
      } else if (head.equals(F.ComplexHead)) {
        final IExpr expr = Complex.CONST.evaluate(ast);
        if (expr != null) {
          return expr;
        }
      } else if (head.equals(F.RationalHead)) {
        final IExpr expr = Rational.CONST.evaluate(ast);
        if (expr != null) {
          return expr;
        }
      }
      return ast;
    }
    if (node instanceof SymbolNode) {
      if (node.getString().equals("I")) {
        // special - convert on input
        return F.CI;
      }
      return F.symbol(node.getString());
    }
    if (node instanceof PatternNode) {
      final PatternNode pn = (PatternNode) node;
      return F.pattern((ISymbol) convert(pn.getSymbol()), convert(pn
          .getConstraint()));
    }
    if (node instanceof IntegerNode) {
      final IntegerNode integerNode = (IntegerNode) node;
      final String iStr = integerNode.getString();
      if (iStr != null) {
        return F.integer(iStr, integerNode.getNumberFormat());
      }
      return F.integer(integerNode.getIntValue());
    }
    if (node instanceof FractionNode) {
      FractionNode fr = (FractionNode) node;
      if (fr.isSign()) {
        return F.fraction((IInteger) convert(fr.getNumerator()),
            (IInteger) convert(fr.getDenominator())).negate();
      }
      return F.fraction(
          (IInteger) convert(((FractionNode) node).getNumerator()),
          (IInteger) convert(((FractionNode) node).getDenominator()));
    }
    if (node instanceof StringNode) {
      return F.stringx(node.getString());
    }
    if (node instanceof FloatNode) {
      return F.num(node.getString());
    }

    return F.symbol(node.toString());
  }
}
