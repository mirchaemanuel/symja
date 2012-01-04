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

package org.matheclipse.symja;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;

import javax.jnlp.PrintService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.matheclipse.symja.plot.ParametricPlotWindow;
import org.matheclipse.symja.plot.Plot3DWindow;
import org.matheclipse.symja.plot.PlotWindow;

/**
 * Symja Swing GUI. Extends JApplet so that it can functions as an applet; if
 * main() is called, the applet is wrapped in a window (JFrame).
 */
public class Main extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static JFrame frame = null;
	protected String fDefaultSystemRulesFilename = null;
	protected File fFile = null;

	private void setArgs(final String args[]) {
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];

			// if (arg.equals("-help") || arg.equals("-h")) {
			// printUsage();
			// return;
			// // } else if (arg.equals("-debug")) {
			// // Config.DEBUG = true;
			// } else
			if (arg.equals("-file") || arg.equals("-f")) {
				try {
					fFile = new File(args[i + 1]);
					i++;
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					final String msg = "You must specify a file when " + "using the -file argument";
					System.out.println(msg);
					return;
				}
			} else if (arg.equals("-default") || arg.equals("-d")) {
				try {
					fDefaultSystemRulesFilename = args[i + 1];
					i++;
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					final String msg = "You must specify a file when " + "using the -file argument";
					System.out.println(msg);
					return;
				}
			}

		}

	}

	/**
	 * Launches CAS as an application. Creates a window (JFrame) and displays the
	 * Cas panel inside that window. Calls init() and start() on the Cas to mimic
	 * the applet initialization behavior.
	 */
	public static void main(final String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createFrame(args);
			}
		});
	}

	private static void createFrame(String args[]) {
		frame = new JFrame("Symja");
		Main panel = new Main();
		panel.setArgs(args);

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
		// frame.pack();
		// frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Displays the previously entered commands and their results. Displayed at
	 * the center of the display.
	 */
	protected JList commands;

	/**
	 * Displays the command list in a red-yellow color scheme.
	 */
	// protected MathRenderer redRenderer;
	/**
	 * Displays the command list in a blue-green color scheme.
	 */
	// protected MathRenderer blueRenderer;
	/**
	 * Displays the command list in a gray color scheme.
	 */
	// protected MathRenderer grayRenderer;
	/**
	 * Contains the previously executed commands for display.
	 */
	// protected DefaultListModel model;

	/**
	 * Area where user enters new commands. Displayed at bottom of screen.
	 */
	// protected JTextField commandLine;

	/**
	 * Button to execute commands. Displayed at bottom right corner of the screen.
	 */
	// protected JButton exec;

	/**
	 * Initializes the applet. Lays out the controls and registers listeners with
	 * them.
	 */
	public void init() {
		JComponent content = (JComponent) getContentPane();
		JPanel primaryView = new JPanel();
		// AnimatingCardLayout layout = new RandomAnimatingCardLayout(new
		// Animation[] {new FadeAnimation(), new SlideAnimation()});
		CardLayout layout = new CardLayout();

		content.setLayout(layout);
		content.add(primaryView, "primary");

		setupContent(primaryView);
		populateContent(primaryView);
	}

	/**
	 * Cached plots tend to choke when applet is reloaded, so we need to clear
	 * them.
	 */
	/*
	 * public void stop() { Plotter.clearCache(); ParametricPlotter.clearCache();
	 * Plotter3D.clearCache(); }
	 */

	/**
	 * Sets the border and layout of content.
	 */
	protected static void setupContent(JComponent content) {
		content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		content.setLayout(new BorderLayout());
	}

	/**
	 * Adds components to content.
	 */
	protected void populateContent(JComponent content) {
		EvalPanel evalPanel = createCenter();
		content.add(evalPanel, BorderLayout.CENTER);
		// content.add (createToolbar (), BorderLayout.NORTH);
		setJMenuBar(createMenubar(evalPanel));
	}

	/**
	 * Sets up the center and bottom portions of the display. Setup of the bottom
	 * part of the screen is delegated to setupBottom().
	 */
	protected EvalPanel createCenter() {
		return new EvalPanel(fFile, fDefaultSystemRulesFilename);
	}

	/**
	 * Sets up the tool bar. Icons are from Sun's Java Look and Feel graphics
	 * repository: http://java.sun.com/developer/techDocs/hi/repository/
	 */
	protected JToolBar createToolbar() {
		JToolBar tools = new JToolBar();
		// JButton insert = new JButton(new
		// ImageIcon(getClass().getResource("Import24.gif")));
		JButton preferences = new JButton(new ImageIcon(getClass().getResource("Preferences24.gif")));
		JButton about = new JButton(new ImageIcon(getClass().getResource("About24.gif")));
		// InsertListener il = new InsertListener(commandLine, this);

		// insert.addActionListener(il);
		// insert.setToolTipText("Insert a function or constant into a command");
		// tools.add(insert);
		preferences.addActionListener(new PreferencesListener(commands,
		// redRenderer, blueRenderer, grayRenderer,
				this));
		preferences.setToolTipText("Preferences");
		tools.add(preferences);
		about.addActionListener(new AboutListener(this));
		about.setToolTipText("About Symja");
		// tools.add(about);

		return tools;
	}

	protected JMenuBar createMenubar(EvalPanel evalPanel) {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenu plot = new JMenu("Plot");
		JMenu examplesMenu = new JMenu("Examples");
		JMenuItem preferences = new JMenuItem("Preferences ...");
		preferences.addActionListener(new PreferencesListener(commands,
		// redRenderer, blueRenderer, grayRenderer,
				this));
		menu.add(preferences);
		JMenuItem print = new JMenuItem("Print...");
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Printable p = new Printable() {
					public int print(Graphics g, PageFormat pageFormat, int page) {
						if (page > 0)
							return NO_SUCH_PAGE;
						Graphics2D g2d = (Graphics2D) g;
						g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
						commands.print(g2d);
						return PAGE_EXISTS;
					}
				};
				try {
					// load the service
					PrintService ps = (PrintService) ServiceManager.lookup("javax.jnlp.PrintService");

					// select a page format
					PageFormat pf = ps.showPageFormatDialog(ps.getDefaultPage());

					// send the page to the printer
					ps.print(p);
				} catch (UnavailableServiceException ex) {
					PrinterJob job = PrinterJob.getPrinterJob();
					job.setPrintable(p);
					if (job.printDialog()) {
						try {
							job.print();
						} catch (Exception exc) {
							JOptionPane.showMessageDialog(frame, exc.toString());
						}
					}
				}
			}
		});
		menu.add(print);
		JMenuItem plot2D = new JMenuItem("New 2D Plot ...");
		plot2D.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog window = new PlotWindow(frame);
				window.pack();
				window.setVisible(true);
			}
		});
		plot.add(plot2D);
		JMenuItem parametricPlot = new JMenuItem("New Parametric Plot ...");
		parametricPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog window = new ParametricPlotWindow(frame);
				window.pack();
				window.setVisible(true);
			}
		});
		plot.add(parametricPlot);
		JMenuItem plot3D = new JMenuItem("New 3D Plot ...");
		plot3D.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog window = new Plot3DWindow(frame);
				window.pack();
				window.setVisible(true);
			}
		});
		plot.add(plot3D);

		// JMenuItem plot3D = new JMenuItem("New 3D Plot ...");
		// plot3D.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// /*
		// * JDialog window = new Plot3DWindow(frame); window.pack();
		// * window.show();
		// */
		// // Plotter3D plot = Plotter3D.getPlotter(false);
		// // plot.plot(JOptionPane.showInputDialog("y(x, z) = ",
		// "Sin[x] + Sin[z]"));
		// }
		// });
		// plot.add(plot3D);

		examplesMenu.setMnemonic('e');
		examplesMenu.add(new EvalExampleMenuItem("Simplify an expression", evalPanel, "a+a+4*b^2+3*b^2"));
		examplesMenu.add(new EvalExampleMenuItem("Factor an integer number", evalPanel, "FactorInteger[2^15-5]"));
		examplesMenu.add(new EvalExampleMenuItem("Derivative of a function", evalPanel, "D[Sin[x^3],x]"));
		examplesMenu.add(new EvalExampleMenuItem("Factor a polynomial", evalPanel, "Factor[-1+x^16]"));
		examplesMenu.add(new EvalExampleMenuItem("Factor a polynomial modulo an integer", evalPanel, "Factor[5+x^12,Modulus->7]"));
		examplesMenu.add(new EvalExampleMenuItem("Expand a polynomial", evalPanel, "Expand[(-1+x)*(1+x)*(1+x^2)*(1+x^4)*(1+x^8)]"));
		examplesMenu.add(new EvalExampleMenuItem("Inverse of a matrix", evalPanel, "Inverse[{{1,2},{3,4}}]"));
		examplesMenu.add(new EvalExampleMenuItem("Determinant of a matrix", evalPanel, "Det[{{1,2},{3,4}}]"));
		// examplesMenu.add(new EvalExampleMenuItem("", evalPanel, ""));
		// examplesMenu.add(new EvalExampleMenuItem("", evalPanel, ""));
		// examplesMenu.add(new EvalExampleMenuItem("", evalPanel, ""));
		// examplesMenu.add(new EvalExampleMenuItem("", evalPanel, ""));
		menuBar.add(menu);
		menuBar.add(plot);
		menuBar.add(examplesMenu);
		return menuBar;
	}

	public void start() {
		// commandLine.selectAll();
		// commandLine.requestFocus();
	}
}

