package org.matheclipse.symja;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

import jsyntaxpane.DefaultSyntaxKit;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.MathMLUtilities;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.eval.TimeConstrainedEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.util.WriterOutputStream;
import org.matheclipse.parser.client.math.MathException;
import org.scilab.forge.jlatexmath.DefaultTeXFont;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXEnvironment;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class EvalPanel extends JPanel { // implements DocumentListener {
	private static final int FONT_SIZE_TEXT = 13;
	// private static final float FONT_SIZE_MATHML = 24;
	private static final int FONT_SIZE_TEX = 24;

	private JEditorPane jInputArea = null;
	private JScrollPane jScrollInputPane = null;

	private JScrollPane jScrollOutputPane = null;

	private OutputTextPane jOutputPane = null;

	private JComboBox jFormComboBox = new JComboBox();

	final static long serialVersionUID = 0x000000001;

	private final static String versionStr = "Input editor keyboard shortcuts:\n" + "  Ctrl+ENTER  - for symbolic evaluation\n"
			+ "  Ctrl+SPACE  - for code completion of function names\n" + "  Page up     - previous input\n"
			+ "  Page down   - next input\n" + "Program arguments:\n"
			+ "  -f or -file <filename>    - use filename as Editor input script\n"
			+ "  -d or -default <filename> - use filename for system startup rules\n";

	private final String commandHistory[] = new String[20];

	private int commandHistoryStoreIndex = 0;

	private int commandHistoryReadIndex = 0;

	private Component popupSource;

	public static EvalEngine EVAL_ENGINE;

	final JCheckBox fPrettyPrintStyle = new JCheckBox("Pretty Formula?", null, false);

	public static TimeConstrainedEvaluator EVAL;

	private InitThread fInitThread = null;

	/**
	 * If a string is on the system clipboard, this method returns it; otherwise
	 * it returns null.
	 */
	private static String getClipboard() {
		final Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

		try {
			if ((t != null) && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				final String text = (String) t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
		} catch (final UnsupportedFlavorException e) {
		} catch (final IOException e) {
		}
		return null;
	}

	// This method writes a string to the system clipboard.
	private static void setClipboard(final String str) {
		final StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}

	/**
	 * Calculation thread implemented as internal class
	 */
	class CalcThread extends Thread {
		String command;

		/**
		 * Pass command string to calculation thread. Must be called before starting
		 * the thread
		 * 
		 * @param cmd
		 *          Command string to parse
		 */
		public void setCommand(final String cmd) {
			command = cmd;
		}

		/**
		 * Thread run method
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				final StringBufferWriter printBuffer = new StringBufferWriter();
				final PrintStream pout = new PrintStream(new WriterOutputStream(printBuffer));

				EVAL_ENGINE.setOutPrintStream(pout);

				final StringBufferWriter buf0 = new StringBufferWriter();

				final IExpr expr = EVAL.constrainedEval(buf0, command, false);
				// eval(buf0, command);

				if (printBuffer.getBuffer().length() > 0) {
					// print error messages ...
					jOutputPane.printOut(printBuffer.toString() + "\n\n");
				}

				if (buf0.getBuffer().length() > 0 && fPrettyPrintStyle.isSelected()) {

					final StringBufferWriter buf1 = new StringBufferWriter();
					// final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE,
					// false);
					final TeXUtilities texUtil = new TeXUtilities(EVAL_ENGINE);
					try {
						if (expr != null) {
							// mathUtil.toMathML(expr, buf1);
							texUtil.toTeX(expr, buf1);
							TeXFormula formula = new TeXFormula(buf1.toString());
							TeXIcon ticon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, FONT_SIZE_TEX, TeXConstants.UNIT_PIXEL, 80,
									TeXConstants.ALIGN_LEFT);
							setBusy(false);
							jOutputPane.addIcon(ticon, 0, true);
							// JMathComponent component = new JMathComponent();
							// component.setFontSize(FONT_SIZE_MATHML);
							// component.setFont(new Font("miscfixed", 0, 16));
							// component.setContent(buf1.toString());
							// setBusy(false);
							// jOutputPane.addComponent(component, 0, true);
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				} else {
					String result = buf0.toString();
					jOutputPane.printOutColored("Out[" + commandHistoryStoreIndex + "]=" + result + "\n");
				}
			} catch (final MathException ex) {
				String mess = ex.getMessage();
				if (mess == null) {
					jOutputPane.printErr(ex.getClass().getName());
				} else {
					jOutputPane.printErr(mess);
				}
			} catch (final Throwable ex) {
				ex.printStackTrace();
				String mess = ex.getMessage();
				if (mess == null) {
					jOutputPane.printErr(ex.getClass().getName());
				} else {
					jOutputPane.printErr(mess);
				}
			} finally {
				setBusy(false);
			}
		}

		protected double[][] eval(final StringBufferWriter buf, final String evalStr) throws Exception {
			final IExpr expr = EVAL.constrainedEval(buf, evalStr, false);
			if (expr instanceof IAST) {
				final IAST show = (IAST) expr;
				if ((show.size() == 2) && show.isAST("Show")) {
					final IAST graphics = (IAST) show.get(1);
					if (graphics.isAST("Graphics")) {
						// example: Plot[Sin[x],{x,0,10}]
						final IAST data = (IAST) graphics.get(1);
						if (data.isAST("Line") && data.get(1).isList()) {
							final IAST lineData = (IAST) data.get(1);
							IAST pair;
							final double[][] plotPoints = new double[lineData.size() - 1][2];
							for (int i = 1; i < lineData.size(); i++) {
								pair = (IAST) lineData.get(i);
								plotPoints[i - 1][0] = ((INum) pair.get(1)).getRealPart();
								plotPoints[i - 1][1] = ((INum) pair.get(2)).getRealPart();
								// plotPoints[1][i-1] =
								// ((IDouble)pair.get(2)).getRealPart();
							}
							return plotPoints;

						}
					} else if (graphics.get(0).isAST("SurfaceGraphics")) {
						// Plot3D[Sin[x]*Cos[y],{x,-10,10},{y,-10,10}]
					}
				}
			}
			return null;
		}
	}

	/**
	 * Calculation thread implemented as internal class
	 */
	class CalcStepwiseThread extends Thread {
		String command;

		/**
		 * Pass command string to calculation thread. Must be called before starting
		 * the thread
		 * 
		 * @param cmd
		 *          Command string to parse
		 */
		public void setCommand(final String cmd) {
			command = cmd;
		}

		/**
		 * Thread run method
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				final StringBufferWriter printBuffer = new StringBufferWriter();
				final PrintStream pout = new PrintStream(new WriterOutputStream(printBuffer));

				EVAL_ENGINE.setOutPrintStream(pout);

				final StringBufferWriter buf0 = new StringBufferWriter();

				// use evalStepByStep method
				final IExpr expr = EVAL.constrainedEval(buf0, command, true // use
						// evalStepByStep
						// method
						);
				// eval(buf0, command);

				if (printBuffer.getBuffer().length() > 0) {
					// print error messages ...
					jOutputPane.printOut(printBuffer.toString() + "\n\n");
				}

				if (buf0.getBuffer().length() > 0 && fPrettyPrintStyle.isSelected()) {

					final StringBufferWriter buf1 = new StringBufferWriter();
					// final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE,
					// false);
					final TeXUtilities texUtil = new TeXUtilities(EVAL_ENGINE);
					try {
						if (expr != null) {
							// mathUtil.toMathML(expr, buf1);
							texUtil.toTeX(expr, buf1);
							TeXFormula formula = new TeXFormula(buf1.toString());
							TeXIcon ticon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, FONT_SIZE_TEX, TeXConstants.UNIT_PIXEL, 80,
									TeXConstants.ALIGN_LEFT);
							setBusy(false);
							jOutputPane.addIcon(ticon, 0, true);
							// JMathComponent component = new JMathComponent();
							// component.setFontSize(FONT_SIZE_MATHML);
							// component.setFont(new Font("miscfixed", 0, 16));
							// component.setContent(buf1.toString());
							// setBusy(false);
							// jOutputPane.addComponent(component, 0, true);
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				} else {
					String result = buf0.toString();
					if (expr != null) {
						// use the FullForm[] string to avoid problems with Flat and Orderless from input format
						jInputArea.setText(expr.fullFormString());
					}
					jOutputPane.printOutColored("Out[" + commandHistoryStoreIndex + "]=" + result + "\n");
				}
			} catch (final MathException ex) {
				String mess = ex.getMessage();
				if (mess == null) {
					jOutputPane.printErr(ex.getClass().getName());
				} else {
					jOutputPane.printErr(mess);
				}
			} catch (final Throwable ex) {
				ex.printStackTrace();
				String mess = ex.getMessage();
				if (mess == null) {
					jOutputPane.printErr(ex.getClass().getName());
				} else {
					jOutputPane.printErr(mess);
				}
			} finally {
				setBusy(false);
			}
		}
	}

	/**
	 * Enable/disable busy cursor and command input
	 * 
	 * @param busy
	 *          true: enter busy state, false: leave busy state
	 */
	private void setBusy(final boolean busy) {
		if (busy) {
			jInputArea.setEditable(false);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			jInputArea.setEditable(true);
			this.setCursor(Cursor.getDefaultCursor());
			jInputArea.requestFocus();
		}
	}

	class PopupListener extends MouseAdapter {
		@Override
		public void mousePressed(final MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupSource = e.getComponent();
				final boolean canPaste = ((JTextComponent) popupSource).isEditable() && (getClipboard() != null);
				final boolean canCopy = ((JTextComponent) popupSource).getSelectedText() != null;
			}
		}
	}

	public class InitThread extends Thread {
		String fDefaultSystemRulesFilename;

		public InitThread(String defaultSystemRulesFilename) {
			fDefaultSystemRulesFilename = defaultSystemRulesFilename;
		}

		@Override
		public void run() {
			F.initSymbols(fDefaultSystemRulesFilename, null, false);
			EVAL_ENGINE = new EvalEngine();
			EVAL = new TimeConstrainedEvaluator(EVAL_ENGINE, false, 360000);
			// for faster initialization of pretty print output we create a dummy
			// instance here:
			new TeXEnvironment(TeXConstants.STYLE_DISPLAY, new DefaultTeXFont(16));
		}
	}

	// public class InitJEuclidThread extends Thread {
	//
	// public InitJEuclidThread() {
	// }
	//
	// @Override
	// public void run() {
	// JMathComponent component = new JMathComponent();
	// component.setFontSize(FONT_SIZE_MATHML);
	// component.setContent("<math><mo>.</mo></math>");
	// jOutputPane.addComponent(component);
	// }
	// }

	/**
	 * Adds the text from the given file into the input area.
	 * 
	 * @param file
	 *          the text file or <code>null</code> if no file is available
	 */
	private void setInputAreaText(File file) {
		jInputArea.setText("");
		if (file != null) {
			try {
				final BufferedReader f = new BufferedReader(new FileReader(file));
				final StringBuffer buff = new StringBuffer(1024);
				String line;
				while ((line = f.readLine()) != null) {
					buff.append(line);
					buff.append('\n');
				}
				f.close();
				jInputArea.setText(buff.toString());
			} catch (final IOException ioe) {
				final String msg = "Cannot read from the specified input file. "
						+ "Make sure the path exists and you have read permission.";
				jOutputPane.printErr(msg);
				return;
			}
		}
	}

	/**
	 * This method initializes <code>jScrollPane</code>
	 * 
	 * @return javax.swing.JTextField
	 */
	private JScrollPane getJScrollInputPane() {
		if (jScrollInputPane == null) {
			// jInputArea = new JTextArea(4, 80);
			DefaultSyntaxKit.initKit();

			jInputArea = new JEditorPane();
			jScrollInputPane = new JScrollPane(jInputArea);
			jInputArea.setEditable(false);
			// jInputArea.setContentType("text/symja");
			jInputArea.setEditorKit(new jsyntaxpane.syntaxkits.SymjaSyntaxKit());

			jInputArea.setFont(new java.awt.Font("Monospaced", Font.PLAIN, FONT_SIZE_TEXT));
			// jInputArea.setCaretColor(new java.awt.Color(153, 204, 255));

			jInputArea.setText("Loading library...");
			// jInputArea.getDocument().addDocumentListener(this);
			InputMap im = jInputArea.getInputMap();
			ActionMap am = jInputArea.getActionMap();

			jInputArea.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyPressed(final java.awt.event.KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
						evalSymbolicInputField();
						return;
					}
					switch (e.getKeyCode()) {
					case KeyEvent.VK_PAGE_UP:
						commandHistoryReadIndex--;
						if (commandHistoryReadIndex < 0) {
							commandHistoryReadIndex = commandHistory.length - 1;
						}
						jInputArea.setText(commandHistory[commandHistoryReadIndex]);
						break;
					case KeyEvent.VK_PAGE_DOWN:
						commandHistoryReadIndex++;
						if (commandHistoryReadIndex >= commandHistory.length) {
							commandHistoryReadIndex = 0;
						}
						jInputArea.setText(commandHistory[commandHistoryReadIndex]);
						break;
					}
				}
			});
			jScrollInputPane.setViewportView(jInputArea);
		}
		return jScrollInputPane;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollOutputPane() {
		if (jScrollOutputPane == null) {
			jScrollOutputPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollOutputPane.setViewportView(getJOutputPane());
		}
		return jScrollOutputPane;
	}

	/**
	 * This method initializes jOutputPane
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getJOutputPane() {
		if (jOutputPane == null) {
			jOutputPane = new OutputTextPane();
			jOutputPane.setEditable(false);
		}
		return jOutputPane;
	}

	private void evalSymbolicInputField() {
		final String cmd = jInputArea.getText();
		jInputArea.setText("");
		if (cmd.length() > 0) {
			evalSymbolic(cmd);
		}
	}

	private void evalSymbolic(final String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		jInputArea.setText("");
		jOutputPane.printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd + "\n\n");
		setBusy(true);
		CalcThread calcThread = new CalcThread();
		calcThread.setCommand(cmd);
		EventQueue.invokeLater(calcThread);
	}

	private void evalNumeric(String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		cmd = "N[" + cmd + "]";
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		jInputArea.setText("");
		jOutputPane.printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd + "\n\n");
		setBusy(true);
		CalcThread calcThread = new CalcThread();
		calcThread.setCommand(cmd);
		calcThread.start();
	}

	private void evalStepwiseInputField() {
		final String cmd = jInputArea.getText();
		jInputArea.setText("");
		if (cmd.length() > 0) {
			evalStepwise(cmd);
		}
	}

	private void evalStepwise(final String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		jInputArea.setText("");
		jOutputPane.printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd + "\n\n");
		setBusy(true);
		CalcStepwiseThread calcStepwiseThread = new CalcStepwiseThread();
		calcStepwiseThread.setCommand(cmd);
		EventQueue.invokeLater(calcStepwiseThread);
	}

	/**
	 * Print the MathML text gotm
	 * 
	 * @param cmd
	 */
	protected void createMathMLForm(final String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		try {
			setBusy(true);
			final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE, false);
			final StringBufferWriter buf = new StringBufferWriter();

			mathUtil.toMathML(cmd, buf);
			jOutputPane.printOutColored(buf.toString() + "\n\n");
		} catch (final Exception e) {
			e.printStackTrace();
			String mess = e.getMessage();
			if (mess == null) {
				jOutputPane.printOutColored(e.getClass().getName());
			} else {
				jOutputPane.printOutColored(e.getMessage());
			}
		} finally {
			setBusy(false);
		}
	}

	protected void createJavaForm(final String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		try {
			setBusy(true);
			final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE, false);
			final StringBufferWriter buf = new StringBufferWriter();

			mathUtil.toJava(cmd, buf, true);
			jOutputPane.printOutColored(buf.toString() + "\n\n");
		} catch (final Exception e) {
			e.printStackTrace();
			String mess = e.getMessage();
			if (mess == null) {
				jOutputPane.printOutColored(e.getClass().getName());
			} else {
				jOutputPane.printOutColored(e.getMessage());
			}
		} finally {
			setBusy(false);
		}
	}

	protected void createTeXForm(final String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		try {
			setBusy(true);
			final TeXUtilities texUtil = new TeXUtilities(EVAL_ENGINE);
			final StringBufferWriter buf = new StringBufferWriter();
			texUtil.toTeX(cmd, buf);
			jOutputPane.printOutColored(buf.toString() + "\n\n");
		} catch (final Exception e) {
			e.printStackTrace();
			String mess = e.getMessage();
			if (mess == null) {
				jOutputPane.printOutColored(e.getClass().getName());
			} else {
				jOutputPane.printOutColored(e.getMessage());
			}
		} finally {
			setBusy(false);
		}
	}

	// protected void createMathMLComponent(final String cmd) {
	// if (fInitThread != null) {
	// try {
	// fInitThread.join();
	// } catch (InterruptedException e) {
	// }
	// }
	// try {
	// setBusy(true);
	// final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE, false);
	// final StringBufferWriter buf = new StringBufferWriter();
	//
	// mathUtil.toMathML(cmd, buf);
	//
	// try {
	// JMathComponent component = new JMathComponent();
	// component.setFontSize(FONT_SIZE_MATHML);
	// if (STIX_FONT != null) {
	// component.setFont(STIX_FONT.deriveFont(FONT_SIZE_MATHML));
	// }
	// component.setContent(buf.toString());
	// setBusy(false);
	// jOutputPane.addComponent(component, 0, true);
	// } catch (final Exception e) {
	// e.printStackTrace();
	// jOutputPane.printOut("MathML:\n" + buf.toString() + "\n\n");
	// }
	// } catch (final Exception e) {
	// e.printStackTrace();
	// String mess = e.getMessage();
	// if (mess == null) {
	// jOutputPane.printErr(e.getClass().getName());
	// } else {
	// jOutputPane.printErr(e.getMessage());
	// }
	// } finally {
	// setBusy(false);
	// }
	// }

	/**
	 * This method initializes jContentPane
	 * 
	 * @return JPanel
	 */
	public EvalPanel(File file, String defaultSystemRulesFilename) {
		super();

		final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		setLayout(new GridBagLayout());
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 0.25;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;

		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;

		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints3.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.weighty = 0.75;
		gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;

		final JPanel bPanel = new JPanel(); // implicit FlowLayout
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 3, 5, 5));
		final JButton b1 = new JButton("Symbolic");
		b1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				evalSymbolicInputField();
			}
		});
		buttonsPanel.add(b1);

		final JButton b2 = new JButton("Numeric");
		b2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				final String cmd = jInputArea.getText();
				jInputArea.setText("");
				if (cmd.length() > 0) {
					evalNumeric(cmd);
				}
			}
		});
		buttonsPanel.add(b2);

		final JButton bsw = new JButton("Stepwise");
		bsw.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				evalStepwiseInputField();
			}
		});
		buttonsPanel.add(bsw);

		buttonsPanel.add(fPrettyPrintStyle);

		jFormComboBox = new JComboBox(new String[] { "JavaForm", "MathMLForm", "TeXForm" });
		buttonsPanel.add(jFormComboBox);

		final JButton b3 = new JButton("Convert to Form");
		b3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				final String cmd = jInputArea.getText();
				jInputArea.setText("");
				if (cmd.length() > 0) {
					if (jFormComboBox.getSelectedItem().equals("JavaForm")) {
						createJavaForm(cmd);
					} else if (jFormComboBox.getSelectedItem().equals("MathMLForm")) {
						createMathMLForm(cmd);
					} else if (jFormComboBox.getSelectedItem().equals("TeXForm")) {
						createTeXForm(cmd);
					}
				}
			}
		});
		buttonsPanel.add(b3);
		// final JButton b4 = new JButton("Show MathML");
		// b4.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(final java.awt.event.ActionEvent e) {
		// final String cmd = jInputArea.getText();
		// jInputArea.setText("");
		// if (cmd.length() > 0) {
		// createMathMLComponent(cmd);
		// }
		// }
		// });
		// buttonsPanel.add(b4);

		bPanel.add(buttonsPanel);

		add(getJScrollInputPane(), gridBagConstraints1);
		add(bPanel, gridBagConstraints2);
		add(getJScrollOutputPane(), gridBagConstraints3);

		fInitThread = new InitThread(defaultSystemRulesFilename);
		fInitThread.start();

		int width, height, posX, posY;
		width = 300;
		height = 200;
		this.setSize(width, height);
		final Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		p.x -= 300 / 2;
		p.y -= 200 / 2;
		posX = p.x > 0 ? p.x : 0;
		posY = p.y > 0 ? p.y : 0;
		this.setLocation(posX, posY);
		validate(); // force redraw
		setVisible(true);

		jOutputPane.setFont(new Font("Monospaced", Font.PLAIN, FONT_SIZE_TEXT));
		// jInputArea.setFont(f);

		// height = jInputArea.getGraphics().getFontMetrics().getHeight();
		jInputArea.setBounds(jInputArea.getBounds().x, getHeight() - height, jInputArea.getWidth(), height);
		doLayout();

		final MouseListener popupListener = new PopupListener();
		jOutputPane.addMouseListener(popupListener);
		jInputArea.addMouseListener(popupListener);

		jOutputPane.printOut(versionStr + "\n");

		// request Focus
		jInputArea.requestFocus();

		setInputAreaText(file);

		jInputArea.setEditable(true);

		// InitJEuclidThread initJEuclidThread = new InitJEuclidThread();
		// initJEuclidThread.start();
	}

	// Listener methods

	// public void changedUpdate(DocumentEvent ev) {
	// }
	//
	// public void removeUpdate(DocumentEvent ev) {
	// }
	//
	// public void insertUpdate(DocumentEvent ev) {
	// if (ev.getLength() != 1) {
	// return;
	// }
	//
	// int pos = ev.getOffset();
	// String content = null;
	// try {
	// content = jInputArea.getText(0, pos + 1);
	// } catch (BadLocationException e) {
	// e.printStackTrace();
	// }
	//
	// // Find where the word starts
	// int w;
	// for (w = pos; w >= 0; w--) {
	// if (!Character.isLetter(content.charAt(w))) {
	// break;
	// }
	// }
	// if (pos - w < 2) {
	// // Too few chars
	// return;
	// }
	// }

	/**
	 * Set a new text in the input textarea
	 * 
	 * @param text
	 */
	public void setInputText(String text) {
		jInputArea.setText(text);
	}

}
