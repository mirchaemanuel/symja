package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import com.google.common.base.Predicate;

/**
 * Returns <code>True</code>, if the given expression is an atomic object
 * (i.e. no AST instance)
 * 
 */
public class AtomQ extends AbstractFunctionEvaluator implements
		Predicate<IExpr> {
	/**
	 * Constructor for the unary predicate
	 */
	public final static AtomQ CONST = new AtomQ();
	
	public AtomQ() {
//		System.out.println(getClass().getCanonicalName());
	}

	/**
	 * Returns <code>True</code> if the 1st argument is an atomic object;
	 * <code>False</code> otherwise
	 */
	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			throw new WrongNumberOfArguments(functionList, 1, functionList.size() - 1);
		}
		return F.bool(functionList.get(1).isAtom());
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.LISTABLE);
	}

	public boolean apply(final IExpr obj) {
		return obj.isAtom();
	}

}
