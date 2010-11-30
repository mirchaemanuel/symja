package org.matheclipse.core.eval;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.matheclipse.basic.Config;
import org.matheclipse.basic.Util;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.eval.exception.IterationLimitExceeded;
import org.matheclipse.core.eval.exception.RecursionLimitExceeded;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.eval.interfaces.ISymbolEvaluator;
import org.matheclipse.core.expression.ComplexSym;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.MethodSymbol;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IEvaluationEngine;
import org.matheclipse.core.interfaces.IEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.list.algorithms.EvaluationSupport;
import org.matheclipse.core.sql.SerializeVariables2DB;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;

import apache.harmony.math.BigInteger;
import apache.harmony.math.Rational;

/**
 * 
 */
public class EvalEngine implements Serializable, IEvaluationEngine {
	/**
	 * 
	 */
	private static final long serialVersionUID = 407328682800652434L;

	/**
	 * Associate a symbol name in this ThreadLocal with the symbol created in this
	 * thread
	 * 
	 * @see ExprFactory.fSymbolMap for global symbol names
	 */
	private HashMap<String, ISymbol> fVariableMap;

	/**
	 * Associate a symbol name with a local variable stack in this thread
	 * 
	 */
	transient private HashMap<String, Stack<IExpr>> fLocalVariableStackMap = null;

	/**
	 * if set the current thread should stop evaluation;
	 */
	transient volatile boolean fStopRequested;

	transient int fRecursionCounter;

	transient boolean fNumericMode;

	transient String fSessionID;

	// transient IAST fTraceList;

	transient boolean fTraceMode;

	transient boolean fStopAfterEvaluationMode;

	transient Stack<IAST> fTraceStack;

	transient PrintStream fOutPrintStream = null;

	protected int fRecursionLimit;

	protected int fIterationLimit;

	static int fAnonymousCounter = 0;

	protected boolean fPackageMode = false;

	transient int fModuleCounter = 0;

	/**
	 * Increment the module counter by 1 and return the result.
	 * 
	 * @return the module counter
	 */
	public int incModuleCounter() {
		return ++fModuleCounter;
	}

	synchronized public static int getNextAnonymousCounter() {
		return ++fAnonymousCounter;
	}

	synchronized public static String getNextCounter() {
		return Integer.toString(++fAnonymousCounter);
	}

	public boolean isPackageMode() {
		return fPackageMode;
	}

	public void setPackageMode(boolean packageMode) {
		fPackageMode = packageMode;
	}

	// protected ExprFactory fExpressionFactory;

	protected Set<ISymbol> fModifiedVariablesList;

	transient protected List<IExpr> fOutList = new ArrayList<IExpr>(10);

	public final static boolean DEBUG = false;

	transient private static final ThreadLocal<EvalEngine> instance = new ThreadLocal<EvalEngine>() {
		private int fID = 1;

		@Override
		public EvalEngine initialValue() {
			if (DEBUG) {
				System.out.println("ThreadLocal" + fID);
			}
			return new EvalEngine("ThreadLocal" + (fID++), 0, System.out);
		}
	};

	/**
	 * Removes the current thread's value for the EvalEngine's thread-local
	 * variable.
	 * 
	 * @see java.lang.ThreadLocal#remove()
	 */
	public static void remove() {
		instance.remove();
	}

	/**
	 * Get the thread local evaluation engine instance
	 * 
	 * @return
	 */
	public static EvalEngine get() {
		return instance.get();
	}

	/**
	 * Set the thread local evaluation engine instance
	 * 
	 * @return
	 */
	public static void set(final EvalEngine engine) {
		instance.set(engine);
	}

	/**
	 * Public constructor for serialization
	 * 
	 */
	public EvalEngine() {
		this("", 0, System.out);
	}

	/**
	 * Reset the numeric mode flag and the recursion counter
	 * 
	 */
	public void reset() {
		fNumericMode = false;
		fRecursionCounter = 0;
	}

	public EvalEngine(final F f, final PrintStream out) {
		this("", -1, -1, out);
	}

	public EvalEngine(final String sessionID, final PrintStream out) {
		this(sessionID, -1, -1, out);
	}

