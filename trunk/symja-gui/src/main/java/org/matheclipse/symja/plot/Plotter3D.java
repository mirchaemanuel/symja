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

package org.matheclipse.symja.plot;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

import javax.swing.*;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.EventQueue;
import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Arrays;

import org.matheclipse.parser.client.eval.*;
/*import com.hartmath.expression.*;
import com.hartmath.mapping.*;
import com.hartmath.lib.*;*/

/** Responsible for plotting graphs in 3D. Called by
  * com.hartmath.loadable.EPlot3D.
  *
  * Note: "One per Plotter3D object" denotes an object that is
  * created with, and exists for the lifetime of, a Plotter3D object,
  * as opposed to objects that may be disposed of and reinstantiated
  * several times during the lifetime of the Plotter3D.
  */
public class Plotter3D extends Behavior implements Runnable {
    /** Staging area for plotted points. Allows positions to be
      * calculated on one thread, and polygons created on the Java3D
      * thread.
      */
    double values[][][];

    /** The colors associated with the graphs.
      */
    int color[];

    /** The upper z bound of the plot. All points above this should
      * be clipped. If not set explicitly by the user, it is set to
      * the highest calculated point.
      */
    double max;

    /** The lower z bound of the plot. All points below this should
      * be clipped. If not set explicitly by the user, it is set to
      * the lowest calculate point.
      */
    double min;

    /** The lower x bound of the plot. No points are calculated
      * beyond this limit.
      */
    double xMin;

    /** The upper x bound of the plot. No points are
      * calculated beyond this limit.
      */
    double xMax;

    /** The lower y bound of the plot. No points are calculated
      * beyond this limit.
      */
    double yMin;

    /** The upper y bound of the plot. No points are calculated
      * beyond this limit.
      */
    double yMax;

    /** The range of the plot along the x axis. This is the
      * difference of xMax and xMin.
      */
    double xRange;

    /** The range of the plot along the y axis. This is the
      * difference of yMax and yMin.
      */
    double yRange;

    /** The offset in the x direction of the grid marks needed to
      * make them line up right with the origin.
      */
    float xOffset;

    /** The offset in the y direction of the grid marks needed to
      * make them line up right with the origin.
      */
    float yOffset;

    /** The increments ("scale") of grid markings along the x axis.
      */
    float xIncrement;

    /** The increments ("scale") of grid marking along the y axis.
      */
    float yIncrement;

    /** The ID used to post events on the Java3D thread recognized by
      * this object.
      */
    final int ID = 0;

    /** The virtual universe this plot exists in. One is created per
      * Plotter3D object.
      */
    VirtualUniverse vu;

    /** The canvas the plot is displayed on.
      */
    Canvas3D canvas;

    /** The viewpoint of the camera in the virtual universe. One is
      * created per Plotter3D object.
      */
    ViewPlatform vp;

    /** The view of the universe. One is created per Plotter3D
      * object.
      */
    View view;

    /** The configuration associated with this object. One is created
      * per Plotter3D objects.
      */
    GraphicsConfiguration config;

    /** The locale of the graph inside the virtual universe. An
      * object is created every time a graph is displayed.
      */
    Locale locale;

    /** The 3D graph. Obviously, there is only one per Plotter3D
      * object. Multiple functions are represented as multiple
      * geometries of the shape.
      */
    Shape3D graph;

    /** The axes. Only one per Plotter3D object.
      */
    Shape3D axesShape;

    /** The group containing all objects in the virtual object. One
      * per Plotter3D object.
      */
    BranchGroup root;

    /** Contains all the objects that can be rotated by the user (the
      * plot and its axes). One per Plotter3D object.
      */
    TransformGroup rotateGroup;

    /** The arrow shape on the X axis. One per Plotter3D object.
      */
    Cone xArrow;

    /** Marks the maximum bound on the X axis. One per Plotter3D
      * object.
      */
    Cone xMaxMarker;

    /** Marks the minimum bound on the X axis. One per Plotter3D
      * object.
      */
    Cone xMinMarker;

    /** The X character shape. One per Plotter3D object.
      */
    OrientedShape3D xShape;

    /** Shows the value of the maximum bound along the X axis. One
      * per Plotter3D object.
      */
    OrientedShape3D xMaxLabel;

    /** Shows the value of the minimum bound along the X axis. One
      * per Plotter3D object.
      */
    OrientedShape3D xMinLabel;

    /** The translation of the X arrow and character. One per
      * Plotter3D object.
      */
    Transform3D xArrowTransform;

    /** The translation of the X max marker and label. One per
      * Plotter3D object.
      */
    Transform3D xMaxTransform;

    /** The translation of the X min marker and label. One per
      * Plotter3D object.
      */
    Transform3D xMinTransform;

    /** The group containing the X arrow and character. One per
      * Plotter3D object.
      */
    TransformGroup xArrowGroup;

    /** The group containing the X max marker and label. One per
      * Plotter3D object.
      */
    TransformGroup xMaxGroup;

    /** The group containing the X min marker and label. One per
      * Plotter3D object.
      */
    TransformGroup xMinGroup;

    /** The arrow shape on the Y axis. One per Plotter3D object.
      */
    Cone yArrow;

    /** Marks the maximum bound on the Y axis.
      */
    Cone yMaxMarker;

    /** Marks the minimum bound on the Y axis.
      */
    Cone yMinMarker;

    /** The shape of the Y character. One per Plotter3D object.
      */
    OrientedShape3D yShape;

