package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IRational;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Get the denominator part of an expression
 * 
 * See <a href="http://en.wikipedia.org/wiki/Fraction_(mathematics)">Wikipedia:
 * Fraction (mathematics)</a>
 * 
 * @see org.matheclipse.core.reflection.system.Numerator
 */
public class Denominator implements IFunctionEvaluator {

  public Denominator() {
  }

  public IExpr evaluate(final IAST ast) {
    if (ast.size() != 2) {
      throw new WrongNumberOfArguments(ast, 1, ast.size() - 1);
    }
    IExpr expr = ast.get(1);
    if (expr instanceof IRational) {
      return ((IRational) expr).getDenominator();
    }
    IExpr[] parts = Apart.getFractionalParts(expr);
    return parts[1];
  }

  public IExpr numericEval(final IAST functionList) {
    return evaluate(functionList);
  }

  public void setUp(final ISymbol symbol) {
    symbol.setAttributes(ISymbol.LISTABLE);
  }

}
