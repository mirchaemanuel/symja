package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.StringX;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class FromCharacterCode extends AbstractFunctionEvaluator {

	public FromCharacterCode() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2) {
			return null;
		}
		try {
			if (ast.get(1).isList()) {
				final IAST list = (IAST) ast.get(1);
				final StringBuffer buffer = new StringBuffer();
				char ch;
				for (int i = 1; i < list.size(); i++) {
					checkCanceled();
					if (list.get(i) instanceof IInteger) {
						ch = (char) ((IInteger) list.get(i)).toInt();
						buffer.append(ch);
					} else {
						return null;
					}
				}
				return StringX.valueOf(buffer);
			}
			if (ast.get(1) instanceof IInteger) {
				final char ch = (char) ((IInteger) ast.get(1)).toInt();
				return StringX.valueOf(ch);
			}
		} catch (final ArithmeticException e) {

		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
	}

	public static List<IExpr> fromCharcterCode(final String unicodeInput, final String inputEncoding, final List<IExpr> list) {
		try {
			final String utf8String = new String(unicodeInput.getBytes(inputEncoding), "UTF-8");
			int characterCode;
			for (int i = 0; i < utf8String.length(); i++) {
				checkCanceled();
				characterCode = utf8String.charAt(i);
				list.add( F.integer(characterCode));
//				list.add(new IntegerImpl(characterCode));
			}
			return list;
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
