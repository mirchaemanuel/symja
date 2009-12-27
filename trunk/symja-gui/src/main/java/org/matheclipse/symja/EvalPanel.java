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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import net.sourceforge.jeuclid.swing.JMathComponent;

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

public class EvalPanel extends JPanel implements DocumentListener {
  private static final String COMMIT_ACTION = "commit";
  private static final int FONT_SIZE_TEXT = 12;
  private static final int FONT_SIZE_MATHML = 24;

  private static enum Mode {
    INSERT, COMPLETION
  };

  private String fReplacement;
  private int fW;

  private Mode mode = Mode.INSERT;
  private final List<String> fWords = new ArrayList<String>(2048);
  private final List<String> fReplaceWords = new ArrayList<String>(2048);

  private JTextArea jInputArea = null;

  private JScrollPane jScrollInputPane = null;

  private JScrollPane jScrollOutputPane = null;

  private OutputTextPane jOutputPane = null;

  /* custom created attributes */
  final static long serialVersionUID = 0x000000001;

  private final static String versionStr = "Keyboard shortcuts:\n"
      + "  Ctrl+ENTER  - for symbolic evaluation\n"
      + "  Page up     - previous input\n" + "  Page down   - next input\n"
      + "Program arguments:\n"
      + "  -f or -file <filename>    - use filename as Editor input script\n"
      + "  -d or -default <filename> - use filename for system startup rules\n";

  private final String commandHistory[] = new String[20];

  private int commandHistoryStoreIndex = 0;

  private int commandHistoryReadIndex = 0;

  private Component popupSource;

  public static EvalEngine EVAL_ENGINE;

  final JCheckBox fPrettyPrintStyle = new JCheckBox("Pretty Print Output?");

  public static TimeConstrainedEvaluator EVAL;

  private InitThread fInitThread = null;

  // private InitJEuclidThread fInitJEuclidThread = null;

  /**
   * If a string is on the system clipboard, this method returns it; otherwise
   * it returns null.
   */
  private static String getClipboard() {
    final Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard()
        .getContents(null);

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
        final PrintStream pout = new PrintStream(new WriterOutputStream(
            printBuffer));

        EVAL_ENGINE.setOutPrintStream(pout);

        final StringBufferWriter buf0 = new StringBufferWriter();

        final IExpr expr = EVAL.constrainedEval(buf0, command);
        // eval(buf0, command);

        if (printBuffer.getBuffer().length() > 0) {
          // print error messages ...
          jOutputPane.printOut(printBuffer.toString() + "\n\n");
        }

        if (buf0.getBuffer().length() > 0 && fPrettyPrintStyle.isSelected()) {

          final StringBufferWriter buf1 = new StringBufferWriter();
          final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE,
              false);
          try {
            if (expr != null) {
              mathUtil.toMathML(expr, buf1);

              JMathComponent component = new JMathComponent();
              component.setFontSize(FONT_SIZE_MATHML);
              component.setContent(buf1.toString());
              setBusy(false);
              jOutputPane.addComponent(component, 0, true);
            }
          } catch (final Exception e) {
            e.printStackTrace();
          }
        } else {
          String result = buf0.toString();
          jOutputPane.printOutColored("Out[" + commandHistoryStoreIndex + "]="
              + result + "\n");
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

    protected double[][] eval(final StringBufferWriter buf, final String evalStr)
        throws Exception {
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
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      // jOutputPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      // jInputArea.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    } else {
      jInputArea.setEditable(true);
      // jMenuItemBreak.setEnabled(false);
      // jPopupMenuItemBreak.setEnabled(false);
      this.setCursor(Cursor.getDefaultCursor());
      // jOutputPane.setCursor(Cursor.getDefaultCursor());
      // jInputArea.setCursor(Cursor.getDefaultCursor());
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
        final boolean canPaste = ((JTextComponent) popupSource).isEditable()
            && (getClipboard() != null);
        final boolean canCopy = ((JTextComponent) popupSource)
            .getSelectedText() != null;
        // jPopupMenuItemPaste.setEnabled(canPaste);
        // jPopupMenuItemCopy.setEnabled(canCopy);
        // jPopupMenu.show(popupSource, e.getX(), e.getY());
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
      F.initSymbols(fDefaultSystemRulesFilename);
      EVAL_ENGINE = new EvalEngine();
      EVAL = new TimeConstrainedEvaluator(EVAL_ENGINE, false, 360000);
      new CompletionLists(fWords, fReplaceWords);
    }
  }

