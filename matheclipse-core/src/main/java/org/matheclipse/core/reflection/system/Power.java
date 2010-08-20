package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.C1;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Times;
import static org.matheclipse.core.expression.F.fraction;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.DivisionByZero;
import org.matheclipse.core.eval.interfaces.AbstractArg2;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.UnaryBind1st;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.IRational;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;

public class Power extends AbstractArg2 implements INumeric {

	public Power() {
	}

	// public IExpr e2DblComArg(IDoubleComplex d0, IDoubleComplex d1,
	// AbstractExpressionFactory factory) {
	// return d0.pow(d1);
	// }

	@Override
	public IExpr e2DblComArg(final IComplexNum c0, final IComplexNum c1) {
		return c0.pow(c1);
	}

	@Override
	public IExpr e2ComArg(final IComplex c0, final IComplex c1) {
		return null;
	}

	@Override
	public IExpr e2DblArg(final INum d0, final INum d1) {
		if (d0.isNegative()) {
			return F.complexNum(d0.doubleValue()).pow(F.complexNum(d1.doubleValue()));
		}
		return d0.pow(d1);
	}

	@Override
	public IExpr e2IntArg(final IInteger i0, final IInteger i1) {
		if (i0.equals(F.C0)) {
			// all other cases see e2ObjArg
			return null;
		}

		if (i1.isNegative()) {
			return F.fraction(F.C1, i0.pow(((IInteger) i1.negate()).getBigNumerator().intValue()));
		}

		return i0.pow(i1.getBigNumerator().intValue());
	}

	@Override
	public IExpr e2ObjArg(final IExpr o0, final IExpr o1) {
		EvalEngine ee = EvalEngine.get();
		if (ee.isNumericMode()) {
			if (o0.equals(F.CD0)) {
				if (o1.equals(F.CD0)) {
					// 0^0
					throw new DivisionByZero("0^0");
				}

				if ((o1 instanceof ISignedNumber) && ((ISignedNumber) o1).isNegative()) {
					throw new DivisionByZero("");
				}

				return F.CD0;
			}

			if (o1.equals(F.CD0)) {
				return F.CD1;
			}

			if (o1.equals(F.CD1)) {
				return o0;
			}

			if (o0.equals(F.CD1)) {
				return F.CD1;
			}
		}
		if (o0.equals(F.Indeterminate) || o1.equals(F.Indeterminate)) {
			return F.Indeterminate;
		}
		if (o0.equals(F.C0)) {
			if (o1.equals(F.C0)) {
				// 0^0
				// TODO add a real log message
				// throw new DivisionByZero("0^0");
				ee.getOutPrintStream().println("Infinte expression 0^0");
				return F.Indeterminate;
			}

			if ((o1 instanceof ISignedNumber) && ((ISignedNumber) o1).isNegative()) {
				// throw new DivisionByZero("");
				ee.getOutPrintStream().println("Infinte expression 1/0");
				return F.Indeterminate;
			}

			return F.C0;
		}

		if (o1.equals(F.C0)) {
			return F.C1;
		}

		if (o1.equals(F.C1)) {
			return o0;
		}

		if (o0.equals(F.C1)) {
			return F.C1;
		}

		if (o0.isAST(F.DirectedInfinity, 2) && o1 instanceof ISignedNumber) {
			ISignedNumber is = (ISignedNumber) o1;
			if (o0.equals(F.CInfinity)) {
				if (is.isNegative()) {
					return F.C0;
				} else {
					return F.CInfinity;
				}
			}
			if (o0.equals(F.CNInfinity) && o1 instanceof IInteger) {
				IInteger ii = (IInteger) o1;
				if (ii.isNegative()) {
					return F.C0;
				} else {
					if (ii.isOdd()) {
						return F.CNInfinity;
					} else {
						return F.CInfinity;
					}
				}
			}
		}

		if (o1.equals(F.CN1)) {
			if (o0 instanceof IInteger) {
				return F.fraction(F.C1, (IInteger) o0);
			}
			if (o0 instanceof IFraction) {
				return F.fraction(((IFraction) o0).getDenominator(), ((IFraction) o0).getNumerator());
			}
			if (o0 instanceof IComplex) {
				return ((IComplex) o0).reciprocal();
			}
		}

		if ((o0 instanceof ISignedNumber) && ((ISignedNumber) o0).isNegative() && o1.equals(F.C1D2)) {
			// extract I for sqrt
			return F.Times(F.complex(F.C0, F.C1), F.Power(F.Times(F.CN1, o0), o1));
		}

		if ((o1 instanceof INumber) && o0.isAST(F.Power)) {
			final IAST f0 = (IAST) o0;

			if ((f0.size() == 3) && (f0.get(2) instanceof INumber)) {
				return F.function(F.Power, f0.get(1), F.function(F.Times, f0.get(2), o1));
			}
		}

		if (o0.isAST(F.Times)) {
			if (o1 instanceof IInteger) {
				// (a * b * c)^n => a^n * b^n * c^n
				return ((IAST) o0).args().map(Times(), new UnaryBind1st(Power(F.Null, o1)));
			}
			if (o1 instanceof INumber) {
				final IAST f0 = (IAST) o0;

				if ((f0.size() > 1) && (f0.get(1) instanceof INumber)) {
					return Times(Power(f0.get(1), o1), Power(F.ast(f0, F.Times, true, 2, f0.size()), o1));
				}
			}
		}
		return null;
	}

