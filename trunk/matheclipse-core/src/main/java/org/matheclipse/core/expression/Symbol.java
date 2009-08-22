package org.matheclipse.core.expression;

import static org.matheclipse.basic.Util.checkCanceled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.RuleCreationError;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.generic.UnaryVariable2Slot;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IEvaluationEngine;
import org.matheclipse.core.interfaces.IEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.IPatternMatcher;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.patternmatching.PatternMatcherAndEvaluator;
import org.matheclipse.core.patternmatching.PatternMatcherAndInvoker;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;
import org.matheclipse.generic.interfaces.Pair;

/**
 * Implements Symbols for function, constant and variable names
 * 
 */
public class Symbol extends ExprImpl implements ISymbol {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6865099709235625213L;

	// protected static final XmlFormat<SymbolImpl> SYMBOL_XML = new
	// XmlFormat<SymbolImpl>(SymbolImpl.class) {
	// @Override
	// public void format(SymbolImpl obj, XmlElement xml) {
	// SymbolImpl expr = obj;
	// xml.setAttribute("name", expr.fSymbolName);
	// }
	//
	// @Override
	// public SymbolImpl parse(XmlElement xml) {
	// org.matheclipse.core.expression.ExprFactory factory =
	// org.matheclipse.core.expression.ExprFactory.get();
	// String name = xml.getAttribute("name", "");
	// return (SymbolImpl) factory.createSymbol(name);
	// }
	// };

	private int fAttributes = NOATTRIBUTE;

	private Map<IExpr, Pair<ISymbol, IExpr>> fEqualRules = null;

	private transient IEvaluator fEvaluator;

	private Map<Integer, List<IPatternMatcher<IExpr>>> fSimplePatternRules = null;

	private List<IPatternMatcher<IExpr>> fPatternRules = null;

	/* package private */String fSymbolName;

	public Symbol(final String symbolName) {
		this(symbolName, null);
	}

	/**
	 * do not use directly, needed for XML transformations
	 * 
	 */
	protected Symbol() {
		super();
		fSymbolName = null;
		fEvaluator = null;
		// hash = fSymbolName.hashCode();
	}

	public Symbol(final String symbolName, final IEvaluator evaluator) {
		super();
		fSymbolName = symbolName;
		fEvaluator = evaluator;
		// hash = fSymbolName.hashCode();
	}

	public IExpr apply(IExpr... expressions) {
		return F.ast(expressions, this);
	}

	public void pushLocalVariable() {
		pushLocalVariable(null);
	}

	public void pushLocalVariable(final IExpr expression) {
		final Stack<IExpr> localVariableStack = EvalEngine.localStackCreate(fSymbolName);
		localVariableStack.push(expression);
	}

	public void popLocalVariable() {
		final Stack<IExpr> fLocalVariableStack = EvalEngine.localStack(fSymbolName);
		fLocalVariableStack.pop();
	}

	public void clear() {
		if (Config.SERVER_MODE && (fSymbolName.charAt(0) != '$')) {
			throw new RuleCreationError(null);
		}
		fEqualRules = null;
		fSimplePatternRules = null;
		fPatternRules = null;
	}

