package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.generic.UnaryMap;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.generic.nested.LevelSpec;

/**
 */
public class Map extends AbstractFunctionEvaluator {

	public Map() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if ((functionList.size() != 3) && (functionList.size() != 4)) {
			return null;
		}
		LevelSpec level = null;
		if (functionList.size() == 4) {
			level = new LevelSpecification(functionList.get(3));
		} else {
			level = new LevelSpec(1); 
		}
		final IAST ast = F.ast(functionList.get(1));
		final IExpr result = (IExpr) AST.COPY.map(functionList.get(2), new UnaryMap(ast), level, 1);
		return result == null ? functionList.get(2) : result;

	}

}
