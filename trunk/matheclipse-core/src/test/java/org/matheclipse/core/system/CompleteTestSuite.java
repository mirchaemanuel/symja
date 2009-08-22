package org.matheclipse.core.system;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * General test suite. Starts all other tests.
 */
public class CompleteTestSuite extends TestCase {

	public CompleteTestSuite(String name) {
		super(name);
	}

	/**
	 * A unit test suite
	 * 
	 *@return The test suite
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test Suite (without MathML tests");
		suite.addTest(new TestSuite(org.matheclipse.core.system.SystemTestCase.class));
		suite.addTest(new TestSuite(org.matheclipse.core.system.PatternMatchingTestCase.class));
		suite.addTest(new TestSuite(org.matheclipse.core.system.CombinatoricTestCase.class));
		// suite.addTest(new
		// TestSuite(org.matheclipse.core.system.PolynomialTestCase.class));
		// suite.addTest(new
		// TestSuite(org.matheclipse.core.form.mathml.MathMLPresentationTestCase
		// .class));
		// suite.addTest(new
		// TestSuite(org.matheclipse.core.form.mathml.MathMLPrefixPresentationTestCase
		// .class));

		return suite;

	}

	/**
	 * Run all tests in a swing GUI
	 * 
	 *@param args
	 *          Description of Parameter
	 */
	// public static void main(String args[]) {
	// junit.swingui.TestRunner.run(CompleteTestSuite.class);
	// }
}
