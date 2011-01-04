/*
CAS Computer Algebra System
Copyright (C) 2005  William Tracy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
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

package org.matheclipse.symja.plot;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.eval.DoubleVariable;

/**
 * Plots parametric shapes.
 */
public class ParametricPlotter extends AbstractPlotter2D {
	/**
	 * The number of points to plot on each line on this plot.
	 */
	protected int thisResolution;

	/**
	 * The number of lines to plot.
	 */
	protected int numFuncs;

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
	// protected HObject t;

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
	protected static List cache = new LinkedList();

	public void updatePlot() {
		thisResolution = newResolution;
		setupText();
		EventQueue.invokeLater(this);
	}

	public void setFunctions(java.util.List functions) {
		numFuncs = functions.size() / 2;

		xPoints = new double[numFuncs][thisResolution + 1];
		yPoints = new double[numFuncs][thisResolution + 1];
		color = new Color[numFuncs];
		DoubleEvaluator engine = new DoubleEvaluator();

		ListIterator i = functions.listIterator();
		while (i.hasNext()) {
			String sx = (String) i.next();
			String sy = (String) i.next();
			populateFunction(sx, sy, (i.previousIndex() - 1) / 2, engine);
		}

		updatePlot();
	}

	/**
	 * Populates the point arrays and readies plot for display.
	 */
	/*
	 * public void plot(HFunction args) { HFunction funcs; HFunction tArgs;
	 * 
	 * if (args.size() != 2) throw new IllegalArgumentException(
	 * "Incorrect number of arguments"); if (!(args.get(0).isList() &&
	 * args.get(1).isList())) throw new IllegalArgumentException(
	 * "Both arguments must be lists");
	 * 
	 * thisResolution = newResolution;
	 * 
	 * funcs = (HFunction)args.get(0); if (funcs.get(0).isList()) { functions =
	 * funcs.size(); } else { funcs = args; functions = 1; }
	 * 
	 * tArgs = (HFunction)args.get(1); if (tArgs.size() != 3) throw new
	 * IllegalArgumentException( "Variable name and bounds malformed"); t =
	 * tArgs.get(0); tMin = ((HDouble)C.EV(C.N.f(tArgs.get(1)))).doubleValue();
	 * tMax = ((HDouble)C.EV(C.N.f(tArgs.get(2)))).doubleValue(); tRange = tMax -
	 * tMin;
	 * 
	 * xPoints = new double[functions][thisResolution + 1]; yPoints = new
	 * double[functions][thisResolution + 1]; color = new Color[functions];
	 * 
	 * xMax = xMin = new HUnaryNumerical( ((HFunction)funcs.get(0)).get(0),
	 * t).map(tMin); yMax = yMin = new HUnaryNumerical(
	 * ((HFunction)funcs.get(0)).get(1), t).map(tMin);
	 * 
	 * for (int f = 0; f < functions; ++f) { doPlot(funcs, f); }
	 * 
	 * if (xMax <= xMin) { if (xMax < 0) { xMax = 0; } else if (xMin > 0) { xMin =
	 * 0; } else { ++xMax; --xMin; } }
	 * 
	 * if (yMax <= yMin) { if (yMax < 0) { yMax = 0; } else if (yMin > 0) { yMin =
	 * 0; } else { ++yMax; --yMin; } }
	 * 
	 * xRange = xMax - xMin; yRange = yMax - yMin;
	 * 
	 * setupText();
	 * 
	 * EventQueue.invokeLater(this); }
	 */

	protected void populateFunction(String xExpression, String yExpression, int func, DoubleEvaluator engine) {
		for (int counter = 0; counter <= thisResolution; ++counter) {
			try {
				plotPoint(func, xExpression, yExpression, counter, engine);
			} catch (Exception e) {
				xPoints[func][counter] = Double.POSITIVE_INFINITY;
				yPoints[func][counter] = Double.POSITIVE_INFINITY;
			}
		}
		colorPlot(func);
	}

	/**
	 * Plots shape number f, and sets its color.
	 */
	/*
	 * protected void doPlot(HFunction funcs, int f) { if
	 * (((HFunction)funcs.get(f)).size() < 2) throw new IllegalArgumentException(
	 * "Two functions required for plot #" + f);
	 * 
	 * x = new HUnaryNumerical(((HFunction)funcs.get(f)).get(0), t); y = new
	 * HUnaryNumerical(((HFunction)funcs.get(f)).get(1), t);
	 * 
	 * for(int counter = 0; counter <= thisResolution; ++counter) { plotPoint(f,
	 * counter); } colorPlot(funcs, f); }
	 */

	protected void colorPlot(int index) {
		color[index] = COLOR[index % COLOR.length];
	}

	protected void plotPoint(int func, String xExpression, String yExpression, int t, DoubleEvaluator engine) {
		// System.out.println("Plotting point #" + t + " of function #" + func);
		double tVal = tMin + (tRange * t) / (double) thisResolution;
		engine.defineVariable("t", new DoubleVariable(tVal));
		xPoints[func][t] = engine.evaluate(xExpression);
		yPoints[func][t] = engine.evaluate(yExpression);
	}

	/**
	 * Paints the plotted shapes on the display.
	 */
	protected void paintPlots(Graphics2D g2d, int top, int height, int bottom, int left, int width, int right) {
		int x[] = new int[thisResolution + 1];
		int y[] = new int[thisResolution + 1];

		for (int f = 0; f < numFuncs; ++f) {
			paintPlot(g2d, top, height, bottom, left, width, right, x, y, f);
		}
	}

	/**
	 * Paints a shape on the display.
	 */
	protected void paintPlot(Graphics2D g2d, int top, int height, int bottom, int left, int width, int right, int x[], int y[], int f) {
		g2d.setColor(color[f]);
		for (int counter = 0; counter <= thisResolution; ++counter) {
			convertPoint(top, height, bottom, left, width, right, x, y, f, counter);
		}
		g2d.drawPolyline(x, y, thisResolution + 1);
	}

	/**
	 * Positions a point on shape f at point n in the display coordinate system.
	 */
	protected void convertPoint(int top, int height, int bottom, int left, int width, int right, int x[], int y[], int f, int n) {
		x[n] = left + (int) ((xPoints[f][n] - xMin) * width / xRange);
		y[n] = top + height - (int) ((yPoints[f][n] - yMin) * height / yRange);
	}

	public void setTMax(double in) {
		tMax = in;
		tRange = tMax - tMin;
	}

	public void setTMin(double in) {
		tMin = in;
		tRange = tMax - tMin;
	}

	/**
	 * Returns either a new plot or a plot from a cache.
	 */
	public static ParametricPlotter getParametricPlotter() {
		if (cache.isEmpty()) {
			return new ParametricPlotter();
		} else {
			ParametricPlotter pp = (ParametricPlotter) cache.get(0);
			cache.remove(pp);
			return pp;
		}
	}

	/**
	 * Caches the unused plot.
	 */
	public void reclaim() {
		xPoints = null;
		yPoints = null; 
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