/**
 * Responsible for executing a command when the user presses enter or clicks
 * "execute". The parsed text and command return value are appended to the
 * ListModel.
 */
// class ExecuteListener implements ActionListener {
// /**
// * This is the area that commands originate from.
// */
// JTextField source;
//
// /**
// * Completed commands and their results are appended to this.
// */
// DefaultListModel sink;
//
// /**
// * Scrolls the display to the bottom to show the new command's result.
// */
// Scroller scroller;
//
// /**
// * Displays fun patterns while a command is executed.
// */
// InfiniteProgressPanel progress;
//
// /**
// * Creates an ExecuteLister that takes commands from in, writes them to out,
// * scrolls to the bottom of scroll, and displays a wait animation in parent.
// */
// public ExecuteListener(JTextField in, DefaultListModel out, JScrollPane
// scroll, RootPaneContainer parent) {
// if (in == null)
// throw new NullPointerException("No input field");
// if (out == null)
// throw new NullPointerException("No output model");
// if (scroll == null)
// throw new NullPointerException("No scroll pane");
// if (parent == null)
// throw new NullPointerException("No parent");
//
// source = in;
// sink = out;
//
// scroller = new Scroller(scroll);
// progress = new InfiniteProgressPanel();
// parent.setGlassPane(progress);
// }
//
// /**
// * Sets up the wait animation and starts execution of the command.
// */
// public void actionPerformed(ActionEvent ae) {
// progress.start();
// new CommandExecutor(sink, source, scroller, progress).start();
// }
// }

