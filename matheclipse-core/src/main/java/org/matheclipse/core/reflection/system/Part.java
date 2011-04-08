package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class Part implements IFunctionEvaluator {
	public Part() {
	}

	public IExpr evaluate(final IAST ast) {
		if (ast.size() == 3) {
			if (ast.get(1) instanceof IAST) {
				final IAST arg1 = (IAST) ast.get(1);
				final IExpr arg2 = ast.get(2);

				if (arg2 instanceof IInteger) {
					try {
						final int indx = Validate.checkIntType(ast, 2);
						return getIndex(arg1, indx); // (IInteger) arg1);
					} catch (final IndexOutOfBoundsException e) {
						if (Config.DEBUG) {
							e.printStackTrace();
						}
						return null;
					}
				}
				if (arg2.isList()) {
					final IAST lst = (IAST) arg2;
					final IAST result = F.ast(F.List);

					for (int i = 1; i < lst.size(); i++) {
						final IExpr expr = lst.get(i);

						if (expr instanceof IInteger) {
							IExpr ires = null;

							final int indx = Validate.checkIntType(lst, i);
							ires = getIndex(arg1, indx);// (IInteger) expr);

							if (ires == null) {
								return null;
							}

							result.add(ires);
						} else {

							final IAST part = F.ast(F.Part);
							part.add(expr);
							result.add(part);
						}
					}

					return result;
				}
			}
		}

		return null;
	}

	IExpr getIndex(final IAST ast, int indx) {

		// final int indx = iIndx.toInt();
		// check index range
		
		Validate.checkRange(ast, 0, ast.size());
		if ((indx < 0) || (indx >= ast.size())) {
			throw new IndexOutOfBoundsException("index: " + indx);
		}
		return ast.get(indx);
		// StringBuffer buf = new StringBuffer("index ");
		//
		// buf.append(hi.toString());
		// buf.append(" out of range [1..");
		// buf.append(new Integer(fun.size()).toString());
		// buf.append("] in expression: ");
		// buf.append(fun.toString());
		//
		// throw new ThrowException(C.OutOfRangeError, C.Part, new
		// HString(buf.toString()));
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
	}
}
