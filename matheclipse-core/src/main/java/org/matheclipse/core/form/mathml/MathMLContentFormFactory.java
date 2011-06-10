package org.matheclipse.core.form.mathml;

import java.math.BigInteger;
import java.util.Hashtable;

import org.apache.commons.math.fraction.BigFraction;
import org.matheclipse.basic.Config;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.expression.NumberUtil;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.operator.ASTNodeFactory;

/**
 * PresentationGenerator generates MathML presentation output
 * 
 * @deprecated
 */
public class MathMLContentFormFactory extends AbstractMathMLFormFactory implements IConstantHeaders {

	private class Operator {
		String fOperator;

		Operator(final String oper) {
			fOperator = oper;
		}

		/**
		 * 
		 * @param buf
		 */
		public void convert(final StringBuffer buf) {
			tagStart(buf, "mo");
			buf.append(fOperator);
			tagEnd(buf, "mo");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return fOperator;
		}

	}

	/**
	 * Table for constant symbols
	 */
	public final Hashtable<String, Object> CONSTANT_SYMBOLS = new Hashtable<String, Object>(199);

	/**
	 * Description of the Field
	 */
	public final Hashtable<String, AbstractConverter> operTab = new Hashtable<String, AbstractConverter>(199);

	private int plusPrec;

	/**
	 * Constructor
	 * 
	 * @deprecated
	 */
	public MathMLContentFormFactory() {
		this("");
	}

	/**
	 * 
	 * @param tagPrefix
	 * @deprecated
	 */
	public MathMLContentFormFactory(final String tagPrefix) {
		super(tagPrefix);
		init();
	}

	public void convertDouble(final StringBuffer buf, final INum d, final int precedence) {
		if (d.isNegative() && (precedence > plusPrec)) {
			tagStart(buf, "mrow");
			tag(buf, "mo", "(");
		}
		tagStart(buf, "mn");
		buf.append(d.toString());
		tagEnd(buf, "mn");
		if (d.isNegative() && (precedence > plusPrec)) {
			tag(buf, "mo", ")");
			tagEnd(buf, "mrow");
		}
	}

	public void convertDoubleComplex(final StringBuffer buf, final IComplexNum dc, final int precedence) {
		tagStart(buf, "mrow");
		buf.append(String.valueOf(dc.getRealPart()));
		tag(buf, "mo", "+");
		tagStart(buf, "mrow");
		buf.append(String.valueOf(dc.getImaginaryPart()));

		tag(buf, "mo", "&InvisibleTimes;");
		// <!ENTITY ImaginaryI "&#x02148;" >
		tag(buf, "mi", "&#x02148;");
		tagEnd(buf, "mrow");
		tagEnd(buf, "mrow");
	}

	public void convertInteger(final StringBuffer buf, final IInteger i, final int precedence) {
		if (i.isNegative() && (precedence > plusPrec)) {
			tagStart(buf, "mrow");
			tag(buf, "mo", "(");
		}
		tagStart(buf, "mn");
		buf.append(i.getBigNumerator().toString());
		tagEnd(buf, "mn");
		if (i.isNegative() && (precedence > plusPrec)) {
			tag(buf, "mo", ")");
			tagEnd(buf, "mrow");
		}
	}

	public void convertFraction(final StringBuffer buf, final IFraction f, final int precedence) {
		if (f.isNegative() && (precedence > plusPrec)) {
			tagStart(buf, "mrow");
			tag(buf, "mo", "(");
		}
		tagStart(buf, "mfrac");
		tagStart(buf, "mn");
		buf.append(f.getBigNumerator().toString());
		tagEnd(buf, "mn");
		tagStart(buf, "mn");
		buf.append(f.getBigDenominator().toString());
		tagEnd(buf, "mn");
		tagEnd(buf, "mfrac");
		if (f.isNegative() && (precedence > plusPrec)) {
			tag(buf, "mo", ")");
			tagEnd(buf, "mrow");
		}
	}