    /** Shows the value of the maximum bound on the Y axis. One per
      * Plotter3D object.
      */
    OrientedShape3D yMaxLabel;

    /** Shows the value of the minimum bound on the Y axis. One per
      * Plotter3D object.
      */
    OrientedShape3D yMinLabel;

    /** The translation of the Y arrow and character.
      * One per Plotter3D object.
      */
    Transform3D yArrowTransform;

    /** The translation of the Y max marker and label. One per
      * Plotter3D object.
      */
    Transform3D yMaxTransform;

    /** The translation of the Y min marker and label. One per
      * Plotter3D object.
      */
    Transform3D yMinTransform;

    /** The group containing the Y arrow and character. One per
      * Plotter3D object.
      */
    TransformGroup yArrowGroup;

    /** The group containing the Y max marker and label. One per
      * Plotter3D object.
      */
    TransformGroup yMaxGroup;

    /** The group containing the Y min marker and label. One per
      * Plotter3D object.
      */
    TransformGroup yMinGroup;

    /** The arrow shape on the Z axis. One per Plotter3D object.
      */
    Cone zArrow;

    /** Marks the maximum bound on the Z axis.
      */
    Cone zMaxMarker;

    /** Marks the minimum bound on the Z axis.
      */
    Cone zMinMarker;

    /** The shape of the Z character. One per Plotter3D object.
      */
    OrientedShape3D zShape;

    /** Displays the value of the maximum bound along the Z axis. One
      * per Plotter3D object.
      */
    OrientedShape3D zMaxLabel;

    /** Displays the value of the minimum bound along the Z axis. One
      * per Plotter3D object.
      */
    OrientedShape3D zMinLabel;

    /** The translation of the Z arrow and character
      * shape. One per Plotter3D object.
      */
    Transform3D zArrowTransform;

    /** The translation of the Z max marker and label. One per
      * Plotter3D object.
      */
    Transform3D zMaxTransform;

    /** The translation of the Z min marker and label. One per
      * Plotter3D object.
      */
    Transform3D zMinTransform;

    /** The group containing the Z arrow and character. One per
      * Plotter3D object.
      */
    TransformGroup zArrowGroup;

    /** The group containing the Z max marker and label. One per
      * Plotter3D object.
      */
    TransformGroup zMaxGroup;

    /** The group containing the Z min marker and label. One per
      * Plotter3D object.
      */
    TransformGroup zMinGroup;

    /** The frame containing the plot's canvas. One is created per
      * Plotter3D object; for some reason, we have to empty the
      * content pane and repopulate it every time a graph is
      * displayed.
      */
    JFrame frame;

    /** The label on the X axis.
      */
    String x = "x";

    /** The label on the Y axis.
      */
    String y = "y";

    /** The label on the Z axis.
      */
    String z = "z";

    /** Evaluates the the function's values over a range, then
      * launches the plotting code. Evaluation takes place on the
      * Harmath thread; graphing takes place on the Java3D thread,
      * with some setup done on the AWT event thread.
      *
      * This method's primary function is to parse and validate the
      * input, and initialize the values for the plotting code. The
      * actual function evaluation and plotting is delegated
      * elsewhere.
      *
      * Throws an IllegalArgumentException if the input is malformed.
      */
    public void plot(String arg) {
        int resolution = Plotter3DFactory.getResolution();

	xMin = -1;
	xMax = 1;
	yMin = -1;
	yMax = 1;
        setupDependentRanges();

	values = new double[resolution + 1]
		[resolution + 1]
		[1];
	color = new int[1];
	DoubleEvaluator engine = new DoubleEvaluator();
	populateFirst(arg, engine);

	color[0] = Plotter3DFactory.RED_YELLOW;

        if (max <= min) {
            if (max < 0) {
                max = 0;
            }
            if (min > 0) {
                min = 0;
            }
            if (max <= min) {
                double med = (max + min) / 2;
                max = med + 1;
                min = med - 1;
            }
        }

        EventQueue.invokeLater(this);
    }

    /** Sets up the ranges of the dependent variables.
      */
    protected void setupDependentRanges() {
        xRange = xMax - xMin;
        if (xRange <= 0)
            throw new IllegalArgumentException("Illegal range over "
                                                     + x);
        yRange = yMax - yMin;
        if (yRange <= 0)
            throw new IllegalArgumentException("Illegal range over "
                                               + y);
    }

    /** Initializes min and max for the first time (they will later
      * be adjusted to include all output values so as to
      * automatically scale the graph, then, if the user explicitly
      * sets bounds, then those values will override the automatic
      * values). Populates the graph of the very first function.
      */
    protected void populateFirst(String expression, DoubleEvaluator engine) {
        try {
	    engine.defineVariable("x", new DoubleVariable(xMin));
	    engine.defineVariable("z", new DoubleVariable(yMin));
            max = min = engine.evaluate(expression);
        } catch (Exception e) {
            max = min = 0;
        }
        populate(expression, engine, 0);
    }


    /** Returns true if the value appears to be the name of a color,
      * false otherwise.
      */
    protected boolean isColor(String s) {
        return s.startsWith("blue")
            || s.endsWith("green")
            || s.startsWith("red")
            || s.endsWith("yellow")
            || s.startsWith("purple")
            || s.endsWith("pink");
    }

