package org.matheclipse.symja;

import static org.matheclipse.core.expression.F.N;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.eval.IDoubleCallbackFunction;

public class CoreCallbackFunction implements IDoubleCallbackFunction {
  public final static CoreCallbackFunction CONST = new CoreCallbackFunction();

  public double evaluate(DoubleEvaluator doubleEngine,
      FunctionNode functionNode, double[] args) {
    String symbolName = functionNode.getNode(0).toString();
    IAST fun = F.function(symbolName);
    for (int i = 0; i < args.length; i++) {
      fun.add(F.num(args[i]));
    }
    final IExpr result = F.evaln(fun);
    if (result instanceof INum) {
      return ((INum) result).getRealPart();
    } else {
      throw new ArithmeticException(
          "CoreCallbackFunction#evaluate() not possible for: "
              + functionNode.toString());
    }
  }

}
