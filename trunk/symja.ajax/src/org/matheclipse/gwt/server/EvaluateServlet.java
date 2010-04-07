package org.matheclipse.gwt.server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;

import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.MathMLUtilities;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.math.MathException;

public class EvaluateServlet extends HttpServlet {
	private static final long serialVersionUID = 6265703737413093134L;

	// private static final boolean UNIT_TEST = false;

	// private static final boolean USE_DATABASE = false;

	public static int APPLET_NUMBER = 1;

	public static final String UTF8 = "utf-8";

	public static final String EVAL_ENGINE = EvalEngine.class.getName();

	public static DataSource DATA_SOURCE = null;

	public static CacheManager CACHE_MANAGER = null;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("text/plain");
		String name = "evaluate";
		String value = req.getParameter(name);
		if (value == null) {
			out.println(URLEncoder.encode("0;error;No input expression posted!", "UTF-8"));
			return;
		}
		if (value.length() > Short.MAX_VALUE) {
			out.println(URLEncoder.encode("0;error;Input expression to large!", "UTF-8"));
			return;
		}
		value = value.trim();
		try {
			String result = evaluate(req, value, "", 0);
			out.println(result);// URLEncoder.encode(result, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String evaluate(HttpServletRequest request, String expression, String function, int counter) {
		if (expression == null || expression.length() == 0) {
			return counter + ";error;No input expression posted!";
		}
		if (expression.trim().length() == 0) {
			return counter + ";error;No input expression posted!";
		} else if (expression.length() >= Short.MAX_VALUE) {
			return counter + ";error;Input expression greater than: " + Short.MAX_VALUE + " characters!";
		}

		HttpSession session = request.getSession();
		final StringBufferWriter outWriter = new StringBufferWriter();
		WriterOutputStream wouts = new WriterOutputStream(outWriter);
		PrintStream outs = new PrintStream(wouts);
		EvalEngine engine = null;
		if (session != null) {
			engine = (EvalEngine) session.getAttribute(EVAL_ENGINE);
			if (engine == null) {
				// ExprFactory f = new ExprFactory(new SystemNamespace());
				// PrintStream pout = new PrintStream();
				engine = new EvalEngine(session.getId(), 256, 256, outs);
				session.setAttribute(EVAL_ENGINE, engine);
			} else {
				engine.setOutPrintStream(outs);
				engine.setSessionID(session.getId());
			}
		}

		try {
			String[] result = evaluateString(request, engine, expression, function);
			StringBuffer buf = outWriter.getBuffer();
			buf.append(result[1]);
			return counter + ";" + result[0] + ";" + buf.toString();
		} catch (Exception e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
			return counter + ";error;Exception occurred in evaluate!";
		}

	}

	public static String[] evaluateString(HttpServletRequest request, EvalEngine engine, String inputString, String function)
			throws UnsupportedEncodingException {

		Connection conn = null; // the database connection
		try {
			// if (USE_DATABASE) {
			// conn = getConnection();
			// saveLogInDatabase(conn, inputString, request.getRemoteAddr(),
			// request.getRemoteHost(), request.getSession().getId());
			// }
			Parser parser = new Parser();
			ASTNode node = parser.parse(inputString);
			IExpr inExpr = AST2Expr.CONST.convert(node);
			if (inExpr != null) {
				// inExpr contains the user input from the web interface in
				// internal
				// format now
				StringBufferWriter outBuffer = new StringBufferWriter();
				IExpr outExpr;
				// if (USE_DATABASE) {
				// outExpr = getFromDatabase(conn, factory, parser, inExpr);
				// if (outExpr != null) {
				// if (!outExpr.equals(ExprFactory.Null)) {
				// OutputFormFactory.convert(outBuffer, outExpr);
				// return createOutput(outBuffer, outExpr, engine, function);
				// }
				// }
				// }
				MathEvaluator.eval(engine, outBuffer, inExpr);
				// TimeConstrainedEvaluator utility = new
				// TimeConstrainedEvaluator(engine, false,
				// Config.TIME_CONSTRAINED_MILLISECONDS);
				// outExpr = utility.constrainedEval(outBuffer, inExpr);
				// if (outExpr != null) {
				// if (USE_DATABASE) {
				// if (inExpr != outExpr) { // compare pointers
				// saveInDatabase(conn, factory, inExpr, outExpr);
				// }
				// }
				return createOutput(outBuffer, null, engine, function);
				// } else {
				// // show error messages:
				// return new String[] { "error", outBuffer.toString() };
				// // out.println(toHTML(outBuffer.toString()));
				// }

			}
		} catch (MathException se) {
			return new String[] { "error", se.getMessage() };
		} catch (Exception e) {
			// error message
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
			return new String[] { "error", "Error in evaluateString" };
			// out.println("<font color=\"red\">");
			// out.println(toHTMLNL(e.getMessage()));
			// out.println("</font>");
		} finally {
			if (conn != null) {
				try {
					// release the database connection
					conn.close();
					conn = null;
				} catch (SQLException e) {
					if (Config.SHOW_STACKTRACE) {
						e.printStackTrace();
					}
					return new String[] { "error", "SQL Error in evaluateString" };
				}
			}
		}
		return new String[] { "error", "Error in evaluateString" };
	}

