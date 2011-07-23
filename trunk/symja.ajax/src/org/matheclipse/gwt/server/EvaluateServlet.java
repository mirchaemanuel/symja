package org.matheclipse.gwt.server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.convert.AST2Expr;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.MathMLUtilities;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.reflection.system.Function;
import org.matheclipse.gwt.server.entity.UserDataEntity;
import org.matheclipse.gwt.server.entity.UserDataService;
import org.matheclipse.gwt.server.entity.UserSymbolEntity;
import org.matheclipse.gwt.server.entity.UserSymbolService;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.math.MathException;

import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class EvaluateServlet extends HttpServlet {
	private static final long serialVersionUID = 6265703737413093134L;

	private static final Logger log = Logger.getLogger(EvaluateServlet.class.getName());

	// private static final boolean UNIT_TEST = false;

	private static final boolean DEBUG = true;

	private static final boolean USE_MEMCACHE = false;

	private static final int MAX_NUMBER_OF_VARS = 100;

	public static Cache cache = null;

	public static int APPLET_NUMBER = 1;

	public static final String UTF8 = "utf-8";

	public static final String EVAL_ENGINE = EvalEngine.class.getName();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {
			res.setContentType("text/plain");
			res.setHeader("Cache-Control", "no-cache");

			String name = "evaluate";
			String value = req.getParameter(name);
			if (value == null) {
				out.println(URLEncoder.encode("0;error;No input expression posted!", "UTF-8"));
				return;
			}
			value = value.trim();
			if (value.length() > Short.MAX_VALUE) {
				out.println(URLEncoder.encode("0;error;Input expression to large!", "UTF-8"));
				return;
			}
			log.warning("In::" + value);

			String result = evaluate(req, value, "", 0);
			// log.warning("Out::" + result);
			out.println(result);// URLEncoder.encode(result, "UTF-8"));
		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg != null) {
				out.println(URLEncoder.encode("0;error;Exception: " + msg, "UTF-8"));
			}
			out.println(URLEncoder.encode("0;error;Exception: " + e.getClass().getSimpleName(), "UTF-8"));
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
			// engine = (EvalEngine) session.getAttribute(EVAL_ENGINE);
			// if (engine == null) {
			// ExprFactory f = new ExprFactory(new SystemNamespace());
			// PrintStream pout = new PrintStream();
			engine = new EvalEngine(session.getId(), 256, 256, outs, false);
			// session.setAttribute(EVAL_ENGINE, engine);
			// } else {
			// engine.init();
			// engine.setOutPrintStream(outs);
			// engine.setSessionID(session.getId());
			// // init ThreadLocal instance:
			// EvalEngine.set(engine);
			// }
		} else {
			engine = new EvalEngine("no-session", 256, 256, outs, false);
		}

		try {
			String[] result = evaluateString(request, engine, expression, function);
			if (!saveModifiedUserSymbols(engine)) {
				return counter + ";error;Number of user '$'-symbols\ngreater than " + MAX_NUMBER_OF_VARS + " or \nformula to big!";
			}
			StringBuilder buf = outWriter.getBuffer();
			buf.append(result[1]);
			return counter + ";" + result[0] + ";" + buf.toString();
			// } catch (Exception e) {
			// if (Config.SHOW_STACKTRACE) {
			// e.printStackTrace();
			// }
			// return counter + ";error;Exception occurred in evaluate!";
		} finally {
			// tear down associated ThreadLocal from EvalEngine
			EvalEngine.remove();
		}

	}

	private boolean saveModifiedUserSymbols(EvalEngine engine) {
		UserService userService = UserServiceFactory.getUserService();
		if (userService.getCurrentUser() != null) {
			User user = userService.getCurrentUser();
			if (user != null) {
				try {
					UserDataEntity userData = UserDataService.findByUserId(user);
					if (userData == null) {
						userData = new UserDataEntity(user);
						UserDataService.save(userData);
					}
					Set<ISymbol> modifiedSymbols = engine.getModifiedVariables();
					for (ISymbol symbol : modifiedSymbols) {
						// StringBuilder bldr = new StringBuilder(256);
						// List<IAST> defList = symbol.definition();
						//
						// if (defList.size() > 0) {
						// for (int i = 0; i < defList.size(); i++) {
						// bldr.append(defList.get(i).toString());
						// if (i < defList.size() - 1) {
						// bldr.append(";");
						// }
						// }
						// }
						int attributes = symbol.getAttributes();
						String source;

						source = symbol.definitionToString();
						if (source.length() > Short.MAX_VALUE) {
							return false;
						}
						UserSymbolEntity symbolEntity = new UserSymbolEntity(user, symbol.toString(), source, attributes);
						UserSymbolEntity newSymbolEntity = UserSymbolService.modify(symbolEntity);
						if (newSymbolEntity != null) {
							userData.incSymbolCounter();
							if (userData.getSymbolCounter() > MAX_NUMBER_OF_VARS) {
								UserSymbolService.delete(newSymbolEntity);
								userData.decSymbolCounter();
								return false;
							}
						}
					}
					UserDataService.update(userData, new Date());
				} catch (IOException e) {
					if (DEBUG) {
						e.printStackTrace();
					}
					return false;
				}
			}
		}
		return true;
	}

	public static String[] evaluateString(HttpServletRequest request, EvalEngine engine, String inputString, String function) {

		try {
			Parser parser = new Parser();
			ASTNode node = parser.parse(inputString);
			IExpr inExpr = AST2Expr.CONST.convert(node);
			if (inExpr != null) {
				if (inExpr instanceof IAST) {
					IAST ast = (IAST) inExpr;
					ISymbol sym = ast.topHead();
					if (sym.toString().equals("UserVariables")) {
						UserService userService = UserServiceFactory.getUserService();
						if (userService.getCurrentUser() != null) {
							User user = userService.getCurrentUser();
							if (user != null) {
								return listUserVariables(user.getUserId());
							}
						}

					}
				}
				// inExpr contains the user input from the web interface in
				// internal format now
				StringBufferWriter outBuffer = new StringBufferWriter();
				IExpr outExpr;
				if (USE_MEMCACHE) {
					outExpr = getFromMemcache(inExpr);
					if (outExpr != null) {
						if (!outExpr.equals(F.Null)) {
							OutputFormFactory.get().convert(outBuffer, outExpr);
							return createOutput(outBuffer, null, engine, function);
						}
					}
				}
				outExpr = MathEvaluator.eval(engine, outBuffer, inExpr);
				// TimeConstrainedEvaluator utility = new
				// TimeConstrainedEvaluator(engine, false,
				// Config.TIME_CONSTRAINED_MILLISECONDS);
				// outExpr = utility.constrainedEval(outBuffer, inExpr);
				// if (outExpr != null) {
				if (USE_MEMCACHE) {
					if (inExpr != outExpr && outExpr != null) { // compare pointers
						putToMemcache(inExpr, outExpr);
					}
				}
				return createOutput(outBuffer, null, engine, function);
				// } else {
				// // show error messages:
				// return new String[] { "error", outBuffer.toString() };
				// // out.println(toHTML(outBuffer.toString()));
				// }

			} else {
				return new String[] { "error", "Input string parsed to null" };
			}
		} catch (MathException se) {
			return new String[] { "error", se.getMessage() };
		} catch (IOException e) {
			String msg = e.getMessage();
			if (msg != null) {
				return new String[] { "error", "IOException occured: " + msg };
			}
			return new String[] { "error", "IOException occured" };
		} catch (Exception e) {
			// error message
			// if (Config.SHOW_STACKTRACE) {
			// e.printStackTrace();
			// }
			String msg = e.getMessage();
			if (msg != null) {
				return new String[] { "error", "Error in evaluateString: " + msg };
			}
			return new String[] { "error", "Error in evaluateString" + e.getClass().getSimpleName() };

		}
	}

	private static String[] listUserVariables(String userId) {
		StringBuilder bldr = new StringBuilder();
		boolean rest = false;
		bldr.append("{");
		QueryResultIterable<UserSymbolEntity> qri = UserSymbolService.getAll(userId);
		for (UserSymbolEntity userSymbolEntity : qri) {
			if (rest) {
				bldr.append(", ");
			} else {
				rest = true;
			}
			bldr.append(userSymbolEntity.getSymbolName());
		}
		bldr.append("}");
		return new String[] { "expr", bldr.toString() };
	}

	private static String[] createOutput(StringBufferWriter buffer, IExpr rhsExpr, EvalEngine engine, String function)
			throws IOException {

		boolean textEval = true;
		// if (rhsExpr != null && rhsExpr instanceof IAST && rhsExpr.isAST(F.Show,
		// 2)) {
		// IAST ast = (IAST) rhsExpr;
		// if (ast.size() == 2 && ast.get(0).toString().equals("Show")) {
		// StringBufferWriter outBuffer = new StringBufferWriter();
		// outBuffer = new StringBufferWriter();
		// StringBufferWriter graphicBuf = new StringBufferWriter();
		// IExpr result = (IExpr) ast.get(1);
		// graphicBuf.setIgnoreNewLine(true);
		// OutputFormFactory outputFormFactory = OutputFormFactory.get();
		// outputFormFactory.convert(graphicBuf, result);
		// createJavaView(outBuffer, graphicBuf.toString());
		// textEval = false;
		// return new String[] { "applet", outBuffer.toString() };
		// }
		// }

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
	 * Try to read an older evaluation from the Memcache
	 * 
	 * @return null if there is no suitable evaluation stored in the memcache
	 */
	private static IExpr getFromMemcache(IExpr lhsExpr) {
		try {
			IAST list = F.ast(null);
			Map<IExpr, IExpr> map = new HashMap<IExpr, IExpr>();
			lhsExpr = lhsExpr.variables2Slots(map, list);
			if (lhsExpr != null) {
				String lhsString = lhsExpr.toString();
				IExpr expr = (IExpr) cache.get(lhsString);
				if (expr != null) {
					if (list.size() > 1) {
						expr = Function.replaceSlots(expr, list);
					}
					return expr;
				}

			}
		} catch (Exception e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Save an evaluation in the memcache.
	 * 
	 * @return false if the lhsExpr or rhsExpr expressions contain $-variables or
	 *         patterns
	 */
	private static boolean putToMemcache(IExpr lhsExpr, IExpr rhsExpr) {
		try {
			IAST list = F.ast(null);
			Map<IExpr, IExpr> map = new HashMap<IExpr, IExpr>();
			lhsExpr = lhsExpr.variables2Slots(map, list);
			rhsExpr = rhsExpr.variables2Slots(map, list);
			if (lhsExpr != null && rhsExpr != null) {
				String lhsString = lhsExpr.toString();
				int lhsHash = lhsExpr.hashCode();
				cache.put(lhsString, rhsExpr);
				return true;
			}
		} catch (Exception e) {
			if (Config.SHOW_STACKTRACE) {
				e.printStackTrace();
			}
		}
		return false;
	}

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

	@Override
	public void init() throws ServletException {
		super.init();
		// try {
		// Class.forName("com.mysql.jdbc.Driver");
		// } catch (ClassNotFoundException e) {
		// if (Config.SHOW_STACKTRACE) {
		// e.printStackTrace();
		// }
		// }
		if (!Config.SERVER_MODE) {
			F.initSymbols(null, new SymbolObserver(), false);
			Config.SERVER_MODE = true; 
			log.info("Config.SERVER_MODE = true");

			// TODO optimize - the following initialization is very slow
			// F.Integrate.setEvaluator(org.matheclipse.core.reflection.system.Integrate.CONST);

			// System.out.println("Config.SERVER_MODE = true");
		}
		if (cache == null) {
			try {
				CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
				cache = cacheFactory.createCache(Collections.emptyMap());
			} catch (Exception e) {
				if (Config.SHOW_STACKTRACE) {
					e.printStackTrace();
				}
			}
		}
	}

	// public static void createJavaView(Writer buffer, String graphicData) throws
	// IOException {
	// buffer.write("<applet name=\"jvLite\" code=\"jvLite.class\" "
	// +
	// // jamwiki path
	// "codebase=\"../static/lib\" "
	// +
	//
	// // standalone path
	// // "codebase=\"lib\" " +
	// "width=\"720\" height=\"400\" " + "alt=\"JavaView lite applet\" " +
	// "archive=\"jvLite.jar\" id=\"applet" + APPLET_NUMBER
	// + "\">\n" + "<param name=\"Axes\" value=\"show\" />\n" +
	// "<param name=\"mathematica\" value=\"");
	// // System.out.println(graphicData);
	// // writer.write(replace(graphicData));
	// buffer.write(graphicData);
	// buffer.write("\" /></applet>");
	// APPLET_NUMBER++;
	// }
}
