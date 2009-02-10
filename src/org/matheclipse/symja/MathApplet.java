package org.matheclipse.symja;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.matheclipse.core.eval.CompletionLists;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.MathMLUtilities;
import org.matheclipse.core.eval.TimeConstrainedEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.util.WriterOutputStream;

public class MathApplet extends JApplet implements DocumentListener {
	private static final String COMMIT_ACTION = "commit";

	private static enum Mode {
		INSERT, COMPLETION
	};

	private String fReplacement;
	private int fW;

	private Mode mode = Mode.INSERT;
	private final List<String> fWords = new ArrayList<String>(2048);
	private final List<String> fReplaceWords = new ArrayList<String>(2048);

	private javax.swing.JPanel jContentPane = null;

	private JTextArea jInputArea = null;

	private JScrollPane jScrollInputPane = null;

	private JScrollPane jScrollOutputPane = null;

	private JTextPane jOutputPane = null;

	// private JMenuBar jJMenuBar = null;
	//
	// private JMenu jMenuFile = null;
	//
	// private JMenuItem jMenuItemExit = null;
	//
	// private JMenu jMenuOptions = null;
	//
	// private JMenuItem jMenuItemColors = null;
	//
	// private JMenu jMenuView = null;
	//
	// private JMenuItem jMenuItemClear = null;
	//
	// private JMenuItem jMenuItemScale = null;
	//
	// // private JMenuItem jMenuItemSkriptPath = null;
	//
	// private JMenuItem jMenuItemBreak = null;
	//
	// private JMenu jMenuHelp = null;
	//
	// private JMenuItem jMenuItemHelp = null;

	// private JPopupMenu jPopupMenu = null;

	// private JMenuItem jPopupMenuItemCopy = null;
	//
	// private JMenuItem jPopupMenuItemPaste = null;
	//
	// private JMenuItem jPopupMenuItemBreak = null;

	/* custom created attributes */
	final static long serialVersionUID = 0x000000001;

	private final static String versionStr = "Keyboard shortcuts\n" + "- Ctrl+ENTER - for symbolic evaluation\n"
			+ "- Cursor up - previoues input\n" + "- Cursor down - next input\n";

	private final static String iniStr = "me.ini";

	private static String jreStr;

	private static String iniFileStr; // including path

	// private String scriptPath;

	private DefaultStyledDocument jOutputDoc;

	// private Props props;

	private SimpleAttributeSet outputAtr, errorAtr, outputOpAtr, outputStringAtr;

	private SimpleAttributeSet outputNum1Atr, outputNum2Atr, outputBracketAtr, outputCommentAtr;

	private int fontSize;

	private final int maxDocSize = 1000000; // to work around bad TextPane
	// performance

	// private MathApplet jCalc;

	private final String commandHistory[] = new String[20];

	private int commandHistoryStoreIndex = 0;

	private int commandHistoryReadIndex = 0;

	// private boolean addLinefeed = false;

	private Component popupSource;

	private int internalScale = 64;

	public static EvalEngine EVAL_ENGINE;

	public static TimeConstrainedEvaluator EVAL;

	// public Boolean IS_INITIALIZED = Boolean.FALSE;

	final static String bracketChars = "[](){}";

	final static String operatorChars = "~+*;,.#'-:<>|&/=!^@";

	final static String numericBreakCharacters = operatorChars + bracketChars;

	private final String cName[] = { "Output", "Num1", "Num2", "Comment", "Operator", "Bracket", "String", "Error" };

	private Color cColor[]; /* selected color */

	private final Color cColorDefault[] /* default colors */= { Color.BLUE, Color.BLACK, new Color(100, 100, 255), Color.GRAY,
			new Color(255, 100, 0), new Color(100, 0, 50), new Color(50, 150, 0), Color.RED };

	private Thread fInitThread = null;

	// If a string is on the system clipboard, this method returns it;
	// otherwise it returns null.
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
	// otherwise it returns null.
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

				// if (!IS_INITIALIZED) {
				// // wait until eval engine is initialized
				// for (int i = 0; i < 30; i++) {
				// yield();
				// if (IS_INITIALIZED) {
				// break;
				// }
				// }
				// }
				EVAL_ENGINE.setOutPrintStream(pout);

				final StringBufferWriter buf = new StringBufferWriter();

