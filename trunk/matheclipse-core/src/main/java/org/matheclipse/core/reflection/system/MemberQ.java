package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;

/**
 */
public class MemberQ implements IFunctionEvaluator {

  public MemberQ() {
  }


  public IExpr evaluate(final IAST functionList) {
    if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST)) {
      final IAST ast = (IAST) functionList.get(1);
      final PatternMatcher matcher = new PatternMatcher(functionList.get(2));
      for (int i = 1; i < ast.size(); i++) {
        checkCanceled();
				if (matcher.apply(ast.get(i))) {
          return F.True;
        }
      }
    }
    return F.False;
  }

  public IExpr numericEval(final IAST functionList) {
    return evaluate(functionList);
  }

  public void setUp(final ISymbol symbol) {
  }

}
