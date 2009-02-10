package org.matheclipse.symja.plot;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class ParametricPlotWindow extends AbstractPlotWindow {
	protected SpinnerNumberModel tMin;
	protected SpinnerNumberModel tMax;

	public ParametricPlotWindow(JFrame parent) {
		super(parent);
	}

	protected ParametricPlotter createPlot() {
		return ParametricPlotter.getParametricPlotter();
	}

	protected JPanel createMinMaxControls() {
		JPanel controls = super.createMinMaxControls();
		tMin = new SpinnerNumberModel(0.0, -999.0, 999.0, 1.0);
		tMax = new SpinnerNumberModel(0.0, -999.0, 999.0, 1.0);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 2, 2);

		c.gridwidth = 1;
		c.weightx = 0.0;
		controls.add(new JLabel("T min: "));

		c.weightx = 1.0;
		controls.add(new JSpinner(tMin), c);

		c.weightx = 0.0;
		controls.add(new JLabel("T max: "));

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		controls.add(new JSpinner(tMax), c);

		tMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double value = tMin.getNumber().doubleValue();
				if (tMax.getNumber().doubleValue() <= value)
					tMax.setValue(value + 1);
				((ParametricPlotter)plot).setTMin(value);
			}
		});
		tMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double value = tMax.getNumber().doubleValue();
				if (tMin.getNumber().doubleValue() >= value)
					tMin.setValue(value - 1);
				((ParametricPlotter)plot).setTMax(value);
			}
		});

		return controls;
	}


	public void addField() {
		addField("x(t) = ");
		addField("y(t) = ");
	}
}
