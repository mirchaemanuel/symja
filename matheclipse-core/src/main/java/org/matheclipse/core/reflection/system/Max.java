package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INumber;

public class Max extends AbstractFunctionEvaluator {
	public Max() {
	}

	public IExpr evaluate(final IAST ast) {
		IAST list = ast;
		if (list.size() > 1) {
			IAST resultList = list.copyHead();
			if (AST.COPY.flatten(F.List, list, resultList, 1)) {
				list = resultList;
			}
			// IExpr max = F.Times(F.CN1, ExprFactory.Infinity);
			IExpr max1;
			IExpr max2;
			max1 = list.get(1);
			IAST f = list.copyHead();
			int comp;
			for (int i = 2; i < list.size(); i++) {
				max2 = list.get(i);
				comp = Less.CONST.compare(max1, max2);

				if (comp == 1) {
					max1 = max2;
				} else {
					if (comp == 0) {
						// undetermined
						if (max1 instanceof INumber) {
							f.add(max2);
						} else {
							f.add(max1);
							max1 = max2;
						}
					}
				}
			}
			if (f.size() > 1) {
				f.add(1, max1);
				if (((AST) f).equals((Object) list)) {
					return null;
				}
				return f;
			} else {
				return max1;
			}
		}
		return null;
	}

}
