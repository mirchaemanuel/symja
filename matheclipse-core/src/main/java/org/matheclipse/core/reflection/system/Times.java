package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Power;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractArgMultiple;
import org.matheclipse.core.eval.interfaces.INumeric;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISignedNumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.HashedOrderlessMatcher;

public class Times extends AbstractArgMultiple implements INumeric {
	/**
	 * Constructor for the singleton
	 */
	public final static Plus CONST = new Plus();
	
	private static HashedOrderlessMatcher ORDERLESS_MATCHER = new HashedOrderlessMatcher(true);

	@Override
	public HashedOrderlessMatcher getHashRuleMap() {
		return ORDERLESS_MATCHER;
	}
	
	public Times() {
	}

	@Override
	public IExpr e2ComArg(final IComplex c0, final IComplex c1) {
		return c0.multiply(c1);
	}

	@Override
	public IExpr e2DblArg(final INum d0, final INum d1) {
		return d0.multiply(d1);
	}

	@Override
	public IExpr e2DblComArg(final IComplexNum d0, final IComplexNum d1) {
		return d0.multiply(d1);
	}

	@Override
	public IExpr e2FraArg(final IFraction f0, final IFraction f1) {
		return f0.multiply(f1);
	}

	@Override
	public IExpr e2IntArg(final IInteger i0, final IInteger i1) {
		return i0.multiply(i1);
	}

	@Override
	public IExpr e2ObjArg(final IExpr o0, final IExpr o1) {
		EvalEngine ee = EvalEngine.get();
		IExpr temp = null;
		if (ee.isNumericMode()) {
			if (o0.equals(F.CD0)) {
				if (o1.isAST(F.DirectedInfinity, 2)) {
					return F.Indeterminate;
				}
				return F.CD0;
			}

			if (o1.equals(F.CD0)) {
				if (o0.isAST(F.DirectedInfinity, 2)) {
					return F.Indeterminate;
				}
				return F.CD0;
			}

			if (o0.equals(F.CD1)) {
				return o1;
			}

			if (o1.equals(F.CD1)) {
				return o0;
			}
		}
		if (o0.equals(F.Indeterminate) || o1.equals(F.Indeterminate)) {
			return F.Indeterminate;
		}
		
		if (o0.equals(F.C0)) {
			if (o1.isAST(F.DirectedInfinity, 2)) {
				return F.Indeterminate;
			}
			return F.C0;
		}

		if (o1.equals(F.C0)) {
			if (o0.isAST(F.DirectedInfinity, 2)) {
				return F.Indeterminate;
			}
			return F.C0;
		}

		if (o0.equals(F.C1)) {
			return o1;
		}

		if (o1.equals(F.C1)) {
			return o0;
		}

		if (o0.equals(o1)) {
			return F.function(F.Power, o0, F.C2);
		}

		if (o0.isAST(F.DirectedInfinity, 2)) {
			temp = eInfinity(o0, o1);
		} else if (o1.isAST(F.DirectedInfinity, 2)) {
			temp = eInfinity(o1, o0);
		}
		if (temp != null) {
			return temp;
		}
		
		if (o0.isAST(F.Power, 3)) {
			final IAST f0 = (IAST) o0;
			if (f0.get(2) instanceof INumber) {
				if (f0.get(1).equals(o1)) {
					return Power(o1, Plus(F.C1, f0.get(2)));
				}

				if (o1.isAST(F.Power, 3)) {
					final IAST f1 = (IAST) o1;

					if (f1.get(2) instanceof INumber) {
						if (f0.get(1).equals(f1.get(1))) {
							return Power(f0.get(1), Plus(f0.get(2), f1.get(2)));
						}
					}
				}
			}
		}

		if (o1.isAST(F.Power, 3) && (((IAST) o1).get(2) instanceof IInteger)) {
			final IAST f1 = (IAST) o1;

			if (f1.get(1).equals(o0)) {
				return Power(o0, Plus(F.C1, f1.get(2)));
			}
		}

		return null;
	}

	private IExpr eInfinity(IExpr inf, IExpr o1) {
		if (inf.equals(F.CInfinity)) {
			if (o1.equals(F.CInfinity)) {
				return F.CInfinity;
			} else if (o1.equals(F.CNInfinity)) {
				return F.CNInfinity;
			} else if (o1 instanceof ISignedNumber) {
				if (((ISignedNumber) o1).isNegative()) {
					return F.CNInfinity;
				} else {
					return F.CInfinity;
				}
			}
		} else if (inf.equals(F.CNInfinity)) {
			if (o1.equals(F.CInfinity)) {
				return F.CNInfinity;
			} else if (o1.equals(F.CNInfinity)) {
				return F.CInfinity;
			} else if (o1 instanceof ISignedNumber) {
				if (((ISignedNumber) o1).isNegative()) {
					return F.CInfinity;
				} else {
					return F.CNInfinity;
				}
			}
		}
		return null;
	}

	@Override
	public IExpr eComIntArg(final IComplex c0, final IInteger i1) {
		return c0.multiply(F.complex(i1, F.C0));
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() == 3) {
			// following normalization conflicts with FactorTerms:
			// if ((functionList.get(1) instanceof ISignedNumber) &&
			// (functionList.get(2) instanceof IAST)
			// && ((IAST) functionList.get(2)).getHeader().equals(F.Plus)) {
			// final IAST arg2 = (IAST) functionList.get(2);
			// final IAST result = F.function(F.Plus);
			// for (int i = 1; i < arg2.size(); i++) {
			// checkCanceled();
			// result.add(F.function(F.Times, functionList.get(1), arg2.get(i)));
			// }
			// return result;
			// }
			return binaryOperator(functionList.get(1), functionList.get(2));
		}

		if (functionList.size() > 3) {
			final ISymbol sym = functionList.topHead();
			final IAST result = F.function(sym);
			IExpr tres;
			IExpr temp = functionList.get(1);
			boolean evaled = false;
			int i = 2;

			while (i < functionList.size()) {

				tres = binaryOperator(temp, functionList.get(i));

				if (tres == null) {

					for (int j = i + 1; j < functionList.size(); j++) {
						tres = binaryOperator(temp, functionList.get(j));

						if (tres != null) {
							evaled = true;
							temp = tres;

							functionList.remove(j);

							break;
						}
					}

					if (tres == null) {
						result.add(temp);
						if (i == functionList.size() - 1) {
							result.add(functionList.get(i));
						} else {
							temp = functionList.get(i);
						}
						i++;
					}

				} else {
					evaled = true;
					temp = tres;

					if (i == (functionList.size() - 1)) {
						result.add(temp);
					}

					i++;
				}
			}

			if (evaled) {
				if ((result.size() == 2) && ((sym.getAttributes() & ISymbol.ONEIDENTITY) == ISymbol.ONEIDENTITY)) {
					return result.get(1);
				}

				return result;
			}
		}

		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.ONEIDENTITY | ISymbol.ORDERLESS | ISymbol.FLAT | ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
	}

	public double evalReal(final double[] stack, final int top, final int size) {
		double result = 1;
		for (int i = top - size + 1; i < top + 1; i++) {
			result *= stack[i];
		}
		return result;
	}
}