	private static String[] createOutput(StringBufferWriter buffer, IExpr rhsExpr, EvalEngine engine, String function)
			throws IOException {

		boolean textEval = true;
		if (rhsExpr != null && rhsExpr instanceof IAST && rhsExpr.isAST(F.Show, 2)) {
			IAST ast = (IAST) rhsExpr;
			if (ast.size() == 2 && ast.get(0).toString().equals("Show")) {
				StringBufferWriter outBuffer = new StringBufferWriter();
				outBuffer = new StringBufferWriter();
				StringBufferWriter graphicBuf = new StringBufferWriter();
				IExpr result = (IExpr) ast.get(1);
				graphicBuf.setIgnoreNewLine(true);
				OutputFormFactory outputFormFactory = OutputFormFactory.get();
				outputFormFactory.convert(graphicBuf, result);
				createJavaView(outBuffer, graphicBuf.toString());
				textEval = false;
				return new String[] { "applet", outBuffer.toString() };
			}
		}

		if (textEval) {
			String res = buffer.toString();
			if (function.length() > 0 && function.equals("$mathml")) {
				MathMLUtilities mathUtil = new MathMLUtilities(engine, false);
				StringWriter stw = new StringWriter();
				mathUtil.toMathML(res, stw);
				return new String[] { "mathml", stw.toString() };
			} else if (function.length() > 0 && function.equals("$tex")) {
				TeXUtilities texUtil = new TeXUtilities(engine);
				StringWriter stw = new StringWriter();
				texUtil.toTeX(res, stw);
				return new String[] { "tex", stw.toString() };
			} else {
				return new String[] { "expr", res };
			}
		}
		return new String[] { "error", "Error in createOutput" };
	}

	/**
	 * Tr to read an older evaluation from the SQL database
	 * 
	 * @return null if there is no suitable evaluation stored in the SQL database
	 */
	// private static IExpr getFromDatabase(Connection conn, F factory, Parser
	// parser, IExpr lhsExpr) {
	// try {
	// IAST list = F.ast(null);
	// Map map = new HashMap();
	// lhsExpr = lhsExpr.variables2Slots(map, list);
	// if (lhsExpr != null) {
	// String lhsString = lhsExpr.toString();
	//
	// Cache cache = CACHE_MANAGER.getCache("meCache");
	// Element element = cache.get(lhsString);
	// if (element != null) {
	// IExpr expr = (IExpr) element.getValue();
	// if (expr != null) {
	// if (list.size() > 1) {
	// expr = Function.replaceSlots(expr, list);
	// }
	// }
	// return expr;
	// }
	//
	// int lhsHash = lhsExpr.hashCode();
	// IExpr rhsExpr = SQLExpressionMap.select(conn, parser, lhsHash, lhsString);
	// if (rhsExpr != null) {
	// cachePut(lhsString, rhsExpr);
	// if (list.size() > 1) {
	// rhsExpr = Function.replaceSlots(rhsExpr, list);
	// }
	// }
	// return rhsExpr;
	// }
	// } catch (Exception e) {
	// if (Config.SHOW_STACKTRACE) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	/**
	 * Save an evaluation in the database
	 * 
	 * @return false if the inExpr or outExpr expressions contain $-variables or
	 *         patterns
	 */
	// private static boolean saveInDatabase(Connection conn, F factory, IExpr
	// lhsExpr, IExpr rhsExpr) {
	// try {
	// IAST list = F.ast(null);
	// Map map = new HashMap();
	// lhsExpr = lhsExpr.variables2Slots(map, list);
	// rhsExpr = rhsExpr.variables2Slots(map, list);
	// if (lhsExpr != null && rhsExpr != null) {
	// String lhsString = lhsExpr.toString();
	// int lhsHash = lhsExpr.hashCode();
	// cachePut(lhsString, rhsExpr);
	// SQLExpressionMap.insert(conn, lhsHash, lhsString, rhsExpr);
	// return true;
	// }
	// } catch (Exception e) {
	// if (Config.SHOW_STACKTRACE) {
	// e.printStackTrace();
	// }
	// }
	// return false;
	// }

