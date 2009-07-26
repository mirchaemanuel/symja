package org.matheclipse.symja;

import javax.swing.JMenuItem;

public class EvalExampleMenuItem extends JMenuItem {
	private static final long serialVersionUID = -8598960236908164571L;

	private final EvalPanel fEvalPanel;
	private final String fInputText;

	public EvalExampleMenuItem(String menuText, EvalPanel evalPanel, String inputText) {
		fEvalPanel = evalPanel;
		fInputText = inputText;
		setText(menuText);
		addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// insert example math command into input textarea
				fEvalPanel.setInputText(fInputText);
			}
		});
	}

}
