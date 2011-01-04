package org.matheclipse.core.reflection.system;


import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.swing.plot.ParametricPlotter;
import org.matheclipse.swing.plot.PlotFrame;

/**
 * Plots parametric shapes.
 */
public class ParametricPlot implements IFunctionEvaluator {

//	private final static int N = 100;

	public ParametricPlot() {
	}

	public IExpr evaluate(final IAST ast) {
		if (Config.SWING_PLOT_FRAME) {
			ParametricPlotter plotter = ParametricPlotter.getParametricPlotter();
			PlotFrame frame = new PlotFrame(plotter, ast);
			frame.invokeLater();
			return F.Null;
		}
		return F.Null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