	/**
	 * Constructor for an EvaluationEngine
	 * 
	 * @param sessionID
	 *          an ID which uniquely identifies this session
	 * @param recursionLimit
	 *          the maximum allowed recursion limit (if set to zero, no limit will
	 *          be proofed)
	 * @see javax.servlet.http.HttpSession#getID()
	 */
	public EvalEngine(final String sessionID, final int recursionLimit, final PrintStream out) {
		this(sessionID, recursionLimit, -1, out);
	}

	public EvalEngine(final String sessionID, final int recursionLimit, final int iterationLimit, final PrintStream out) {
		fSessionID = sessionID;
		// fExpressionFactory = f;
		fRecursionLimit = recursionLimit;
		fIterationLimit = iterationLimit;
		fOutPrintStream = out;

		// fNamespace = fExpressionFactory.getNamespace();

		init();

		// set this EvalEngine to the thread local
		set(this);
		// set up global symbols
		// F.initSymbols();
	}

	final public void init() {
		fRecursionCounter = 0;
		fNumericMode = false;
		fTraceMode = false;
		fStopAfterEvaluationMode = false;
		fTraceStack = new Stack<IAST>();
		// fTraceList = null;
		fStopRequested = false;
		fModifiedVariablesList = new HashSet<ISymbol>();
	}

	/**
	 * Evaluate an object without resetting the numeric mode after the evaluation
	 * step. If evaluation is not possible return the input object,
	 * 
	 * @param expr
	 *          the object which should be evaluated
	 * @return the evaluated object
	 * 
	 */
	public final IExpr evalWithoutNumericReset(final IExpr expr) {
		IExpr temp = evalLoop(expr);
		return temp == null ? expr : temp;
	}

	/**
	 * 
	 * Evaluate an object and reset the numeric mode to the value before the
	 * evaluation step. If evaluation is not possible return the input object.
	 * 
	 * @param expr
	 *          the object which should be evaluated
	 * @param numericMode
	 *          reset the numericMode to this value after evaluation
	 * @return the evaluated object
	 */
	public final IExpr evaluate(final IExpr expr) {
		boolean numericMode = fNumericMode;
		// StackContext.enter();
		try {
			return evalWithoutNumericReset(expr);
			// if (fTraceMode) {
			// fTraceList = StackContext.outerCopy(fTraceList);
			// }
			// return StackContext.outerCopy(temp);
		} finally {
			fNumericMode = numericMode;
			// StackContext.exit();
		}
	}

	/**
	 * Evaluate an expression. If evaluation is not possible return the input
	 * object.
	 * 
	 * @param expr
	 *          the expression which should be evaluated
	 * @return the evaluated object
	 * @see EvalEngine#evalWithoutNumericReset(IExpr)
	 */
	public static final IExpr eval(final IExpr expr) {
		return (instance.get()).evaluate(expr);
	}

	/**
	 * 
	 * Evaluate an object and reset the numeric mode to the value before the
	 * evaluation step. If evaluation is not possible return <code>null</code>.
	 * 
	 * @param expr
	 *          the object which should be evaluated
	 * @param numericMode
	 *          reset the numericMode to this value after evaluation
	 * @return the evaluated object of <code>null</code> if no evaluation was
	 *         possible
	 */
	public final IExpr evaluateNull(final IExpr expr) {
		// boolean numericMode = fNumericMode;
		// // StackContext.enter();
		// try {
		return evalLoop(expr);
		// } finally {
		// fNumericMode = numericMode;
		// // StackContext.exit();
		// }
	}

	/**
	 * Evaluate an expression. If evaluation is not possible return the input
	 * object.
	 * 
	 * @param expr
	 *          the expression which should be evaluated
	 * @return the evaluated object or <code>null</code> if no evaluation was
	 *         possible
	 * @see EvalEngine#evalWithoutNumericReset(IExpr)
	 */
	public static final IExpr evalNull(final IExpr expr) {
		return (instance.get()).evaluateNull(expr);
	}

	/**
	 * Evaluate an expression step by step. If evaluation is not possible return
	 * <code>null</code>, otherwise return the first evaluated subexpression.
	 * 
	 * @param expr
	 * @return an array of 2 elements, which contain the currently evaluated
	 *         subexpression ans the result of this evaluation
	 */
	public final IExpr evalStepByStep(final IExpr expr) {
		IExpr result = null;
		boolean numericMode = fNumericMode;
		try {
			setTraceMode(true);
			setStopAfterEvaluationMode(true);
			result = evalLoop(expr);
		} finally {
			setStopAfterEvaluationMode(false);
			setTraceMode(false);
			setNumericMode(numericMode);
		}
		return result;
	}

