package org.matheclipse.core.generic;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Retun the evaluation of an unary AST object
 * 
 */
public class UnaryEval extends UnaryFunctorImpl<IExpr> {
  protected final EvalEngine fEngine;

  protected final IAST fAST;

  /**
   * Define an unary AST with the header <code>head</code>.
   * 
   * @param head
   *          the AST's head expresion
   */
  public UnaryEval(final IExpr head) {
    fEngine = EvalEngine.get();
    fAST = F.ast(head, 1, false);
  }

  /**
   * Return the evaluation of an unary AST object by settings it's frist
   * argument to <code>firstArg</code>
   * 
   */
  public IExpr apply(final IExpr firstArg) {
    final IAST ast = (IAST) fAST.clone();
    ast.add(firstArg);
    return fEngine.evaluate(ast);
  }

}
