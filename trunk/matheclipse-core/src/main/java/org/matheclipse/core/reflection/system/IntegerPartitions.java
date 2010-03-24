package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.basic.Util;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.generic.combinatoric.NumberPartitionsIterable;

/**
 * Generate all integer partitions for a given integer number. See <a
 * href="http://en.wikipedia.org/wiki/Integer_partition">Wikipedia - Integer
 * partition</a>
 * 
 */
public class IntegerPartitions extends AbstractFunctionEvaluator {

	public IntegerPartitions() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator#evaluate
	 * (org.matheclipse.parser.interfaces.AbstractExpressionFactory,
	 * org.matheclipse.parser.interfaces.IAST)
	 */
	@Override
	public IExpr evaluate(final IAST functionList) {

		if ((functionList.size() == 2) && (functionList.get(1) instanceof IInteger)) {
			final int n = ((IInteger) functionList.get(1)).getBigNumerator().intValue();
			final IAST result = F.function(F.List);
			IAST temp;
			final NumberPartitionsIterable comb = new NumberPartitionsIterable(n);
			int j[];
			while ((j = comb.next()) != null) {
				checkCanceled();
				temp = F.function(F.List);
				for (int i = 0; i < j.length; i++) {
					checkCanceled();
					if (j[i] != 0) {
						temp.add(F.integer(j[i]));
					}

					Util.checkCanceled();
				}
				result.add(temp);
			}
			return result;
		}
		return null;
	}

}
