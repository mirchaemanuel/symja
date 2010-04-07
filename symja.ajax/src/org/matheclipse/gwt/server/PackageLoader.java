package org.matheclipse.gwt.server;

import java.util.HashMap;
import java.util.HashSet;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.gwt.server.entity.PackageEntity;
import org.matheclipse.gwt.server.entity.PackageService;
import org.matheclipse.gwt.server.entity.SymbolService;
import org.matheclipse.parser.client.math.MathException;

/**
 * Package[{&lt;list of public package rule headers&gt;}, {&lt;list of rules in
 * this package&gt;}}
 * 
 */
public class PackageLoader {

	public PackageLoader() {
	}

	// public IExpr evaluate(final IAST ast) {
	// if (ast.size() != 4 || !(ast.get(1) instanceof IStringX) ||
	// !ast.get(2).isList() || !ast.get(3).isList()) {
	// throw new WrongNumberOfArguments(ast, 1, ast.size() - 1);
	// }
	// if (Config.SERVER_MODE) {
	// throw new RuleCreationError(null);
	// }
	// IAST symbols = (IAST) ast.get(2);
	// IAST list = (IAST) ast.get(3);
	// evalPackage(symbols, list);
	// // System.out.println(resultList);
	// return F.Null;
	// }

	public static void evalPackage(Long id, IAST publicSymbols, IAST list) {
		HashMap<ISymbol, ISymbol> convertedSymbolMap = new HashMap<ISymbol, ISymbol>();
		HashSet<ISymbol> publicSymbolSet = new HashSet<ISymbol>();
		String strId = Long.toString(id);

		ISymbol toSymbol;
		for (int i = 1; i < publicSymbols.size(); i++) {
			IExpr expr = publicSymbols.get(i);
			if (expr instanceof ISymbol) {
				publicSymbolSet.add((ISymbol) expr);
				toSymbol = F.predefinedSymbol(((ISymbol) expr).toString());
				convertedSymbolMap.put((ISymbol) expr, toSymbol);
			}
		}

		// determine "private package rule headers" in convertedSymbolMap
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i) instanceof IAST) {
				determineRuleHead(strId, (IAST) list.get(i), publicSymbolSet, convertedSymbolMap);
			}
		}

		// convert the rules into a new list:
		IAST resultList = F.List();
		for (int i = 1; i < list.size(); i++) {
			resultList.add(convertSymbolsInExpr(list.get(i), convertedSymbolMap));
		}
		EvalEngine engine = EvalEngine.get();
		try {
			engine.setPackageMode(true);
			// evaluate the new converted rules
			for (int i = 1; i < resultList.size(); i++) {
				EvalEngine.eval(resultList.get(i));
			}
		} finally {
			engine.setPackageMode(false);
		}
	}

	/**
	 * Delete the package data.
	 * 
	 * @param id
	 * @param publicSymbols
	 * @param list
	 */
	public static void deletePackage(final Long id, EvalEngine engine, IAST publicSymbols, IAST list) {
		HashMap<ISymbol, ISymbol> convertedSymbolMap = new HashMap<ISymbol, ISymbol>();
		HashSet<ISymbol> publicSymbolSet = new HashSet<ISymbol>();
		String strId = Long.toString(id);
		ISymbol toSymbol;
		for (int i = 1; i < publicSymbols.size(); i++) {
			IExpr expr = publicSymbols.get(i);
			if (expr instanceof ISymbol) {
				publicSymbolSet.add((ISymbol) expr);
				toSymbol = F.predefinedSymbol(((ISymbol) expr).toString());
				convertedSymbolMap.put((ISymbol) expr, toSymbol);
			}
		}

		// determine "private package rule headers" in convertedSymbolMap
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i) instanceof IAST) {
				determineRuleHead(strId, (IAST) list.get(i), publicSymbolSet, convertedSymbolMap);
			}
		}
		try {
			engine.setPackageMode(true);
			for (ISymbol symbol : publicSymbolSet) {
				symbol.clearAll(engine);
				SymbolService.delete(symbol.toString());
			}

			for (ISymbol symbol : convertedSymbolMap.values()) {
				symbol.clearAll(engine);
			}
		} finally {
			engine.setPackageMode(false);
		}
	}

	/**
	 * Determine the head symbol of the given rule
	 * 
	 * @param rule
	 * @param publicSymbolSet
	 * @param convertedSymbolMap
	 */
	private static void determineRuleHead(String id, IAST rule, HashSet<ISymbol> publicSymbolSet,
			HashMap<ISymbol, ISymbol> convertedSymbolMap) {
		ISymbol lhsHead;
		if (rule.size() > 1 && (rule.head().equals(F.Set) || rule.head().equals(F.SetDelayed))) {
			// determine the head to which this rule is associated
			lhsHead = null;
			if (rule.get(1) instanceof IAST) {
				lhsHead = ((IAST) rule.get(1)).topHead();
			} else if (rule.get(1) instanceof ISymbol) {
				lhsHead = (ISymbol) rule.get(1);
			}

			if (lhsHead != null && !publicSymbolSet.contains(lhsHead)) {
				ISymbol toSymbol = convertedSymbolMap.get(lhsHead);
				if (toSymbol == null) {
					// define a package private symbol
					toSymbol = F.predefinedSymbol("@" + id + lhsHead.toString());
					convertedSymbolMap.put(lhsHead, toSymbol);
				}
			}

		}
	}

	/**
	 * Convert all symbols which are keys in <code>convertedSymbols</code> in the
	 * given <code>expr</code> and return the resulting expression.
	 * 
	 * @param expr
	 * @param convertedSymbols
	 * @return
	 */
	private static IExpr convertSymbolsInExpr(IExpr expr, HashMap<ISymbol, ISymbol> convertedSymbols) {
		IExpr result = expr;
		if (expr instanceof IAST) {
			result = convertSymbolsInList((IAST) expr, convertedSymbols);
		} else if (expr instanceof ISymbol) {
			ISymbol toSymbol = convertedSymbols.get((ISymbol) expr);
			if (toSymbol != null) {
				result = toSymbol;
			}
		}

		return result;
	}

	/**
	 * Convert all symbols which are keys in <code>convertedSymbols</code> in the
	 * given <code>ast</code> list and return the resulting list.
	 * 
	 * @param ast
	 * @param convertedSymbols
	 * @return
	 */
	private static IAST convertSymbolsInList(IAST ast, HashMap<ISymbol, ISymbol> convertedSymbols) {
		IAST result = (IAST) ast.clone();
		for (int i = 0; i < result.size(); i++) {
			IExpr expr = result.get(i);
			if (expr instanceof IAST) {
				result.set(i, convertSymbolsInList((IAST) expr, convertedSymbols));
			} else if (expr instanceof ISymbol) {
				ISymbol toSymbol = convertedSymbols.get((ISymbol) expr);
				if (toSymbol != null) {
					result.set(i, toSymbol);
				}
			}
		}

		return result;
	}

	public IExpr numericEval(IAST functionList) {
		return null;
	}

	public void setUp(ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

	/**
	 * Load a package from the given input string
	 * 
	 */
	public static void loadPackage(final EvalEngine engine, final PackageEntity packageEntity) {
		// to avoid stack overflows the public symbols must be predefined in the
		// first step.
		String[] symbolNames = packageEntity.getPublicSymbols();
		for (int i = 0; i < symbolNames.length; i++) {
			F.predefinedSymbol(symbolNames[i]);
		}
		
		IExpr parsedExpression = engine.parse(packageEntity.getSource());
		if (parsedExpression != null && parsedExpression.isAST("Package", 4)) {
			IAST ast = (IAST) parsedExpression;
			if (ast.size() != 4 || !(ast.get(1) instanceof IStringX) || !ast.get(2).isList() || !ast.get(3).isList()) {
				throw new WrongNumberOfArguments(ast, 3, ast.size() - 1);
			}
			String packageName = ((IStringX) ast.get(1)).toString();

			IAST symbols = (IAST) ast.get(2);
			IAST list = (IAST) ast.get(3);
			evalPackage(packageEntity.getId(), symbols, list);
		} else {
			throw new MathException("Invalid Package format!");
		}
	}

	/**
	 * Install a new package. If old package with the given package name exists,
	 * delete old package data.
	 * 
	 * @param engine
	 * @param inputString
	 * @return
	 */
	public static String installPackage(final EvalEngine engine, final String inputString) {

		IExpr parsedExpression = engine.parse(inputString);
		if (parsedExpression != null && parsedExpression.isAST("Package", 4)) {
			IAST ast = (IAST) parsedExpression;
			if (ast.size() != 4 || !(ast.get(1) instanceof IStringX) || !ast.get(2).isList() || !ast.get(3).isList()) {
				throw new WrongNumberOfArguments(ast, 3, ast.size() - 1);
			}
			IAST publicSymbols = (IAST) ast.get(2);
			String[] symbolNames = new String[publicSymbols.size() - 1];
			String packageName = ((IStringX) ast.get(1)).toString();
			for (int i = 1; i < publicSymbols.size(); i++) {
				if (publicSymbols.get(i) instanceof ISymbol) {
					String name = publicSymbols.get(i).toString();
					if (Character.isUpperCase(name.charAt(0))) {
						symbolNames[i - 1] = publicSymbols.get(i).toString();
					} else {
						throw new MathException("Invalid package format - symbol name \"" + name + "\" at index: " + i + " in package \""
								+ packageName + "\" must start with an upper case character!");
					}
				} else {
					throw new MathException("Invalid package format - expecting a symbol name at index: " + i + " in package " + packageName);
				}
			}

			PackageEntity packageData = PackageService.findByName(packageName);
			if (packageData == null) {
				packageData = new PackageEntity(packageName, inputString, symbolNames);
				// the save method generates a new PackageEntity#id for us:
				PackageService.save(packageData);
			} else {
				deletePackageData(packageData.getId(), engine, packageData.getSource());
			}

			IAST list = (IAST) ast.get(3);
			evalPackage(packageData.getId(), publicSymbols, list);
			packageData.setSource(inputString);
			PackageService.save(packageData);
			SymbolService.update(publicSymbols, packageName);
			return packageName;
		}
		throw new MathException("Invalid package format!");
	}

	private static void deletePackageData(final Long id, final EvalEngine engine, final String inputString) {
		try {
			IExpr parsedExpression = engine.parse(inputString);
			if (parsedExpression != null && parsedExpression.isAST("Package", 4)) {
				IAST ast = (IAST) parsedExpression;
				if (ast.size() != 4 || !(ast.get(1) instanceof IStringX) || !ast.get(2).isList() || !ast.get(3).isList()) {
					throw new WrongNumberOfArguments(ast, 3, ast.size() - 1);
				}
				// String packageName = ((IStringX) ast.get(1)).toString();
				IAST symbols = (IAST) ast.get(2);
				IAST list = (IAST) ast.get(3);
				deletePackage(id, engine, symbols, list);
			} else {
				throw new MathException("Invalid Package format!");
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