	protected IExpr evalComplex(final IComplex obj) {
		if (fNumericMode) {
			final Rational r = obj.getRealPart();
			final Rational i = obj.getImaginaryPart();
			double nr = 0.0;
			double dr = 1.0;
			double ni = 0.0;
			double di = 1.0;
			// if (r instanceof IFraction) {
			nr = r.getNumerator().doubleValue();
			dr = r.getDenominator().doubleValue();
			// }
			// if (r instanceof IInteger) {
			// nr = ((IInteger) r).getNumerator().doubleValue();
			// }
			// if (i instanceof IFraction) {
			ni = i.getNumerator().doubleValue();
			di = i.getDenominator().doubleValue();
			// }
			// if (i instanceof IInteger) {
			// ni = ((IInteger) i).getNumerator().doubleValue();
			// }
			return F.complexNum(nr / dr, ni / di);
		}
		if (obj instanceof ComplexSym) {
			final INumber cTemp = ((ComplexSym) obj).normalize();
			if (cTemp == obj) {
				return null;
			}
			return cTemp;
		}
		return null;
	}

	protected IExpr evalDouble(final INum dbl) {
		if (!fNumericMode) {
			fNumericMode = true;
			return dbl;
		}
		return null;
	}

	protected IExpr evalDoubleComplex(final IComplexNum obj) {
		if (!fNumericMode) {
			fNumericMode = true;
			return obj;
		}
		return null;
	}

	protected IExpr evalFraction(final IFraction obj) {
		if (fNumericMode) {
			final double n = obj.getBigNumerator().doubleValue();
			final double d = obj.getBigDenominator().doubleValue();
			return F.num(n / d);
		}
		if (obj.getBigDenominator().equals(BigInteger.ONE)) {
			return F.integer(obj.getBigNumerator());
		}
		return null;
	}

	/**
	 * Evaluate an AST with only one argument (i.e. <code>head[arg1]</code>). The
	 * evaluation steps are controlled by the header attributes.
	 * 
	 * @param ast
	 * @return
	 */
	private IExpr evalASTArg1(final IAST ast) {
		// special case ast.size() == 2
		// head == ast[0] --- arg1 == ast[1]
		IExpr result;
		if ((result = evalLoop(ast.head())) != null) {
			// first evaluate the header !
			IAST resultList = ast.clone();
			resultList.setHeader(result);
			return resultList;
		}

		final ISymbol symbol = ast.topHead();
		final int attr = symbol.getAttributes();
		final IExpr arg1 = ast.get(1);
		if ((ISymbol.ONEIDENTITY & attr) == ISymbol.ONEIDENTITY) {
			return arg1;
		}

		if ((ISymbol.FLAT & attr) == ISymbol.FLAT && arg1.topHead().equals(symbol)) {
			// associative
			return arg1;
		}

		if ((result = evalArgs(ast, attr)) != null) {
			return result;
		}

		if ((ISymbol.LISTABLE & attr) == ISymbol.LISTABLE && arg1.isList()) {
			// thread over the list
			if ((result = EvaluationSupport.threadList(ast, ((IAST) arg1).size() - 1, 1)) != null) {
				return result;
			}
		}

		return evalASTBuiltinFunction(symbol, ast);
	}

