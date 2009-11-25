package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IRational;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Get the numerator part of an expression
 * 
 * See <a href="http://en.wikipedia.org/wiki/Fraction_(mathematics)">Wikipedia:
 * Fraction (mathematics)</a>
 * 
 * @see org.matheclipse.core.reflection.system.Denominator
 */
public class Numerator implements IFunctionEvaluator {

  public Numerator() {
  }

  public IExpr evaluate(final IAST functionList) {
    if (functionList.size() != 2) {
      throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
    }
    IExpr arg = functionList.get(1);
    if (arg instanceof IRational) {
      return ((IRational) arg).getNumerator();
    }
    if (arg.isAST(F.Times)) {
      final EvalEngine engine = EvalEngine.get();
      IAST f = (IAST) arg;

      if (f.size() <= 1) {
        return f;
      }

      IAST result = F.Times();
      IExpr temp;

      for (int i = 1; i < f.size(); i++) {
        temp = engine.evaluate(F.Numerator(f.get(i)));

        if (!temp.equals(F.C1)) {
          result.add(temp);
        }
      }

      if (result.size() > 1) {
        return result;
      }

      return F.C1;
    } else {
      if (arg.isAST(F.Power)) {
        IAST f = (IAST) arg;

        if (f.size() != 3) {
          return F.C1;
        }

        if (f.get(2) instanceof ISignedNumber) {
          if (((ISignedNumber) f.get(2)).isNegative()) {
            return F.C1;
          }
        }

        if (f.get(2) instanceof IAST) {
          IAST exp = (IAST) f.get(2);

          if (exp.size() > 1 && exp.isAST(F.Times)) {
            if (exp.get(1) instanceof ISignedNumber) {
              if (((ISignedNumber) exp.get(1)).isNegative()) {
                return F.C1;
              }
            }
          }
        }
        return f;
      }
    }

    return arg;
  }

  public IExpr numericEval(final IAST functionList) {
    return evaluate(functionList);
  }

  public void setUp(final ISymbol symbol) {
    symbol.setAttributes(ISymbol.LISTABLE);
  }

}
