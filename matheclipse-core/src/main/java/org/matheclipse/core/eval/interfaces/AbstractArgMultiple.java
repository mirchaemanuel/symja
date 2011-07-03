package org.matheclipse.core.eval.interfaces;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.HashedOrderlessMatcher;
import org.matheclipse.parser.client.SyntaxError;

/**
 *
 */
public class AbstractArgMultiple extends AbstractArg2 {

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() > 2) {
			IAST temp = evaluateHashs(ast);
			if (temp != null) {
				return temp;
			}
		}
		if (ast.size() == 3) {
			return binaryOperator(ast.get(1), ast.get(2));
		}

		if (ast.size() > 3) {
			final ISymbol sym = ast.topHead();
			final IAST result = F.function(sym);
			IExpr tres;
			IExpr temp = ast.get(1);
			boolean evaled = false;
			int i = 2;

			while (i < ast.size()) {

				tres = binaryOperator(temp, ast.get(i));

				if (tres == null) {

					for (int j = i + 1; j < ast.size(); j++) {
						tres = binaryOperator(temp, ast.get(j));

						if (tres != null) {
							evaled = true;
							temp = tres;

							ast.remove(j);

							break;
						}
					}

					if (tres == null) {
						result.add(temp);
						if (i == ast.size() - 1) {
							result.add(ast.get(i));
						} else {
							temp = ast.get(i);
						}
						i++;
					}

				} else {
					evaled = true;
					temp = tres;

					if (i == (ast.size() - 1)) {
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
		return hashRuleMap.evaluate(ast);
	}
	
	/**
	 * @param lhs1
	 * @param lhs2
	 * @param rhs
	 * @param condition
	 * @see org.matheclipse.core.patternmatching.HashedOrderlessMatcher#setUpHashRule(org.matheclipse.core.interfaces.IExpr, org.matheclipse.core.interfaces.IExpr, org.matheclipse.core.interfaces.IExpr, org.matheclipse.core.interfaces.IExpr)
	 */
	public void setUpHashRule(IExpr lhs1, IExpr lhs2, IExpr rhs, IExpr condition) {
		getHashRuleMap().setUpHashRule(lhs1, lhs2, rhs, condition);
	}

	/**
	 * @param lhs1Str
	 * @param lhs2Str
	 * @param rhsStr
	 * @param conditionStr
	 * @throws SyntaxError
	 * @see org.matheclipse.core.patternmatching.HashedOrderlessMatcher#setUpHashRule(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setUpHashRule(String lhs1Str, String lhs2Str, String rhsStr, String conditionStr) throws SyntaxError {
		getHashRuleMap().setUpHashRule(lhs1Str, lhs2Str, rhsStr, conditionStr);
	}

	/**
	 * @param lhs1Str
	 * @param lhs2Str
	 * @param rhsStr
	 * @throws SyntaxError
	 * @see org.matheclipse.core.patternmatching.HashedOrderlessMatcher#setUpHashRule(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setUpHashRule(String lhs1Str, String lhs2Str, String rhsStr) throws SyntaxError {
		getHashRuleMap().setUpHashRule(lhs1Str, lhs2Str, rhsStr);
	}

}
