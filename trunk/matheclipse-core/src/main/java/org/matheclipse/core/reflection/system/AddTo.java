package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractArg2;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Operator +=
 * 
 */
public class AddTo extends AbstractArg2 {
	@Override
	public IExpr e2ObjArg(final IExpr o0, final IExpr o1) {
		final EvalEngine engine = EvalEngine.get();
		if (o0 instanceof ISymbol) {

			final IExpr v1 = engine.evaluate(o1);
			final ISymbol sym = (ISymbol) o0;
			IExpr result;
			IExpr temp;
			if (sym.hasLocalVariableStack()) {
				result = sym.get();
				temp = execute(result, v1, engine);
				sym.set(temp);
				return temp;
			}

			result = engine.evaluate(sym);
			if ((result != null) && !result.equals(sym)) {
				temp = execute(result, v1, engine);

				if (temp != null) {
//					HeapContext.enter();
//					try {
						// temp = temp.copy();
						sym.putDownRule(F.Set, true, sym, temp);
						return temp;
//					} finally {
//						HeapContext.exit();
//					}
				}

			}

		}

		return null;
	}

	public IExpr execute(final IExpr first, final IExpr second, final EvalEngine engine) {
		return engine.evaluate(F.Plus(first, second));
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}