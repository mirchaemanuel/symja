package org.matheclipse.core.form.mathml;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.MathMLUtilities;
import org.matheclipse.core.expression.F;

/**
 * Tests MathML presentation function
 */
public class MathMLPrefixPresentationTestCase extends TestCase {
	MathMLUtilities mathUtil;

	public MathMLPrefixPresentationTestCase(String name) {
		super(name);
		F.initSymbols(null, null);
	}

	/**
	 * Test mathml function
	 */
	public void testMathMLPresentation() {
		check("a*b+c",
				"<m:mrow><m:mi>c</m:mi><m:mo>+</m:mo><m:mrow><m:mi>a</m:mi><m:mo>&#x2062;</m:mo><m:mi>b</m:mi></m:mrow></m:mrow>");
		check("I", "<m:mi>&#x02148;</m:mi>");
		check("2I", "<m:mrow><m:mn>2</m:mn><m:mo>&#x2062;</m:mo><m:mi>&#x02148;</m:mi></m:mrow>");
		check("2/3", "<m:mfrac><m:mn>2</m:mn><m:mn>3</m:mn></m:mfrac>");

		check("a+b", "<m:mrow><m:mi>a</m:mi><m:mo>+</m:mo><m:mi>b</m:mi></m:mrow>");
		check("a*b", "<m:mrow><m:mi>a</m:mi><m:mo>&#x2062;</m:mo><m:mi>b</m:mi></m:mrow>");
		check("a^b", "<m:msup><m:mi>a</m:mi><m:mi>b</m:mi></m:msup>");
		check("E", "<m:mi>&#02147;</m:mi>");
		check("n!", "<m:mrow><m:mi>n</m:mi><m:mo>!</m:mo></m:mrow>");
		check("k/2", "<m:mfrac><m:mi>k</m:mi><m:mn>2</m:mn></m:mfrac>");
		check(
				"Binomial[n,k/2]",
				"<m:mrow><m:mo>(</m:mo><m:mfrac linethickness=\"0\"><m:mi>n</m:mi><m:mfrac><m:mi>k</m:mi><m:mn>2</m:mn></m:mfrac></m:mfrac><m:mo>)</m:mo></m:mrow>");

		check(
				"x^2+4*x+4==0",
				"<m:mrow><m:mrow><m:msup><m:mi>x</m:mi><m:mn>2</m:mn></m:msup><m:mo>+</m:mo><m:mrow><m:mn>4</m:mn><m:mo>&#x2062;</m:mo><m:mi>x</m:mi></m:mrow><m:mo>+</m:mo><m:mn>4</m:mn></m:mrow><m:mo>=</m:mo><m:mn>0</m:mn></m:mrow>");

		check("n!", "<m:mrow><m:mi>n</m:mi><m:mo>!</m:mo></m:mrow>");

	}

	public void check(String strEval, String strResult) {
		StringWriter stw = new StringWriter();
		mathUtil.toMathML(strEval, stw);
		assertEquals(stw.toString(), "<m:math>" + strResult + "</m:math>");
	}

	/**
	 * The JUnit setup method
	 */
	protected void setUp() {
		try {
			EvalEngine engine = new EvalEngine();
			mathUtil = new MathMLUtilities(engine,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