	/**
	 * Evaluate an AST. The evaluation steps are controlled by the header
	 * attributes.
	 * 
	 * @param ast
	 * @return
	 */
	public IExpr evalAST(final IAST ast) {
		if (ast.size() == 2) {
			return evalASTArg1(ast);
		}
		IExpr result;

		if ((result = evalLoop(ast.head())) != null) {
			// first evaluate the header !
			IAST resultList = ast.clone();
			resultList.setHeader(result);
			return resultList;
		}

		final ISymbol symbol = ast.topHead();

		if (ast.size() != 1) {
			final int attr = symbol.getAttributes();

			if (ast.size() == 2 && (ISymbol.ONEIDENTITY & attr) == ISymbol.ONEIDENTITY) {
				return ast.get(1);
			}

			if ((ISymbol.FLAT & attr) == ISymbol.FLAT) {
				// associative
				if ((result = EvaluationSupport.flatten(ast)) != null) {
					return result;
				}
			}

			IAST resultList = evalArgs(ast, attr);
			if (resultList != null) {
				return resultList;
			}

			if ((ISymbol.LISTABLE & attr) == ISymbol.LISTABLE) {
				// thread over the lists
				int listLength = 0;

				for (int i = 0; i < ast.size(); i++) {
					if ((ast.get(i) instanceof IAST) && (((IAST) ast.get(i)).head().equals(F.List))) {
						if (listLength == 0) {
							listLength = ((IAST) ast.get(i)).size() - 1;
						} else {
							if (listLength != ((IAST) ast.get(i)).size() - 1) {
								listLength = 0;
								break;
								// for loop
							}
						}
					}
				}
				if ((listLength != 0) && ((result = EvaluationSupport.threadList(ast, listLength, 1)) != null)) {
					return result;
				}
			}

			if (ast.size() > 2 && (ISymbol.ORDERLESS & attr) == ISymbol.ORDERLESS) {
				// commutative
				EvaluationSupport.sort(ast);
			}
		}

		return evalASTBuiltinFunction(symbol, ast);
	}

	/**
	 * 
	 * @param symbol
	 * @param ast
	 * @return
	 */
	private IExpr evalASTBuiltinFunction(final ISymbol symbol, final IAST ast) {
		IExpr result;
		if ((result = symbol.evalDownRule(this, ast)) != null) {
			return result;
		}

		if (symbol instanceof MethodSymbol) {
			return ((MethodSymbol) symbol).invoke(ast);
		} else {
			final IEvaluator module = symbol.getEvaluator();
			if (module instanceof IFunctionEvaluator) {
				// evaluate a built-in function.
				if (fNumericMode) {
					return ((IFunctionEvaluator) module).numericEval(ast);
				}
				return ((IFunctionEvaluator) module).evaluate(ast);
			}
		}
		return null;
	}

	/**
	 * Evaluate the arguments of the given ast, taking the attributes HoldFirst,
	 * HoldRest into account.
	 * 
	 * @param ast
	 * @param attr
	 * @return
	 */
	private IAST evalArgs(final IAST ast, final int attr) {
		if (ast.size() > 1) {
			IAST resultList = null;
			IExpr evaledExpr;
			if ((ISymbol.HOLDFIRST & attr) == ISymbol.NOATTRIBUTE) {
				// the HoldFirst attribute isn't set here
				if ((evaledExpr = evalLoop(ast.get(1))) != null) {
					if (resultList == null) {
						resultList = ast.clone();
					}
					resultList.set(1, evaledExpr);
					if (ast.size() == 2) {
						return resultList;
					}
				}
			}
			if ((ISymbol.HOLDREST & attr) == ISymbol.NOATTRIBUTE) {
				// the HoldRest attribute isn't set here
				for (int i = 2; i < ast.size(); i++) {
					if ((evaledExpr = evalLoop(ast.get(i))) != null) {
						if (resultList == null) {
							resultList = ast.clone();
						}
						resultList.set(i, evaledExpr);
					}
				}
			}
			if (resultList != null) {
				return resultList;
			}
		}
		return null;
	}