    /** Evaluates points on the function. Populates the values array
      * with function outputs.
      */
    protected void populate(String expression, DoubleEvaluator engine, int z) {
        int resolution = Plotter3DFactory.getResolution();

        for (int x = 0; x <= resolution; ++x) {
            for (int y = 0; y <= resolution; ++y) {
                try {
		    engine.defineVariable("x", new DoubleVariable(xMin + (x * xRange) / resolution));
		    engine.defineVariable("z", new DoubleVariable(yMin + (y * yRange) / resolution));
                    values[x][y][z] = engine.evaluate(expression);
                    if (values[x][y][z] > max)
                        max = values[x][y][z];
                    if (values[x][y][z] < min)
                        min = values[x][y][z];
                } catch (Exception e) {
                    values[x][y][z] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    /** Creates a new Plotter3D. Application uses getPlotter() to get
      * instances, instead of calling the constructor directly.
      */
    protected Plotter3D(boolean isTransparent) {
        this.isTransparent = isTransparent;

        setupUniverse();

        frame = createWindow();
    }

    /** Sets up the virtual universe, and creates the associated
      * canvas and views.
      */
    protected void setupUniverse() {
        vu = new VirtualUniverse();

        config = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                 .getDefaultScreenDevice()
                                 .getBestConfiguration(
                                     new GraphicsConfigTemplate3D());

        view = new View();
        view.setPhysicalBody(new PhysicalBody());
        view.setPhysicalEnvironment(new PhysicalEnvironment());

        root = buildBranchGraph();

        canvas = new Canvas3D(config);
        view.addCanvas3D(canvas);
    }

    /** Creates a window to display plots in.
      */
    protected JFrame createWindow() {
        JFrame window;

        window = new JFrame("3D Plot--Cas");
        window.setSize(600, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.addWindowListener(new Plot3DWindowListener(this));

        return window;
    }

    /** Builds the contents of the virtual universe, and returns the
      * root of the new graph.
      *
      * This is an awfully long method for such a short comment.
      */
    protected BranchGroup buildBranchGraph() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 2);
        BranchGroup root = new BranchGroup();
        TransformGroup rotateGroup = buildGraphGroup();

        root.setCapability(BranchGroup.ALLOW_DETACH);

        MouseRotate rotator = new MouseRotate();
        rotator.setTransformGroup(rotateGroup);
        rotator.setSchedulingBounds(new BoundingSphere());

        setupEnvironment(root, bounds);

        root.addChild(buildViewGroup());
        root.addChild(rotator);
        root.addChild(rotateGroup);
        root.addChild(this);
        root.compile();

        return root;
    }

    /** Builds the group containing the ViewPlatform.
      */
    protected Group buildViewGroup() {
        Transform3D viewTransform = new Transform3D();
        TransformGroup viewGroup
                             = new TransformGroup(viewTransform);

        vp = new ViewPlatform();
        view.attachViewPlatform(vp);

        viewTransform.set(new Vector3f(.0f, 0f, 3.0f));
        viewGroup.setTransform(viewTransform);
        viewGroup.addChild(vp);

        return viewGroup;
    }

    /** Creates the graph, axes, and labels.
      */
    protected TransformGroup buildGraphGroup() {
        Appearance axesAppearance = new Appearance();
        Transform3D textTransform = new Transform3D();
        Transform3D numbersTransform = new Transform3D();
        Transform3D xArrowRotate = new Transform3D();
        Transform3D zArrowRotate = new Transform3D();
        Transform3D initialRotate = new Transform3D();

        rotateGroup = new TransformGroup();
        rotateGroup.setCapability(
                           TransformGroup.ALLOW_TRANSFORM_READ);
        rotateGroup.setCapability(
                           TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotateGroup.setCapability(
                           TransformGroup.ALLOW_CHILDREN_WRITE);
        initialRotate.rotZ(-Math.PI / 2);
        rotateGroup.setTransform(initialRotate);

        graph = new Shape3D();
        if (isTransparent)
            graph.setAppearance(
                    Plotter3DFactory.getTransparentAppearance());
        else
            graph.setAppearance(
                    Plotter3DFactory.getOpaqueAppearance());
        graph.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        rotateGroup.addChild(graph);

        axesShape = new Shape3D();
        axesAppearance.setLineAttributes(
                 new LineAttributes(1f,
                                    LineAttributes.PATTERN_SOLID,
                                    true));
        axesAppearance.setColoringAttributes(
                             new ColoringAttributes(
                                 new Color3f(),
                                 ColoringAttributes.SHADE_FLAT));
        axesShape.setAppearance(axesAppearance);
        axesShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        rotateGroup.addChild(axesShape);

        textTransform.setScale(0.1);
        numbersTransform.setScale(0.05d);

        xArrowRotate.rotZ(-Math.PI / 2);

        rotateGroup.addChild(buildXArrowGroup(xArrowRotate,
                                              textTransform));
        rotateGroup.addChild(buildXMaxGroup(xArrowRotate,
                                            numbersTransform));
        rotateGroup.addChild(buildXMinGroup(xArrowRotate,
                                            numbersTransform));

        rotateGroup.addChild(buildYArrowGroup(textTransform));
        rotateGroup.addChild(buildYMaxGroup(numbersTransform));
        rotateGroup.addChild(buildYMinGroup(numbersTransform));

        zArrowRotate.rotX(Math.PI / 2);

        rotateGroup.addChild(buildZArrowGroup(zArrowRotate,
                                              textTransform));
        rotateGroup.addChild(buildZMaxGroup(zArrowRotate,
                                            numbersTransform));
        rotateGroup.addChild(buildZMinGroup(zArrowRotate,
                                            numbersTransform));

        return rotateGroup;
    }

    /** Builds the arrow and label marking the positive direction on
      * the X axis.
      */
    protected Group buildXArrowGroup(Transform3D xArrowRotate,
                                     Transform3D textTransform) {
        TransformGroup xSize = new TransformGroup();
        TransformGroup xRotateGroup = new TransformGroup();

        xShape = new OrientedShape3D();
        xShape.setCapability(OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        xShape.setGeometry(new Text3D(Plotter3DFactory.getFont(), x));
        xShape.setAlignmentMode(
                             OrientedShape3D.ROTATE_ABOUT_POINT);
        xShape.setAppearance(
                         Plotter3DFactory.getOpaqueAppearance());
        xSize.setTransform(textTransform);

        xArrow = new Cone(0.025f, 0.05f);
        xArrowTransform = new Transform3D();
        xArrowGroup = new TransformGroup();
        xArrowGroup.setCapability(
                           TransformGroup.ALLOW_TRANSFORM_WRITE);

        xRotateGroup.setTransform(xArrowRotate);
        xRotateGroup.addChild(xArrow);
        xArrowGroup.addChild(xRotateGroup);
        xSize.addChild(xShape);
        xArrowGroup.addChild(xSize);

        return xArrowGroup;
    }

    /** Builds the label and marker for the maximum bound on the X
      * axis.
      */
    protected Group buildXMaxGroup(Transform3D xArrowRotate,
                                   Transform3D numbersTransform) {
        TransformGroup xMaxSize = new TransformGroup();
        TransformGroup xMaxRotateGroup = new TransformGroup();

        xMaxLabel = new OrientedShape3D();
        xMaxLabel.setCapability(
                               OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        xMaxLabel.setAlignmentMode(
                               OrientedShape3D.ROTATE_ABOUT_POINT);
        xMaxLabel.setAppearance(
                             Plotter3DFactory.getOpaqueAppearance());
        xMaxSize.setTransform(numbersTransform);
        xMaxSize.addChild(xMaxLabel);
        xMaxMarker = new Cone(0.025f, 0.001f);
        xMaxTransform = new Transform3D();
        xMaxGroup = new TransformGroup();
        xMaxGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);
        xMaxRotateGroup.setTransform(xArrowRotate);
        xMaxRotateGroup.addChild(xMaxMarker);
        xMaxGroup.addChild(xMaxRotateGroup);
        xMaxGroup.addChild(xMaxSize);

        return xMaxGroup;
    }

    /** Builds the label and marker for the minimum bound on the X
      * axis.
      */
    protected Group buildXMinGroup(Transform3D xArrowRotate,
                                   Transform3D numbersTransform) {
        TransformGroup xMinRotateGroup = new TransformGroup();
        TransformGroup xMinSize = new TransformGroup();

        xMinLabel = new OrientedShape3D();
        xMinLabel.setCapability(
                               OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        xMinLabel.setAlignmentMode(
                               OrientedShape3D.ROTATE_ABOUT_POINT);
        xMinLabel.setAppearance(
                             Plotter3DFactory.getOpaqueAppearance());

        xMinSize.setTransform(numbersTransform);
        xMinSize.addChild(xMinLabel);

        xMinMarker = new Cone(0.025f, 0.001f);

        xMinTransform = new Transform3D();
        xMinGroup = new TransformGroup();

        xMinGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);

        xMinRotateGroup.setTransform(xArrowRotate);
        xMinRotateGroup.addChild(xMinMarker);

        xMinGroup.addChild(xMinRotateGroup);
        xMinGroup.addChild(xMinSize);

        return xMinGroup;
    }

    /** Builds the arrow and label showing the positive direction of
      * the Y axis.
      */
    protected Group buildYArrowGroup(Transform3D textTransform) {
        TransformGroup ySize = new TransformGroup();

        yShape = new OrientedShape3D();
        yShape.setCapability(OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        yShape.setGeometry(new Text3D(Plotter3DFactory.getFont(), y));
        yShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
        yShape.setAppearance(Plotter3DFactory.getOpaqueAppearance());
        ySize.setTransform(textTransform);

        yArrow = new Cone(0.025f, 0.05f);
        yArrowTransform = new Transform3D();
        yArrowGroup = new TransformGroup();
        yArrowGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);
        yArrowGroup.addChild(yArrow);
        ySize.addChild(yShape);
        yArrowGroup.addChild(ySize);

        return yArrowGroup;
    }

    /** Builds the group containing the marker and label for the
      * maximum bound on the Y axis.
      */
    protected Group buildYMaxGroup(Transform3D numbersTransform) {
        TransformGroup yMaxSize = new TransformGroup();

        yMaxLabel = new OrientedShape3D();
        yMaxLabel.setCapability(
                               OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        yMaxLabel.setAlignmentMode(
                               OrientedShape3D.ROTATE_ABOUT_POINT);
        yMaxLabel.setAppearance(
                             Plotter3DFactory.getOpaqueAppearance());
        yMaxSize.setTransform(numbersTransform);
        yMaxSize.addChild(yMaxLabel);
        yMaxMarker = new Cone(0.025f, 0.001f);
        yMaxTransform = new Transform3D();
        yMaxGroup = new TransformGroup();
        yMaxGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);
        yMaxGroup.addChild(yMaxMarker);
        yMaxGroup.addChild(yMaxSize);

        return yMaxGroup;
    }

    /** Builds the group containing the marker and label showing the
      * minimum bound on the Y axis.
      */
    protected Group buildYMinGroup(Transform3D numbersTransform) {
        TransformGroup yMinSize = new TransformGroup();

        yMinLabel = new OrientedShape3D();
        yMinLabel.setCapability(
                               OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        yMinLabel.setAlignmentMode(
                               OrientedShape3D.ROTATE_ABOUT_POINT);
        yMinLabel.setAppearance(
                             Plotter3DFactory.getOpaqueAppearance());

        yMinSize.setTransform(numbersTransform);
        yMinSize.addChild(yMinLabel);

        yMinMarker = new Cone(0.025f, 0.001f);
        yMinTransform = new Transform3D();

        yMinGroup = new TransformGroup();
        yMinGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);
        yMinGroup.addChild(yMinMarker);
        yMinGroup.addChild(yMinSize);

        return yMinGroup;
    }

    /** Builds the group containing the arrow and accompanying label
      * showing the positive direction on the Z axis,
      */
    protected Group buildZArrowGroup(Transform3D zArrowRotate,
                                     Transform3D textTransform) {
        TransformGroup zSize = new TransformGroup();
        TransformGroup zRotateGroup = new TransformGroup();

        zShape = new OrientedShape3D();
        zShape.setCapability(OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        zShape.setGeometry(new Text3D(Plotter3DFactory.getFont(), z));
        zShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
        zShape.setAppearance(Plotter3DFactory.getOpaqueAppearance());
        zSize.setTransform(textTransform);

        zArrow = new Cone(0.025f, 0.05f);
        zArrowTransform = new Transform3D();
        zArrowGroup = new TransformGroup();
        zArrowGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);
        zRotateGroup.setTransform(zArrowRotate);
        zRotateGroup.addChild(zArrow);
        zArrowGroup.addChild(zRotateGroup);
        zSize.addChild(zShape);
        zArrowGroup.addChild(zSize);

        return zArrowGroup;
    }

    /** Builds the group containing the marker and label deliniating
      * the maximum bound along the Z axis.
      */
    protected Group buildZMaxGroup(Transform3D zArrowRotate,
                                   Transform3D numbersTransform) {
        TransformGroup zMaxRotateGroup = new TransformGroup();
        TransformGroup zMaxSize = new TransformGroup();

        zMaxLabel = new OrientedShape3D();
        zMaxLabel.setCapability(
                               OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        zMaxLabel.setAlignmentMode(
                               OrientedShape3D.ROTATE_ABOUT_POINT);
        zMaxLabel.setAppearance(
                             Plotter3DFactory.getOpaqueAppearance());
        zMaxSize.setTransform(numbersTransform);
        zMaxSize.addChild(zMaxLabel);
        zMaxMarker = new Cone(0.025f, 0.001f);
        zMaxTransform = new Transform3D();
        zMaxGroup = new TransformGroup();
        zMaxGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);
        zMaxRotateGroup.setTransform(zArrowRotate);
        zMaxRotateGroup.addChild(zMaxMarker);
        zMaxGroup.addChild(zMaxRotateGroup);
        zMaxGroup.addChild(zMaxSize);

        return zMaxGroup;
    }

    /** Builds the group containing the marker and label deliniating
      * the minimum bound along the Z axis.
      */
    protected Group buildZMinGroup(Transform3D zArrowRotate,
                                   Transform3D numbersTransform) {
        TransformGroup zMinRotateGroup = new TransformGroup();
        TransformGroup zMinSize = new TransformGroup();

        zMinLabel = new OrientedShape3D();
        zMinLabel.setCapability(
                               OrientedShape3D.ALLOW_GEOMETRY_WRITE);
        zMinLabel.setAlignmentMode(
                               OrientedShape3D.ROTATE_ABOUT_POINT);
        zMinLabel.setAppearance(
                             Plotter3DFactory.getOpaqueAppearance());

        zMinSize.setTransform(numbersTransform);
        zMinSize.addChild(zMinLabel);

        zMinMarker = new Cone(0.025f, 0.001f);
        zMinTransform = new Transform3D();

        zMinGroup = new TransformGroup();
        zMinGroup.setCapability(
                               TransformGroup.ALLOW_TRANSFORM_WRITE);

        zMinRotateGroup.setTransform(zArrowRotate);
        zMinRotateGroup.addChild(zMinMarker);

        zMinGroup.addChild(zMinRotateGroup);
        zMinGroup.addChild(zMinSize);

        return zMinGroup;
    }

    /** Sets up the background and lighting for the graph scene.
      */
    protected void setupEnvironment(BranchGroup root,
                                    BoundingSphere bounds) {
        AmbientLight ambient = new AmbientLight();
        BranchGroup temp = new BranchGroup();
        DirectionalLight directional = new DirectionalLight();
        Background bg = new Background(1f, 1f, 1f);

        bg.setApplicationBounds(bounds);

        ambient.setInfluencingBounds(bounds);
        directional.setInfluencingBounds(bounds);
        directional.setDirection(-01f, -01.f, -01.f);
        setSchedulingBounds(bounds);

        root.addChild(directional);
        root.addChild(ambient);
        root.addChild(bg);
    }

    /** Overrides the method in Behavior.
      */
    public void initialize() {
        wakeupOn(new WakeupOnBehaviorPost(this, ID));
    }

    /** Overrides method in Behavior. Readies the objects in the
      * VirtualUniverse for display by setting up the geometries of
      * the graphs and axes.
      */
    public void processStimulus(Enumeration criteria) {
        Point3d xPoint = new Point3d(
                                  1.0d,
                                  xAxisY(),
                                  xAxisZ());
        Point3d yPoint = new Point3d(
                                yAxisX(),
                                1.0d,
                                yAxisZ());
        Point3d zPoint = new Point3d(zAxisX(),
                                    zAxisY(),
                                    1.0d);

        setAxes(xPoint, yPoint, zPoint);

        graph.removeAllGeometries();
        for (int counter = 0; counter < values[0][0].length;
                              ++counter) {
	    System.out.println("Building geometry for function #" + counter);
	    GeometryArray geom = createGeometry(counter);
	    System.out.println(geom);
            graph.addGeometry(geom);
        }
        values = null;

        xArrowTransform.set(new Vector3d(xPoint));
        setLabel(xShape, x, xArrowGroup, xArrowTransform);
        xMaxTransform.set(new Vector3d(0.5d,
                                       xAxisY(),
                                       xAxisZ()));
        setLabel((Shape3D)xMaxLabel, xMax, xMaxGroup, xMaxTransform);
        xMinTransform.set(new Vector3d(-0.5d,
                                       xAxisY(),
                                       xAxisZ()));
        setLabel(xMinLabel, xMin, xMinGroup, xMinTransform);

        yArrowTransform.set(new Vector3d(yPoint));
        setLabel(yShape, y, yArrowGroup, yArrowTransform);
        yMaxTransform.set(new Vector3d(yAxisX(),
                                       0.5d,
                                       yAxisZ()));
        setLabel(yMaxLabel, yMax, yMaxGroup, yMaxTransform);
        yMinTransform.set(new Vector3d(yAxisX(),
                                       -0.5d,
                                       yAxisZ()));
        setLabel(yMinLabel, yMin, yMinGroup, yMinTransform);

        setLabel(zShape, z, zArrowGroup, zArrowTransform);
        zMaxTransform.set(new Vector3d(zAxisX(),
                                       zAxisY(),
                                       0.5d));
        setLabel(zMaxLabel, max, zMaxGroup, zMaxTransform);
        zMinTransform.set(new Vector3d(zAxisX(),
                                       zAxisY(),
                                       -0.5d));
        setLabel(zMinLabel, min, zMinGroup, zMinTransform);

        wakeupOn(new WakeupOnBehaviorPost(this, ID));
    }

    /** Takes the coordinates for the maximum x, y, and z points, and
      * sets up the axes' geometry.
      */
    protected void setAxes(Point3d xPoint, Point3d yPoint, Point3d zPoint) {
        LineArray axes = new LineArray(6, LineArray.COORDINATES);

        axesShape.removeAllGeometries();
        axes.setCoordinate(0, new Point3d(
                                 -1.d,
                                 xAxisY(),
                                 xAxisZ()));
        axes.setCoordinate(1, xPoint);
        axes.setCoordinate(2, new Point3d(
                                 yAxisX(),
                                 -1.d,
                                 yAxisZ()));
        axes.setCoordinate(3, yPoint);
        axes.setCoordinate(4, new Point3d(
                                 zAxisX(),
                                 zAxisY(),
                                 -1.d));
        axes.setCoordinate(5, zPoint);
        zArrowTransform.set(new Vector3d(zPoint));
        axesShape.addGeometry(axes);
    }

    /** Returns the Y coordinate of the X axis.
      */
    protected double xAxisY(){
        return zAxisY();
    }

    /** Returns the Z coordinate of the X axis.
      */
    protected double xAxisZ(){
        return yAxisZ();
    }

    /** Returns the X coordinate of the Y axis.
      */
    protected double yAxisX(){
        return zAxisX();
    }

    /** Returns the Z coordinate of the Y axis.
      */
    protected double yAxisZ(){
        return -0.5d - min / (max - min);
    }

    /** Returns the X coordinate the Z axis.
      */
    protected double zAxisX(){
        return -0.5d - xMin / xRange;
    }

    /** Returns the Y coordinate of the Z axis.
      */
    protected double zAxisY(){
        return -0.5d - yMin / yRange;
    }

    /** Positions the label's group and sets the label's shape.
      */
    protected static void setLabel(Shape3D shape, double value, TransformGroup group, Transform3D transform) {
        setLabel(shape, Double.toString(value), group, transform);
    }

    /** Positions the label's group and sets the label's shape.
      */
    protected static void setLabel(Shape3D shape, String text, TransformGroup group, Transform3D transform) {
        shape.setGeometry(new Text3D(Plotter3DFactory.getFont(), text));
        group.setTransform(transform);
    }

    /** Initiates plotting of the graph, and displays it in a new
      * window. Should be run on the AWT event thread.
      */
    public void run() {
        try {
            locale = new Locale(vu);
            locale.addBranchGraph(root);

            frame.getContentPane().removeAll();
            frame.getContentPane().add(canvas);

            postId(ID);

            frame.show();
            view.startView();

            canvas.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                                  null,
                                  e.toString(),
                                  "An error occurred while plotting",
                                  JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /** Creates the geometry for the graph of function number index.
      */
    protected GeometryArray createGeometry(int index) {
        int resolution = Plotter3DFactory.getResolution();
        TriangleArray arr
           = new TriangleArray(6 * resolution * (resolution),
                               TriangleArray.COORDINATES
                                 | TriangleArray.NORMALS
                                 | TriangleArray.TEXTURE_COORDINATE_2
                                 | TriangleArray.COLOR_3);
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        Point3d p3 = new Point3d();
        Color3f c1 = new Color3f();
        Color3f c2 = new Color3f();
        Color3f c3 = new Color3f();
        TexCoord2f t1 = new TexCoord2f();
        TexCoord2f t2 = new TexCoord2f();
        TexCoord2f t3 = new TexCoord2f();
        Vector3d normal = new Vector3d();
        Vector3d v1 = new Vector3d();
        Vector3d v2 = new Vector3d();
        Vector3f normalF = new Vector3f();
        int triangle = 0;
        int x = 0;
        int y = 0;

        initOffsets();

        for (x = 0; x < resolution; ++x) {
            y = 0;
            p1.set(transform(x),
                   transform(y),
                   transformOutput(x, y, index));
            getColor(values[x][y][index], c1, index);
            getTextureCoordinate(x, y, t1);
            p2.set(transform(x + 1),
                   transform(y),
                   transformOutput(x + 1, y, index));
            getColor(values[x + 1][y][index], c2, index);
            getTextureCoordinate(x + 1, y, t2);
            for (; y < resolution; ++y) {
                setupTriangleA(index, resolution,
                               arr,
                               p1, p2, p3,
                               c1, c2, c3,
                               t1, t2, t3,
                               normal, v1, v2, normalF,
                               triangle, x, y);
                triangle+=3;

                setupTriangleB(index, resolution,
                               arr,
                               p1, p2, p3,
                               c1, c2, c3,
                               t1, t2, t3,
                               normal, v1, v2, normalF,
                               triangle, x, y);
                triangle+=3;
            }
        }
        return arr;
    }

    /** Sets up a triangle in the geometry like this (looking down
      * the Z axis):
      *
      *   Y
      *   ^   p3
      *   |   | \
      *   |   |  \
      *   |   p1--p2
      *   |
      *   +-------->X
      *
      * working under the assumption and p1 and p2 have already been
      * initialized (presumably by the previous call to
      * setupTriangleB()).
      */
    protected void setupTriangleA(int index,
                                  int resolution,
                                  TriangleArray arr,
                                  Point3d p1,
                                  Point3d p2,
                                  Point3d p3,
                                  Color3f c1,
                                  Color3f c2,
                                  Color3f c3,
                                  TexCoord2f t1,
                                  TexCoord2f t2,
                                  TexCoord2f t3,
                                  Vector3d normal,
                                  Vector3d v1,
                                  Vector3d v2,
                                  Vector3f normalF,
                                  int triangle,
                                  int x,
                                  int y) {
                p3.set(transform(x),
                            transform(y + 1),
                            transformOutput(x, y + 1, index));
                getColor(values[x][y + 1][index], c3, index);
                getTextureCoordinate(x, y + 1, t3);

                if (inBounds(values[x][y][index],
                             values[x + 1][y][index],
                             values[x][y + 1][index])) {
                    v1.sub(p2, p1);
                    v2.sub(p3, p1);
                    normal.cross(v1, v2);
                    normalF.set(normal);
                    normalF.normalize();
                    arr.setNormal(triangle, normalF);
                    arr.setNormal(triangle + 1, normalF);
                    arr.setNormal(triangle + 2, normalF);
    
                    arr.setCoordinate(triangle, p1);
                    arr.setColor(triangle, c1);
                    arr.setTextureCoordinate(0, triangle, t1);
                    arr.setCoordinate(triangle + 1, p2);
                    arr.setColor(triangle + 1, c2);
                    arr.setTextureCoordinate(0, triangle, t1);
                    arr.setTextureCoordinate(0, triangle + 1, t2);
                    arr.setCoordinate(triangle + 2, p3);
                    arr.setColor(triangle + 2, c3);
                    arr.setTextureCoordinate(0, triangle + 2, t3);
                }
    }

    /** Setups up a triangle in the geometry like this (looking down
      * the Z axis):
      *
      *   Y
      *   ^   p1--p2
      *   |   . \  |
      *   |   .  \ |
      *   |   ....p3
      *   |
      *   +-------->X
      *
      * working under the assumption that the initial values of p1
      * and p2 and p3 are valid results from a call to
      * setupTriangleA(), whose geometry is indicated by the dots in
      * the above diagram.
      */
    protected void setupTriangleB(int index,
                                  int resolution,
                                  TriangleArray arr,
                                  Point3d p1,
                                  Point3d p2,
                                  Point3d p3,
                                  Color3f c1,
                                  Color3f c2,
                                  Color3f c3,
                                  TexCoord2f t1,
                                  TexCoord2f t2,
                                  TexCoord2f t3,
                                  Vector3d normal,
                                  Vector3d v1,
                                  Vector3d v2,
                                  Vector3f normalF,
                                  int triangle,
                                  int x,
                                  int y) {
                p1.set(p3);
                c1.set(c3);
                t1.set(t3);
                p3.set(p2);
                c3.set(c2);
                t3.set(t2);
                p2.set(transform(x + 1),
                       transform(y + 1),
                       transformOutput(x + 1, y + 1, index));
                getColor(values[x + 1][y + 1][index], c2, index);
                getTextureCoordinate(x + 1, y + 1, t2);

                if (inBounds(values[x + 1][y][index],
                             values[x][y + 1][index],
                             values[x + 1][y + 1][index])) {
                    v1.sub(p2, p1);
                    v2.sub(p3, p1);
                    normal.cross(v1, v2);
                    normalF.set(normal);
                    normalF.normalize();
                    arr.setNormal(triangle, normalF);
                    arr.setNormal(triangle + 1, normalF);
                    arr.setNormal(triangle + 2, normalF);
    
                    arr.setCoordinate(triangle, p1);
                    arr.setColor(triangle, c1);
                    arr.setTextureCoordinate(0, triangle, t1);
                    arr.setCoordinate(triangle + 1, p2);
                    arr.setColor(triangle + 1, c2);
                    arr.setTextureCoordinate(0, triangle + 1, t2);
                    arr.setCoordinate(triangle + 2, p3);
                    arr.setColor(triangle + 2, c3);
                    arr.setTextureCoordinate(0, triangle + 2, t3);
                }
    }

    /** Returns true if all arguments lie between max and min, false
      * otherwise.
      */
    protected boolean inBounds(double a, double b, double c) {
        return a <= max && a >= min
            && b <= max && b >= min
            && c <= max && c >= min;
    }

    /** Transforms the value from the array's coordinate
      * system to the 3D graph's coordinate system.
      */
    protected double transform(int value) {
        return (double)value
             / (double)Plotter3DFactory.getResolution() - 0.5d;
    }

    /** Transforms the value from the function's output coordinate
      * system to the 3D graph's coordinate system.
      */
    protected double transformOutput(int x, int y, int index) {
        return (values[x][y][index] - min) / (max - min) - 0.5d;
    }

    /** Generates a color for a point on a plot.
      * @param position The position of the point on the z axis.
      * @param c The uninitialized color object.
      * @return c, after it is initialized.
      */
    protected Color3f getColor(double position,
                               Color3f c,
                               int index) {
        if (color[index] == Plotter3DFactory.RED_YELLOW) {
            c.set(1f,
                  1f - (float)((position - min) / (max - min)),
                  0f);
        } else if (color[index] == Plotter3DFactory.BLUE_GREEN) {
            c.set(0f,
                  (float)((position - min) / (max - min)),
                  (float)(1d - (position - min) / (max - min)));
        } else if(color[index] == Plotter3DFactory.PURPLE_PINK) {
            c.set(1f,
                  0.5f * (float)((position - min) / (max - min)),
                  1f - 0.5f
                          * (float)((position - min) / (max - min)));
        } else {
		c.set(1f, 1f, 1f);
	}
        return c;
    }

    /** Initializes xOffset and yOffset.
      */
    protected void initOffsets() {
        xOffset = (float)(xMin / xIncrement)
            - Math.round(xMin / xIncrement);
        yOffset = (float)(yMin / yIncrement)
            - Math.round(yMin / yIncrement);
    }

    /** Gets the texture coordinate for a point.
      * @param x the x position of the point.
      * @param y the y position of the point.
      * @param t the uninitalized coordinate object.
      * @return t, after initializing it.
      */
    protected TexCoord2f getTextureCoordinate(int x,
                                              int y,
                                              TexCoord2f t) {
        t.set((float)(x * xRange)
                           / (float)(Plotter3DFactory.getResolution()
                           * xIncrement)
                           + xOffset,
              (float)(y * yRange)
                           / (float)(Plotter3DFactory.getResolution()
                           * yIncrement)
                           + yOffset);
        return t;
    }

    /** Called to recycle the  object. Clears locale, geometries, and
      * places the object in a cache for re-use.
      */
    public void dispose() {
        vu.removeAllLocales();

        axesShape.removeAllGeometries();
        graph.removeAllGeometries();
        if (isTransparent)
            transparent.add(this);
        else
            opaque.add(this);
    }

    /** Stored opaque graphs for re-use.
      */
    protected static List opaque = new ArrayList();

    /** Transparent graphs for re-use.
      */
    protected static List transparent = new ArrayList();

    /** Whether this instance is transparent. Allowing changing a
      * graph from opaque to transparent and back greatly slows down
      * Java3D, so opaque and transparent graphs are managed
      * separately.
      */
    protected boolean isTransparent;

    /** Returns a Plotter3D, either a new instance, or a cached one.
      */
    public static Plotter3D getPlotter(boolean isTransparent) {
        Plotter3D plotter;
        if (!isTransparent) {
            if (opaque.isEmpty()) {
                plotter = new Plotter3D(isTransparent);
            } else {
                plotter = (Plotter3D)opaque.get(0);
                opaque.remove(plotter);
            }
        } else {
            if (transparent.isEmpty()) {
                plotter = new Plotter3D(isTransparent);
            } else {
                plotter = (Plotter3D)transparent.get(0);
                transparent.remove(plotter);
            }
        }
        return plotter;
    }

    /** Called when applet is reloaded to avoid weird Java3D issues.
      * Dumps all cached graphs.
      */
    public static void clearCache() {
        opaque.clear();
        transparent.clear();
    }
}

/** Reclaims the Plotter3D when its associated JFrame is closed.
  */
class Plot3DWindowListener extends WindowAdapter {
    /** The plot the window is associated with.
      */
    Plotter3D plot;

    /** Creates a new listener.
      */
    public Plot3DWindowListener(Plotter3D p) {
        plot = p;
    }

    /** Reclaims the plot.
      */
    public void windowClosing(WindowEvent e) {
        plot.dispose();
    }
}
