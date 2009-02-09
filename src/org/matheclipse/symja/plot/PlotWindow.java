package org.matheclipse.symja.plot;

import java.awt.Frame;

public class PlotWindow extends AbstractPlotWindow {
	public PlotWindow(Frame parent) {
		super(parent);
	}

	public Plotter createPlot() {
		return Plotter.getPlotter();
	}

	public void addField() {
		addField("y(x) = ");
	}
}
