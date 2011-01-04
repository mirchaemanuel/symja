package org.matheclipse.symja;

import java.awt.Color;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 * JTextPane with special support for Images
 */
public class OutputTextPane extends JTextPane {
	protected StyledDocument jOutputDoc;

	private final static String BRACKET_CHARACTERS = "[](){}";

	private final static String OPERATOR_CHARACTERS = "~+*;,.#'-:<>|&/=!^@";

	private final static String NUMERIC_BREAK_CHARCTERS = OPERATOR_CHARACTERS + BRACKET_CHARACTERS;

	public final static String NAMED_IMAGE_STYLE = "NamedImage";
	
	public final static String NAMED_COMPONENT_STYLE = "NamedComponent";

	private final static SimpleAttributeSet OUTPUT_ATTR, ERROR_ATTR, OUTPUT_OPERATOR_ATTR, OUTPUT_STRING_ATTR, OUTPUT_NUM_ATTR,
			OUTPUT_NUM2_ATTR, OUTPUT_BRACKET_ATTR, OUTPUT_COMMENT_ATTR;

	private final static Color cColor[]; /* selected color */

	private final static Color cColorDefault[] /* default colors */= { Color.BLUE, Color.BLACK, new Color(100, 100, 255),
			Color.GRAY, new Color(255, 100, 0), new Color(100, 0, 50), new Color(50, 150, 0), Color.RED };

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 6324491080121702039L;

	static {
		cColor = new Color[8];
		cColor[Colors.OUTPUT] = new Color(cColorDefault[Colors.OUTPUT].getRGB());
		cColor[Colors.ERROR] = new Color(cColorDefault[Colors.ERROR].getRGB());
		cColor[Colors.OPERATOR] = new Color(cColorDefault[Colors.OPERATOR].getRGB());
		cColor[Colors.STRING] = new Color(cColorDefault[Colors.STRING].getRGB());
		cColor[Colors.NUM1] = new Color(cColorDefault[Colors.NUM1].getRGB());
		cColor[Colors.NUM2] = new Color(cColorDefault[Colors.NUM2].getRGB());
		cColor[Colors.BRACKET] = new Color(cColorDefault[Colors.BRACKET].getRGB());
		cColor[Colors.COMMENT] = new Color(cColorDefault[Colors.COMMENT].getRGB());
		// error
		ERROR_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(ERROR_ATTR, cColor[Colors.ERROR]);
		// normal output
		OUTPUT_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(OUTPUT_ATTR, cColor[Colors.OUTPUT]);
		// operator output
		OUTPUT_OPERATOR_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(OUTPUT_OPERATOR_ATTR, cColor[Colors.OPERATOR]);
		// string output
		OUTPUT_STRING_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(OUTPUT_STRING_ATTR, cColor[Colors.STRING]);
		// number 1 output
		OUTPUT_NUM_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(OUTPUT_NUM_ATTR, cColor[Colors.NUM1]);
		// number 2 output
		OUTPUT_NUM2_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(OUTPUT_NUM2_ATTR, cColor[Colors.NUM2]);
		// bracket output
		OUTPUT_BRACKET_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(OUTPUT_BRACKET_ATTR, cColor[Colors.BRACKET]);
		// comment output
		OUTPUT_COMMENT_ATTR = new SimpleAttributeSet();
		StyleConstants.setForeground(OUTPUT_COMMENT_ATTR, cColor[Colors.STRING]);

	}

	public OutputTextPane(StyledDocument doc) {
		super(doc);
		jOutputDoc = doc;
		jOutputDoc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
	}

	public OutputTextPane() {
		this(new DefaultStyledDocument());
	}

	public void addImage(Image image) {
		try {
			StyledDocument doc = (StyledDocument) this.getDocument();
			Style style = doc.addStyle(NAMED_IMAGE_STYLE, null);
			StyleConstants.setIcon(style, new ImageIcon(image));
			doc.insertString(doc.getLength(), " ", style);
		} catch (BadLocationException ex) {
		}
	}

	public void addImage(Image image, int location) {
		addImage(image, location, true);
	}

