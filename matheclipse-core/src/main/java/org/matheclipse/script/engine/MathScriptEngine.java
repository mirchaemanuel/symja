package org.matheclipse.script.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.apache.commons.math.exception.util.ExceptionContextProvider;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.convert.Object2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.math.MathException;

public class MathScriptEngine extends AbstractScriptEngine {
	public final static String RETURN_OBJECT = "RETURN_OBJECT";

	private EvalUtilities fUtility;

	// static {
	// run the static groovy code for the MathEclipse domain specific language
	// DSL groovyDSL = new DSL();
	// }

	public MathScriptEngine() {
		// get the thread local evaluation engine
		EvalEngine engine = new EvalEngine();
		// engine.setIterationLimit(10);
		fUtility = new EvalUtilities(engine, false);
	}

	public Bindings createBindings() {
		return null;
	}

	public Object eval(final Reader reader, final ScriptContext context)
			throws ScriptException {
		final BufferedReader f = new BufferedReader(reader);
		final StringBuffer buff = new StringBuffer(1024);
		String line;
		try {
			while ((line = f.readLine()) != null) {
				buff.append(line);
				buff.append('\n');
			}
			return eval(buff.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object eval(final String script, final ScriptContext context)
			throws ScriptException {
		final ArrayList<ISymbol> list = new ArrayList<ISymbol>();
		try {
			// first assign the EvalEngine to the current thread:
			fUtility.startRequest();

			final Bindings bindings = context
					.getBindings(ScriptContext.ENGINE_SCOPE);
			ISymbol symbol;
			for (Map.Entry<String, Object> currEntry : bindings.entrySet()) {
				symbol = F.$s(currEntry.getKey());
				symbol.pushLocalVariable(Object2Expr.CONST.convert(currEntry
						.getValue()));
				list.add(symbol);
			}

			// evaluate an expression
			final Object stepwise = get("STEPWISE");
			IExpr result;
			if (Boolean.TRUE.equals(stepwise)) {
				result = fUtility.evalStepByStep(script);
			} else {
				result = fUtility.evaluate(script);
			}
			final Object returnType = context.getAttribute("RETURN_OBJECT");
			if ((returnType != null) && returnType.equals(Boolean.TRUE)) {
				// return the object "as is"
				return result;
			} else {
				// return the object as String representation
				if (result.equals(F.Null)) {
					return "";
				}
				final StringBufferWriter buf = new StringBufferWriter();
				OutputFormFactory.get().convert(buf, result);
				// print the result in the console
				return buf.toString();
			}

		} catch (final MathException e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
			// catch parser errors here
			return e.getMessage();
		} catch (final Exception e) {
			if (e instanceof ExceptionContextProvider) {
				if (Config.DEBUG) {
					e.printStackTrace();
				}
				return e.getMessage();
			}
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
			return e.getMessage();
		} finally {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).popLocalVariable();
				}
			}
		}

	}

	public ScriptEngineFactory getFactory() {
		return new MathScriptEngineFactory();
	}
}
