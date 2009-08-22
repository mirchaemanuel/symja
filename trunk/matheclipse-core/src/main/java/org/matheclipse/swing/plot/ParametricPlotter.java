/*
CAS Computer Algebra System
Copyright (C) 2005  William Tracy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package org.matheclipse.swing.plot;

import static org.matheclipse.basic.Util.checkCanceled;
import static org.matheclipse.core.expression.F.N;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.generic.UnaryNumerical;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Plots parametric shapes.
 */
public class ParametricPlotter extends AbstractPlotter2D {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3712508834083106491L;

	/**
	 * The minimum bound on the independent variable.
	 */
	protected double tMin;

	/**
	 * The maximum bounds on the independent variable.
	 */
	protected double tMax;

	/**
	 * The range of the independent variable.
	 */
	protected double tRange;

	/**
	 * The independent variable.
	 */
	protected ISymbol t;

	/**
	 * The first dependent variable.
	 */
	protected UnaryNumerical x;

	/**
	 * The second dependent variable.
	 */
	protected UnaryNumerical y;

	/**
	 * The x coordinates of the points plotted. They are indexed first by plot
	 * number, then by the number of the point along the plot (i.e.
	 * xPoints[functionNumber][pointNumber]).
	 */
	protected double xPoints[][];

	/**
	 * The y coordinates of the points plotted.
	 */
	protected double yPoints[][];

	/**
	 * Contains the hidden plots for re-use.
	 */
	protected final static List cache = new LinkedList();

	/**
	 * The number of shapes plotted. Poorly chosen variable name. :-P
	 */
	protected int functions;

	/**
	 * Populates the point arrays and readies plot for display.
	 */
	@Override
	public void plot(final IAST args) {
		IAST funcs;
		AST tArgs;

		if (args.size() != 3) {
			throw new IllegalArgumentException("Incorrect number of arguments");
		}
		if (!(args.get(1).isList() && args.get(2).isList())) {
			throw new IllegalArgumentException("Both arguments must be lists");
		}

		thisResolution = newResolution;
		final EvalEngine engine = EvalEngine.get();
		funcs = (AST) args.get(1);
		if (funcs.get(1).isList()) {
			functions = funcs.size() - 1;
		} else {
			funcs = args;
			functions = 1;
		}

		tArgs = (AST) args.get(2);
		if (tArgs.size() != 4) {
			throw new IllegalArgumentException("Variable name and bounds malformed");
		}
		t = (ISymbol) tArgs.get(1);
		tMin = ((INum) engine.evaluate(N(tArgs.get(2)))).getRealPart();
		tMax = ((INum) engine.evaluate(N(tArgs.get(3)))).getRealPart();
		tRange = tMax - tMin;

		xPoints = new double[functions][thisResolution + 1];
		yPoints = new double[functions][thisResolution + 1];
		color = new Color[functions];

		xMax = xMin = new UnaryNumerical(((AST) funcs.get(1)).get(1), t, engine).value(tMin);
		yMax = yMin = new UnaryNumerical(((AST) funcs.get(1)).get(2), t, engine).value(tMin);

		for (int f = 0; f < functions; ++f) {
			checkCanceled();
			doPlot(funcs, f, engine);
		}

		if (xMax <= xMin) {
			if (xMax < 0) {
				xMax = 0;
			} else if (xMin > 0) {
				xMin = 0;
			} else {
				++xMax;
				--xMin;
			}
		}

		if (yMax <= yMin) {
			if (yMax < 0) {
				yMax = 0;
			} else if (yMin > 0) {
				yMin = 0;
			} else {
				++yMax;
				--yMin;
			}
		}

		xRange = xMax - xMin;
		yRange = yMax - yMin;

		setupText();

		// EventQueue.invokeLater(this);
	}

