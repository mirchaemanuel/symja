package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
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
			final int k = Validate.checkIntType(functionList, 2);
			final IAST result = F.List();
			final KPartitionsList<IExpr, IAST> iter = new KPartitionsList<IExpr, IAST>(listArg0, k, F.function(F.List), AST.COPY, 1);
			for (IAST part : iter) {
				result.add(part);
			}
			return result;
		}
		return null;
	}

}