				eval(buf, command);
				if (printBuffer.getBuffer().length() > 0) {
					printOut(printBuffer.toString() + "\n\n");
				}
				if (buf.getBuffer().length() > 0) {
					printOutColored("Out[" + commandHistoryStoreIndex + "]=" + buf.toString() + "\n");
				}
				// Value result = jParse.eval(command);
				// if (addLinefeed) {
				// printOut("\n");
				// addLinefeed = false;
				// }
			} catch (final Throwable ex) {
				// ex.printStackTrace();
				String mess = ex.getMessage();
				if (mess == null) {
					printErr(ex.getClass().getName());
				} else {
					printErr(mess);
				}
			} finally {
				setBusy(false);
			}
			// catch (ParseException ex) {
			// if (addLinefeed) {
			// printOut("\n");
			// addLinefeed = false;
			// }
			// ErrorPos epos = jParse.getErrorPos();
			// if (epos.line > 1) {
			// String file = epos.file.substring(5);
			// printErr("\n");
			// if (epos.file.length()>0)
			// printErr("File: "+file+", ");
			// printErr("Line: "+epos.line+", Pos: "+epos.pos+"\n");
			// }
			// try {
			// command = jParse.getErrorString();
			// } catch (ParseException e) {
			// command = e.getMessage();
			// };
			// printErr("\n"+command+"\n");
			// StringBuffer sb = new StringBuffer();
			// for (int i=0; i<epos.getPos();i++)
			// sb.append('-');
			// sb.append("^\n");
			// printErr(sb.toString());
			//
			// printErr(ex.getMessage()+"\n\n");
			// setBusy(false);
			//
			// }
		}

		protected double[][] eval(final StringBufferWriter buf, final String evalStr) throws Exception {
			final IExpr expr = EVAL.constrainedEval(buf, evalStr);
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
								// plotPoints[1][i-1] = ((IDouble)pair.get(2)).getRealPart();
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
	 * Enable/disable busy cursor and command input
	 * 
	 * @param busy
	 *          true: enter busy state, false: leave busy state
	 */
	private void setBusy(final boolean busy) {
		if (busy) {
			jInputArea.setEditable(false);
			// jMenuItemBreak.setEnabled(true);
			// jPopupMenuItemBreak.setEnabled(true);
			jOutputPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			jInputArea.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		} else {
			jInputArea.setEditable(true);
			// jMenuItemBreak.setEnabled(false);
			// jPopupMenuItemBreak.setEnabled(false);
			jOutputPane.setCursor(Cursor.getDefaultCursor());
			jInputArea.setCursor(Cursor.getDefaultCursor());
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
				// jPopupMenuItemPaste.setEnabled(canPaste);
				// jPopupMenuItemCopy.setEnabled(canCopy);
				// jPopupMenu.show(popupSource, e.getX(), e.getY());
			}
		}
	}

	/**
	 * Checks is a character is a valid (starting) character for a number
	 * 
	 * @param c
	 *          the char to be checked
	 * @return true (is numeric) or false (is not numeric)
	 */
	private boolean isNumeric(final char c) {
		return ("0123456789.".indexOf(c) != -1);
	}

	/**
	 * Color text in output pane depending on the contents of the inserted string
	 * 
	 * @param s
	 *          String that was inserted into the output pane
	 * @param offset
	 *          Start offset where string was inserted
	 */
	private void colorOutput(final String s, final int offset) {
		int startIdx = 0;
		char c;
		for (int idx = 0; idx < s.length(); idx++) {
			c = s.charAt(idx);
			startIdx = idx;
			// check for line comments
			if ((c == '/') && (idx < s.length() - 1) && (s.charAt(idx + 1) == '/')) {
				startIdx = idx;
				idx++;
				while ((++idx < s.length()) && ((c = s.charAt(idx)) != '\n')) {
					;
				}
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx, outputCommentAtr, true);
			}
			// check for operators
			startIdx = idx;
			while ((operatorChars.indexOf(c) != -1) && (idx < s.length())) {
				c = s.charAt(++idx);
			}
			if (idx != startIdx) {
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx, outputOpAtr, true);
			}
			// check for brackets (no own state, since there's usually only one
			// bracket in a row)
			if (bracketChars.indexOf(c) != -1) {
				jOutputDoc.setCharacterAttributes(offset + idx, 1, outputBracketAtr, true);
				continue;
			}
			// check for strings
			if (c == '"') {
				startIdx = idx;
				while ((++idx < s.length()) && ((c = s.charAt(idx)) != '"')) {
					;
				}
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx + 1, outputStringAtr, true);
			}
			// check for numbers
			if (isNumeric(c)) {
				int len, step, point = -1;
				boolean color1 = true;
				startIdx = idx;
				// search for break character
				while ((++idx < s.length()) && (numericBreakCharacters.indexOf(c = s.charAt(idx)) == -1)) {
					if (c == '.') {
						point = idx - startIdx; // position of point inside number
					}
				}
				if (idx != s.length()) {
					idx--; // idx on last numeric character
				}
				len = idx - startIdx + 1;
				step = 3;
				// check kind of number
				if (len > 1) {
					if (s.charAt(startIdx) == '0') {
						// special cases: octal, binary, hexadecimal
						switch (s.charAt(startIdx + 1)) {
						case 'x':
							step = 2;
							startIdx += 2;
							len -= 2;
							break; // octal
						case 'b':
							step = 4;
							startIdx += 2;
							len -= 2;
							break; // octal
						case '.':
							step = 3;
							break; // decimal
						default:
							step = 2;
							break; // octal
						}
					}
				}
				// special case: search for 'e' and 'p'
				// if (dec) {
				// for (int pos = startIdx; pos < startIdx + len; pos++) {
				// c = s.charAt(pos);
				// if (c == 'e' || c == 'E') {
				// idx = pos;
				// len = idx - startIdx;
				// break;
				// }
				// }
				// }
				if (point != -1) {
					int step2 = step;
					// special case: decimal point
					// first color characters after the point from left to right
					for (int pos = point + 1; pos < len; pos += step2) {
						if (pos + step2 >= len) {
							step2 = len - pos; // don't color outside number
						}
						jOutputDoc.setCharacterAttributes(offset + startIdx + pos, step2, color1 ? outputNum1Atr : outputNum2Atr, true);
						color1 = !color1; // toggle
					}
					// then color characters before the point from right to left
					len = point;
					color1 = true;
				}
				for (int pos = len; pos > 0; pos -= step) {
					if (pos - step < 0) {
						step = pos;
					}
					jOutputDoc.setCharacterAttributes(offset + startIdx + pos - step, step, color1 ? outputNum1Atr : outputNum2Atr, true);
					color1 = !color1; // toggle
				}

			}
		}
	}

	/**
	 * Print text to output pane with syntax coloring
	 * 
	 * @param s
	 *          text to print
	 */
	public void printOutColored(final String s) {
		try {
			// int length = jOutputDoc.getLength();
			// if (length > maxDocSize) {
			// jOutputDoc.remove(0, length - maxDocSize);
			// length = jOutputDoc.getLength();
			// }
			jOutputDoc.insertString(0, s, outputAtr);
			// now start the coloring
			colorOutput(s, 0);
			jOutputPane.setCaretPosition(0);// jOutputDoc.getLength());
		} catch (final BadLocationException ble) {
			System.out.println("Couldn't write to Output Pane");
		}
	}

	/**
	 * Print text to output pane
	 * 
	 * @param s
	 *          text to print
	 */
	public void printOut(final String s) {
		try {
			// int length = jOutputDoc.getLength();
			// if (length > maxDocSize) {
			// jOutputDoc.remove(0, length - maxDocSize);
			// length = jOutputDoc.getLength();
			// }
			jOutputDoc.insertString(0, s, outputAtr);
			jOutputPane.setCaretPosition(0);// jOutputDoc.getLength());
		} catch (final BadLocationException ble) {
			System.out.println("Couldn't write to Output Pane");
		}
	}

	/**
	 * Print text to error pane
	 * 
	 * @param s
	 *          text to print
	 */
	public void printErr(final String s) {
		if (s == null) {
			return;
		}
		try {
			// int length = jOutputDoc.getLength();
			// if (length > maxDocSize) {
			// jOutputDoc.remove(0, length - maxDocSize);
			// length = jOutputDoc.getLength();
			// }
			jOutputDoc.insertString(0, s + "\n", errorAtr);
			jOutputPane.setCaretPosition(0);// jOutputDoc.getLength());
		} catch (final BadLocationException ble) {
			System.out.println("Couldn't write to Output Pane");
		}
	}

	/**
	 * Common exit method to use in exit events
	 */
	// private void exit() {
	// // store width and height
	// try {
	// final Dimension d = this.getSize();
	// props.set("frameWidth", d.width);
	// props.set("frameHeight", d.height);
	// // store frame pos
	// final Point p = this.getLocation();
	// props.set("framePosX", p.x);
	// props.set("framePosY", p.y);
	// //
	//
	// props.save(iniFileStr);
	// } catch (final Exception e) {
	// e.printStackTrace();
	// }
	// System.exit(0);
	// }
	public class InitThread extends Thread {
		@Override
		public void run() {
			// synchronized (IS_INITIALIZED) {
			F.initSymbols();
			EVAL_ENGINE = new EvalEngine();
			EVAL = new TimeConstrainedEvaluator(EVAL_ENGINE, false, 360000);
			// IS_INITIALIZED = Boolean.TRUE;
			new CompletionLists(fWords, fReplaceWords);
			// }
		}
	}

	/**
	 * Initialize instance, load properties etc.
	 */
	public void init() {
		setContentPane(getJContentPane());
		fInitThread = new InitThread();
		fInitThread.start();
		// get ini path
		String s = this.getClass().getName().replace('.', '/') + ".class";
		final URL url = this.getClass().getClassLoader().getResource(s);
		int pos;
		try {
			iniFileStr = URLDecoder.decode(url.getPath(), "UTF-8");
		} catch (final UnsupportedEncodingException ex) {
		}
		;
		// special handling for JAR
		if (((pos = iniFileStr.toLowerCase().indexOf("file:")) != -1)) {
			iniFileStr = iniFileStr.substring(pos + 5);
		}
		if ((pos = iniFileStr.toLowerCase().indexOf(s.toLowerCase())) != -1) {
			iniFileStr = iniFileStr.substring(0, pos);
		}
		s = (this.getClass().getName().replace('.', '/') + ".jar").toLowerCase();
		if ((pos = iniFileStr.toLowerCase().indexOf(s)) != -1) {
			iniFileStr = iniFileStr.substring(0, pos);
		}
		//
		iniFileStr += iniStr;
		// load properties
		// props = new Props();
		// props.setHeader(versionStr);
		if (iniFileStr != null) {
			// props.load(iniFileStr);
		}
		// read props
		internalScale = 64;// props.get("internalScale", 64);
		// scriptPath = props.get("scriptPath", ".");
		fontSize = 12; // props.get("fontSize", 12);
		cColor = new Color[8];
		cColor[Colors.OUTPUT] = new Color(cColorDefault[Colors.OUTPUT].getRGB());//props.get("outputColor", cColorDefault[Colors.OUTPUT].getRGB()));
		cColor[Colors.ERROR] = new Color(cColorDefault[Colors.ERROR].getRGB());//props.get("errorColor", cColorDefault[Colors.ERROR].getRGB()));
		cColor[Colors.OPERATOR] = new Color(cColorDefault[Colors.OPERATOR].getRGB());//props.get("outputOperatorColor", cColorDefault[Colors.OPERATOR].getRGB()));
		cColor[Colors.STRING] = new Color(cColorDefault[Colors.STRING].getRGB());//props.get("outputStringColor", cColorDefault[Colors.STRING].getRGB()));
		cColor[Colors.NUM1] = new Color(cColorDefault[Colors.NUM1].getRGB());//props.get("outputNumberColor1", cColorDefault[Colors.NUM1].getRGB()));
		cColor[Colors.NUM2] = new Color(cColorDefault[Colors.NUM2].getRGB());//props.get("outputNumberColor2", cColorDefault[Colors.NUM2].getRGB()));
		cColor[Colors.BRACKET] = new Color(cColorDefault[Colors.BRACKET].getRGB());//props.get("outputBracketColor", cColorDefault[Colors.BRACKET].getRGB()));
		cColor[Colors.COMMENT] = new Color(cColorDefault[Colors.COMMENT].getRGB());//props.get("outputCommentColor", cColorDefault[Colors.COMMENT].getRGB()));
		// read frame props
		int width, height, posX, posY;
		width = 300;// props.get("frameWidth", 300);
		height = 200;// props.get("frameHeight", 200);
		this.setSize(width, height);
		final Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		p.x -= 300 / 2;
		p.y -= 200 / 2;
		posX = p.x > 0 ? p.x : 0;// props.get("framePosX", p.x > 0 ? p.x : 0);
		posY = p.y > 0 ? p.y : 0; // props.get("framePosY", p.y > 0 ? p.y : 0);
		this.setLocation(posX, posY);
		validate(); // force redraw
		setVisible(true);

		// jCalc = this;
		// jOutputDoc = jOutputPane.getStyledDocument();
		jOutputDoc = new DefaultStyledDocument();
		jOutputPane.setDocument(jOutputDoc);
		final Font f = new Font("Monospaced", Font.PLAIN, fontSize);
		jOutputPane.setFont(f);
		jInputArea.setFont(f);
		height = jInputArea.getGraphics().getFontMetrics().getHeight();
		jInputArea.setBounds(jInputArea.getBounds().x, jContentPane.getHeight() - height, jInputArea.getWidth(), height);
		jContentPane.doLayout();

		// popupmenu
		// getJPopupMenu();
		final MouseListener popupListener = new PopupListener();
		jOutputPane.addMouseListener(popupListener);
		jInputArea.addMouseListener(popupListener);

		// error
		errorAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(errorAtr, cColor[Colors.ERROR]);
		// normal output
		outputAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputAtr, cColor[Colors.OUTPUT]);
		// operator output
		outputOpAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputOpAtr, cColor[Colors.OPERATOR]);
		// string output
		outputStringAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputStringAtr, cColor[Colors.STRING]);
		// number 1 output
		outputNum1Atr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputNum1Atr, cColor[Colors.NUM1]);
		// number 2 output
		outputNum2Atr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputNum2Atr, cColor[Colors.NUM2]);
		// bracket output
		outputBracketAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputBracketAtr, cColor[Colors.BRACKET]);
		// comment output
		outputCommentAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputCommentAtr, cColor[Colors.STRING]);

		// create Parser
		// try {
		// jParse = new JParse();
		// jParse.addDefaultOperators();
		// jParse.addDefaultFunctions();
		// jParse.addDefaultConstants();
		// } catch (ParseException ex) {
		// printErr("Init failed: "+ex.getMessage()+"\n");
		// }
		//
		// jParse.setScale(internalScale);
		// jParse.setScriptPath(scriptPath);

		// print status info
		printOut(versionStr + "\n");
		// printOut("Running in JRE "+jreStr+"\n");
		// printOut("Loading settings from "+iniFileStr+"\n\n");

		// add parse listener
		// jParse.addParseListener(new JParseListener() {
		// public void parseEvent(JParseEvent ev) {
		// printOut(ev.getMessage());
		// addLinefeed = true;
		// // synchronize with AWT thread
		// try {
		// SwingUtilities.invokeAndWait(new Runnable() { public void run() {} });
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// });
		// request Focus
		jInputArea.requestFocus();

		// F.initSymbols();
		// EVAL_ENGINE = new EvalEngine();
		// EVAL = new TimeConstrainedEvaluator(EVAL_ENGINE, false, 360000);
		// IS_INITIALIZED = true;
		// new CompletionLists(fWords, fReplaceWords);
		jInputArea.setText("");
		jInputArea.setEditable(true);
	}

	private JScrollPane getJScrollInputPane() {
		if (jScrollInputPane == null) {
			jScrollInputPane = new JScrollPane();
			jScrollInputPane.setViewportView(getJInputArea());
		}
		return jScrollInputPane;
	}

	/**
	 * This method initializes jInputField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextArea getJInputArea() {
		if (jInputArea == null) {
			jInputArea = new JTextArea(4, 80);
			jInputArea.setEditable(false);
			jInputArea.setText("Loading library...");
			jInputArea.getDocument().addDocumentListener(this);
			InputMap im = jInputArea.getInputMap();
			ActionMap am = jInputArea.getActionMap();
			im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
			am.put(COMMIT_ACTION, new CommitAction());

			jInputArea.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyPressed(final java.awt.event.KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
						evalSymbolicInputField();
						return;
					}
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						commandHistoryReadIndex--;
						if (commandHistoryReadIndex < 0) {
							commandHistoryReadIndex = commandHistory.length - 1;
						}
						jInputArea.setText(commandHistory[commandHistoryReadIndex]);
						break;
					case KeyEvent.VK_DOWN:
						commandHistoryReadIndex++;
						if (commandHistoryReadIndex >= commandHistory.length) {
							commandHistoryReadIndex = 0;
						}
						jInputArea.setText(commandHistory[commandHistoryReadIndex]);
						break;
					}
				}
			});

			// jInputField.addActionListener(new java.awt.event.ActionListener() {
			// public void actionPerformed(java.awt.event.ActionEvent e) {
			// String cmd = e.getActionCommand();
			// if (cmd.length() > 0) {
			// commandHistory[commandHistoryStoreIndex++] = cmd;
			// if (commandHistoryStoreIndex >= commandHistory.length)
			// commandHistoryStoreIndex = 0;
			// commandHistoryReadIndex = commandHistoryStoreIndex;
			// jInputField.setText("");
			// printOutColored(cmd + "\n");
			// setBusy(true);
			// calcThread = new CalcThread();
			// calcThread.setCommand(cmd);
			// calcThread.start();
			//			
			// /*
			// * try { result = jParse.parse(cmd); printOut(" out =
			// * "+result.toString()+"\n\n"); } catch (ParseException ex) {
			// * StringBuffer sb = new StringBuffer(); for (int i=0;
			// i<jParse.getErrorPos();i++)
			// * sb.append('-'); sb.append("^\n"); printErr(sb.toString());
			// * printErr(ex.getMessage()+"\n\n"); }
			// */
			// }
			// }
			// });
		}
		return jInputArea;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollOutputPane() {
		if (jScrollOutputPane == null) {
			jScrollOutputPane = new JScrollPane();
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
			jOutputPane = new JTextPane();
			jOutputPane.setEditable(false);
		}
		return jOutputPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	// private JMenuBar getJJMenuBar() {
	// if (jJMenuBar == null) {
	// jJMenuBar = new JMenuBar();
	// jJMenuBar.add(getJMenuFile());
	// jJMenuBar.add(getJMenuOptions()); // Generated
	// jJMenuBar.add(getJMenuView()); // Generated
	// jJMenuBar.add(getJMenuHelp()); // Generated
	// }
	// return jJMenuBar;
	// }
	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	// private JMenu getJMenuFile() {
	// if (jMenuFile == null) {
	// jMenuFile = new JMenu();
	// jMenuFile.setText("File");
	// // jMenuFile.add(getJMenuItemSkriptPath()); // Generated
	// jMenuFile.add(getJMenuItemBreak()); // Generated
	// jMenuFile.add(getJMenuItemExit());
	// }
	// return jMenuFile;
	// }
	/**
	 * This method initializes jMenuItemExit
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJMenuItemExit() {
	// if (jMenuItemExit == null) {
	// jMenuItemExit = new JMenuItem();
	// jMenuItemExit.setText("Exit");
	// jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// exit();
	// }
	// });
	// }
	// return jMenuItemExit;
	// }
	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	// private JMenu getJMenuOptions() {
	// if (jMenuOptions == null) {
	// jMenuOptions = new JMenu();
	// jMenuOptions.setText("Options"); // Generated
	// jMenuOptions.add(getJMenuItemColors()); // Generated
	// jMenuOptions.add(getJMenuItemScale()); // Generated
	// }
	// return jMenuOptions;
	// }
	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJMenuItemColors() {
	// if (jMenuItemColors == null) {
	// jMenuItemColors = new JMenuItem();
	// jMenuItemColors.setText("Set Font & Colors"); // Generated
	// jMenuItemColors.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// final ColorDialog cDiag = new ColorDialog(jCalc, true);
	// cDiag.setParameters(cName, cColor, cColorDefault, fontSize);
	// cDiag.setVisible(true);
	// fontSize = cDiag.getFontSize();
	// cColor = cDiag.getColors();
	// // set new setting
	// final Font f = new Font("Monospaced", Font.PLAIN, fontSize);
	// jOutputPane.setFont(f);
	// jInputField.setFont(f);
	// final int height = jInputField.getGraphics().getFontMetrics().getHeight();
	// jInputField.setBounds(jInputField.getBounds().x, jContentPane.getHeight() -
	// height, jInputField.getWidth(), height);
	// jContentPane.doLayout();
	// StyleConstants.setForeground(errorAtr, cColor[Colors.ERROR]);
	// StyleConstants.setForeground(outputAtr, cColor[Colors.OUTPUT]);
	// StyleConstants.setForeground(outputOpAtr, cColor[Colors.OPERATOR]);
	// StyleConstants.setForeground(outputStringAtr, cColor[Colors.STRING]);
	// StyleConstants.setForeground(outputNum1Atr, cColor[Colors.NUM1]);
	// StyleConstants.setForeground(outputNum2Atr, cColor[Colors.NUM2]);
	// StyleConstants.setForeground(outputBracketAtr, cColor[Colors.BRACKET]);
	// StyleConstants.setForeground(outputCommentAtr, cColor[Colors.STRING]);
	// // store new settings
	// props.set("fontSize", fontSize);
	// props.set("outputColor", cColor[Colors.OUTPUT].getRGB());
	// props.set("errorColor", cColor[Colors.ERROR].getRGB());
	// props.set("outputOperatorColor", cColor[Colors.OPERATOR].getRGB());
	// props.set("outputStringColor", cColor[Colors.STRING].getRGB());
	// props.set("outputNumberColor1", cColor[Colors.NUM1].getRGB());
	// props.set("outputNumberColor2", cColor[Colors.NUM2].getRGB());
	// props.set("outputBracketColor", cColor[Colors.BRACKET].getRGB());
	// props.set("outputCommentColor", cColor[Colors.COMMENT].getRGB());
	//
	// }
	// });
	// }
	// return jMenuItemColors;
	// }
	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	// private JMenu getJMenuView() {
	// if (jMenuView == null) {
	// jMenuView = new JMenu();
	// jMenuView.setText("View"); // Generated
	// jMenuView.add(getJMenuItemClear()); // Generated
	// }
	// return jMenuView;
	// }
	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJMenuItemClear() {
	// if (jMenuItemClear == null) {
	// jMenuItemClear = new JMenuItem();
	// jMenuItemClear.setText("Clear View"); // Generated
	// jMenuItemClear.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// jOutputPane.setText("");
	// }
	// });
	// }
	// return jMenuItemClear;
	// }
	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJMenuItemScale() {
	// if (jMenuItemScale == null) {
	// jMenuItemScale = new JMenuItem();
	// jMenuItemScale.setText("Set Scale"); // Generated
	// jMenuItemScale.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// final ScaleDialog sDiag = new ScaleDialog(jCalc, true);
	// sDiag.setScale(internalScale);
	// sDiag.setVisible(true);
	// internalScale = sDiag.getScale();
	// props.set("internalScale", internalScale);
	// // jParse.setScale(internalScale);
	// }
	// });
	// }
	// return jMenuItemScale;
	// }
	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJMenuItemSkriptPath() {
	// if (jMenuItemSkriptPath == null) {
	// jMenuItemSkriptPath = new JMenuItem();
	// jMenuItemSkriptPath.setText("Evaluate"); // Generated
	// jMenuItemSkriptPath.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(java.awt.event.ActionEvent e) {
	// String cmd = jInputField.getText();
	// jInputField.setText("");
	// if (cmd.length() > 0) {
	// evalSymbolic(cmd);
	//
	// /*
	// * try { result = jParse.parse(cmd); printOut(" out =
	// * "+result.toString()+"\n\n"); } catch (ParseException ex) {
	// * StringBuffer sb = new StringBuffer(); for (int i=0;
	// i<jParse.getErrorPos();i++)
	// * sb.append('-'); sb.append("^\n"); printErr(sb.toString());
	// * printErr(ex.getMessage()+"\n\n"); }
	// */
	// }
	// // JFileChooser jf = new JFileChooser(scriptPath);
	// // jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	// // int returnVal = jf.showOpenDialog(jCalc);
	// // if (returnVal == JFileChooser.APPROVE_OPTION) {
	// // scriptPath = jf.getSelectedFile().getAbsolutePath();
	// // }
	// // jParse.setScriptPath(scriptPath);
	// // props.set("scriptPath", scriptPath);
	// }
	//
	// });
	// }
	// return jMenuItemSkriptPath;
	// }
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
		printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd + "\n\n");
		setBusy(true);
		CalcThread calcThread = new CalcThread();
		calcThread.setCommand(cmd);
		calcThread.start();
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
		printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd + "\n\n");
		setBusy(true);
		CalcThread calcThread = new CalcThread();
		calcThread.setCommand(cmd);
		calcThread.start();
	}

	protected void createMathML(final String cmd) {
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
		final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE, false);
		final StringBufferWriter buf = new StringBufferWriter();
		try {
			mathUtil.toMathML(cmd, buf);
			printOutColored("MathML:\n" + buf.toString() + "\n\n");
		} catch (final Exception e) {
			e.printStackTrace();
			String mess = e.getMessage();
			if (mess == null) {
				printOutColored(e.getClass().getName());
			} else {
				printOutColored(e.getMessage());
			}
		}
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJMenuItemBreak() {
	// if (jMenuItemBreak == null) {
	// jMenuItemBreak = new JMenuItem();
	// jMenuItemBreak.setText("Break Calculation"); // Generated
	// jMenuItemBreak.setEnabled(false);
	// jMenuItemBreak.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// EVAL.stopRequest();
	// // jParse.breakExec();
	// }
	// });
	// }
	// return jMenuItemBreak;
	// }
	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	// private JMenu getJMenuHelp() {
	// if (jMenuHelp == null) {
	// jMenuHelp = new JMenu();
	// jMenuHelp.setText("Help"); // Generated
	// jMenuHelp.add(getJMenuItemHelp()); // Generated
	// }
	// return jMenuHelp;
	// }
	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJMenuItemHelp() {
	// if (jMenuItemHelp == null) {
	// jMenuItemHelp = new JMenuItem();
	// jMenuItemHelp.setText("Help"); // Generated
	// jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// final Help help = new Help();
	// help.setLocation(getX() + 30, getY() + 30);
	// help.setSize(800, 600);
	// help.setVisible(true);
	// }
	// });
	// }
	// return jMenuItemHelp;
	// }
	/**
	 * This method initializes jPopupMenu
	 * 
	 * @return javax.swing.JPopupMenu
	 */
	// private JPopupMenu getJPopupMenu() {
	// if (jPopupMenu == null) {
	// jPopupMenu = new JPopupMenu();
	// jPopupMenu.add(getJPopupMenuItemBreak()); // Generated
	// jPopupMenu.add(getJPopupMenuItemCopy()); // Generated
	// jPopupMenu.add(getJPopupMenuItemPaste()); // Generated
	// // jPopupMenu.setVisible(false); // Generated
	// }
	// return jPopupMenu;
	// }
	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJPopupMenuItemCopy() {
	// if (jPopupMenuItemCopy == null) {
	// jPopupMenuItemCopy = new JMenuItem();
	// jPopupMenuItemCopy.setText("Copy"); // Generated
	// jPopupMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// final String s = ((JTextComponent) popupSource).getSelectedText();
	// if (s != null) {
	// setClipboard(s);
	// }
	// }
	// });
	// }
	// return jPopupMenuItemCopy;
	// }
	/**
	 * This method initializes jMenuItem1
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJPopupMenuItemPaste() {
	// if (jPopupMenuItemPaste == null) {
	// jPopupMenuItemPaste = new JMenuItem();
	// jPopupMenuItemPaste.setText("Paste"); // Generated
	// jPopupMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// final String s = getClipboard();
	// if (s != null) {
	// ((JTextComponent) popupSource).paste();
	// }
	// }
	// });
	//
	// }
	// return jPopupMenuItemPaste;
	// }
	/**
	 * This method initializes jMenuItem2
	 * 
	 * @return javax.swing.JMenuItem
	 */
	// private JMenuItem getJPopupMenuItemBreak() {
	// if (jPopupMenuItemBreak == null) {
	// jPopupMenuItemBreak = new JMenuItem();
	// jPopupMenuItemBreak.setText("Break"); // Generated
	// jPopupMenuItemBreak.setEnabled(false); // Generated
	// jPopupMenuItemBreak.addActionListener(new java.awt.event.ActionListener() {
	// public void actionPerformed(final java.awt.event.ActionEvent e) {
	// EVAL.stopRequest();
	// // jParse.breakExec();
	// }
	// });
	// }
	// return jPopupMenuItemBreak;
	// }
	/**
	 * Main function, check JVM version, set Look and Feel
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		/*
		 * Set "Look and Feel" to system default
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) { /* don't care */
		}
		/*
		 * Check JVM version
		 */
		jreStr = System.getProperty("java.version");
		final String vs[] = jreStr.split("[._]");
		double vnum;
		if (vs.length >= 3) {
			vnum = (Integer.parseInt(vs[0])) + (Integer.parseInt(vs[1])) * 0.1;
			if (vnum < 1.5) {
				JOptionPane.showMessageDialog(null, "Run HartMath with JVM >= 5.0", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}

		Toolkit.getDefaultToolkit().setDynamicLayout(true);

		// create Instance of this static class and call init function
		new MathApplet().init();
	}

	/**
	 * This is the default constructor
	 */
	// public MathEclipse() {
	// super();
	// initialize();
	// }
	/**
	 * This method initializes this
	 */
	// private void initialize() {
	// setJMenuBar(getJJMenuBar());
	// setResizable(true);
	// this.setSize(300, 200);
	// setContentPane(getJContentPane());
	// setTitle("HartMath");
	// setVisible(false);
	// addWindowListener(new java.awt.event.WindowAdapter() {
	// @Override
	// public void windowClosing(final java.awt.event.WindowEvent e) {
	// exit();
	// }
	//
	// @Override
	// public void windowClosed(final java.awt.event.WindowEvent e) {
	// exit();
	// }
	// });
	// }
	/**
	 * This method initializes jContentPane
	 * 
	 * @return JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();

			final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new GridBagLayout());
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
			final JButton b3 = new JButton("MathML");
			b3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(final java.awt.event.ActionEvent e) {
					final String cmd = jInputArea.getText();
					jInputArea.setText("");
					if (cmd.length() > 0) {
						createMathML(cmd);
					}
				}
			});
			buttonsPanel.add(b3);
			bPanel.add(buttonsPanel);

			jContentPane.add(getJScrollInputPane(), gridBagConstraints1);
			jContentPane.add(bPanel, gridBagConstraints2);
			jContentPane.add(getJScrollOutputPane(), gridBagConstraints3);

		}
		return jContentPane;
	}

	private class Colors {
		final static int OUTPUT = 0;

		final static int NUM1 = 1;

		final static int NUM2 = 2;

		final static int COMMENT = 3;

		final static int OPERATOR = 4;

		final static int BRACKET = 5;

		final static int STRING = 6;

		final static int ERROR = 7;
	}

	// Listener methods

	public void changedUpdate(DocumentEvent ev) {
	}

	public void removeUpdate(DocumentEvent ev) {
	}

	public void insertUpdate(DocumentEvent ev) {
		if (ev.getLength() != 1) {
			return;
		}

		int pos = ev.getOffset();
		String content = null;
		try {
			content = jInputArea.getText(0, pos + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		// Find where the word starts
		int w;
		for (w = pos; w >= 0; w--) {
			if (!Character.isLetter(content.charAt(w))) {
				break;
			}
		}
		if (pos - w < 2) {
			// Too few chars
			return;
		}

		String prefix = content.substring(w + 1).toLowerCase();
		int n = Collections.binarySearch(fWords, prefix);
		if (n < 0 && -n <= fWords.size()) {
			String match = fWords.get(-n - 1);
			if (match.startsWith(prefix)) {
				// A completion is found
				String completion = match.substring(pos - w);
				// We cannot modify Document from within notification,
				// so we submit a task that does the change later
				SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1, w + 1, fReplaceWords.get(-n - 1)));
			}
		} else {
			// Nothing found
			mode = Mode.INSERT;
		}
	}

	private class CompletionTask implements Runnable {
		String completion;
		int position;
		int w;
		String replacement;

		CompletionTask(String completion, int position, int w, String replacement) {
			this.completion = completion;
			this.position = position;
			this.w = w;
			this.replacement = replacement.substring(0, position - w);
		}

		public void run() {
			jInputArea.insert(completion, position);
			jInputArea.setCaretPosition(position + completion.length());
			jInputArea.moveCaretPosition(position);
			fW = w;
			fReplacement = replacement;
			mode = Mode.COMPLETION;
		}
	}

	private class CommitAction extends AbstractAction {
		public void actionPerformed(ActionEvent ev) {
			if (mode == Mode.COMPLETION) {
				int pos = jInputArea.getSelectionEnd();
				jInputArea.replaceRange(fReplacement, fW, fW + fReplacement.length());
				// jInputField.insert(" ", pos);
				try {
					String endChar = jInputArea.getText(pos - 1, 1);
					if ("]".equals(endChar)) {
						jInputArea.setCaretPosition(pos - 1);
					} else {
						jInputArea.setCaretPosition(pos);
					}
				} catch (BadLocationException e) {
					jInputArea.setCaretPosition(pos);
				}
				mode = Mode.INSERT;
			} else {
				jInputArea.replaceSelection("\n");
			}
		}
	}

}