	/**
	 * Transform the ast recursively, according to the attributes Flat, HoldAll,
	 * HoldFirst, HoldRest, Orderless for the left-hand-side of a Set[] or
	 * SetDelayed[] expression
	 * 
	 * @param ast
	 * @return
	 */
	public IAST evalSetAttributes(IAST ast) {
		if ((ast.getEvalFlags() & IAST.IS_FLATTENED_OR_SORTED_MASK) != 0x0000) {
			// already flattend or sorted
			return ast;
		}
		final ISymbol symbol = ast.topHead();
		final int attr = symbol.getAttributes();
		IAST resultList = ast;
		IAST result;
		if ((ISymbol.FLAT & attr) == ISymbol.FLAT) {
			// associative
			if ((result = EvaluationSupport.flatten(ast)) != null) {
				resultList = result;
			}
		}
		if ((ISymbol.HOLDALL & attr) != ISymbol.HOLDALL) {
			resultList = ast.clone();
			if ((ISymbol.HOLDFIRST & attr) == ISymbol.NOATTRIBUTE) {
				// the HoldFirst attribute isn't set here
				if (ast.size() > 1 && ast.get(1) instanceof IAST) {
					resultList.set(1, evalSetAttributes((IAST) ast.get(1)));
				}
			}
			if ((ISymbol.HOLDREST & attr) == ISymbol.NOATTRIBUTE) {
				// the HoldRest attribute isn't set here
				for (int i = 2; i < ast.size(); i++) {
					if (ast.get(i) instanceof IAST) {
						resultList.set(i, evalSetAttributes((IAST) ast.get(i)));
					}
				}
			}

		}
		if (resultList.size() > 2) {
			if ((ISymbol.ORDERLESS & attr) == ISymbol.ORDERLESS) {
				EvaluationSupport.sort(resultList);
			}
		}
		return resultList;
	}

	protected IExpr evalInteger(final IInteger obj) {
		if (fNumericMode) {
			return F.num(obj.getBigNumerator().doubleValue());
		}
		return null;
	}

	/**
	 * Evaluate an object, if evaluation is not possible return <code>null</code>.
	 * 
	 * @param expr
	 *          the expression which should be evaluated
	 * @return the evaluated expression or <code>null</code> is evasluation isn't
	 *         possible
	 * @see EvalEngine#evalWithoutNumericReset(IExpr)
	 */
	public IExpr evalLoop(final IExpr expr) {

		if ((fRecursionLimit > 0) && (fRecursionCounter > fRecursionLimit)) {
			if (Config.DEBUG) {
				System.out.println(expr.toString());
			}
			RecursionLimitExceeded.throwIt(fRecursionLimit, expr);
		}

		IExpr result = expr;
		IExpr temp = expr;
		boolean evaled = false;
		int iterationCounter = 1;
		IAST traceList = null;
		try {
			fRecursionCounter++;
			temp = evalObject(result);
			if (temp != null) {
				if (fTraceMode) {
					if (fStopAfterEvaluationMode) {
						// stop evaluation if, we've got a result
						return temp;
					}
					traceList = F.List();
					traceList.add(expr);
					traceList.add(temp);
					fTraceStack.push(traceList);
				}
				evaled = true;
				result = temp;
			} else {
				return null;
			}

			while (temp != null) {
				temp = evalObject(result);
				if (temp != null) {
					if (fTraceMode) {
						traceList.add(temp);
					}
					result = temp;
					if (fIterationLimit >= 0 && fIterationLimit <= ++iterationCounter) {
						IterationLimitExceeded.throwIt(iterationCounter, result);
					}
				}
			}
			if (evaled) {
				return result;
			}
		} finally {
			if (evaled) {
				if (traceList != null) {
					// HeapContext.enter();
					// try {
					fTraceStack.pop();
					IAST topStack = fTraceStack.peek();
					topStack.add(traceList);// .copy());
					// } finally {
					// HeapContext.exit();
					// }
					// fTraceList = fTraceStack.pop();
				}
			}
			fRecursionCounter--;
		}
		return null;
	}

	/**
	 * Evaluate an object
	 * 
	 * @param obj
	 *          the object which should be evaluated
	 * @return the evaluated Object or null if evaluation is not possible
	 */
	protected IExpr evalObject(final IExpr obj) {
		if (Config.DEBUG) {
			System.out.println(obj.toString());
		}
		Util.checkCanceled();
		if (obj.isAST()) {
			return evalAST((IAST) obj);
		}

		if (obj instanceof INumber) {
			if (obj instanceof ISignedNumber) {
				return evalSignedNumber((ISignedNumber) obj);
			}
			if (obj instanceof IComplexNum) {
				return evalDoubleComplex((IComplexNum) obj);
			}
			if (obj instanceof IComplex) {
				return evalComplex((IComplex) obj);
			}
		}
		if (obj instanceof ISymbol) {
			return evalSymbol((ISymbol) obj);
		}
		if (obj instanceof IStringX) {
			return null;
		}
		if (obj instanceof IPattern) {
			return evalPattern((IPattern) obj);
		}

		return null;
	}

