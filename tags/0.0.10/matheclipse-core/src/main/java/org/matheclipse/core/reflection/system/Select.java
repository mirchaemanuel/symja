package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.generic.Predicates;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class Select implements IFunctionEvaluator {

	public Select() {
	}

	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() == 3) && (functionList.get(1) instanceof IAST)) {
			return select((IAST) functionList.get(1), functionList.get(2));
		} else if ((functionList.size() == 4) && (functionList.get(1) instanceof IAST) && (functionList.get(3) instanceof IInteger)) {
			final int resultLimit = Validate.checkIntType(functionList, 3);
			return select((IAST) functionList.get(1), functionList.get(2), resultLimit);
		}
		return null;
	}

	public static IAST select(final IAST list, final IExpr head) {
		return list.filter(list.copyHead(), Predicates.isTrue(head));
	}

	public static IAST select(final IAST list, final IExpr head, final int resultLimit) {
		return list.args().filter(list.copyHead(), Predicates.isTrue(head), resultLimit);
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}

}
