package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.SingularValueDecompositionImpl;
import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.Convert;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * 
 * See <a
 * href="http://en.wikipedia.org/wiki/Singular_value_decomposition">Wikipedia:
 * Singular value decomposition</a>
 */
public class SingularValueDecomposition extends AbstractFunctionEvaluator {

  public SingularValueDecomposition() {
    super();
  }

  @Override
  public IExpr evaluate(final IAST function) {
    RealMatrix matrix;
    try {
      if (function.size() == 2) {
        final IAST list = (IAST) function.get(1);
        matrix = Convert.list2RealMatrix(list);
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
            matrix);
        final RealMatrix uMatrix = svd.getU();
        final RealMatrix sMatrix = svd.getS();
        final RealMatrix vMatrix = svd.getV();

        final IAST result = List();
        final IAST uMatrixAST = Convert.realMatrix2List(uMatrix);
        final IAST sMatrixAST = Convert.realMatrix2List(sMatrix);
        final IAST vMatrixAST = Convert.realMatrix2List(vMatrix);
        result.add(uMatrixAST);
        result.add(sMatrixAST);
        result.add(vMatrixAST);
        return result;
      }
    } catch (final ClassCastException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    } catch (final IndexOutOfBoundsException e) {
      if (Config.SHOW_STACKTRACE) {
        e.printStackTrace();
      }
    }

    return null;
  }

  @Override
  public IExpr numericEval(final IAST functionList) {
    return evaluate(functionList);
  }
 
}