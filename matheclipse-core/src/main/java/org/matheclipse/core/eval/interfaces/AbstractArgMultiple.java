package org.matheclipse.core.eval.interfaces;

import java.util.List;

import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPatternMatcher;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.visit.HashValueVisitor;
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
		if (functionList.size() > 4) {
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

	public void setUpHashRule(final String lhs1Str, final String lhs2Str, final String rhsStr) throws SyntaxError {

		final Parser parser = new Parser();
		ASTNode parsedAST = parser.parse(lhs1Str);
		final IExpr lhs1 = AST2Expr.CONST.convert(parsedAST);
		parsedAST = parser.parse(lhs2Str);
		final IExpr lhs2 = AST2Expr.CONST.convert(parsedAST);
		parsedAST = parser.parse(rhsStr);
		final IExpr rhs = AST2Expr.CONST.convert(parsedAST);
		setUpHashRule(lhs1, lhs2, rhs);
	}

	private void setUpHashRule(final IExpr lhs1, final IExpr lhs2, final IExpr rhs) {
		HashRule hashRule = new HashRule(lhs1, lhs2, rhs);
		getHashRuleMap().put(hashRule.getHash1(), hashRule);
	}

	public ArrayListMultimap<Integer, HashRule> getHashRuleMap() {
		return null;
	}

	public IAST evaluateHashs(final IAST ast) {
		ArrayListMultimap<Integer, HashRule> hashRuleMap = getHashRuleMap();
		if (hashRuleMap == null || hashRuleMap.size() == 0) {
			return null;
		}
		boolean evaled = false;
		IAST result = null;
		int[] hashValues = new int[(ast.size() - 1)];
		HashValueVisitor v = new HashValueVisitor();
		for (int i = 0; i < hashValues.length; i++) {
			hashValues[i] = ast.get(i + 1).accept(v);
			v.setUp();
		}
		for (int i = 0; i < hashValues.length; i++) {
			if (hashValues[i] == 0) {
				// already used entry
				continue;
			}
			final List<HashRule> hashRuleList = hashRuleMap.get(hashValues[i]);
			if (hashRuleList != null) {
				evaled: for (HashRule hashRule : hashRuleList) {
					IPatternMatcher<IExpr> matcher1 = hashRule.getLHS1();
					if (matcher1.apply(ast.get(i + 1))) {
						for (int j = 0; j < hashValues.length; j++) {
							if (hashValues[j] != hashRule.getHash2() || j == i) {
								// already used entry
								continue;
							}

							IPatternMatcher<IExpr> matcher2 = hashRule.getLHS2();
							if (matcher2.apply(ast.get(j + 1)) && matcher1.checkPatternMatcher((PatternMatcher) matcher2)) {
								// found binary combination; mark entries as used
								hashValues[i] = 0;
								hashValues[j] = 0;
								if (!evaled) {
									result = ast.copyHead();
									evaled = true;
								}
								result.add(hashRule.getRHS());
								break evaled;
							}
						}
					}
				}
			}
		}

		if (evaled) {
			// append rest of unevaluated arguments
			for (int i = 0; i < hashValues.length; i++) {
				if (hashValues[i] != 0) {
					result.add(ast.get(i + 1));
				}
			}
		}
		return result;
	}
}
