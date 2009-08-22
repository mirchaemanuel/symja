package org.matheclipse.core.reflection.system;


import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractArg1;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Decrement extends AbstractArg1 {

	@Override
	public IExpr e1ObjArg(final IExpr o0) {
		final EvalEngine engine = EvalEngine.get();
		if (o0 instanceof ISymbol) {

			final ISymbol sym = (ISymbol) o0;
			IExpr result;
			IExpr temp;
			if (sym.hasLocalVariableStack()) {
				result = sym.get();
				temp = execute(result, engine);
				sym.set(temp);
				return result;
			}

			result = engine.evaluate(sym);
			if ((result != null) && !result.equals(sym)) {
				temp = execute(result, engine);

				if (temp != null) {
//					HeapContext.enter();
//					try {
//						temp = temp.copy();
						sym.putDownRule(F.Set, true, sym, temp);
						return result;
//					} finally {
//						HeapContext.exit();
//					}
				}

			}

		}

		return null;
	}

	public IExpr execute(final IExpr first, final EvalEngine engine) {
		return engine.evaluate(F.Plus(first, F.CN1));
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}