	public void convertFraction(final StringBuffer buf, final BigFraction f, final int precedence) {
		if (NumberUtil.isNegative(f) && (precedence > plusPrec)) {
			tagStart(buf, "mrow");
			tag(buf, "mo", "(");
		}
		if (f.getDenominator().equals(BigInteger.ONE)) {
			tagStart(buf, "mn");
			buf.append(f.getNumerator().toString());
			tagEnd(buf, "mn");
		} else {
			tagStart(buf, "mfrac");
			tagStart(buf, "mn");
			buf.append(f.getNumerator().toString());
			tagEnd(buf, "mn");
			tagStart(buf, "mn");
			buf.append(f.getDenominator().toString());
			tagEnd(buf, "mn");
			tagEnd(buf, "mfrac");
		}
		if (NumberUtil.isNegative(f) && (precedence > plusPrec)) {
			tag(buf, "mo", ")");
			tagEnd(buf, "mrow");
		}
	}

	public void convertComplex(final StringBuffer buf, final IComplex c, final int precedence) {
		tagStart(buf, "mrow");
		convertFraction(buf, c.getRealPart(), precedence);
		tag(buf, "mo", "+");
		tagStart(buf, "mrow");
		convertFraction(buf, c.getImaginaryPart(), ASTNodeFactory.TIMES_PRECEDENCE);

		tag(buf, "mo", "&InvisibleTimes;");
		// <!ENTITY ImaginaryI "&#x02148;" >
		tag(buf, "mi", "&#x02148;");
		tagEnd(buf, "mrow");
		tagEnd(buf, "mrow");
	}

	public void convertString(final StringBuffer buf, final String str) {
		tagStart(buf, "mtext");
		buf.append(str);
		tagEnd(buf, "mtext");
	}