/**
 * Executes a command in a new thread. This allows the AWT thread to display a
 * wait animation while the command executes.
 */
// class CommandExecutor extends Thread {
// /**
// * The command to be executed.
// */
// protected String in;
//
// /**
// * The model to which the output is appended.
// */
// protected DefaultListModel out;
//
// /**
// * The text field the command came from.
// */
// protected JTextField source;
//
// /**
// * Scrolls the scroll pane down.
// */
// Scroller scroller;
//
// /**
// * Displays the wait animation.
// */
// InfiniteProgressPanel progress;
//
// /**
// * Creates a CommandExecutor to execute the command in the text field. When
// * the thread is started, progress is displayed, the command is executed, the
// * result is appended to out, scroller is scrolled to the bottom, and progress
// * is then hidden.
// */
// public CommandExecutor(DefaultListModel out, JTextField source, Scroller
// scroller, InfiniteProgressPanel progress) {
// in = source.getText();
// this.out = out;
// this.source = source;
// this.scroller = scroller;
// this.progress = progress;
// }
//
// /**
// * Executes the command.
// */
// public void run() {
// try {
// ComplexEvaluator evaluator = new ComplexEvaluator();
// String result = ComplexEvaluator.toString(evaluator.evaluate(in));
//
// EventQueue.invokeLater(new Displayer(in, result, out, source, scroller,
// progress));
// } catch (Throwable thrown) {
// thrown.printStackTrace();
// EventQueue.invokeLater(new Displayer(in, thrown.toString(), out, source,
// scroller, progress));
// }
// }
// }

