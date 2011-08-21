package org.matheclipse.core.convert;

import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.ISignedNumber;

public class Expr2Object {
	public static double[] toDoubleVector(IAST ast) throws WrongArgumentType {
		double[] result = new double[ast.size() - 1];
		for (int i = 1; i < ast.size(); i++) {
			if (ast.get(i) instanceof ISignedNumber) {
				result[i - 1] = ((ISignedNumber) ast.get(i)).doubleValue();
			} else {
				throw new WrongArgumentType(ast, ast.get(i), i, "Trying to convert the argument into the double number range: "
						+ Double.MIN_VALUE + " - " + Double.MAX_VALUE);
			}
		}
		return result;
	}
}
