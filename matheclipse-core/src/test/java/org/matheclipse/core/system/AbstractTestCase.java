package org.matheclipse.core.system;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import junit.framework.TestCase;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.eval.TimeConstrainedEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;

/**
 * Tests system.reflection classes
 */
public abstract class AbstractTestCase extends TestCase {
	private ScriptEngine fScriptEngine;
	protected static ScriptEngineManager fScriptManager = new ScriptEngineManager();

	public AbstractTestCase(String name) {
		super(name);
		F.initSymbols(null, null);
	}

	// public void check(String evalString, String expectedResult) {
	// check(true, evalString, expectedResult);
	// }
	public void check(String evalString, String expectedResult) {
		check(fScriptEngine, evalString, expectedResult);
	}

	public void check(ScriptEngine scriptEngine, String evalString,
			String expectedResult) {
		try {
			if (evalString.length() == 0 && expectedResult.length() == 0) {
				return;
			}

			String evaledResult = (String) scriptEngine.eval(evalString);

			assertEquals(evaledResult, expectedResult);
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(e, "");
		}
	}

	public void check(IAST ast, String strResult) {
		check(EvalEngine.get(), true, ast, strResult);
	}

	public void check(EvalEngine engine, boolean configMode, IAST ast, String strResult) {
		try {

			IExpr result;
			StringBufferWriter buf = new StringBufferWriter();
			buf.setIgnoreNewLine(true);
			// F.initSymbols();
			Config.SERVER_MODE = configMode;
			if (Config.SERVER_MODE) {
				// Parser parser = new Parser();
				// ASTNode node = parser.parse(strEval);
				IAST inExpr = ast;
				TimeConstrainedEvaluator utility = new TimeConstrainedEvaluator(engine, false, Config.FOREVER);
				result = utility.constrainedEval(buf, inExpr);
			} else {
				result = ast;
				if ((result != null) && !result.equals(F.Null)) {
					OutputFormFactory.get().convert(buf, result);
				}
			}

			assertEquals(buf.toString(), strResult);
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(e, "");
		}
	}


	/**
	 * The JUnit setup method
	 */
	protected void setUp() {
		try {
			synchronized (fScriptManager) {
				fScriptEngine = fScriptManager.getEngineByExtension("m");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// assertEquals("", "ParserError");
		}
		// setup the expression factory (and bind to current thread)
		// fParser.setFactory(ExpressionFactory.get());

	}

}
