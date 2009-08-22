package org.matheclipse.core.form.java;

import static org.matheclipse.basic.Util.checkCanceled;

import java.util.Hashtable;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class JavaFormFactory {
	/**
	 * Table for constant symbols
	 */
	public final Hashtable<String, String> CONSTANT_SYMBOLS = new Hashtable<String, String>(199);

	private String fTypeName;

	public JavaFormFactory(final String typeName) {
		fTypeName = typeName;
		init();
	}

	private void init() {
		CONSTANT_SYMBOLS.put("Times", "multiply");
		CONSTANT_SYMBOLS.put("Plus", "add");
		CONSTANT_SYMBOLS.put("PrimeQ", "isProbablePrime");
	}

	public void convertHead(final StringBuffer buf, final IExpr expr) {
		if (expr instanceof ISymbol) {
			final String ho = CONSTANT_SYMBOLS.get(((ISymbol) expr).toString());
			if (ho != null) {
				buf.append(ho);
			} else {
				final String eStr = expr.toString();
				buf.append(Character.toLowerCase(eStr.charAt(0)));
				buf.append(eStr.substring(1));
			}
			return;
		}
		convert(buf, expr);
	}

	public void convertAST(final StringBuffer buf, final IAST f) {
		if (f.size() == 1) {
			convertHead(buf, f.head());
			buf.append("()");
		} else if (f.size() == 2) {
			convert(buf, f.get(1));
			buf.append(".");
			convertHead(buf, f.head());
			buf.append("()");
		} else {
			for (int i = 2; i < f.size(); i++) {
				checkCanceled();
				convert(buf, f.get(i));
				if (i < f.size() - 1) {
				}
			}
		}
	}

	public void convert(final StringBuffer buf, final IExpr o) {
		if (o instanceof IAST) {
			final IAST f = ((IAST) o);
			convertAST(buf, f);
			return;
		}
		if (o instanceof INum) {
			buf.append(o.toString());
			return;
		}
		if (o instanceof IInteger) {
			buf.append(o.toString());
			return;
		}
		if (o instanceof ISymbol) {
			buf.append(o.toString());
			return;
		}
	}
}
