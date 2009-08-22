package org.matheclipse.core.list.algorithms;

import static org.matheclipse.basic.Util.checkCanceled;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.matheclipse.core.expression.AST;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.ExprComparator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * 
 */
public class EvaluationSupport {

	/**
	 * Flatten the list [i.e. the list getHeader() has the attribute ISymbol.FLAT]
	 * example: suppose the Symbol f has the attribute ISymbol.FLAT
	 * f[a,b,f[x,y,f[u,v]],z] ==> f[a,b,x,y,u,v,z]
	 * 
	 * @param list
	 * 
	 * @return returns the flattened list
	 */
	public static IAST flatten(final IAST list) {
		final IAST res = F.ast(list.getHeader());

		if (AST.COPY.flatten(list.getHeader(), list, res, 1)) {
			return res;
		}
		return null;
	}

	/**
	 * Sort the list [i.e. the list getHeader() has the attribute
	 * ISymbol.ORDERLESS] example: suppose the Symbol s has the attribute
	 * ISymbol.ORDERLESS f[z,d,a,b] ==> f[a,b,d,z]
	 * 
	 * @param session
	 * @param list
	 * @return returns the sorted list
	 */
	public final static void sort(final IAST ast) {
		if (ast.size() > 2) {
			if (ast.size() == 3) {
				// optimize special case
				if (ast.get(1).compareTo(ast.get(2)) > 0) {
					// swap arguments
					IExpr temp = ast.get(2);
					ast.set(2, ast.get(1));
					ast.set(1, temp);
				}
			} else {
				ast.args().sort(ExprComparator.CONS);
			}
		}
	}

	public final static void sort(final IAST list, Comparator<IExpr> comparator) {
		list.args().sort(comparator);
	}

	/**
	 * Thread through all lists in the arguments of the IAST [i.e. the list header
	 * has the attribute ISymbol.LISTABLE] example: Sin[{2,x,Pi}] ==>
	 * {Sin[2],Sin[x],Sin[Pi]}
	 * 
	 * @param list
	 * @param listLength
	 *          he length of the list
	 * 
	 * 
	 * @return Description of the Returned Value
	 */
	public static IAST threadList(final IAST list, final int listLength, final int headOffset) {

		final IAST res0 = F.ast(F.List, listLength, true);

		for (int j = headOffset; j < listLength + headOffset; j++) {
			checkCanceled();
			final IAST res1 = F.ast(list.getHeader(), list.size() - headOffset, true);

			for (int i = headOffset; i < list.size(); i++) {
				checkCanceled();
				if ((list.get(i) instanceof IAST) && (((IAST) list.get(i)).getHeader() == F.List)) {
					final IAST arg = (IAST) list.get(i);
					res1.set(i, arg.get(j));
				} else {
					res1.set(i, list.get(i));
				}
			}
			// end for j

			res0.set(j, res1);
		}
		// end for i

		return res0;
	}

	/**
	 * Substitute all symbols in the given expression with the current value of
	 * the given arrays
	 * 
	 * @param expression
	 * 
	 * @return
	 */
	public static <T extends IExpr> IExpr substituteLocalVariables(final IExpr expression, final ArrayList<T> symbolList,
			final IExpr[] valueList) {
		final Map<IExpr, IExpr> ruleMap = new HashMap<IExpr, IExpr>();
		for (int i = 0; i < symbolList.size(); i++) {
			checkCanceled();
			ruleMap.put(symbolList.get(i), valueList[i]);
		}
		final IExpr result = (IExpr) AST.COPY.replaceAll(expression, ruleMap, 1);
		return (result == null) ? expression : result;
	}
}
