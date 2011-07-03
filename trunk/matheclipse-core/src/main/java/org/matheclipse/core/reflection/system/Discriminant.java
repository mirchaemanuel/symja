package org.matheclipse.core.reflection.system;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.exception.JASConversionException;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import static org.matheclipse.core.expression.F.*;
import org.matheclipse.core.generic.Functors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

/**
 * 
 * See <a href="http://en.wikipedia.org/wiki/Discriminant">Wikipedia -
 * Discriminant</a>
 */
public class Discriminant extends AbstractFunctionEvaluator {
	// b^2-4*a*c
	private final static IExpr QUADRATIC = Plus(Power($s("b"), C2), Times(CN1, Times(Times(C4, $s("a")), $s("c"))));

	// b^2*c^2-4*a*c^3-4*b^3*d-27*a^2*d^2+18*a*b*c*d
	private final static IExpr CUBIC = Plus(Plus(Plus(Plus(Times(Power($s("b"), C2), Power($s("c"), C2)), Times(CN1, Times(Times(C4,
			$s("a")), Power($s("c"), C3)))), Times(CN1, Times(Times(C4, Power($s("b"), C3)), $s("d")))), Times(CN1, Times(Times(
			integer(27L), Power($s("a"), C2)), Power($s("d"), C2)))), Times(Times(Times(Times(integer(18L), $s("a")), $s("b")), $s("c")),
			$s("d")));

	// Page 405
	// http://books.google.com/books?id=-gGzjSnNnR0C&lpg=PA402&vq=quartic&hl=de&pg=PA405#v=snippet&q=quartic&f=false

	// 256*a0^3*a4^3-27*a0^2*a3^4-27*a1^4*a4^2+16*a0*a2^3*a4
	// -4*a0*a2^3*a3^2-4*a1^2*a2^3*a4-4*a1^3*a3^3+a1^2*a2^2*a3^2
	// -192*a0^2*a1*a3*a4^2-128a0^2*a2^2*a4^2
	// +144*a0^2*a2*a3^2*a4+144*a0*a1^2*a2*a4^2-6*a0*a1^2*a3^2*a4
	// -80*a0*a1*a2^2*a3*a4+18*a0*a1*a2*a3^3+18*a1^3*a2*a3*a4

	// 256*a^3*e^3-27*a^2*d^4-27*b^4*e^2+16*a*c^3*e
	// -4*a*c^3*d^2-4*b^2*c^3*e-4*b^3*d^3+b^2*c^2*d^2
	// -192*a^2*b*d*e^2-128a^2*c^2*e^2
	// +144*a^2*c*d^2*e+144*a*b^2*c*e^2-6*a*b^2*d^2*e
	// -80*a*b*c^2*d*e+18*a*b*c*d^3+18*b^3*c*d*e
	private final static IExpr QUARTIC = Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Plus(Times(Times(
			integer(256L), Power($s("a"), C3)), Power($s("e"), C3)), Times(CN1, Times(Times(integer(27L), Power($s("a"), C2)), Power(
			$s("d"), C4)))), Times(CN1, Times(Times(integer(27L), Power($s("b"), C4)), Power($s("e"), C2)))), Times(Times(Times(
			integer(16L), $s("a")), Power($s("c"), C3)), $s("e"))), Times(CN1, Times(Times(Times(C4, $s("a")), Power($s("c"), C3)),
			Power($s("d"), C2)))), Times(CN1, Times(Times(Times(C4, Power($s("b"), C2)), Power($s("c"), C3)), $s("e")))), Times(CN1,
			Times(Times(C4, Power($s("b"), C3)), Power($s("d"), C3)))), Times(Times(Power($s("b"), C2), Power($s("c"), C2)), Power(
			$s("d"), C2))), Times(CN1,
			Times(Times(Times(Times(integer(192L), Power($s("a"), C2)), $s("b")), $s("d")), Power($s("e"), C2)))), Times(CN1, Times(
			Times(Times(integer(128L), Power($s("a"), C2)), Power($s("c"), C2)), Power($s("e"), C2)))), Times(Times(Times(Times(
			integer(144L), Power($s("a"), C2)), $s("c")), Power($s("d"), C2)), $s("e"))), Times(Times(Times(
			Times(integer(144L), $s("a")), Power($s("b"), C2)), $s("c")), Power($s("e"), C2))), Times(CN1, Times(Times(Times(Times(
			integer(6L), $s("a")), Power($s("b"), C2)), Power($s("d"), C2)), $s("e")))), Times(CN1, Times(Times(Times(Times(Times(
			integer(80L), $s("a")), $s("b")), Power($s("c"), C2)), $s("d")), $s("e")))), Times(Times(Times(Times(integer(18L), $s("a")),
			$s("b")), $s("c")), Power($s("d"), C3))), Times(Times(Times(Times(integer(18L), Power($s("b"), C3)), $s("c")), $s("d")),
			$s("e")));

	private ISymbol[] vars = { $s("a"), $s("b"), $s("c"), $s("d"), $s("e") };

	public Discriminant() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkSize(ast, 3);
		IExpr expr = F.evalExpandAll(ast.get(1));
		IExpr arg2 = ast.get(2);
		if (!arg2.isSymbol()) {
			// TODO allow multinomials
			return null;
		}
		IAST result = F.List();
		IAST resultListDiff = F.List();
		try {
			long degree = CoefficientList.univariateCoefficientList(expr, (ISymbol) arg2, result, resultListDiff);
			if (degree >= Short.MAX_VALUE) {
				throw new WrongArgumentType(ast, ast.get(1), 1, "Polynomial degree" + degree + " is larger than: " + Short.MAX_VALUE);
			}
			if (degree >= 1L && degree <= 4L) {
				IAST rules = F.List();
				for (int i = 1; i < result.size(); i++) {
					rules.add(F.Rule(vars[i - 1], result.get(i)));
				}
				switch ((int) degree) {
				case 2:
					return QUADRATIC.replaceAll(rules);
				case 3:
					return CUBIC.replaceAll(rules);
				case 4:
					return QUARTIC.replaceAll(rules);
				}

			}
			IExpr resultant = Resultant.resultant(result, resultListDiff);
			IExpr disc;
			degree *= (degree - 1);
			degree /= 2;
			IExpr factor = F.Power(result.get(result.size() - 1), F.CN1);
			if (degree % 2L != 0L) {
				factor = F.Times(F.CN1, factor);
			}
			if (resultant.isPlus()) {
				IAST res = (IAST) resultant;
				// distribute the factor over the sum
				res = res.map(Functors.replace1st(F.Times(F.Null, factor)));
				disc = F.eval(res);
			} else {
				disc = F.eval(F.Times(resultant, factor));
			}
			return disc;
		} catch (JASConversionException jce) {
			if (Config.DEBUG) {
				jce.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE);
		super.setUp(symbol);
	}
}