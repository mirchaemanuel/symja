package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.generic.UnaryFunctorImpl;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

/**
 */
public class MapThread extends AbstractFunctionEvaluator {

  class UnaryMapThread extends UnaryFunctorImpl<IExpr> {
    final IExpr fConstant;

    public UnaryMapThread(final IExpr constant) {
      fConstant = constant;
    }

    public IExpr apply(final IExpr firstArg) {
      if (firstArg instanceof IAST) {
        IExpr result = Thread.threadList((IAST) firstArg, F.List, fConstant, 1);
        if (result == null) {
          return firstArg;
        }
        return result;
      }
      return firstArg;
    }

  }

  public MapThread() {
  }

  @Override
  public IExpr evaluate(final IAST functionList) {
    if ((functionList.size() != 3) && (functionList.size() != 4)) {
      return null;
    }
    LevelSpec level = null;
    if (functionList.size() == 4) {
      level = new LevelSpecification(functionList.get(3));
    } else {
      level = new LevelSpec(0);
    }
//    final IAST ast = F.ast(functionList.get(1));
    final IExpr result = (IExpr) AST.COPY.map(functionList.get(2),
        new UnaryMapThread(functionList.get(1)), level, 1);
    return result == null ? functionList.get(2) : result;
  }

}
