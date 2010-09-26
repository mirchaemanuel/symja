package org.matheclipse.core.eval.interfaces;

import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.HashedOrderlessMatcher;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.ast.ASTNode;

import com.google.common.collect.ArrayListMultimap;

/**
 *
 */
public class AbstractArgMultiple extends AbstractArg2 {

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() > 2) {
			IAST temp = evaluateHashs(functionList);
			if (temp != null) {
				return temp;
			}
		}
		if (functionList.size() == 3) {
			return binaryOperator(functionList.get(1), functionList.get(2));
		}

		if (functionList.size() > 3) {
			final ISymbol sym = functionList.topHead();
			final IAST result = F.function(sym);
			IExpr tres;
			IExpr temp = functionList.get(1);
			boolean evaled = false;
			int i = 2;

			while (i < functionList.size()) {

				tres = binaryOperator(temp, functionList.get(i));

				if (tres == null) {

					for (int j = i + 1; j < functionList.size(); j++) {
						tres = binaryOperator(temp, functionList.get(j));

						if (tres != null) {
							evaled = true;
							temp = tres;

							functionList.remove(j);

							break;
						}
					}

					if (tres == null) {
						result.add(temp);
						if (i == functionList.size() - 1) {
							result.add(functionList.get(i));
						} else {
							temp = functionList.get(i);
						}
						i++;
					}

				} else {
					evaled = true;
					temp = tres;

					if (i == (functionList.size() - 1)) {
						result.add(temp);
					}

					i++;
				}
			}

			if (evaled) {

				if ((result.size() == 2) && ((sym.getAttributes() & ISymbol.ONEIDENTITY) == ISymbol.ONEIDENTITY)) {
					return result.get(1);
				}

				return result;
			}
		}

		return null;
	}

	public HashedOrderlessMatcher getHashRuleMap() {
		return null;
	}

	public IAST evaluateHashs(final IAST ast) {
		HashedOrderlessMatcher hashRuleMap = getHashRuleMap();
		if (hashRuleMap == null) {
			return null;
		}
		return hashRuleMap.evaluate2(ast);
	}

}
