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
import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.generic.UnaryNumerical;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
/**
 * Plots functions in 2D.
 */
public class Plotter extends AbstractPlotter2D {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1281925675150410124L;

	/**
	 * The number of lines to plot.
	 */
	protected int numFuncs;

	/**
	 * The points on the plotted lines.
	 */
	protected double point[][];

	/**
	 * The Y coordinates of the points in the component's graphics coordinate
	 * space.
	 */
	protected int paintPoint[];

	/**
	 * The X coordinates of the points in the component's graphics coordinate
	 * space.
	 */
	protected int xPoint[];

	/**
	 * The cached plotters.
	 */
 	protected static final List<Plotter> cache = new ArrayList<Plotter>();

	/**
	 * Readies a plot for display and displays it in a new window.
	 */
	@Override
	public void plot(final IAST args) {
		IAST funcs;
		ISymbol xVar;
		AST xArgs;

		if (args.size() < 3) {
			throw new IllegalArgumentException("At least two arguments needed.");
		}
		thisResolution = newResolution;
		final EvalEngine engine = EvalEngine.get();
		if (args.get(1).isList()) {
			funcs = (AST) args.get(1);
			numFuncs = funcs.size()-1;
		} else {
			funcs = args;
			numFuncs = 1;
		}
		point = new double[numFuncs][thisResolution + 1];
		paintPoint = new int[thisResolution + 1];
		xPoint = new int[thisResolution + 1];
		color = new Color[numFuncs];

		xArgs = (AST) args.get(2);
		xVar = (ISymbol) xArgs.get(1);//engine.evaluate(N(xArgs.getArg(0)));
		xText = xVar.toString();
		xMin = ((INum) engine.evaluate(N(xArgs.get(2)))).getRealPart();
		xMax = ((INum) engine.evaluate(N(xArgs.get(3)))).getRealPart();
		xRange = xMax - xMin;

		try {
			yMax = yMin = new UnaryNumerical(funcs.get(1), xVar, engine).value(xMin);
		} catch (final Exception e) {
			yMax = yMin = 0;
		}
		for (int func = 0; func < numFuncs; ++func) {
			checkCanceled();
			populate(funcs, func, xVar, engine);
		}

		for (int counter = 3; counter < args.size(); ++counter) {
			checkCanceled();
			final AST currentArgs = (AST) args.get(counter);

			if (getColor(currentArgs.get(1)) != null) {
				for (int i = 0; i < color.length; ++i) {
					checkCanceled();
					color[i] = getColor(currentArgs.get((i % (currentArgs.size()-1))+1));
				}
			} else {
				yText = currentArgs.get(1).toString();
				yMin = ((INum) engine.evaluate(N(currentArgs.get(2)))).getRealPart();
				yMax = ((INum) engine.evaluate(N(currentArgs.get(3)))).getRealPart();
			}
		}

		if (yMax <= yMin) {
			if (yMax < 0) {
				yMax = 0;
			}
			if (yMin > 0) {
				yMin = 0;
			}
			if (yMax <= yMin) {
				yMax = yMin = (yMax + yMin) / 2;
				++yMax;
				--yMin;
			}
		}

		yRange = yMax - yMin;

		setupText();

	}

	/**
	 * Populates the points array with function values.
	 */
	protected void populate(final IAST funcs, final int func, final ISymbol  xVar, final EvalEngine engine) {
		final UnaryNumerical un = new UnaryNumerical(funcs.get(func+1), xVar, engine);
		color[func] = COLOR[func % COLOR.length];
		for (int counter = 0; counter <= thisResolution; ++counter) {
			checkCanceled();
			try {
				populatePoint(func, un, counter);
			} catch (final Exception e) {
				point[func][counter] = Double.POSITIVE_INFINITY;
			}
		}
	}

	/**
	 * Inserts a function value into the appropriate point in the array.
	 */
	protected void populatePoint(final int func, final UnaryNumerical un, final int x) {
		point[func][x] = un.value(xMin + xRange * x / (thisResolution));
		if (point[func][x] < yMin) {
			yMin = point[func][x];
		} else if (point[func][x] > yMax) {
			yMax = point[func][x];
		}
	}

	/**
	 * Returns the color encoded in the HObject, or null if the HObject doesn't
	 * encode a color.
	 */
	protected Color getColor(final IExpr ho) {
		final String s = ho.toString().toLowerCase();

		if (s.startsWith("blue")) {
			return Color.BLUE;
		} else if (s.startsWith("cyan")) {
			return Color.CYAN;
		} else if (s.startsWith("green")) {
			return Color.GREEN;
		} else if (s.startsWith("magenta")) {
			return Color.MAGENTA;
		} else if (s.equals("orange")) {
			return Color.ORANGE;
		} else if (s.equals("pink")) {
			return Color.PINK;
		} else if (s.equals("red")) {
			return Color.RED;
		} else if (s.equals("yellow")) {
			return Color.YELLOW;
		} else {
			return null;
		}
	}

	/**
	 * Paints the functions.
	 */
	@Override
	protected void paintPlots(final Graphics2D g2d, final int top, final int height, final int bottom, final int left, final int width, final int right) {
		for (int func = 0; func < numFuncs; ++func) {
			checkCanceled();
			paintPlot(g2d, top, height, bottom, left, width, right, func);
		}
	}

	/**
	 * Paints a function.
	 */
	protected void paintPlot(final Graphics2D g2d, final int top, final int height, final int bottom, final int left, final int width, final int right, final int func) {
		int index = 0;

		g2d.setColor(color[func]);

		for (int counter = 0; counter <= thisResolution; ++counter) {
			checkCanceled();
			xPoint[index] = left + counter * width / thisResolution;
			paintPoint[index] = top + height - (int) ((point[func][counter] - yMin) * height / yRange);
			if ((paintPoint[index] >= top) && (paintPoint[index] <= bottom)) {
				++index;
			} else {
				--index;
				g2d.drawPolyline(xPoint, paintPoint, index);
				index = 0;
			}
		}
		g2d.drawPolyline(xPoint, paintPoint, index);
	}

	/**
	 * Returns a new instance of this class or a cached instance.
	 */
	public static Plotter getPlotter() {
		if (cache.isEmpty()) {
			return new Plotter();
		} else {
			final Plotter p = (Plotter) cache.get(0);
			cache.remove(p);
			return p;
		}
	}

	/**
	 * Enters this instance into the cache.
	 */
	@Override
	public void reclaim() {
		cache.add(this);
	}

	/**
	 * Empties the cache.
	 */
	public static void clearCache() {
		cache.clear();
	}
}
