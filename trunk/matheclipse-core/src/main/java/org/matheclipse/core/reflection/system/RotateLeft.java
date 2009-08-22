package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.NonNegativeIntegerExpected;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

public class RotateLeft extends AbstractFunctionEvaluator {

	public RotateLeft() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() != 2) && (functionList.size() != 3)) {
			return null;
		}
		try {
			if (!functionList.get(1).isAtom()) {
				final IAST result = F.ast(functionList.get(1).head());
				if (functionList.size() == 2) {
					ASTRange range = ((IAST) functionList.get(1)).args();
					range.rotateLeft(result, 1);
					// Rotating.rotateLeft((IAST) list.get(1), result, 2, 1);
					return result;
				} else {
					if (functionList.get(2) instanceof IInteger) {
						int n = 0;
						try {
							n = ((IInteger) functionList.get(2)).toInt();// throws
																	// ArithmeticException
							if (n < 0) {
								throw new NonNegativeIntegerExpected(functionList, 2);
							}
						} catch (final ArithmeticException e) {
							throw new NonNegativeIntegerExpected(functionList, 3);
						}
						ASTRange range = ((IAST) functionList.get(1)).args();
						range.rotateLeft(result, n);
						// Rotating.rotateLeft((IAST) list.get(1), result, n+1,
						// 1);
						return result;
					}
				}

			}
		} catch (final ArithmeticException e) {

		}
		return null;
	}

}
