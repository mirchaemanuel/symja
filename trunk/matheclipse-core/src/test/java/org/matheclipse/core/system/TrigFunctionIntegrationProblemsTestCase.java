package org.matheclipse.core.system;

import org.matheclipse.core.system.AbstractTestCase;

/**
 * Tests for combinatorial functions
 * 
 */
public class TrigFunctionIntegrationProblemsTestCase extends AbstractTestCase {
	public TrigFunctionIntegrationProblemsTestCase(String name) {
		super(name);
	}

	/**
	 * Test combinatorial functions
	 */
	public void testTrig001() {
		check("Integrate[Sin[a + b*x],x]", "(-1)*Cos[b*x+a]*b^(-1)");
		check("Integrate[Sin[a + b*x]^2,x]", "-1/2*Cos[b*x+a]*b^(-1)*Sin[b*x+a]+1/2*x");
		check("Integrate[Sin[a + b*x]^3,x]", "1/3*b^(-1)*Cos[b*x+a]^3-Cos[b*x+a]*b^(-1)");
		// check("Integrate[Sin[a + b*x]^4,x]", "");
		check("Integrate[Sin[a + b*x]^5,x]", "-1/5*b^(-1)*Cos[b*x+a]^5+2/3*b^(-1)*Cos[b*x+a]^3-Cos[b*x+a]*b^(-1)");

		// check("Integrate[Sin[a + b*x]^(1/2),x]", "");
		// check("Integrate[Sin[a + b*x]^(3/2),x]", "");
		// check("Integrate[Sin[a + b*x]^(5/2),x]", "");

		// check("Integrate[,x]", "");
	}
}
