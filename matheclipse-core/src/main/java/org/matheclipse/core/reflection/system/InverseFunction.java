package org.matheclipse.core.reflection.system;

import java.util.HashMap;
import java.util.Map;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 *  
 */
public class InverseFunction extends AbstractFunctionEvaluator {
	private static Map<IExpr, IExpr> INVERSE_FUNCTIONS = new HashMap<IExpr, IExpr>();

	static {
		INVERSE_FUNCTIONS.put(F.Cos, F.ArcCos);
		INVERSE_FUNCTIONS.put(F.Sin, F.ArcSin);
		INVERSE_FUNCTIONS.put(F.Tan, F.ArcTan);
		
		INVERSE_FUNCTIONS.put(F.ArcCos, F.Cos);
		INVERSE_FUNCTIONS.put(F.ArcSin, F.Sin);
		INVERSE_FUNCTIONS.put(F.ArcTan, F.Tan);
	}

	public InverseFunction() {

	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 2);

		IExpr arg1 = ast.get(1);
		IExpr inverseFunction = INVERSE_FUNCTIONS.get(arg1);
		if (inverseFunction != null) {
			return inverseFunction;
		}
		return null;
	}

}