	public void addImage(Image image, int location, boolean addNewline) {
		try {
			StyledDocument doc = (StyledDocument) this.getDocument();
			Style style = doc.addStyle(NAMED_IMAGE_STYLE, null);
			StyleConstants.setIcon(style, new ImageIcon(image));
			doc.insertString(location, " ", style);
			if (addNewline)
				doc.insertString(location + 1, "\n", style);
		} catch (BadLocationException ex) {
		}
	}

	public void addComponent(JComponent component) {
		try {
			StyledDocument doc = (StyledDocument) this.getDocument();
			Style style = doc.addStyle(NAMED_COMPONENT_STYLE, null);
			StyleConstants.setComponent(style, component);
			doc.insertString(doc.getLength(), " ", style);
		} catch (BadLocationException ex) {
		}
	}

	public void addComponent(JComponent component, int location, boolean addNewline) {
		try {
			StyledDocument doc = (StyledDocument) this.getDocument();
			Style style = doc.addStyle(NAMED_COMPONENT_STYLE, null);
			StyleConstants.setComponent(style, component);
			doc.insertString(location, " ", style);
			if (addNewline) {
				doc.insertString(location + 1, "\n\n", style);
			}
		} catch (BadLocationException ex) {
		}
	}

	public void addIcon(Icon icon, int location, boolean addNewline) {
		try {
			StyledDocument doc = (StyledDocument) this.getDocument();
			Style style = doc.addStyle(NAMED_COMPONENT_STYLE, null);
			StyleConstants.setIcon(style, icon);
			doc.insertString(location, " ", style);
			if (addNewline) {
				doc.insertString(location + 1, "\n", null);
			}
		} catch (BadLocationException ex) {
		}
	}
	
	public void addImage0(Image image, boolean addNewline) {
		addImage(image, 0, true);
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
				 
				}
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx, OUTPUT_COMMENT_ATTR, true);
			}
			// check for operators
			startIdx = idx;
			 
			while ((OPERATOR_CHARACTERS.indexOf(c) != -1) && (++idx < s.length())) {
				c = s.charAt(idx);
			}
			 
			if (idx != startIdx) {
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx, OUTPUT_OPERATOR_ATTR, true);
			}
			// check for brackets (no own state, since there's usually only one
			// bracket in a row)
			if (BRACKET_CHARACTERS.indexOf(c) != -1) {
				jOutputDoc.setCharacterAttributes(offset + idx, 1, OUTPUT_BRACKET_ATTR, true);
				continue;
			}
			// check for strings
			if (c == '"') {
				startIdx = idx;
				while ((++idx < s.length()) && ((c = s.charAt(idx)) != '"')) {
			 
				}
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx + 1, OUTPUT_STRING_ATTR, true);
			}
			// check for numbers
			if (isNumeric(c)) {
				int len, step, point = -1;
				boolean color1 = true;
				startIdx = idx;
				// search for break character
				while ((++idx < s.length()) && (NUMERIC_BREAK_CHARCTERS.indexOf(c = s.charAt(idx)) == -1)) {
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

				if (point != -1) {
					int step2 = step;
					// special case: decimal point
					// first color characters after the point from left to right
					for (int pos = point + 1; pos < len; pos += step2) {
						if (pos + step2 >= len) {
							step2 = len - pos; // don't color outside number
						}
						jOutputDoc.setCharacterAttributes(offset + startIdx + pos, step2, color1 ? OUTPUT_NUM_ATTR : OUTPUT_NUM2_ATTR, true);
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
					jOutputDoc
							.setCharacterAttributes(offset + startIdx + pos - step, step, color1 ? OUTPUT_NUM_ATTR : OUTPUT_NUM2_ATTR, true);
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
			jOutputDoc.insertString(0, s, OUTPUT_ATTR);
			// now start the coloring
			colorOutput(s, 0);
			setCaretPosition(0);// jOutputDoc.getLength());
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
			jOutputDoc.insertString(0, s, OUTPUT_ATTR);
			setCaretPosition(0);// jOutputDoc.getLength());
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
			jOutputDoc.insertString(0, s + "\n", ERROR_ATTR);
			setCaretPosition(0);// jOutputDoc.getLength());
		} catch (final BadLocationException ble) {
			System.out.println("Couldn't write to Output Pane");
		}
	}
}
