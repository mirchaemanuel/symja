package org.matheclipse.core.reflection.system;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseAbstractUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.analysis.solvers.MullerSolver;
import org.apache.commons.math.analysis.solvers.NewtonSolver;
import org.apache.commons.math.analysis.solvers.RiddersSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.matheclipse.core.basic.Config;
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
 * href="http://en.wikipedia.org/wiki/Root-finding_algorithm">numerically
 * finding roots</a> of a univariate real function.
 * 
 * Uses the <a href="http://commons.apache.org/math/apidocs/org/apache/commons/math/analysis/solvers/UnivariateRealSolver.html"
 * >Commons math BisectionSolver, BrentSolver, MullerSolver, NewtonSolver,
 * RiddersSolver, SecantSolver</a> implementations.
 */
public class FindRoot extends AbstractFunctionEvaluator implements IConstantHeaders {

	public FindRoot() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if ((ast.size() == 3 || ast.size() == 4)) {
			String method = "Newton";
			if (ast.size() == 4 && ast.get(3) instanceof ISymbol) {
				method = ast.get(3).toString();
			}
			if ((ast.get(2).isList())) {
				IAST list = (IAST) ast.get(2);
				IExpr function = ast.get(1);
				if (list.size() == 4 && list.get(1) instanceof ISymbol && list.get(2) instanceof ISignedNumber
						&& list.get(3) instanceof ISignedNumber) {
					if (function.isAST(Equal, 3)) {
						function = F.Plus(((IAST) function).get(1), F.Times(F.CN1, ((IAST) function).get(2)));
					}
					try {
						return F.List(F.Rule(list.get(1), Num.valueOf(findRoot(method, list, function))));
					} catch (ConvergenceException e) {
						if (Config.SHOW_STACKTRACE) {
							e.printStackTrace();
						}
					} catch (MathException e) {
						if (Config.SHOW_STACKTRACE) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}

	private double findRoot(String method, IAST list, IExpr function) throws ConvergenceException {
		ISymbol xVar = (ISymbol) list.get(1);
		ISignedNumber min = (ISignedNumber) list.get(2);
		ISignedNumber max = (ISignedNumber) list.get(3);
		final EvalEngine engine = EvalEngine.get();
		function = F.eval(function);
		DifferentiableUnivariateRealFunction f = new UnaryNumerical(function, xVar, engine);
		BaseAbstractUnivariateRealSolver<UnivariateRealFunction> solver = null;
		if (method.equals("Bisection")) {
			solver = new BisectionSolver();
		} else if (method.equals("Brent")) {
			solver = new BrentSolver();
			// } else if (method.equals("Laguerre")) {
			// solver = new LaguerreSolver();
		} else if (method.equals("Muller")) {
			solver = new MullerSolver();
		} else if (method.equals("Ridders")) {
			solver = new RiddersSolver();
		} else if (method.equals("Secant")) {
			solver = new SecantSolver();
		} else {
			// default: NewtonSolver
			BaseAbstractUnivariateRealSolver<DifferentiableUnivariateRealFunction> solver2 = new NewtonSolver();
			return solver2.solve(Integer.MAX_VALUE, f, min.doubleValue(), max.doubleValue());
		}
		return solver.solve(Integer.MAX_VALUE, f, min.doubleValue(), max.doubleValue());

	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDFIRST);
	}
}