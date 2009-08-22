package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.basic.Util;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;


public class Partition extends AbstractFunctionEvaluator {

  public Partition() {
  }

	/* (non-Javadoc)
	 * @see org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator#evaluate(org.matheclipse.parser.interfaces.AbstractExpressionFactory, org.matheclipse.parser.interfaces.IAST)
	 */
	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() >= 3) && (functionList.size() <= 4) && (functionList.get(1) instanceof IAST)) {
			if (functionList.get(2) instanceof IInteger) {
				final IAST f = (IAST) functionList.get(1);
				final int n = ((IInteger) functionList.get(2)).getBigNumerator().intValue();
				final IAST result = F.ast(f.head());
				IAST temp;
				int i = n;
				int v = n;
				if ((functionList.size() == 4) && (functionList.get(3) instanceof IInteger)) {
					v = ((IInteger) functionList.get(3)).getBigNumerator().intValue();
				}
				while (i <= f.size()-1) {
					checkCanceled();
					temp = F.ast(f.head());
					for (int j = i - n; j < i; j++) {
						checkCanceled();
						temp.add(f.get(j+1));
					}
					i += v;
					result.add(temp);

					Util.checkCanceled();
				}
				return result;
			}
		}
		return null;
  }

}