	protected IExpr evalSignedNumber(final ISignedNumber obj) {
		if (obj instanceof INum) {
			return evalDouble((INum) obj);
		}
		if (obj instanceof IInteger) {
			return evalInteger((IInteger) obj);
		}
		if (obj instanceof IFraction) {
			return evalFraction((IFraction) obj);
		}
		return null;
	}

	/**
	 * Evaluate an object, if evaluation is not possible return <code>null</code>
	 * 
	 * @param obj
	 *          the object which should be evaluated
	 * @return the evaluated object or <code>null</code>
	 * 
	 */
	// protected Object evalStep(Object obj) {
	// if (fRecursionLimit > 0 && fExprStack.size() > fRecursionLimit) {
	// throw new RecursionLimitExceeded(fRecursionLimit);
	// }
	// fExprStack.push(obj);
	// Object result = evalPeek();
	// fExprStack.pop();
	// return result;
	// }
	protected IExpr evalPattern(final IPattern obj) {
		return null;
	}

	public IExpr evalSymbol(final ISymbol symbol) {
		IExpr result;
		if (symbol.hasLocalVariableStack()) {// &&
			// !symbol.isLocalVariableStackEmpty())
			// {
			return symbol.get();
		}
		if ((result = symbol.evalDownRule(this, symbol)) != null) {
			return result;
		}
		final IEvaluator module = symbol.getEvaluator();
		if (module instanceof ISymbolEvaluator) {
			if (fNumericMode) {
				return ((ISymbolEvaluator) module).numericEval(symbol);
			}
			return ((ISymbolEvaluator) module).evaluate(symbol);
		}
		return null;
	}

	/**
	 * @return
	 */
	// public ExprFactory getExpressionFactory() {
	// return fExpressionFactory;
	// }
	/**
	 * @return
	 */
	public int getRecursionLimit() {
		return fRecursionLimit;
	}

	public int getIterationLimit() {
		return fIterationLimit;
	}

	/**
	 * @return
	 */
	public String getSessionID() {
		return fSessionID;
	}

	/**
	 * @return
	 */
	public IAST getTraceList() {
		return fTraceStack.pop();
		// return fTraceList;
	}

	/**
	 * @return
	 */
	public boolean isNumericMode() {
		return fNumericMode;
	}

	/**
	 * 
	 * @return <code>true</code> if the <i>stop after evaluation mode</i> is set.
	 */
	public boolean isStopAfterEvaluationMode() {
		return fStopAfterEvaluationMode;
	}

	/**
	 * If the trace mode is set the system writes an evaluation trace list or if
	 * additionally the <i>stop after evaluation mode</i> is set returns the first
	 * evaluated result.
	 * 
	 * @return
	 * @see org.matheclipse.core.reflection.system.Trace
	 */
	public boolean isTraceMode() {
		return fTraceMode;
	}

	/**
	 * @param b
	 */
	public void setNumericMode(final boolean b) {
		fNumericMode = b;
	}

	/**
	 * @param i
	 */
	public void setRecursionLimit(final int i) {
		fRecursionLimit = i;
	}

	public void setIterationLimit(final int i) {
		fIterationLimit = i;
	}

	/**
	 * @param string
	 */
	public void setSessionID(final String string) {
		fSessionID = string;
	}

	/**
	 * Set the <i>stop after evaluation mode</i>, for getting the result of an
	 * evaluation step b step..
	 * 
	 * @param b
	 */
	public void setStopAfterEvaluationMode(final boolean b) {
		fStopAfterEvaluationMode = b;
	}

	/**
	 * @param list
	 */
	public void setTraceList(final IAST list) {
		fTraceStack.push(list);
		// fTraceList = list;
	}

	/**
	 * @param b
	 */
	public void setTraceMode(final boolean b) {
		fTraceMode = b;
	}

	/**
	 * @return Returns the stopRequested.
	 */
	public boolean isStopRequested() {
		return fStopRequested;
	}

	/**
	 * @param stopRequested
	 *          The stopRequested to set.
	 */
	public void setStopRequested(final boolean stopRequested) {
		fStopRequested = stopRequested;
	}

	public void stopRequest() {
		fStopRequested = true;
	}

	public PrintStream getOutPrintStream() {
		return fOutPrintStream;
	}

