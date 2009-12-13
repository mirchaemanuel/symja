package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

/**
 * 
 */
public class PrimitiveRoots extends AbstractTrigArg1 {

	public PrimitiveRoots() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return null;
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return null;
	}

	@Override
	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1 instanceof IInteger) {
			try {
				IInteger[] roots = ((IntegerSym) arg1).primitiveRoots();
				IAST list = F.List();
				for (int i = 0; i < roots.length; i++) {
					list.add(roots[i]);
				}
				return list;
			} catch (ArithmeticException e) {
				// integer to large?
			}
		}
		return null;
	}

}
