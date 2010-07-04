package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.N;
import static org.matheclipse.core.expression.F.Rule;
import static org.matheclipse.core.expression.F.Show;
import static org.matheclipse.core.expression.F.SurfaceGraphics;

import javax.swing.JOptionPane;

import org.matheclipse.basic.Alloc;
import org.matheclipse.core.convert.Object2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.eval.util.Options;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.BinaryNumerical;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;
 
/**
 * Represents the Plot3D function
 * 
 * @see org.matheclipse.core.eval.util.Options
 */
public class Plot3D implements IFunctionEvaluator {
	/**
	 * Runs if Java3D is not installed.
	 */
	static class NoJ3DRunnable implements Runnable {
		public void run() {
			JOptionPane
					.showMessageDialog(
							null,
							"MatheEclipse requires the Java3D extension to be installed to plot 3D graphs.\nWindows users can download Java3D from java.com.\nMacintosh users can download Java3D from Apple's web site.\nLinux/x86 users can download Java3D from Blackdown.org",
							"MathEclipse: Java3D not installed", JOptionPane.ERROR_MESSAGE);
		}
	}

	private final static int NUMBER_OF_DIVISIONS = 21;

	public Plot3D() {
	}

	public IExpr evaluate(final IAST ast) {
		// if (Config.SWING_PLOT_FRAME) {
		// try {
		// Plotter3D.getPlotter(Plotter3DFactory.getTransparent()).plot(ast);
		// return AbstractExpressionFactory.Null;
		// } catch (final NoClassDefFoundError e) {
		// EventQueue.invokeLater(new NoJ3DRunnable());
		// }
		// }
		final EvalEngine engine = EvalEngine.get();
		// ISymbol optionsArray[] = new ISymbol[] { f.BoxRatios, f.PlotRange };
		if ((ast.size() >= 4) && ast.get(2).isList() && ast.get(3).isList()) {
			final IAST graphics = SurfaceGraphics();
			IExpr temp;
			final IAST lst1 = (IAST) ast.get(2); // x-Range
			final IAST lst2 = (IAST) ast.get(3); // y-Range
			if ((lst1.size() == 4) && (lst2.size() == 4)) {
				final Options hOptions = new Options(F.Plot3D, ast, 4);
				// IAST allOptions = List();
				// for (int i = 0; i < optionsArray.length; i++) {
				// allOptions.add(optionsArray[i]);
				// }
				// allOptions = hOptions.replaceAll(allOptions);

				final IExpr a = engine.evaluate(N(lst1.get(2)));
				final IExpr b = engine.evaluate(N(lst1.get(3)));
				final IExpr c = engine.evaluate(N(lst2.get(2)));
				final IExpr d = engine.evaluate(N(lst2.get(3)));
				if ((!(a instanceof INum)) || (!(b instanceof INum)) || (!(c instanceof INum)) || (!(d instanceof INum))) {
					return null;
				}
				final double ad = ((INum) a).getRealPart();
				final double bd = ((INum) b).getRealPart();
				final double cd = ((INum) c).getRealPart();
				final double dd = ((INum) d).getRealPart();
				if (bd <= ad) {
					return null;
				}
				if (dd <= cd) {
					return null;
				}
				// double y0d = -10.0f;
				// double y1d = 10.0f;
				// double params[] = {ad, bd, cd, dd, -10.0, 10.0};

				temp = plotArray(ad, bd, cd, dd, ast.get(1), (ISymbol) lst1.get(1), (ISymbol) lst2.get(1), engine);
				if (temp != null) {
					graphics.add(temp);
				}

				final IAST options = List();
				// for (int i = 0; i < optionsArray.length; i++) {
				// options.add(Rule(optionsArray[i], allOptions.get(i)));
				// }
				options.add(Rule(F.PlotRange, F.Automatic));
				options.add(Rule(F.MeshRange, List(List(a, b), List(c, d))));

				graphics.add(options);
				return Show(graphics);
			}
		}
		return F.Null;
	}

	public IExpr plotArray(final double ad, final double bd, final double cd, final double dd, final IExpr function,
			final ISymbol xVar, final ISymbol yVar, final EvalEngine session) {
		final double xStep = (bd - ad) / NUMBER_OF_DIVISIONS;
		final double yStep = (dd - cd) / NUMBER_OF_DIVISIONS;

		final BinaryNumerical hbn = new BinaryNumerical(function, xVar, yVar);

		Alloc alloc = Alloc.get();
		final double data[][] = alloc.matrixCheck(NUMBER_OF_DIVISIONS + 1, NUMBER_OF_DIVISIONS + 1);
		double x = ad;
		double y;
		double z;
		// double zmin = Double.MAX_VALUE;
		// double zmax = Double.MAX_VALUE;

		for (int i = 0; i < NUMBER_OF_DIVISIONS; i++) {
			checkCanceled();
			y = cd;
			for (int j = 0; j < NUMBER_OF_DIVISIONS; j++) {
				checkCanceled();
				try {
					z = hbn.value(x, y);
					// if (Double.isNaN(z)) {
					// TODO
					// }
				} catch (final Throwable se) {
					z = Double.NaN;
				}

				data[i][j] = z;

				y += yStep;

			}
			x += xStep;
		}
		// return Convert.objectToExpr(data);
		return Object2Expr.CONST.convert(data);
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
