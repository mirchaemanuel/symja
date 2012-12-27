package org.matheclipse.android;

import java.io.OutputStream;
import java.io.PrintStream;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;

/**
 * Evaluate a symja expression
 * 
 */
public class SymjaInterpreter {
	private EvalUtilities util;

	String codeString;
	PrintStream outStream;

	static {
		// initialize the global available symbols
		F.initSymbols();
	}

	/**
	 * Create a new command interpreter attached to the passed in streams.
	 */
	public SymjaInterpreter(String codeString, OutputStream out) {
		EvalEngine engine = new EvalEngine();
		this.util = new EvalUtilities(engine, false);
		this.codeString = codeString;

		if (out instanceof PrintStream) {
			this.outStream = (PrintStream) out;
		} else {
			this.outStream = new PrintStream(out);
		}
		engine.setOutPrintStream(outStream);
	}

	public String interpreter(final String strEval) {
		IExpr result;
		final StringBufferWriter buf = new StringBufferWriter();
		try {
			result = util.evaluate(strEval);
			OutputFormFactory.get().convert(buf, result);
			return buf.toString();
		} catch (final RuntimeException re) {
			Throwable me = re.getCause();
			if (me instanceof MathException) {
				printException(buf, me);
			} else {
				printException(buf, re);
			}
		} catch (final Exception e) {
			printException(buf, e);
		}
		return buf.toString();
	}

	private void printException(final StringBufferWriter buf, final Throwable e) {
		String msg = e.getMessage();
		if (msg != null) {
			buf.write("\nError: " + msg);
		} else {
			buf.write("\nError: " + e.getClass().getSimpleName());
		}
	}

	public void eval() {
		String result = interpreter(codeString);
		outStream.print(result);
	}
}
