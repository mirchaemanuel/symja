package org.matheclipse.core.form.tex.reflection;

import org.matheclipse.core.form.tex.AbstractConverter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Operator function conversions
 */
public class MatrixForm extends AbstractConverter {
	public MatrixForm() {
	}

	/**
	 * Converts a given function into the corresponding TeX output
	 * 
	 * @param buf
	 *          StringBuffer for TeX output
	 * @param f
	 *          The math function which should be converted to TeX
	 */
	public boolean convert(final StringBuffer buf, final IAST f, final int precedence) {
		if (f.size() != 2) {
			return false;
		}
		final IAST matrix = matrixQ(f.get(1));
		if (matrix == null) {
			final IAST vector = vectorQ(f.get(1));
			if (vector == null) {
				return false;
			} else {
				buf.append("\\begin{matrix}");
				IExpr temp;
				for (int i = 1; i < vector.size(); i++) {
					temp = vector.get(i);
					fFactory.convert(buf, temp, 0);
				}
				buf.append("\\end{matrix}");
			}
		} else {
			buf.append("\\begin{matrix}");
			IAST temp;
			for (int i = 1; i < matrix.size(); i++) {
				temp = (IAST) matrix.get(i); 
				for (int j = 1; j < temp.size(); j++) {
					buf.append(' ');
					fFactory.convert(buf, temp.get(j), 0);
					buf.append(' ');
				}
				buf.append("\\\n");
			}

			buf.append("\\end{matrix}");
		}
		return true;
	}

	public IAST matrixQ(final IExpr expr) {
		if (!(expr instanceof IAST)) {
			return null;
		}
		final IAST list = (IAST) expr;
		if (!list.topHead().toString().equals("List")) {
			return null;
		}
		final int size = list.size();
		int subSize = -1;
		IExpr temp;
		for (int i = 1; i < size; i++) {
			temp = list.get(i);
			if (!(temp instanceof IAST)) {
				return null;
			}
			final IAST subList = (IAST) temp;
			if (!subList.topHead().toString().equals("List")) {
				return null;
			}
			if (subSize < 0) {
				subSize = subList.size();
			} else if (subSize != subList.size()) {
				return null;
			}
		}
		return list;
	}

	public IAST vectorQ(final IExpr expr) {
		if (!(expr instanceof IAST)) {
			return null;
		}
		final IAST list = (IAST) expr;
		if (!list.topHead().toString().equals("List")) {
			return null;
		}
		final int size = list.size();
		IExpr temp;
		for (int i = 1; i < size; i++) {
			temp = list.get(i);
			if ((temp instanceof IAST) && (((IAST) temp).topHead().toString().equals("List"))) {
				return null;
			}
		}
		return list;
	}
}