	public void clearAll() {
		if (Config.SERVER_MODE && (fSymbolName.charAt(0) != '$')) {
			throw new RuleCreationError(null);
		}
		clear();
		fAttributes = NOATTRIBUTE;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Symbol)) {
			return false;
		}
		// if (hash != ((SymbolImpl) obj).hash) {
		// return false;
		// }
		if (fSymbolName.equals(((Symbol) obj).fSymbolName)) {
			if (Config.DEBUG) {
				System.err.println(fSymbolName + " EQUALS " + ((Symbol) obj).fSymbolName);
			}
			return true;
		}
		return false;
	}

	public IExpr evalDownRule(final IEvaluationEngine ee, final IExpr expression) {
		Pair<ISymbol, IExpr> res;
		if (fEqualRules != null) {
			res = fEqualRules.get(expression);
			if (res != null) {
				// if (expression instanceof ISymbol) {
				// System.out.println(expression + " = " + res.getSecond());
				// }
				return res.getSecond();
			}
		}

		IExpr result;
		PatternMatcher pmEvaluator;
		if ((fSimplePatternRules != null) && (expression instanceof IAST)) {
			final Integer hash = Integer.valueOf(((IAST) expression).patternHashCode());
			final List<IPatternMatcher<IExpr>> list = fSimplePatternRules.get(hash);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					checkCanceled();
					pmEvaluator = (PatternMatcher) list.get(i);
					result = pmEvaluator.eval(expression);
					if (result != null) {
						return result;
					}
				}
			}
		}

		if (fPatternRules != null) {
			for (int i = 0; i < fPatternRules.size(); i++) {
				checkCanceled();
				pmEvaluator = (PatternMatcher) fPatternRules.get(i);
				result = pmEvaluator.eval(expression);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	public int getAttributes() {
		return fAttributes;
	}

	public IEvaluator getEvaluator() {
		return fEvaluator;
	}

	public boolean hasLocalVariableStack() {
		final Stack<IExpr> localVariableStack = EvalEngine.localStack(fSymbolName);
		return (localVariableStack != null) && !(localVariableStack.isEmpty());
	}

	public IExpr get() {
		final Stack<IExpr> localVariableStack = EvalEngine.localStack(fSymbolName);
		if (localVariableStack == null) {
			return null;
		}
		return localVariableStack.peek();
	}

	public void set(final IExpr value) {
		final Stack<IExpr> localVariableStack = EvalEngine.localStack(fSymbolName);

		localVariableStack.set(localVariableStack.size() - 1, value);
	}

	@Override
	public int hashCode() {
		return fSymbolName.hashCode();
	}

	public int hierarchy() {
		return SYMBOLID;
	}

	// public boolean isLocalVariableStackEmpty() {
	// ExpressionFactory f = ExpressionFactory.get();
	// Stack<IExpr> localVariableStack = f.getLocalVarStack(fSymbolName);
	// return localVariableStack.isEmpty();
	// }

	public boolean isString(final String str) {
		return fSymbolName.equals(str);
	}

	public IPatternMatcher putDownRule(ISymbol symbol, final boolean equalRule, final IExpr leftHandSide, final IExpr rightHandSide) {
		return putDownRule(symbol, equalRule, leftHandSide, rightHandSide, null, DEFAULT_RULE_PRIORITY);
	}

	public IPatternMatcher putDownRule(ISymbol symbol, final boolean equalRule, final IExpr leftHandSide, final IExpr rightHandSide,
			final IExpr condition) {
		return putDownRule(symbol, equalRule, leftHandSide, rightHandSide, condition, DEFAULT_RULE_PRIORITY);
	}

	public PatternMatcher putDownRule(ISymbol setSymbol, final boolean equalRule, final IExpr leftHandSide,
			final IExpr rightHandSide, final IExpr condition, final int priority) {
		if (Config.DEBUG) {
			if (rightHandSide.isAST("Condition")) {
				throw new RuntimeException("Condition  not allowed in right-hand-side");
			}
		}
		EvalEngine engine = EvalEngine.get();
		if (!engine.isPackageMode()) {
			if (Config.SERVER_MODE && (fSymbolName.charAt(0) != '$')) {
				throw new RuleCreationError(leftHandSide);
			}

			engine.addModifiedVariable(this);
		}

		if (equalRule) {
			if (fEqualRules == null) {
				fEqualRules = new HashMap<IExpr, Pair<ISymbol, IExpr>>();
			}
			fEqualRules.put(leftHandSide, new Pair<ISymbol, IExpr>(setSymbol, rightHandSide));
			if (condition != null) {
				throw new RuleCreationError(leftHandSide, rightHandSide, condition);
			}
			return null;
		}

		final PatternMatcherAndEvaluator pmEvaluator = new PatternMatcherAndEvaluator(setSymbol, leftHandSide, rightHandSide);

		if (pmEvaluator.isRuleWithoutPatterns()) {
			if (fEqualRules == null) {
				fEqualRules = new HashMap<IExpr, Pair<ISymbol, IExpr>>();
			}
			fEqualRules.put(leftHandSide, new Pair<ISymbol, IExpr>(setSymbol, rightHandSide));
			if (condition != null) {
				throw new RuleCreationError(leftHandSide, rightHandSide, condition);
			}
			return null;
		}

		pmEvaluator.setCondition(condition);
		if (!isRuleWithPatternAsFirstArgument(leftHandSide)) {
			if (fSimplePatternRules == null) {
				fSimplePatternRules = new HashMap<Integer, List<IPatternMatcher<IExpr>>>();
			}
			return addSimplePatternRule(leftHandSide, pmEvaluator);

		} else {

			if (fPatternRules == null) {
				fPatternRules = new ArrayList<IPatternMatcher<IExpr>>();
			}

			for (int i = 0; i < fPatternRules.size(); i++) {
				checkCanceled();
				if (pmEvaluator.equals(fPatternRules.get(i))) {
					fPatternRules.set(i, pmEvaluator);

					return pmEvaluator;
				}
			}
			fPatternRules.add(pmEvaluator);
			return pmEvaluator;
		}

	}

	private PatternMatcher addSimplePatternRule(final IExpr leftHandSide, final PatternMatcher pmEvaluator) {
		final Integer hash = Integer.valueOf(((IAST) leftHandSide).patternHashCode());
		List<IPatternMatcher<IExpr>> list = fSimplePatternRules.get(hash);
		if (list == null) {
			list = new ArrayList<IPatternMatcher<IExpr>>();
			fSimplePatternRules.put(hash, list);
		} else {
			for (int i = 0; i < list.size(); i++) {
				checkCanceled();
				if (pmEvaluator.equals(fSimplePatternRules.get(i))) {
					list.set(i, pmEvaluator);
					return pmEvaluator;
				}
			}
		}
		list.add(pmEvaluator);
		return pmEvaluator;
	}

	public PatternMatcher putDownRule(final PatternMatcherAndInvoker pmEvaluator) {
		final IExpr leftHandSide = pmEvaluator.getLHS();
		if (!isRuleWithPatternAsFirstArgument(leftHandSide)) {
			if (fSimplePatternRules == null) {
				fSimplePatternRules = new HashMap<Integer, List<IPatternMatcher<IExpr>>>();
			}
			return addSimplePatternRule(leftHandSide, pmEvaluator);

		} else {

			if (fPatternRules == null) {
				fPatternRules = new ArrayList<IPatternMatcher<IExpr>>();
			}

			for (int i = 0; i < fPatternRules.size(); i++) {
				checkCanceled();
				if (pmEvaluator.equals(fPatternRules.get(i))) {
					fPatternRules.set(i, pmEvaluator);

					return pmEvaluator;
				}
			}
			fPatternRules.add(pmEvaluator);
			return pmEvaluator;
		}
	}

	private boolean isRuleWithPatternAsFirstArgument(final IExpr patternExpr) {
		if (patternExpr instanceof IAST) {
			final IAST ast = ((IAST) patternExpr);
			if (ast.size() > 1) {
				if ((ast.get(1) instanceof IPattern)) {
					return true;
				}
				final int attr = ast.topHead().getAttributes();
				if ((ISymbol.ORDERLESS & attr) == ISymbol.ORDERLESS) {
					return true;
				}
			}
		} else if (patternExpr instanceof IPattern) {
			return true;
		}
		return false;
	}

	public void setAttributes(final int attributes) {
		fAttributes = attributes;
	}

	/**
	 * @param evaluator
	 */
	public void setEvaluator(final IEvaluator evaluator) {
		fEvaluator = evaluator;
	}

	// public String toString() {
	// return fSymbolName;
	// }

	/**
	 * @return Returns the equalRules.
	 */
	public Map<IExpr, Pair<ISymbol, IExpr>> getEqualRules() {
		return fEqualRules;
	}

	/**
	 * @param equalRules
	 *          The equalRules to set.
	 */
	public void setEqualRules(final Map<IExpr, Pair<ISymbol, IExpr>> equalRules) {
		fEqualRules = equalRules;
	}

	/**
	 * @return Returns the patternRules.
	 */
	// public List<IPatternMatcher> getPatternRules() {
	// return fSimplePatternRules;
	// }
	/**
	 * @param patternRules
	 *          The patternRules to set.
	 */
	// public void setPatternRules(List<IPatternMatcher> patternRules) {
	// fSimplePatternRules = patternRules;
	// }
	// @Override
	// public boolean move(final ObjectSpace os) {
	// return super.move(os);
	// }

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(final IExpr obj) {
		if (obj instanceof Symbol) {
			return fSymbolName.compareToIgnoreCase(((Symbol) obj).fSymbolName);
		}
		if (obj instanceof AST) {
			final AST ast = (AST) obj;
			final IExpr header = ast.head();
			if (ast.size() > 1) {
				if (header == F.Power && ast.size() == 3) {
					if (ast.get(1) instanceof ISymbol) {
						final int cp = fSymbolName.compareToIgnoreCase(((Symbol) ast.get(1)).fSymbolName);
						if (cp != 0) {
							return cp;
						}
						return F.C1.compareTo(ast.get(2));
					}
				} else if (header == F.Times) {
					// compare with the last ast element:
					final IExpr lastTimes = ast.get(ast.size() - 1);
					if (lastTimes instanceof AST) {
						final IExpr lastTimesHeader = ((IAST) lastTimes).head();
						if ((lastTimesHeader == F.Power) && (((IAST) lastTimes).size() == 3)) {
							final int cp = compareTo(((IAST) lastTimes).get(1));
							if (cp != 0) {
								return cp;
							}
							return F.C1.compareTo(((IAST) lastTimes).get(2));
						}
					}
					final int cp = compareTo(lastTimes);
					if (cp != 0) {
						return cp;
					}
				}
			}
			return -1;
		}
		return (hierarchy() - (obj).hierarchy());
	}

	@Override
	public boolean isAtom() {
		return true;
	}

	@Override
	public boolean isTrue() {
		return fSymbolName.equals("True");
	}

	@Override
	public boolean isFalse() {
		return fSymbolName.equals("False");
	}

	public ISymbol head() {
		return F.SymbolHead;
	}

	public String getSymbol() {
		return fSymbolName;
	}

	@Override
	public IExpr variables2Slots(final Map<IExpr, IExpr> map, final List<IExpr> variableList) {
		final UnaryVariable2Slot uv2s = new UnaryVariable2Slot(map, variableList);
		return uv2s.apply(this);
	}

	@Override
	public String toString() {
		return fSymbolName;
	}

	public List<IAST> definition() {
		ArrayList<IAST> definitionList = new ArrayList<IAST>();
		Iterator<IExpr> iter;
		IExpr key;
		Pair<ISymbol, IExpr> pair;
		IExpr condition;
		ISymbol setSymbol;
		IAST ast;
		PatternMatcherAndEvaluator pmEvaluator;
		if (fEqualRules != null && fEqualRules.size() > 0) {
			iter = fEqualRules.keySet().iterator();
			while (iter.hasNext()) {
				key = iter.next();
				pair = fEqualRules.get(key);
				setSymbol = pair.getFirst();
				ast = F.ast(setSymbol);
				ast.add(key);
				ast.add(pair.getSecond());
				definitionList.add(ast);
			}
		}
		if (fSimplePatternRules != null && fSimplePatternRules.size() > 0) {
			Iterator<List<IPatternMatcher<IExpr>>> listIter;
			listIter = fSimplePatternRules.values().iterator();
			while (listIter.hasNext()) {
				final List<IPatternMatcher<IExpr>> list = listIter.next();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof PatternMatcherAndEvaluator) {
						pmEvaluator = (PatternMatcherAndEvaluator) list.get(i);
						setSymbol = pmEvaluator.getSetSymbol();

						ast = F.ast(setSymbol);
						ast.add(pmEvaluator.getLHS());
						condition = pmEvaluator.getCondition();
						if (condition != null) {
							ast.add(F.Condition(pmEvaluator.getRHS(), condition));
						} else {
							ast.add(pmEvaluator.getRHS());
						}
						definitionList.add(ast);
					}
				}
			}
		}
		if (fPatternRules != null && fPatternRules.size() > 0) {
			for (int i = 0; i < fPatternRules.size(); i++) {
				if (fPatternRules.get(i) instanceof PatternMatcherAndEvaluator) {
					pmEvaluator = (PatternMatcherAndEvaluator) fPatternRules.get(i);
					setSymbol = pmEvaluator.getSetSymbol();
					ast = F.ast(setSymbol);
					ast.add(pmEvaluator.getLHS());
					condition = pmEvaluator.getCondition();
					if (condition != null) {
						ast.add(F.Condition(pmEvaluator.getRHS(), condition));
					} else {
						ast.add(pmEvaluator.getRHS());
					}
					definitionList.add(ast);
				}
			}

		}

		return definitionList;
	}

	public String definitionToString() throws IOException {
		StringBufferWriter buf = new StringBufferWriter();
		buf.setIgnoreNewLine(true);
		List<IAST> list = definition();
		buf.append("{\n  ");
		for (int i = 0; i < list.size(); i++) {
			OutputFormFactory.get().convert(buf, list.get(i));
			if (i < list.size()-1) {
				buf.append(",\n  ");
			}
		}
		buf.append("\n}\n");
		return buf.toString();
	}

	/**
	 * Deserialize Symbol from stream
	 * 
	 * @param stream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readSymbol(java.io.ObjectInputStream stream) throws IOException {
		fSymbolName = stream.readUTF();
		fAttributes = stream.read();

		String astString;
		IExpr key;
		IExpr value;
		EvalEngine engine = EvalEngine.get();
		ISymbol setSymbol;
		int len = stream.read();
		if (len > 0) {
			fEqualRules = new HashMap<IExpr, Pair<ISymbol, IExpr>>();
			for (int i = 0; i < len; i++) {
				astString = stream.readUTF();
				setSymbol = F.symbol(astString);

				astString = stream.readUTF();
				key = engine.parse(astString);
				astString = stream.readUTF();
				value = engine.parse(astString);
				fEqualRules.put(key, new Pair<ISymbol, IExpr>(setSymbol, value));
			}
		}

		len = stream.read();
		IExpr lhs;
		IExpr rhs;
		IExpr condition;
		int listLength;
		int condLength;
		PatternMatcherAndEvaluator pmEvaluator;
		if (len > 0) {
			fSimplePatternRules = new HashMap<Integer, List<IPatternMatcher<IExpr>>>();

			for (int i = 0; i < len; i++) {
				listLength = stream.read();
				for (int j = 0; j < listLength; j++) {
					astString = stream.readUTF();
					setSymbol = F.symbol(astString);

					astString = stream.readUTF();
					lhs = engine.parse(astString);
					astString = stream.readUTF();
					rhs = engine.parse(astString);
					pmEvaluator = new PatternMatcherAndEvaluator(setSymbol, lhs, rhs);

					condLength = stream.read();
					if (condLength == 0) {
						condition = null;
					} else {
						astString = stream.readUTF();
						condition = engine.parse(astString);
						pmEvaluator.setCondition(condition);
					}
					addSimplePatternRule(lhs, pmEvaluator);
				}

			}
		}

		len = stream.read();
		if (len > 0) {
			fPatternRules = new ArrayList<IPatternMatcher<IExpr>>();
			listLength = stream.read();
			for (int j = 0; j < listLength; j++) {
				astString = stream.readUTF();
				setSymbol = F.symbol(astString);

				astString = stream.readUTF();
				lhs = engine.parse(astString);
				astString = stream.readUTF();
				rhs = engine.parse(astString);
				pmEvaluator = new PatternMatcherAndEvaluator(setSymbol, lhs, rhs);

				condLength = stream.read();
				if (condLength == 0) {
					condition = null;
				} else {
					astString = stream.readUTF();
					condition = engine.parse(astString);
					pmEvaluator.setCondition(condition);
				}
				addSimplePatternRule(lhs, pmEvaluator);
			}
		}
	}

	public void writeSymbol(java.io.ObjectOutputStream stream) throws java.io.IOException {
		stream.writeUTF(fSymbolName);
		stream.write(fAttributes);
		Iterator<IExpr> iter;
		IExpr key;
		IExpr condition;
		Pair<ISymbol, IExpr> pair;
		ISymbol setSymbol;
		PatternMatcherAndEvaluator pmEvaluator;
		if (fEqualRules == null || fEqualRules.size() == 0) {
			stream.write(0);
		} else {
			stream.write(fEqualRules.size());
			iter = fEqualRules.keySet().iterator();
			while (iter.hasNext()) {
				key = iter.next();
				pair = fEqualRules.get(key);
				stream.writeUTF(pair.getFirst().toString());
				stream.writeUTF(key.fullFormString());
				stream.writeUTF(pair.getSecond().fullFormString());
			}
		}
		if (fSimplePatternRules == null || fSimplePatternRules.size() == 0) {
			stream.write(0);
		} else {
			Iterator<List<IPatternMatcher<IExpr>>> listIter;
			stream.write(fSimplePatternRules.size());
			listIter = fSimplePatternRules.values().iterator();
			while (listIter.hasNext()) {
				final List<IPatternMatcher<IExpr>> list = listIter.next();
				stream.write(list.size());
				for (int i = 0; i < list.size(); i++) {
					pmEvaluator = (PatternMatcherAndEvaluator) list.get(i);
					setSymbol = pmEvaluator.getSetSymbol();
					stream.writeUTF(setSymbol.toString());
					stream.writeUTF(pmEvaluator.getLHS().fullFormString());
					stream.writeUTF(pmEvaluator.getRHS().fullFormString());
					condition = pmEvaluator.getCondition();
					if (condition == null) {
						stream.write(0);
					} else {
						stream.write(1);
						stream.writeUTF(condition.fullFormString());
					}

				}
			}
		}
		if (fPatternRules == null || fPatternRules.size() == 0) {
			stream.write(0);
		} else {
			stream.write(fPatternRules.size());

			for (int i = 0; i < fPatternRules.size(); i++) {
				pmEvaluator = (PatternMatcherAndEvaluator) fPatternRules.get(i);
				setSymbol = pmEvaluator.getSetSymbol();
				stream.writeUTF(setSymbol.toString());
				stream.writeUTF(pmEvaluator.getLHS().fullFormString());
				stream.writeUTF(pmEvaluator.getRHS().fullFormString());
				condition = pmEvaluator.getCondition();
				if (condition == null) {
					stream.write(0);
				} else {
					stream.write(1);
					stream.writeUTF(condition.fullFormString());
				}
			}

		}
	}

	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public boolean accept(IVisitorBoolean visitor) {
		return visitor.visit(this);
	}

	public int accept(IVisitorInt visitor) {
		return visitor.visit(this);
	}
}