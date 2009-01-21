package org.matheclipse.symja.plot;

import java.awt.*;
import javax.swing.*;

public class PlotWindow extends JDialog {
	private SpinnerNumberModel xMin = new SpinnerNumberModel(-1.0, -999.0, 999.0, 1.0);
	private SpinnerNumberModel xMax  = new SpinnerNumberModel(1.0, -999.0, 999.0, 1.0);

	public PlotWindow() {
		super();
		Container c = getContentPane();
		JPanel controls = createControls();
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JLabel("<Graph Would Go Here!>"), controls);

		c.add(split);
	}

	protected JComponent createFields() {
		JComponent container = new JPanel();
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(2, 2, 2, 2);
		container.add(new JTextField("Sin[x]"), c);
		container.add(new JTextField("Cos[x]"), c);
		c.fill = GridBagConstraints.NONE;
		JButton add = new JButton("Add Function");
		add.setEnabled(false);
		container.add(add, c);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		container.add(Box.createGlue(), c);
		return container;
	}

	protected JPanel createControls() {
		JPanel controls = new JPanel();
		controls.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 2, 2);

		c.weightx = 0.0;
		controls.add(new JLabel("X min: "), c);
		c.weightx = 1.0;
		controls.add(new JSpinner(xMin), c);
		c.weightx = 0.0;
		controls.add(new JLabel("X max: "), c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		controls.add(new JSpinner(xMax), c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		controls.add(new JScrollPane(createFields()), c);
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		JButton update = new JButton("Update");
		update.setEnabled(false);
		controls.add(update, c);
		
		return controls;
	}
}
