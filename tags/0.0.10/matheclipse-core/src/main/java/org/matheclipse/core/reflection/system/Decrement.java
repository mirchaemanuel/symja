package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractArg1;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
 

public class Decrement extends AbstractArg1 {

	public Decrement() {
		super();
	}

	@Override
	public IExpr e1ObjArg(final IExpr o0) {
		final EvalEngine engine = EvalEngine.get();
		if (o0 instanceof ISymbol) {

			final ISymbol sym = (ISymbol) o0;

			if (sym.hasLocalVariableStack()) {
				IExpr symbolValue = sym.get();
				IExpr calculatedResult = execute(symbolValue, engine);
				sym.set(calculatedResult);
				return getResult(symbolValue, calculatedResult);
			} else {
				IExpr symbolValue = engine.evaluate(sym);
				if ((symbolValue != null) && !symbolValue.equals(sym)) {
					IExpr calculatedResult = execute(symbolValue, engine);
					sym.putDownRule(F.Set, true, sym, calculatedResult);
					return getResult(symbolValue, calculatedResult);
				}
			}

		}

		return null;
	}

	protected IExpr getResult(IExpr symbolValue, IExpr calculatedResult) {
		return symbolValue;
	}

	protected IExpr execute(final IExpr first, final EvalEngine engine) {
		return engine.evaluate(F.Plus(first, F.CN1));
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}