	// private static void cachePut(String lhsString, IExpr outExpr) {
	// Cache cache = CACHE_MANAGER.getCache("meCache");
	// Element element = new Element(lhsString, outExpr);
	// cache.put(element);
	// }

	// private static boolean saveLogInDatabase(Connection conn, String inExpr,
	// String addr, String host, String sessionId) {
	// try {
	// SQLExpressionMap.insertLog(conn, inExpr, addr, host, sessionId);
	// return true;
	// } catch (Exception e) {
	// if (Config.SHOW_STACKTRACE) {
	// e.printStackTrace();
	// }
	// }
	// return false;
	// }

	// private static Connection getConnection() throws NamingException,
	// Exception, SQLException {
	// if (UNIT_TEST) {
	// String url = "jdbc:mysql://localhost:3306/jamwiki";
	// return DriverManager.getConnection(url, "root", "");
	// }
	// if (DATA_SOURCE != null && USE_DATABASE) {
	// return DATA_SOURCE.getConnection();
	// }
	// return null;
	// }

	public static String toHTML(String res) {
		if (res != null) {
			StringBuffer sbuf = new StringBuffer(res.length() + 50);

			char ch;
			for (int i = 0; i < res.length(); i++) {
				ch = res.charAt(i);
				switch (ch) {
				case '>':
					sbuf.append("&gt;");
					break;
				case '<':
					sbuf.append("&lt;");
					break;
				case '&':
					sbuf.append("&amp;");
					break;
				case '"':
					sbuf.append("&quot;");
					break;
				default:
					sbuf.append(res.charAt(i));
				}
			}
			return sbuf.toString();
		}
		return "";
	}

	public static String toHTMLNL(String res) {
		if (res != null) {
			StringBuffer sbuf = new StringBuffer(res.length() + 50);

			char ch;
			for (int i = 0; i < res.length(); i++) {
				ch = res.charAt(i);
				switch (ch) {
				case '>':
					sbuf.append("&gt;");
					break;
				case '<':
					sbuf.append("&lt;");
					break;
				case '&':
					sbuf.append("&amp;");
					break;
				case '"':
					sbuf.append("&quot;");
					break;
				case '\n':
					sbuf.append("<br/>");
					break;
				case ' ':
					sbuf.append("&nbsp;");
					break;
				default:
					sbuf.append(res.charAt(i));
				}
			}
			return sbuf.toString();
		}
		return "";
	}

	public void init() throws ServletException {
		super.init();
		// try {
		// Class.forName("com.mysql.jdbc.Driver");
		// } catch (ClassNotFoundException e) {
		// if (Config.SHOW_STACKTRACE) {
		// e.printStackTrace();
		// }
		// }
		F.initSymbols(null, new SymbolObserver());
		Config.SERVER_MODE = true;
		System.out.println("Config.SERVER_MODE = true");
		try {
			// if (CACHE_MANAGER == null) {
			// URL url = getClass().getResource("/ehcache.xml");
			// CACHE_MANAGER = new CacheManager(url);
			// Cache memoryOnlyCache = new Cache("meCache", 1000, false, false, 0, 0);
			// CACHE_MANAGER.addCache(memoryOnlyCache);
			// }
			// Context ctx = new InitialContext();
			// if (ctx == null) {
			// System.out.println("Context not found");
			// }
			// if (USE_DATABASE) {
			// if (UNIT_TEST == true) {
			// MysqlDataSource mysqlDS = new MysqlDataSource();
			// mysqlDS.setUser("root");
			// mysqlDS.setPassword("");
			// mysqlDS.setDatabaseName("jamwiki");
			// DATA_SOURCE = mysqlDS;
			// } else {
			// DATA_SOURCE = (DataSource) ctx.lookup("java:comp/env/jdbc/jamwiki");
			// if (DATA_SOURCE == null) {
			// System.out.println("Datasource not found");
			// }
			// }
			// }
		} catch (Exception e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
	}

	public static void createJavaView(Writer buffer, String graphicData) throws IOException {
		buffer.write("<applet name=\"jvLite\" code=\"jvLite.class\" "
				+
				// jamwiki path
				"codebase=\"../static/lib\" "
				+

				// standalone path
				// "codebase=\"lib\" " +
				"width=\"720\" height=\"400\" " + "alt=\"JavaView lite applet\" " + "archive=\"jvLite.jar\" id=\"applet" + APPLET_NUMBER
				+ "\">\n" + "<param name=\"Axes\" value=\"show\" />\n" + "<param name=\"mathematica\" value=\"");
		// System.out.println(graphicData);
		// writer.write(replace(graphicData));
		buffer.write(graphicData);
		buffer.write("\" /></applet>");
		APPLET_NUMBER++;
	}
}
