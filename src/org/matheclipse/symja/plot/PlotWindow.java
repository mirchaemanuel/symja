package org.matheclipse.symja.plot;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class PlotWindow extends AbstractPlotWindow {
	public PlotWindow(Frame parent) {
		super(parent);
	}

	protected Plotter createPlot() {
		return Plotter.getPlotter();
	}

	public void addField() {
		addField("y(x) = ");
	}
}
