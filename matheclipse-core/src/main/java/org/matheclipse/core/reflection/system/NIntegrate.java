package org.matheclipse.core.reflection.system;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.integration.LegendreGaussIntegrator;
import org.apache.commons.math.analysis.integration.RombergIntegrator;
import org.apache.commons.math.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl;
import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.generic.UnaryNumerical;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Function for <a
 * href="http://en.wikipedia.org/wiki/Numerical_integration">numerical
 * integration</a> of univariate real functions.
 * 
 * Uses the <a href="http://commons.apache.org/math/apidocs/org/apache/commons/math/analysis/integration/UnivariateRealIntegratorImpl.html"
 * >Commons math LegendreGaussIntegrator, RombergIntegrator, SimpsonIntegrator,
 * TrapezoidIntegrator</a> implementations.
 */
public class NIntegrate extends AbstractFunctionEvaluator implements
    IConstantHeaders {

  public NIntegrate() {
  }

  @Override
  public IExpr evaluate(final IAST lst) {
    if ((lst.size() == 3 || lst.size() == 4)) {
      String method = "Trapezoid";
      if (lst.size() == 4 && lst.get(3) instanceof ISymbol) {
        method = lst.get(3).toString();
      }
      if ((lst.get(2).isList())) {
        IAST list = (IAST) lst.get(2);
        IExpr function = lst.get(1);
        if (list.size() == 4 && list.get(1) instanceof ISymbol
            && list.get(2) instanceof ISignedNumber
            && list.get(3) instanceof ISignedNumber) {
          if (function.isAST(Equal, 3)) {
            function = F.Plus(((IAST) function).get(1), F.Times(F.CN1,
                ((IAST) function).get(2)));
          }
          try {
            return Num.valueOf(integrate(method, list, function));
          } catch (ConvergenceException e) {
            if (Config.SHOW_STACKTRACE) {
              e.printStackTrace();
            }
          } catch (FunctionEvaluationException e) {
            if (Config.SHOW_STACKTRACE) {
              e.printStackTrace();
            }
          }
        }
      }
    }
    return null;
  }

  private double integrate(String method, IAST list, IExpr function)
      throws ConvergenceException, FunctionEvaluationException {
    ISymbol xVar = (ISymbol) list.get(1);
    ISignedNumber min = (ISignedNumber) list.get(2);
    ISignedNumber max = (ISignedNumber) list.get(3);
    final EvalEngine engine = EvalEngine.get();
    function = F.eval(function);
    DifferentiableUnivariateRealFunction f = new UnaryNumerical(function, xVar,
        engine);
    UnivariateRealIntegratorImpl integrator = new TrapezoidIntegrator();
    if (method.equals("Simpson")) {
      integrator = new SimpsonIntegrator();
    } else if (method.equals("LegendreGauss")) {
      integrator = new LegendreGaussIntegrator(3, 64);
    } else if (method.equals("Romberg")) {
      integrator = new RombergIntegrator();
    } else {
      // default: TrapezoidIntegrator
    }
    return integrator.integrate(f, min.doubleValue(), max.doubleValue());

  }

  @Override
  public void setUp(final ISymbol symbol) {
    symbol.setAttributes(ISymbol.HOLDFIRST);
  }
}