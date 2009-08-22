package org.matheclipse.swing.plot;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Reacts to closing of the window.
 */
public class PlotWindowAdapter extends WindowAdapter {
	/**
	 * The plot to reclaim.
	 */
	protected final AbstractPlotter2D plot;

	private final boolean fSystemExitOnClosing;

	/**
	 * Creates a listener for the frame associated with the plot.
	 * 
	 * @param aPlot
	 */
	public PlotWindowAdapter(final AbstractPlotter2D aPlot) {
		this(aPlot, false);
	}

	/**
	 * Creates a listener for the frame associated with the plot.
	 * 
	 * @param aPlot
	 * @param systemExitOnClosing
	 *          call <code>System.exit(0)</code> when user closes the window
	 */
	public PlotWindowAdapter(final AbstractPlotter2D aPlot, boolean systemExitOnClosing) {
		fSystemExitOnClosing = systemExitOnClosing;
		plot = aPlot;
	}

	/**
	 * Reclaims the plot associated with the closed window.
	 */
	@Override
	public void windowClosing(final WindowEvent e) {
		plot.reclaim();
		if (fSystemExitOnClosing) {
			System.exit(0);
		}
	}
}