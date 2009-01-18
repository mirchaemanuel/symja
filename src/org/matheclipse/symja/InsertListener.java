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
import javax.swing.tree.*;

/** Displays a list of available functions and constants, and allows
  * the user to insert them into the working command. This listener
  * is in its own file because there are a LOT of built-in functions
  * and constants. Replaces its parent's content pane with a JTree,
  * and when a component from the JTree is selected, the parent's
  * content pane is restored. This is so that, in the case of an
  * applet, the web browser can't steal the keyboard focus when a
  * dialog is created and dismissed. Warning: Has a tendency to
  * restore whatever contentPane the applet has when the constructer
  * is called. Beware of later switching the content pane outside
  * this class.
  */
public class InsertListener implements ActionListener {
    /** The component to temporarily replace the parent's content
      * pane.
      */
    JComponent container;

    /** The parent component.
      */
    Container parent;

    /** The tree of built-in functions and constants.
      */
    JTree tree;

    /** The layout used by the parent. Used to switch to and from the
      * different displays.
      */
    CardLayout layout;

    /** Sets up an InsertListener that inserts text into area, and
      * displays its content inside applet.
      */
    public InsertListener(JTextField area,
                          RootPaneContainer applet) {
        parent = applet.getContentPane();
        layout = (CardLayout)parent.getLayout();
        TreeListener listener = new TreeListener(parent,
                                                 area,
                                                 layout);

        tree = new JTree(createTree());
        tree.getSelectionModel().setSelectionMode(
                           TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(listener);
        tree.addKeyListener(listener);
        container = new JScrollPane(tree);
        parent.add(container, "insert");
        layout.first(parent);
    }

    /** Displays a JTree and inserts the selected value from the tree
      * if "OK" is clicked. The first selected value is inserted if
      * it is a leaf (path length 3). If the selected value is a
      * function (the second entry in the path has a text value of
      * "Functions"), and opening parenthesis is also inserted.
      */
	public void actionPerformed(ActionEvent e) {
        layout.show(parent, "insert");
        tree.requestFocus();
	}


    /** This function is a little long.
      * It initalizes and populates the tree. It does not conform to
      * any real style standards because it is just nasty.
      */
	protected static MutableTreeNode createTree() {
		MutableTreeNode root = new DefaultMutableTreeNode("Built-ins");
		MutableTreeNode constants = new DefaultMutableTreeNode("Constants");
		MutableTreeNode functions = new DefaultMutableTreeNode("Functions");

		constants.insert(new DefaultMutableTreeNode("Catalan"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Constant"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Degree"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("E"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("EulerGamma"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("False"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Flat"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("HoldAll"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("HoldFirst"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("HoldRest"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("I"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Infinity"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Listable"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Null"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("OneIdentity"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Orderless"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Pi"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("True"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Yotta"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Zetta"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Exa"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Peta"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Tera"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Giga"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Mega"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Myria"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Kilo"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Hecto"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Deca"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Deci"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Centi"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Milli"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Micro"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Nano"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Pico"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Femto"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Atto"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Zopto"), constants.getChildCount());
        constants.insert(new DefaultMutableTreeNode("Yocto"), constants.getChildCount());




        functions.insert(new DefaultMutableTreeNode("$Poly"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ACos"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ACosh"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ACot"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ACoth"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ASin"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ASinh"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ATan"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ATanh"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Abs"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Add"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("AddTo"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("AiryAi"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("And"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Append"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("AppendTo"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Apply"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ArithmeticMean"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Array"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Atto"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("BairstowP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Beta"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Binomial"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Bisection"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Block"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Break"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Catalan"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("CatalanNumber"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Catch"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Ceil"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Centi"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Check"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Clear"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("CoefficientP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Conjugate"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ConstrainedMax"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Continue"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ConvertTemperature"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Cos"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Cosh"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Cot"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Coth"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("CrossProduct"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Csc"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("D"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("DP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Deca"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Deci"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Decrement"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Definition"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Degree"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("DegreeP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Delete"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Denominator"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Depth"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Det"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("DiagonalMatrix"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Dimensions"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Distribution"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Div"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("DivP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Divide"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("DivideBy"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Do"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Dot"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("E"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("EigenvaluesHermitian"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("EigenvaluesSymmetric"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Equal"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Erf"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("EulerGamma"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("EvalP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Exa"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Exec"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Exp"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Expand"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ExpandAll"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("EzMath"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("FFT"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("FactorInteger"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Factorial"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Factorial2"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Femto"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Fibonacci"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("FindStr"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("First"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Fit"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("FixedPoint"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Floor"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("For"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("FullForm"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("GCD"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("GCDP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Gamma"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("GeometricMean"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("GetFileStr"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Giga"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Greater"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("GreaterEqual"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("HarmonicMean"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Head"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Hecto"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Help"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("HermitianAdjoint"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Hilbert"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("I"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IdentityMatrix"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("If"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Im"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Increment"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Insert"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Int"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Inverse"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("InverseFFT"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("InverseMod"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsAtom"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsComplex"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsFraction"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsFunction"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsHermitian"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsInteger"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsMatrix"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsMember"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsNegative"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsNoMember"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsNonNegative"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsNumber"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsPositive"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsProbablePrime"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsRational"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsReal"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsSame"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsTrue"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsUnitary"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IsVector"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("IterationLimit"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Join"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("KPartitions"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("KSubsets"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Kilo"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("LCMP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Lambda"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Last"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Length"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("LengthStr"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Less"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("LessEqual"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Level"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("LoadClassLibrary"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Log"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("MML"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("MMLContent"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Map"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("MatrixPow"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Max"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Mega"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Micro"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Milli"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Min"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Mod"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ModP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Multiply"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("MultiplyCoefficientsP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("MultiplyP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Myria"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("N"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("NInt"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("NLinearSolve"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("NTermsP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Nano"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Neg"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Nest"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("New"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Not"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("NthCoefficientP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("NthMonomialP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("NthTermP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("NumberPartitions"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Numerator"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Or"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ParametricPlot"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Part"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Partition"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Permutations"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Peta"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Pi"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Pico"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Plot"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Plot3D"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Poly"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("PolynomialList"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Pow"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("PowMod"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("PreDecrement"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("PreIncrement"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Prepend"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("PrependTo"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Print"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Product"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Random"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("RandomInteger"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Range"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Rationalize"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Re"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ReadObject"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("RecursionLimit"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ReplaceAll"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ReplaceStr"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Rest"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Return"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Rule"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("RuleDelayed"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Sec"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Set"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("SetAttributes"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("SetDelayed"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Sign"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Signum"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("SignumCmp"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Sin"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Sinh"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("SolveP"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Sort"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Spur"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Sqrt"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("StandardDeviation"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Statement"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Subtract"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("SubtractFrom"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Sum"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Switch"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Table"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Tan"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Tanh"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Taylor"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Tera"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Throw"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("TimeConstrained"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("TimesBy"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Timing"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ToBase"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ToJava"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ToLowerCaseStr"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ToObject"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("ToUpperCaseStr"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Together"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Trace"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Transpose"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Trunc"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Unequal"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Union"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("UpSet"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("UpSetDelayed"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Vandermonde"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Variables"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Variance"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("While"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("WriteObject"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Xor"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Yocto"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Yotta"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Zetta"), functions.getChildCount());
        functions.insert(new DefaultMutableTreeNode("Zopto"), functions.getChildCount());

		root.insert(constants, 0);
		root.insert(functions, 1);
		return root;
	}
}

/** Responsible for restoring the parent's content pane and inserts
  * the selected values when the user double-clicks or presses enter
  * (the parent is restored without a value being entered if escape
  * is pressed).
  */
class TreeListener extends MouseAdapter implements KeyListener {
    /** The parent container.
      */
    Container parent;

    /** The text field that gets the content inserted.
      */
    JTextField out;

    /** The parent's layout.
      */
    CardLayout layout;

    /** Creates a TreeListener with the passed parent, that replaces
      * the parent's content pane with the passed content, and
      * inserts the selected value into out.
      */
    public TreeListener(Container parent,
                        JTextField out,
                        CardLayout clayout) {
        this.parent = parent;
        this.out = out;
        layout = clayout;
    }

    /** If the event is a double-click, restores the content pane and
      * inserts the clicked value into the text field.
      */
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() >= 2) {
            TreePath path = ((JTree)e.getSource())
                            .getPathForLocation(e.getX(), e.getY());
            returnPath(path);
        }
    }

    /** If the key is ENTER, the content pane is restored and the
      * selected value is inserted. If the key is ESCAPE, the content
      * pane is restored, and no value is inserted.
      */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            returnPath(((JTree)e.getSource()).getSelectionPath());
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            restore();
    }

    /** Overrides keyPressed() in KeyListener.
      */
    public void keyPressed(KeyEvent e) {}

    /** Overrides keyTyped() in KeyListener.
      */
    public void keyTyped(KeyEvent e) {}

    /** Inserts the value into the text area.
      * This function should probably have a better name. (The idea
      * was that it "returns" the selected value.)
      */
    protected void returnPath(TreePath selection) {
        if (selection != null && selection.getPathCount() >= 3) {
            Object path[] = selection.getPath();

            out.replaceSelection(path[2].toString());
            if (path[1].toString().equals("Functions")) {
                out.replaceSelection("(");
            }
        }

        restore();
    }

    /** Restores the content pane and gives the text field the
      * focus.
      */
    protected void restore() {
        if (layout == null)
            throw new NullPointerException("layout is null");
        if (parent == null)
            throw new NullPointerException("parent is null");

        layout.first(parent);
        out.requestFocus();
    }
}
