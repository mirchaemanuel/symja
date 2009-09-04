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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

/** Responsible for displaying the preferences dialog and handling
  * preference changes.
  */
class PreferencesListener implements ActionListener {
    /** The parent container.
      */
	protected RootPaneContainer parent;

    /** The dialog launched by this listener.
      */
	protected JDialog dialog;

    /** Creates a preferences listener that displays a dialog whose
      * parent is assigned to the passed parent, that can
      * switch list between the red, blue, and gray renderers, and
      * adjust the width of its parent's borders.
      */
    public PreferencesListener(JList list,
//                               ListCellRenderer redRenderer,
//                               ListCellRenderer blueRenderer,
//                               ListCellRenderer
//                               grayRenderer,
                               JApplet parent) {
		JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
//        Component colors = createColorPanel(list,
//                                            redRenderer,
//                                            blueRenderer,
//                                            grayRenderer);
        Component panel2D = create2DPanel();
        Component panel3D;

		this.parent = parent;

        panel.setLayout(layout);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.weightx = 1.0;
//        layout.setConstraints(colors, constraints);
//        panel.add(colors);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(panel2D, constraints);
        panel.add(panel2D);

        /*try {
            Class.forName("javax.media.j3d.VirtualUniverse");
            panel3D = create3DPanel();
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.weightx = 1;
            constraints.weighty = 1;
            layout.setConstraints(panel3D, constraints);
            panel.add(panel3D);
        } catch (ClassNotFoundException e) {
            System.err.println("Java3D not found");
        }*/

        dialog = new JOptionPane(panel,
                                JOptionPane.INFORMATION_MESSAGE,
                                JOptionPane.DEFAULT_OPTION)
                            .createDialog(parent, "Preferences");
	}

    /** Sets up radio buttons to select color schemes and registers
      * listeners with them. Wraps them in a JComponent with a
      * labeled border and returns the JComponent.
      */
//    protected static JComponent createColorPanel(JList list,
//                                     ListCellRenderer redRenderer,
//                                     ListCellRenderer blueRenderer,
//                                     ListCellRenderer grayRenderer
//                                     ) {
//        Box buttonPanel = new Box(BoxLayout.X_AXIS);
//        JRadioButton   redButton = new JRadioButton("Warm");
//        JRadioButton  blueButton = new JRadioButton("Cool");
//        JRadioButton  grayButton = new JRadioButton("Silver");
//        ButtonGroup radioButtons = new ButtonGroup();
//
//		redButton.setSelected(true);
//		radioButtons.add(redButton);
//		radioButtons.add(blueButton);
//		radioButtons.add(grayButton);
//		buttonPanel.add(redButton);
//		buttonPanel.add(blueButton);
//		buttonPanel.add(grayButton);
//        redButton.addChangeListener(new ColorChangeListener(list,
//                                                       redRenderer));
//        blueButton.addChangeListener(new ColorChangeListener(list,
//                                                      blueRenderer));
//        grayButton.addChangeListener(new ColorChangeListener(list,
//                                                      grayRenderer));
//        buttonPanel.setBorder(BorderFactory.createTitledBorder(
//                                  BorderFactory.createEtchedBorder(),
//                                  "Colors"));
//
//		return buttonPanel;
//	}

    /** Sets up the panel with parametric plotting controls.
      */
    protected static JComponent create2DPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Resolution: ");
        JSpinner resolution = new JSpinner(
                          new SpinnerNumberModel(1000, 2, 10000, 1));
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        panel.setBorder(BorderFactory.createTitledBorder(
                                  BorderFactory.createEtchedBorder(),
                                  "2D Plotting"));
        panel.setLayout(layout);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;

        layout.setConstraints(label, constraints);
        panel.add(label);
        constraints.weightx = 1;
        resolution.addChangeListener(
                                 new Resolution2DListener());
        layout.setConstraints(resolution, constraints);
        panel.add(resolution);

        

        return panel;
    }

    /** Sets up the panel with the 3D graphing controls.
      */
    /*protected static JComponent create3DPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JCheckBox transparency = new JCheckBox("Transparency");
        JLabel resolutionLabel = new JLabel(
                          "Resolution (change at your own risk!): ");
        JSpinner resolution = new JSpinner(
                          new SpinnerNumberModel(100, 2, 500, 1));

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createTitledBorder(
                                  BorderFactory.createEtchedBorder(),
                                  "3D Plotting"));

        transparency.addChangeListener(new TransparencyListener());
        layout.setConstraints(transparency, constraints);
        panel.add(transparency);

        constraints.weightx = 0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        Plotter3DFactory.setResolution(
                        ((Integer)(resolution.getModel().getValue()))
                                                        .intValue());
        resolution.addChangeListener(new ResolutionListener());
        layout.setConstraints(resolutionLabel, constraints);
        panel.add(resolutionLabel);
        constraints.weightx = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(resolution, constraints);
        panel.add(resolution);

        JLabel versionHeader = new JLabel("Your system is running:");
        layout.setConstraints(versionHeader, constraints);
        panel.add(versionHeader);
        Map j3dProperties = VirtualUniverse.getProperties();
        JLabel version = new JLabel(j3dProperties.get("j3d.vendor")
                             + " Java3D version "
                             + j3dProperties.get("j3d.version"));
        layout.setConstraints(version, constraints);
        panel.add(version);
        JLabel specification = new JLabel(
                 j3dProperties.get("j3d.specification.vendor")
                 + " Java3D specification "
                 + j3dProperties.get( "j3d.specification.version"));
        layout.setConstraints(specification, constraints);
        panel.add(specification);

        return panel;
    }*/

	public void actionPerformed(ActionEvent e) {
		dialog.show();
	}
}

/** When this listener's component is selected, this listener applies
  * its renderer to its list. It does nothing when its component is
  * deselected.
  */
class ColorChangeListener implements ChangeListener {
    /** The list to change the color of.
      */
	JList list;

    /** The renderer to change the list to when called.
      */
	ListCellRenderer renderer;

    /** Creates a listener that applies the passed renderer to the
      * passed list when called.
      */
    public ColorChangeListener(JList list,
                               ListCellRenderer renderer) {
        this.list = list;
        this.renderer = renderer;
    }

    /** Applies the renderer is the source has been selected.
      */
    public void stateChanged(ChangeEvent e) {
        if (((AbstractButton)e.getSource()).isSelected())
            list.setCellRenderer(renderer);
    }
}

/** Changes the transparency of subsequent 3D plots.
  */
class TransparencyListener implements ChangeListener {
    /** Casts its component to an AbstractButton, retrieves whether
      * it is selected, and enables the transparency accordingly.
      */
    public void stateChanged(ChangeEvent e) {
        /*Plotter3DFactory.setTransparent(
                       ((AbstractButton)e.getSource()).isSelected());*/
    }
}

/** Changes the resolution of 3D graphs.
  */
class ResolutionListener implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
        /*Plotter3DFactory.setResolution(((Integer)((JSpinner)e.getSource()).getValue()).intValue());*/
    }
}

/** Changes the resolution of parametric plots.
  */
class Resolution2DListener implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
        //AbstractPlotter2D.setResolution(((Integer)((JSpinner)e.getSource()).getValue()).intValue());
    }
}
