package org.matheclipse.symja.plot;

import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.eval.DoubleVariable;

import com.googlecode.surfaceplotter.AbstractSurfaceModel;

public class StringSurfaceModel extends AbstractSurfaceModel implements Runnable {
	private final DoubleEvaluator engine = new DoubleEvaluator();
	private ASTNode f1Node;
	private ASTNode f2Node;
	private String f1Function = "If[x*x+y*y==0,1,Sin[x*x+y*y]/(x*x+y*y)]";
	private String f2Function = "Sin[x*y]";

	StringSurfaceModel() {
		super();

		setPlotFunction2(false);

		setCalcDivisions(100);
		setDispDivisions(30);
		setContourLines(10);

		setXMin(-3);
		setXMax(3);
		setYMin(-3);
		setYMax(3);

		setBoxed(false);
		setDisplayXY(false);
		setExpectDelay(false);
		setAutoScaleZ(true);
		setDisplayZ(false);
		setMesh(false);
		setPlotType(PlotType.SURFACE);
		// setPlotType(PlotType.WIREFRAME);
		// setPlotType(PlotType.CONTOUR);
		// setPlotType(PlotType.DENSITY);

		setPlotColor(PlotColor.SPECTRUM);
		// setPlotColor(PlotColor.DUALSHADE);
		// setPlotColor(PlotColor.FOG);
		// setPlotColor(PlotColor.OPAQUE);
		Parser p = new Parser();
		f1Node = p.parse(f1Function);
		f2Node = p.parse(f2Function);
	}

	/**
	 * @return the f1Function
	 */
	// public String getF1Function() {
	// return f1Function;
	// }

	/**
	 * @return the f2Function
	 */
	// public String getF2Function() {
	// return f2Function;
	// }

	/**
	 * @param f1Function
	 *          the f1Function to set
	 */
	public void setF1Function(String f1Function) {
		try {
			this.f1Node = engine.parse(f1Function);
			this.f1Function = f1Function;
		} catch (SyntaxError se) {
			// TODO show error
		}
	}

	/**
	 * @param f2Function
	 *          the f2Function to set
	 */
	public void setF2Function(String f2Function) {
		try {
			this.f2Node = engine.parse(f2Function);
			this.f2Function = f2Function;
		} catch (SyntaxError se) {
			// TODO show error
		}
	}

	public float f1(float x, float y) {
		engine.defineVariable("x", new DoubleVariable(x));
		engine.defineVariable("y", new DoubleVariable(y));
		return (float) engine.evaluateNode(f1Node);
	}

	public float f2(float x, float y) {
		engine.defineVariable("x", new DoubleVariable(x));
		engine.defineVariable("y", new DoubleVariable(y));
		return (float) engine.evaluateNode(f2Node);
	}

	@Override
	public void run() {
		Plotter p = newPlotter(getCalcDivisions());
		int im = p.getWidth();
		int jm = p.getHeight();
		for (int i = 0; i < im; i++)
			for (int j = 0; j < jm; j++) {
				float x, y;
				x = p.getX(i);
				y = p.getY(j);
				p.setValue(i, j, f1(x, y), f2(x, y));
			}
	}
}
