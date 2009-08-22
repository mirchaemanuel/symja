package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import com.google.common.base.Predicate;

/**
 * Predicate function
 * 
 * Returns <code>True</code> if the 1st argument is an even integer number;
 * <code>False</code> otherwise
 */
public class EvenQ extends AbstractFunctionEvaluator implements
		Predicate<IExpr> {
	/**
	 * Constructor for the unary predicate
	 */
	public final static EvenQ CONST = new EvenQ();
	
	public EvenQ() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		return F.bool(apply(functionList.get(1)));
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.LISTABLE);
	}

	public boolean apply(final IExpr expr) {
		return (expr instanceof IntegerSym) && ((IntegerSym) expr).isEven();
	}
}
