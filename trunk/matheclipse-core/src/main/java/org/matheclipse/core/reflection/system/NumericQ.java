package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * Returns <code>True</code>, if the given expression is a numeric function or
 * value.
 * 
 */
public class NumericQ extends AbstractFunctionEvaluator {
  /**
   * Constructor for the unary predicate
   */
  public final static NumericQ CONST = new NumericQ();

  public NumericQ() {
    // System.out.println(getClass().getCanonicalName());
  }

  /**
   * Returns <code>True</code> if the 1st argument is an atomic object;
   * <code>False</code> otherwise
   */
  @Override
  public IExpr evaluate(final IAST functionList) {
    // TODO add implementation
    return null;
  }

  @Override
  public void setUp(ISymbol symbol) throws SyntaxError {
    symbol.setAttributes(ISymbol.HOLDALL);
  }

}
