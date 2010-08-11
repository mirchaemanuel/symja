package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.generic.combinatoric.KPartitionsList;

public class KPartitions extends AbstractFunctionEvaluator {

	public KPartitions() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {

		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST) && (functionList.get(2) instanceof IInteger)) {
			final IAST listArg0 = (IAST) functionList.get(1);
			try {
				final int k = ((IInteger) functionList.get(2)).toInt();
				final IAST result = F.function(F.List);
				final KPartitionsList<IExpr, IAST> iter = new KPartitionsList<IExpr, IAST>(listArg0, k, F.function(F.List), AST.COPY, 1);
				for (IAST part : iter) {
					result.add(part);
				}
				return result;
			} catch (ArithmeticException ae) {
				// k is not in the "int" number range
			}
		}
		return null;
	}

}