	public void convertSymbol(final StringBuffer buf, final ISymbol sym) {
		final Object convertedSymbol = CONSTANT_SYMBOLS.get(sym.toString());
		if (convertedSymbol == null) {
			tagStart(buf, "mi");
			buf.append(sym.toString());
			tagEnd(buf, "mi");
		} else {
			if (convertedSymbol.equals(True)) {
				tagStart(buf, "mi");
				buf.append("&");
				buf.append(sym.toString());
				buf.append(";");
				tagEnd(buf, "mi");
			} else {
				if (convertedSymbol instanceof Operator) {
					((Operator) convertedSymbol).convert(buf);
				} else {
					tagStart(buf, "mi");
					buf.append(convertedSymbol.toString());
					tagEnd(buf, "mi");
				}
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param buf
	 *          Description of Parameter
	 * @param p
	 *          Description of Parameter
	 */
	// public void convertPattern(StringBuffer buf, HPattern p) {
	// buf.append(" <mi>");
	// buf.append(p.toString());
	// tagEnd(buf, "mi");
	// }
	public void convertHead(final StringBuffer buf, final IExpr obj) {
		if (obj instanceof ISymbol) {
			final Object ho = CONSTANT_SYMBOLS.get(((ISymbol) obj).toString());
			tagStart(buf, "mi");
			if ((ho != null) && ho.equals(True)) {
				buf.append("&");
			}
			buf.append(((ISymbol) obj).toString());
			tagEnd(buf, "mi");

			tag(buf, "mo", "&af;");
			return;
		}
		convert(buf, obj, 0);
	}

	/**
	 * @deprecated
	 */
	public void convert(final StringBuffer buf, final IExpr o, final int precedence) {
		if (o instanceof IAST) {
			final IAST f = ((IAST) o);
			// System.out.println(f.getHeader().toString());
			// IConverter converter = (IConverter)
			// operTab.get(f.getHeader().toString());
			// if (converter == null) {
			// converter = reflection(f.getHeader().toString());
			// if (converter == null || (converter.convert(buf, f, 0) == false))
			// {
			// convertHeadList(buf, f);
			// }
			// } else {
			// if (converter.convert(buf, f, precedence) == false) {
			// convertHeadList(buf, f);
			// }
			// }
			final IConverter converter = reflection(f.head().toString());
			if ((converter == null) || (!converter.convert(buf, f, precedence))) {
				convertHeadList(buf, f);
			}
			return;
		}
		if (o instanceof INum) {
			convertDouble(buf, (INum) o, precedence);
			return;
		}
		if (o instanceof IComplexNum) {
			convertDoubleComplex(buf, (IComplexNum) o, precedence);
			return;
		}
		if (o instanceof IInteger) {
			convertInteger(buf, (IInteger) o, precedence);
			return;
		}
		if (o instanceof IFraction) {
			convertFraction(buf, (IFraction) o, precedence);
			return;
		}
		if (o instanceof IComplex) {
			convertComplex(buf, (IComplex) o, precedence);
			return;
		}
		if (o instanceof ISymbol) {
			convertSymbol(buf, (ISymbol) o);
			return;
		}
		convertString(buf, o.toString());
	}

	private void convertHeadList(final StringBuffer buf, final IAST f) {
		tagStart(buf, "mrow");
		convertHead(buf, f.head());
		tag(buf, "mo", "&af;");
		tagStart(buf, "mrow");
		tag(buf, "mo", "(");
		tagStart(buf, "mrow");
		for (int i = 1; i < f.size(); i++) {
			convert(buf, f.get(i), 0);
			if (i < f.size() - 1) {
				tag(buf, "mo", ",");
			}
		}
		tagEnd(buf, "mrow");
		tag(buf, "mo", ")");
		tagEnd(buf, "mrow");
		tagEnd(buf, "mrow");

	}

	public String getReflectionNamespace() {
		return "org.matheclipse.core.form.mathml.reflection.";
	}

	public IConverter reflection(final String headString) {
		final IConverter converter = operTab.get(headString);
		if (converter != null) {
			return converter;
		}
		final String namespace = getReflectionNamespace() + headString;

		Class clazz = null;
		try {
			clazz = Class.forName(namespace);
		} catch (final ClassNotFoundException e) {
			// not a predefined function
			return null;
		}

		AbstractConverter module;
		try {
			module = (AbstractConverter) clazz.newInstance();
			module.setFactory(this);
			// module.setExpressionFactory(fExprFactory);
			operTab.put(headString, module);
			return module;
		} catch (final Throwable se) {
			if (Config.DEBUG) {
				se.printStackTrace();
			}
		}
		return null;
	}

	public void init() {
		// operTab.put(Plus, new MMLOperator(this, "mrow", "+"));
		// operTab.put(Equal, new MMLOperator(this, "mrow", "="));
		// operTab.put(Less, new MMLOperator(this, "mrow", "&lt;"));
		// operTab.put(Greater, new MMLOperator(this, "mrow", "&gt;"));
		// operTab.put(LessEqual, new MMLOperator(this, "mrow", "&leq;"));
		// operTab.put(GreaterEqual, new MMLOperator(this, "mrow",
		// "&GreaterEqual;"));
		// operTab.put(Rule, new MMLOperator(this, "mrow", "-&gt;"));
		// operTab.put(RuleDelayed, new MMLOperator(this, "mrow",
		// "&RuleDelayed;"));
		// operTab.put(Set, new MMLOperator(this, "mrow", "="));
		// operTab.put(SetDelayed, new MMLOperator(this, "mrow", ":="));
		// operTab.put(And, new MMLOperator(this, "mrow", "&and;"));
		// operTab.put(Or, new MMLOperator(this, "mrow", "&or;"));
		// operTab.put(Not, new MMLNot(this));

		// operTab.put(Times, new MMLTimes(this, "mrow", "&InvisibleTimes;",
		// exprFactory));
		// operTab.put(Power, new MMLOperator(this, "msup", ""));

		// operTab.put("Sin", new MMLFunction(this, "sin"));
		// operTab.put("Cos", new MMLFunction(this, "cos"));
		// operTab.put("Tan", new MMLFunction(this, "tan"));
		// operTab.put("Cot", new MMLFunction(this, "cot"));
		// operTab.put("ArcSin", new MMLFunction(this, "arcsin"));
		// operTab.put("ArcCos", new MMLFunction(this, "arccos"));
		// operTab.put("ArcTan", new MMLFunction(this, "arctan"));
		// operTab.put("ArcCot", new MMLFunction(this, "arccot"));
		// operTab.put("ArcSinh", new MMLFunction(this, "arcsinh"));
		// operTab.put("ArcCosh", new MMLFunction(this, "arccosh"));
		// operTab.put("ArcTanh", new MMLFunction(this, "arctanh"));
		// operTab.put("ArcCoth", new MMLFunction(this, "arccoth"));
		// operTab.put("Log", new MMLFunction(this, "log"));

		// operTab.put("Sum", new MMLSum(this));
		// operTab.put("Integrate", new MMLIntegrate(this));
		// operTab.put("D", new MMLD(this));
		// operTab.put(Factorial, new MMLFactorial(this));
		// operTab.put("Binomial", new MMLBinomial(this));
		plusPrec = ASTNodeFactory.MMA_STYLE_FACTORY.get("Plus").getPrecedence();
		CONSTANT_SYMBOLS.put("E", "&#02147;");
		CONSTANT_SYMBOLS.put("I", "&#x02148;"); // IMaginaryI
		CONSTANT_SYMBOLS.put("HEllipsis", new Operator("&hellip;"));
		// greek Symbols:
		CONSTANT_SYMBOLS.put("Pi", "&pi;");

		CONSTANT_SYMBOLS.put("Alpha", True);
		CONSTANT_SYMBOLS.put("Beta", True);
		CONSTANT_SYMBOLS.put("Chi", True);
		CONSTANT_SYMBOLS.put("Delta", True);
		CONSTANT_SYMBOLS.put("Epsilon", True);
		CONSTANT_SYMBOLS.put("Phi", True);
		CONSTANT_SYMBOLS.put("Gamma", True);
		CONSTANT_SYMBOLS.put("Eta", True);
		CONSTANT_SYMBOLS.put("Iota", True);
		CONSTANT_SYMBOLS.put("varTheta", True);
		CONSTANT_SYMBOLS.put("Kappa", True);
		CONSTANT_SYMBOLS.put("Lambda", True);
		CONSTANT_SYMBOLS.put("Mu", True);
		CONSTANT_SYMBOLS.put("Nu", True);
		CONSTANT_SYMBOLS.put("Omicron", True);

		CONSTANT_SYMBOLS.put("Theta", True);
		CONSTANT_SYMBOLS.put("Rho", True);
		CONSTANT_SYMBOLS.put("Sigma", True);
		CONSTANT_SYMBOLS.put("Tau", True);
		CONSTANT_SYMBOLS.put("Upsilon", True);
		CONSTANT_SYMBOLS.put("Omega", True);
		CONSTANT_SYMBOLS.put("Xi", True);
		CONSTANT_SYMBOLS.put("Psi", True);
		CONSTANT_SYMBOLS.put("Zeta", True);

		CONSTANT_SYMBOLS.put("alpha", True);
		CONSTANT_SYMBOLS.put("beta", True);
		CONSTANT_SYMBOLS.put("chi", True);
		CONSTANT_SYMBOLS.put("selta", True);
		CONSTANT_SYMBOLS.put("epsilon", True);
		CONSTANT_SYMBOLS.put("phi", True);
		CONSTANT_SYMBOLS.put("gamma", True);
		CONSTANT_SYMBOLS.put("eta", True);
		CONSTANT_SYMBOLS.put("iota", True);
		CONSTANT_SYMBOLS.put("varphi", True);
		CONSTANT_SYMBOLS.put("kappa", True);
		CONSTANT_SYMBOLS.put("lambda", True);
		CONSTANT_SYMBOLS.put("mu", True);
		CONSTANT_SYMBOLS.put("nu", True);
		CONSTANT_SYMBOLS.put("omicron", True);
		// see "Pi"
		// CONSTANT_SYMBOLS.put("pi", True);
		CONSTANT_SYMBOLS.put("theta", True);
		CONSTANT_SYMBOLS.put("rho", True);
		CONSTANT_SYMBOLS.put("sigma", True);
		CONSTANT_SYMBOLS.put("tau", True);
		CONSTANT_SYMBOLS.put("upsilon", True);
		CONSTANT_SYMBOLS.put("varomega", True);
		CONSTANT_SYMBOLS.put("omega", True);
		CONSTANT_SYMBOLS.put("xi", True);
		CONSTANT_SYMBOLS.put("psi", True);
		CONSTANT_SYMBOLS.put("zeta", True);

		ENTITY_TABLE.put("&af;", "&#xE8A0;");
		ENTITY_TABLE.put("&dd;", "&#xF74C;");
		ENTITY_TABLE.put("&InvisibleTimes;", "&#xE89E;");

		ENTITY_TABLE.put("&Integral;", "&#x222B;");
		ENTITY_TABLE.put("&PartialD;", "&#x2202;");
		ENTITY_TABLE.put("&Product;", "&#x220F;");

	}

}
