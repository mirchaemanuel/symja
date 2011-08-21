package org.matheclipse.core.eval;

import java.io.Writer;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.interfaces.IExpr;

public class EvalUtilities extends MathMLUtilities {

	public EvalUtilities() {
		super(new EvalEngine(), false);
	}

	public EvalUtilities(final boolean msie) {
		super(new EvalEngine(), msie);
	}

	public EvalUtilities(final EvalEngine evalEngine, final boolean msie) {
		super(evalEngine, msie);
	}

	/**
	 * Evaluate the inputExpression and return an IExpr expression object
	 * 
	 * @param inputExpression
	 * @return
	 */
	public IExpr evaluate(final String inputExpression) throws Exception {
		IExpr parsedExpression = null;
		if (inputExpression != null) {
			// try {
			startRequest();
			fEvalEngine.reset();
			parsedExpression = fEvalEngine.parse(inputExpression);
			if (parsedExpression != null) {
				fEvalEngine.reset();
				IExpr temp = fEvalEngine.evaluate(parsedExpression);
				fEvalEngine.addOut(temp);
				return temp;
			}
		}
		return null;
	}
	
	public IExpr evalStepByStep(final String inputExpression) throws Exception {
		IExpr parsedExpression = null;
		if (inputExpression != null) {
			startRequest();
			fEvalEngine.reset();
			parsedExpression = fEvalEngine.parse(inputExpression);
			if (parsedExpression != null) {
				fEvalEngine.reset();
				IExpr temp = fEvalEngine.evalStepByStep(parsedExpression);
				fEvalEngine.addOut(temp);
				return temp;
			}
		}
		return null;
	}

	public IExpr evaluate(final IExpr parsedExpression) throws RuntimeException {
		if (parsedExpression != null) {
			startRequest();
			fEvalEngine.reset();
			IExpr temp = fEvalEngine.evaluate(parsedExpression);
			fEvalEngine.addOut(temp);
			return temp;
		}
		return null;
	}
	
	public IExpr evalStepByStep(final IExpr parsedExpression) throws RuntimeException {
		if (parsedExpression != null) {
			startRequest();
			fEvalEngine.reset();
			IExpr temp = fEvalEngine.evalStepByStep(parsedExpression);
			fEvalEngine.addOut(temp);
			return temp;
		}
		return null;
	}

	@Override
	synchronized public void toMathML(final String inputExpression, final Writer out) {
		try {
			final IExpr result = evaluate(inputExpression);
			if (result != null) {
				toMathML(result, out);
			}
		} catch (final Throwable e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
	}

}