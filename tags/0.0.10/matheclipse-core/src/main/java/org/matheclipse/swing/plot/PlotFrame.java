package org.matheclipse.swing.plot;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.matheclipse.core.interfaces.IAST;

/**
 * An extended <code>JFrame</code> to show a plot window
 * 
 */
public class PlotFrame extends JFrame implements Runnable {
	private final IAST fAST;

	private final AbstractPlotter2D fPlotter;

	public PlotFrame(AbstractPlotter2D plotter, IAST ast) {
		super();
		fPlotter = plotter;
		fAST = ast;
		setContentPane(plotter);
		setSize(640, 400);
		setLocationRelativeTo(null);
		addWindowListener(new PlotWindowAdapter(plotter));
	}

	/**
	 * Displays the frame (on the AWT thread to avoid deadlock; i.e.
	 * <code>EventQueue.invokeLater(this)</code>.
	 */
	public void invokeLater() {
		EventQueue.invokeLater(this);
	}

	public void run() {
		fPlotter.plot(fAST);
		setVisible(true);
	}
}