  public class InitJEuclidThread extends Thread {

    public InitJEuclidThread() {
    }

    @Override
    public void run() {
      JMathComponent component = new JMathComponent();
      component.setFontSize(FONT_SIZE_MATHML);
      component.setContent("<math><mo>.</mo></math>");
      jOutputPane.addComponent(component);
    }
  }

  private JScrollPane getJScrollInputPane() {
    if (jScrollInputPane == null) {
      jScrollInputPane = new JScrollPane();
      jScrollInputPane.setViewportView(getJInputArea());
    }
    return jScrollInputPane;
  }

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
          buff.append("\n");
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
          if (e.getKeyCode() == KeyEvent.VK_ENTER
              && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
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
    jOutputPane.printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd
        + "\n\n");
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
    jOutputPane.printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd
        + "\n\n");
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
    try {
      setBusy(true);
      final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE, false);
      final StringBufferWriter buf = new StringBufferWriter();

      mathUtil.toMathML(cmd, buf);
      jOutputPane.printOutColored("MathML:\n" + buf.toString() + "\n\n");
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

  protected void createMathMLComponent(final String cmd) {
    if (fInitThread != null) {
      try {
        fInitThread.join();
      } catch (InterruptedException e) {
      }
    }
    try {
      setBusy(true);
      final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE, false);
      final StringBufferWriter buf = new StringBufferWriter();

      mathUtil.toMathML(cmd, buf);

      try {
        JMathComponent component = new JMathComponent();
        component.setFontSize(FONT_SIZE_MATHML);
        component.setContent(buf.toString());
        setBusy(false);
        jOutputPane.addComponent(component, 0, true);
      } catch (final Exception e) {
        e.printStackTrace();
        jOutputPane.printOut("MathML:\n" + buf.toString() + "\n\n");
      }
    } catch (final Exception e) {
      e.printStackTrace();
      String mess = e.getMessage();
      if (mess == null) {
        jOutputPane.printErr(e.getClass().getName());
      } else {
        jOutputPane.printErr(e.getMessage());
      }
    } finally {
      setBusy(false);
    }
  }

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

    buttonsPanel.add(fPrettyPrintStyle);

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
    final JButton b4 = new JButton("Show MathML");
    b4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(final java.awt.event.ActionEvent e) {
        final String cmd = jInputArea.getText();
        jInputArea.setText("");
        if (cmd.length() > 0) {
          createMathMLComponent(cmd);
        }
      }
    });
    buttonsPanel.add(b4);

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
    final Point p = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getCenterPoint();
    p.x -= 300 / 2;
    p.y -= 200 / 2;
    posX = p.x > 0 ? p.x : 0;
    posY = p.y > 0 ? p.y : 0;
    this.setLocation(posX, posY);
    validate(); // force redraw
    setVisible(true);

    final Font f = new Font("Monospaced", Font.PLAIN, FONT_SIZE_TEXT);
    jOutputPane.setFont(f);
    jInputArea.setFont(f);

    // height = jInputArea.getGraphics().getFontMetrics().getHeight();
    jInputArea.setBounds(jInputArea.getBounds().x, getHeight() - height,
        jInputArea.getWidth(), height);
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
        SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1,
            w + 1, fReplaceWords.get(-n - 1)));
      }
    } else {
      // Nothing found
      mode = Mode.INSERT;
    }
  }

  private class CompletionTask implements Runnable {
    final String completion;
    final int position;
    final int w;
    final String replacement;

    CompletionTask(String completion, int position, int w, String replacement) {
      this.completion = completion;
      this.position = position;
      this.w = w;
      this.replacement = replacement;
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

  /**
   * Set a new text in the input textarea
   * 
   * @param text
   */
  public void setInputText(String text) {
    jInputArea.setText(text);
  }

}
