package org.matheclipse.core.reflection.system;

import java.io.IOException;
import java.io.PrintStream;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * Get the default value for a symbol (i.e. <code>1</code> is the default value
 * for <code>Times</code>, <code>0</code> is the default value for
 * <code>Plus</code>).
 */
public class Default extends AbstractFunctionEvaluator {

	public Default() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 2, 4);
		if (!(ast.get(1) instanceof ISymbol)) {
			return null;
		}
		ISymbol symbol = (ISymbol) ast.get(1);
		if (ast.size() > 2) {
			int pos = Validate.checkIntType(ast, 2);
			return symbol.getDefaultValue(pos);
		} else {
			return symbol.getDefaultValue();
		}
	}

}
