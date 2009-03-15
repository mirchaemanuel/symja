package org.matheclipse.symja;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MathApplet extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EvalPanel jContentPane = null;

	/**
	 * Initialize instance, load properties etc.
	 */
	public void init() {
	}

	/**
	 * Initialize instance, load properties etc.
	 */
	public void start() {
		setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new EvalPanel();
		}
		return jContentPane;
	}

	protected static JFrame frame = null;

	/**
	 * Launches CAS as an application. Creates a window (JFrame) and displays the
	 * Cas panel inside that window. Calls init() and start() on the CAS to mimic
	 * the applet initialization behavior.
	 */
	public static void main(String args[]) {
		frame = new JFrame("Symja");
		MathApplet panel = new MathApplet();

		frame.setContentPane(panel);
		panel.init();
		panel.start();

		readyFrame(frame);
	}

	/**
	 * Sizes a frame, makes it quit the application when it closes, and displays
	 * it.
	 */
	protected static void readyFrame(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.show();
	}

}
