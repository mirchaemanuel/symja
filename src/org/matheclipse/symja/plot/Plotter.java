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

/*import com.hartmath.expression.*;
import com.hartmath.mapping.*;
import com.hartmath.lib.*;*/

import java.awt.Graphics2D;
import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.*;

import java.util.*;

/** Plots functions in 2D.
  */
public class Plotter extends AbstractPlotter2D {
    /** The number of points to plot on each line on this plot.
      */
    protected int thisResolution;

    /** The number of lines to plot.
      */
    protected int numFuncs;

    /** The points on the plotted lines.
      */
    protected double point[][];

    /** The Y coordinates of the points in the component's graphics
      * coordinate space.
      */
    protected int paintPoint[];

    /** The X coordinates of the points in the component's graphics
      * coordinate space.
      */
    protected int xPoint[];

    /** The cached plotters.
      */
    protected static List cache = new ArrayList();

    /** Readies a plot for display and displays it in a new window.
      */
    public void plot(/*HFunction args*/) {
        /*HFunction funcs;
        HObject xVar;
        HFunction xArgs;

        if (args.size() < 2)
            throw new IllegalArgumentException(
                                   "At least two arguments needed.");*/
        thisResolution = newResolution;

        /*if (args.get(0).isList()) {
            funcs = (HFunction)args.get(0);
            numFuncs = funcs.size();
        } else {
            funcs = args;
            numFuncs = 1;
        }
        point = new double[numFuncs][thisResolution + 1];
        paintPoint = new int[thisResolution + 1];
        xPoint = new int[thisResolution + 1];
        color = new Color[numFuncs];

        xArgs = (HFunction)args.get(1);
        xVar = C.EV(C.N.f(xArgs.get(0)));
        xText = xVar.toString();
        xMin = ((HDouble)C.EV(C.N.f(xArgs.get(1)))).doubleValue();
        xMax = ((HDouble)C.EV(C.N.f(xArgs.get(2)))).doubleValue();
        xRange = xMax - xMin;

        try {
            yMax = yMin = new HUnaryNumerical(funcs.get(0),
                                              xVar).map(xMin);
        } catch (Exception e) {
            yMax = yMin = 0;
        }
        for (int func = 0; func < numFuncs; ++func) {
            populate(funcs, func, xVar);
        }

        for (int counter = 2; counter < args.size(); ++counter) {
            HFunction currentArgs = (HFunction)args.get(counter);

            if (getColor(currentArgs.get(0)) != null) {
                for (int i = 0; i < color.length; ++i)
                    color[i] = getColor(currentArgs.get(i
                                              % currentArgs.size()));
            } else {
                yText = currentArgs.get(0).toString();
                yMin = ((HDouble)C.EV(C.N.f(currentArgs.get(1))))
                                                      .doubleValue();
                yMax = ((HDouble)C.EV(C.N.f(currentArgs.get(2))))
                                                      .doubleValue();
            }
        }*/

        if (yMax <= yMin) {
            if (yMax < 0)
                yMax = 0;
            if (yMin > 0)
                yMin = 0;
            if (yMax <= yMin) {
                yMax = yMin = (yMax + yMin) /2;
                ++yMax;
                --yMin;
            }
        }

        yRange = yMax - yMin;

        setupText();

        EventQueue.invokeLater(this);
    }

    /** Populates the points array with function values.
      */
    /*protected void populate(HFunction funcs,
                            int func,
                            HObject xVar) {
        HUnaryNumerical un = new HUnaryNumerical(funcs.get(func),
                                                 xVar);
        color[func] = COLOR[func % COLOR.length];
        for (int counter = 0; counter <= thisResolution; ++counter) {
            try {
                populatePoint(func, un, counter);
            } catch (Exception e) {
                point[func][counter] = Double.POSITIVE_INFINITY;
            }
        }
    }*/

    /** Inserts a function value into the appropriate point in the
      * array.
      */
    /*protected void populatePoint(int func,
                                 HUnaryNumerical un,
                                 int x) {
        point[func][x] = un.map(xMin
            + xRange * (double)x / (double)(thisResolution));
        if (point[func][x] < yMin)
            yMin = point[func][x];
        else if (point[func][x] > yMax)
            yMax = point[func][x];
    }*/

    /** Returns the color encoded in the HObject, or null if the
      * HObject doesn't encode a color.
      */
    /*protected Color getColor(HObject ho) {
        String s = ho.toString().toLowerCase();

        if (s.startsWith("blue"))
            return Color.BLUE;
        else if (s.startsWith("cyan"))
            return Color.CYAN;
        else if (s.startsWith("green"))
            return Color.GREEN;
        else if (s.startsWith("magenta"))
            return Color.MAGENTA;
        else if (s.equals("orange"))
            return Color.ORANGE;
        else if (s.equals("pink"))
            return Color.PINK;
        else if (s.equals("red"))
            return Color.RED;
        else if (s.equals("yellow"))
            return Color.YELLOW;
        else
            return null;
    }*/

    /** Paints the functions.
      */
    protected void paintPlots(Graphics2D g2d,
                              int top,
                              int height,
                              int bottom,
                              int left,
                              int width,
                              int right) {
        for (int func = 0; func < numFuncs; ++func) {
            paintPlot(g2d,
                      top,
                      height,
                      bottom,
                      left,
                      width,
                      right,
                      func);
        }
    }

    /** Paints a function.
      */
    protected void paintPlot(Graphics2D g2d,
                             int top,
                             int height,
                             int bottom,
                             int left,
                             int width,
                             int right,
                             int func) {
        int x[] = new int[thisResolution + 1];
        int y[] = new int[thisResolution + 1];
        int index = 0;

        g2d.setColor(color[func]);

        for (int counter = 0; counter <= thisResolution; ++counter) {
            xPoint[index] = left
                     + counter * width / thisResolution;
            paintPoint[index] = top + height
                - (int)((point[func][counter] - yMin)
                                     * height / yRange);
            if (paintPoint[index] >= top
                                    && paintPoint[index] <= bottom) {
                ++index;
            } else {
                --index;
                g2d.drawPolyline(xPoint, paintPoint, index);
                index = 0;
            }
        }
        g2d.drawPolyline(xPoint, paintPoint, index);
    }

    /** Returns a new instance of this class or a cached instance.
      */
    public static Plotter getPlotter() {
        if (cache.isEmpty()) {
            return new Plotter();
        } else {
            Plotter p = (Plotter)cache.get(0);
            cache.remove(p);
            return p;
        }
    }

    /** Enters this instance into the cache.
      */
    public void reclaim() {
        cache.add(this);
    }

    /** Empties the cache.
      */
    public static void clearCache() {
        cache.clear();
    }
}
