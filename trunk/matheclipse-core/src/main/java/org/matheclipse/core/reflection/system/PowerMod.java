package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

import apache.harmony.math.BigInteger;

/**
 * 
 */
public class PowerMod extends AbstractFunctionEvaluator {

	public PowerMod() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 4) {
			return null;
		}
		for (int i = 1; i < functionList.size(); i++) {
			if (!(functionList.get(i) instanceof IInteger)) {
				return null;
			}
		}
		try {
			BigInteger bigResult = powerMod(((IInteger) functionList.get(1)).getBigNumerator(), ((IInteger) functionList.get(2))
					.getBigNumerator(), ((IInteger) functionList.get(3)).getBigNumerator());
			return F.integer(bigResult);
		} catch (ArithmeticException ae) {
			if (Config.SHOW_STACKTRACE) {
				ae.printStackTrace();
			}
		}
		return null;
	}

	public static BigInteger powerMod(BigInteger a, BigInteger b, BigInteger m) throws ArithmeticException {
		return a.modPow(b, m);
	}
}