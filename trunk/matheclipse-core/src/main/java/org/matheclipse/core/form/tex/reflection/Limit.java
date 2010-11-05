package org.matheclipse.core.form.tex.reflection;

import org.matheclipse.core.form.tex.AbstractConverter;
import org.matheclipse.core.interfaces.IAST;

public class Limit extends AbstractConverter {

	public Limit() {
	}

	/**
	 * Converts a given function into the corresponding MathML output
	 * 
	 * @param buf
	 *          StringBuffer for MathML output
	 * @param f
	 *          The math function which should be converted to MathML
	 */
	public boolean convert(final StringBuffer buf, final IAST f, final int precedence) {
		if (f.size() == 3) {
			final Object obj = f.get(2);
			IAST rule = null;
			if (obj instanceof IAST) {
				rule = (IAST) obj;
			}
			if ((rule != null) && (rule.size() == 3) && rule.head().toString().equals("Rule")) {
				buf.append("\\lim_{");
				fFactory.convert(buf, rule.get(1), 0);
				buf.append("\\rightarrow ");
				fFactory.convert(buf, rule.get(2), 0); 
			} else {
				rule = null;
				buf.append("\\lim_{");
				fFactory.convert(buf, f.get(2), 0);
			}
			buf.append('}');

			fFactory.convert(buf, f.get(1), 0);
			return true;
		}
		return false;
	}
}