	public IExpr e2FraArg(IFraction f0, IFraction f1) {
		if (f0.getNumerator().equals(F.C0)) {
			return F.C0;
		}

		if (f1.getNumerator().equals(F.C0)) {
			return F.C1;
		}

		if (!f1.getDenominator().equals(F.C1)) {
			IInteger a;
			IInteger b;
			IFraction f0Temp = f0;
			if (f0.sign() < 0) {
				f0Temp = (IFraction) f0Temp.negate();
			}
			if (f1.isNegative()) {
				b = f0Temp.getNumerator();
				a = f0Temp.getDenominator();
			} else {
				a = f0Temp.getNumerator();
				b = f0Temp.getDenominator();
			}

			// example: (-27)^(2/3) or 8^(1/3)
			if (!f1.getNumerator().equals(F.C1)) {
				try {
					int exp = f1.getNumerator().toInt();
					if (exp < 0) {
						exp *= (-1);
					}
					a = a.pow(exp);
					b = b.pow(exp);
				} catch (ArithmeticException e) {
					return null;
				}
			}

			final IInteger root = f1.getDenominator();

			IInteger[] new_numer = calculateRoot(a, root);
			IInteger[] new_denom = calculateRoot(b, root);
			final IFraction new_root = F.fraction(C1, root);

			if (new_numer != null) {
				if (new_denom != null) {
					IRational p0 = null;
					if (new_denom[1].equals(F.C1)) {
						p0 = new_numer[1];
					} else {
						p0 = fraction(new_numer[1], new_denom[1]);
					}
					if (f0.sign() < 0) {
						return Times(fraction(new_numer[0], new_denom[0]), Power(p0.negate(), new_root));
					}
					return Times(fraction(new_numer[0], new_denom[0]), Power(p0, new_root));
				} else {
					if (a.equals(C1)) {
						return null;
					}
					IRational p0 = null;
					if (b.equals(F.C1)) {
						p0 = new_numer[1];
					} else {
						p0 = fraction(new_numer[1], b);
					}
					if (f0.sign() < 0) {
						return Times(new_numer[0], Power(p0.negate(), new_root));
					}
					return Times(new_numer[0], Power(p0, new_root));
				}
			} else {
				if (new_denom != null) {
					if (b.equals(C1)) {
						return null;
					}
					IRational p0 = null;
					if (new_denom[1].equals(F.C1)) {
						p0 = a;
					} else {
						p0 = F.fraction(a, new_denom[1]);
					}
					if (f0.sign() < 0) {
						return Times(fraction(C1, new_denom[0]), Power(p0.negate(), new_root));
					}
					return Times(fraction(C1, new_denom[0]), Power(p0, new_root));
				}
			}

			return null;
		}
		// now f1 denominator == 1
		int iNumer;
		try {
			iNumer = f1.getNumerator().toInt();
		} catch (ArithmeticException iob) {
			return null;
		}
		return f0.pow(iNumer);
	}

	/**
	 * Split this integer into the nth-root (with prime factors less equal 1021)
	 * and the &quot;rest factor&quot;
	 * 
	 * @return <code>{nth-root, rest factor}</code> or <code>null</code> if the
	 *         root is not available
	 */
	private IInteger[] calculateRoot(IInteger a, IInteger root) {
		try {
			IInteger[] result = new IInteger[2];
			int n = root.toInt();
			if (n > 0) {
				if (a.equals(F.C1)) {
					return null;
					// result[0] = F.C1;
					// result[1] = F.C1;
					// return result;
				}
				if (a.equals(F.CN1)) {
					return null;
					// if (n % 2 == 0) {
					// // even exponent n
					// return null;
					// } else {
					// // odd exponent n
					// result[0] = F.CN1;
					// result[1] = F.C1;
					// return result;
					// }
				}
				result = a.nthRootSplit(n);
				if (result[1].equals(a)) {
					// no roots found
					return null;
				}
				return result;
				// IInteger probe = result.pow(n);
				// if (probe.equals(a)) {
				// return result;
				// }
			}
		} catch (ArithmeticException e) {

		}
		return null;
	}

	@Override
	public IExpr eComIntArg(final IComplex c0, final IInteger i1) {
		if (c0.isZero()) {
			return F.C0;
		}

		if (i1.isZero()) {
			return F.C1;
		}

		return c0.pow(i1.getBigNumerator().intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.matheclipse.parser.interfaces.IEvaluator#setUp(org.matheclipse.parser
	 * .interfaces.ISymbol)
	 */
	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		if (size != 2) {
			throw new UnsupportedOperationException();
		}
		return Math.pow(stack[top - 1], stack[top]);
	}
}