	/**
	 * Plots shape number f, and sets its color.
	 */
	protected void doPlot(final IAST funcs, final int f, final EvalEngine engine) {
		if (((AST) funcs.get(f + 1)).size() < 3) {
			throw new IllegalArgumentException("Two functions required for plot #" + f);
		}

		x = new UnaryNumerical(((AST) funcs.get(f + 1)).get(1), t, engine);
		y = new UnaryNumerical(((AST) funcs.get(f + 1)).get(2), t, engine);

		for (int counter = 0; counter <= thisResolution; ++counter) {
			checkCanceled();
			plotPoint(f, counter);
		}
		colorPlot(funcs, f);
	}

	/**
	 * Chooses the color for a plot.
	 */
	protected void colorPlot(final IAST funcs, final int f) {
		if (((AST) funcs.get(f + 1)).size() > 3) {
			final String s = ((AST) funcs.get(f + 1)).get(3).toString().toLowerCase();
			if (s.startsWith("b")) {
				color[f] = Color.BLUE;
			} else if (s.startsWith("c")) {
				color[f] = Color.CYAN;
			} else if (s.startsWith("g")) {
				color[f] = Color.GREEN;
			} else if (s.startsWith("m")) {
				color[f] = Color.MAGENTA;
			} else if (s.startsWith("o")) {
				color[f] = Color.ORANGE;
			} else if (s.startsWith("p")) {
				color[f] = Color.PINK;
			} else if (s.startsWith("r")) {
				color[f] = Color.RED;
			} else {
				color[f] = Color.YELLOW;
			}
		} else {
			color[f] = COLOR[f % COLOR.length];
		}
	}

	/**
	 * Plots point number n on shape f.
	 */
	protected void plotPoint(final int f, final int n) {
		xPoints[f][n] = x.value(tMin + tRange * n / (thisResolution));
		if (xPoints[f][n] < xMin) {
			xMin = xPoints[f][n];
		} else if (xPoints[f][n] > xMax) {
			xMax = xPoints[f][n];
		}

		yPoints[f][n] = y.value(tMin + tRange * n / (thisResolution));
		if (yPoints[f][n] < yMin) {
			yMin = yPoints[f][n];
		} else if (yPoints[f][n] > yMax) {
			yMax = yPoints[f][n];
		}
	}

	/**
	 * Paints the plotted shapes on the display.
	 */
	@Override
	protected void paintPlots(final Graphics2D g2d, final int top, final int height, final int bottom, final int left,
			final int width, final int right) {
		final int x[] = new int[thisResolution + 1];
		final int y[] = new int[thisResolution + 1];

		for (int f = 0; f < functions; ++f) {
			checkCanceled();
			paintPlot(g2d, top, height, bottom, left, width, right, x, y, f);
		}
	}

	/**
	 * Paints a shape on the display.
	 */
	protected void paintPlot(final Graphics2D g2d, final int top, final int height, final int bottom, final int left,
			final int width, final int right, final int x[], final int y[], final int f) {
		g2d.setColor(color[f]);
		for (int counter = 0; counter <= thisResolution; ++counter) {
			checkCanceled();
			convertPoint(top, height, bottom, left, width, right, x, y, f, counter);
		}
		g2d.drawPolyline(x, y, thisResolution + 1);
	}

	/**
	 * Positions a point on shape f at point n in the display coordinate system.
	 */
	protected void convertPoint(final int top, final int height, final int bottom, final int left, final int width, final int right,
			final int x[], final int y[], final int f, final int n) {
		x[n] = left + (int) ((xPoints[f][n] - xMin) * width / xRange);
		y[n] = top + height - (int) ((yPoints[f][n] - yMin) * height / yRange);
	}

	/**
	 * Returns either a new plot or a plot from a cache.
	 */
	public static ParametricPlotter getParametricPlotter() {
		if (cache.isEmpty()) {
			return new ParametricPlotter();
		} else {
			final ParametricPlotter pp = (ParametricPlotter) cache.get(0);
			cache.remove(pp);
			return pp;
		}
	}

	/**
	 * Caches the unused plot.
	 */
	@Override
	public void reclaim() {
		xPoints = null;
		yPoints = null;
		x = null;
		y = null;
		cache.add(this);
	}

	/**
	 * Empties the cache. Called when the applet is stopped, which hoses the Swing
	 * components.
	 */
	public static void clearCache() {
		cache.clear();
	}
}