	public void setOutPrintStream(final PrintStream outPrintStream) {
		fOutPrintStream = outPrintStream;
	}

	public List<IExpr> getOutList() {
		return fOutList;
	}

	/**
	 * Add an expression to the <code>Out[]</code> list
	 * 
	 */
	public boolean addOut(IExpr arg0) {
		if (arg0 == null) {
			fOutList.add(F.Null);
		}
		return fOutList.add(arg0);
	}

	/**
	 * Get the expression of the <code>Out[]</code> list at the given index
	 * 
	 * @param index
	 * @return
	 */
	public IExpr getOut(int index) {
		return fOutList.get(index);
	}

	/**
	 * The size of the <code>Out[]</code> list
	 * 
	 * @return
	 */
	public int sizeOut() {
		return fOutList.size();
	}

	/**
	 * For every evaluation store the list of modified variables in an internal
	 * list.
	 * 
	 * @param arg0
	 * @return
	 */
	public boolean addModifiedVariable(ISymbol arg0) {
		return fModifiedVariablesList.add(arg0);
	}

	/**
	 * Get the list of modified variables
	 * 
	 * @return
	 */
	public Set<ISymbol> getModifiedVariables() {
		return fModifiedVariablesList;
	}

	public void serializeVariables2DB(Connection con) throws SQLException, IOException {
		SerializeVariables2DB.write(con, fSessionID, fModifiedVariablesList);
	}

	/**
	 * Parse the given <code>expression String</code> into an IExpr without
	 * evaluation.
	 * 
	 * @param astString
	 *          an expression in math formula notation
	 * @return
	 * @throws org.matheclipse.parser.client.SyntaxError
	 *           if a parsing error occurs
	 */
	final public IExpr parse(String expression) {
		final Parser parser = new Parser();
		final ASTNode node = parser.parse(expression);
		return AST2Expr.CONST.convert(node);
	}

	/**
	 * Parse the given <code>expression String</code> into an IExpr and evaluate
	 * it.
	 * 
	 * @param astString
	 *          an expression in math formula notation
	 * @return
	 * @throws org.matheclipse.parser.client.SyntaxError
	 *           if a parsing error occurs
	 */
	final public IExpr evaluate(String expression) {
		return evaluate(parse(expression));
	}

	final public Map<String, Stack<IExpr>> getLocalVariableStackMap() {
		if (fLocalVariableStackMap == null) {
			fLocalVariableStackMap = new HashMap<String, Stack<IExpr>>();
		}
		return fLocalVariableStackMap;
	}

	/**
	 * Get the local variable stack for a given symbol name. If the local variable
	 * stack doesn't exist, return <code>null</code>
	 * 
	 * @param symbolName
	 * @return <code>null</code> if the stack doesn't exist
	 */
	final public static Stack<IExpr> localStack(final String symbolName) {
		return get().getLocalVariableStackMap().get(symbolName);
	}

	/**
	 * Get the local variable stack for a given symbol name. If the local variable
	 * stack doesn't exist, create a new one for the symbol.
	 * 
	 * @param symbolName
	 * @return
	 */
	public static Stack<IExpr> localStackCreate(final String symbolName) {
		Map<String, Stack<IExpr>> localVariableStackMap = get().getLocalVariableStackMap();
		Stack<IExpr> temp = localVariableStackMap.get(symbolName);
		if (temp != null) {
			return temp;
		}
		temp = new Stack<IExpr>();
		localVariableStackMap.put(symbolName, temp);
		return temp;
	}

	/**
	 * Get a local user symbol name in this ThreadLocal associated with the symbol
	 * created in this thread.
	 * 
	 * @see ExprFactory.fSymbolMap for global symbol names
	 */
	final public Map<String, ISymbol> getVariableMap() {
		if (fVariableMap == null) {
			fVariableMap = new HashMap<String, ISymbol>();
		}
		return fVariableMap;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (fVariableMap != null) {
			buf.append(fVariableMap.toString());
		}
		if (fLocalVariableStackMap != null) {
			buf.append(fLocalVariableStackMap.toString());
		}
		if (SystemNamespace.DEFAULT != null) {
			buf.append(SystemNamespace.DEFAULT.toString());
		}
		return buf.toString();
	}

}