/**
 * Displays the results of a command. This must be executed on the AWT event
 * queue to avoid deadlock, so it can't simply be displayed from the thread that
 * generated the results.
 */
// class Displayer implements Runnable {
// /**
// * The text input by the user.
// */
// protected String input;
//
// /**
// * The result of executing the string entered by the user.
// */
// protected String output;
//
// /**
// * The model to which the input and output are to be appended.
// */
// protected DefaultListModel sink;
//
// /**
// * Receives the focus after the input and output have been displayed.
// */
// JTextField source;
//
// /**
// * Scrolls down to display the new output.
// */
// Scroller scroller;
//
// /**
// * Needs to be disabled now that the results have been computed.
// */
// InfiniteProgressPanel progress;
//
// /**
// * Assembles a displayer that, when run, appends in and out to sink. Scroller
// * is scrolled to the bottom, progress is hidden, and source gets the focus.
// */
// public Displayer(String in, String out, DefaultListModel sink, JTextField
// source, Scroller scroller,
// InfiniteProgressPanel progress) {
// input = in;
// output = out;
// this.sink = sink;
// this.source = source;
// this.scroller = scroller;
// this.progress = progress;
// }
//
// /**
// * Does the work.
// */
// public void run() {
// progress.stop();
//
// sink.addElement(input);
// sink.addElement(output);
//
// scroller.scrollToEnd();
// source.selectAll();
// source.requestFocus();
// }
// }

/**
 * Handles drag events on JLists.
 */
// class ListDragListener implements DragGestureListener {
// /**
// * Finds the item the user clicked on, casts it to a String, and transfers it.
// */
// public void dragGestureRecognized(DragGestureEvent dge) {
// JList list = (JList) dge.getComponent();
// Transferable text = null;
//
// for (int counter = list.getFirstVisibleIndex(); counter <=
// list.getLastVisibleIndex(); ++counter) {
// if (list.getCellBounds(counter, counter).contains(dge.getDragOrigin())) {
// text = new StringSelection((String) list.getModel().getElementAt(counter));
// dge.startDrag(DragSource.DefaultCopyDrop, text);
// return;
// }
// }
// }
// }

/**
 * Handles double-click events on JLists. Finds the item the user clicked on,
 * casts it to a String, and inserts it into the text field.
 */
// class InsertSelectedListener extends MouseAdapter {
// /**
// * Receives the text the user double-clicked.
// */
// JTextField target;
//
// /**
// * Initializes the listener. Selected items are inserted into commandLine.
// */
// public InsertSelectedListener(JTextField commandLine) {
// target = commandLine;
// }
//
// /**
// * If the user double-clicked, the source is cast to a JList. Iterates over
// * the list's contents until the selected one is found. Casts the selected
// * object to a String and inserts it into the text field.
// */
// public void mousePressed(MouseEvent e) {
// if (e.getClickCount() >= 2) {
// JList list = (JList) e.getSource();
// String text = null;
//
// for (int counter = list.getFirstVisibleIndex(); counter <=
// list.getLastVisibleIndex(); ++counter) {
// if (list.getCellBounds(counter, counter).contains(e.getPoint())) {
// text = ((String) list.getModel().getElementAt(counter));
// target.replaceSelection(text);
//
// return;
// }
// }
// }
// }
// }

class AboutListener implements ActionListener {
	Component parent;

	public AboutListener(Component c) {
		parent = c;
	}

	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(parent, "(Description would go here.)", "About Symja", JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(getClass().getResource("About24.gif")));
	}
}
