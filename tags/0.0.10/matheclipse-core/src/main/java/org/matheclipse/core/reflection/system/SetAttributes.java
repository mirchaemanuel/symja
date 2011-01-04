package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.RuleCreationError;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IConstantHeaders;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Set the attributes for a symbol
 * 
 */
public class SetAttributes extends AbstractFunctionEvaluator implements IConstantHeaders {

	public SetAttributes() {
	}

	/**
   *
   */
	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 3) {
			throw new WrongNumberOfArguments(functionList, 2, functionList.size() - 1);
		}

		if (functionList.get(1) instanceof ISymbol) {
			final ISymbol sym = ((ISymbol) functionList.get(1));
			if (!EvalEngine.get().isPackageMode()) {
				if (Config.SERVER_MODE && (sym.toString().charAt(0) != '$')) {
					throw new RuleCreationError(sym);
				}
			}
			if (functionList.get(2) instanceof ISymbol) {
				if (((ISymbol) functionList.get(2)) == F.Flat) {
					sym.setAttributes(ISymbol.FLAT);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.Listable) {
					sym.setAttributes(ISymbol.LISTABLE);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.OneIdentity) {
					sym.setAttributes(ISymbol.ONEIDENTITY);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.Orderless) {
					sym.setAttributes(ISymbol.ORDERLESS);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.HoldAll) {
					sym.setAttributes(ISymbol.HOLDALL);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.HoldFirst) {
					sym.setAttributes(ISymbol.HOLDFIRST);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.HoldRest) {
					sym.setAttributes(ISymbol.HOLDREST);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.NHoldAll) {
					sym.setAttributes(ISymbol.NHOLDALL);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.NHoldFirst) {
					sym.setAttributes(ISymbol.NHOLDFIRST);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.NHoldRest) {
					sym.setAttributes(ISymbol.NHOLDREST);

					return F.Null;
				}

				if (((ISymbol) functionList.get(2)) == F.NumericFunction) {
					sym.setAttributes(ISymbol.NUMERICFUNCTION);

					return F.Null;
				}

			} else {
				if ((functionList.get(2) instanceof IAST) && (((IAST) functionList.get(2)).head() == F.List)) {
					final IAST lst = (IAST) functionList.get(2);
					int symbolAttributes = ISymbol.NOATTRIBUTE;
					for (int i = 1; i < lst.size(); i++) {

						if (((ISymbol) lst.get(i)) == F.Flat) {
							sym.setAttributes(symbolAttributes | ISymbol.FLAT);
						}

						if (((ISymbol) lst.get(i)) == F.Listable) {
							sym.setAttributes(symbolAttributes | ISymbol.LISTABLE);
						}

						if (((ISymbol) lst.get(i)) == F.OneIdentity) {
							sym.setAttributes(symbolAttributes | ISymbol.ONEIDENTITY);
						}

						if (((ISymbol) lst.get(i)) == F.Orderless) {
							sym.setAttributes(symbolAttributes | ISymbol.ORDERLESS);
						}

						if (((ISymbol) lst.get(i)) == F.HoldAll) {
							sym.setAttributes(symbolAttributes | ISymbol.HOLDALL);
						}

						if (((ISymbol) lst.get(i)) == F.HoldFirst) {
							sym.setAttributes(symbolAttributes | ISymbol.HOLDFIRST);
						}

						if (((ISymbol) lst.get(i)) == F.HoldRest) {
							sym.setAttributes(symbolAttributes | ISymbol.HOLDREST);
						}

						if (((ISymbol) lst.get(i)) == F.NHoldAll) {
							sym.setAttributes(symbolAttributes | ISymbol.NHOLDALL);
						}

						if (((ISymbol) lst.get(i)) == F.NHoldFirst) {
							sym.setAttributes(symbolAttributes | ISymbol.NHOLDFIRST);
						}

						if (((ISymbol) lst.get(i)) == F.NHoldRest) {
							sym.setAttributes(symbolAttributes | ISymbol.NHOLDREST);
						}

						if (((ISymbol) lst.get(i)) == F.NumericFunction) {
							sym.setAttributes(symbolAttributes | ISymbol.NUMERICFUNCTION);
						}

						symbolAttributes = sym.getAttributes();
					}
					// end for

					return F.Null;
				}
			}
		}
		return null;
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
		symbol.setAttributes(ISymbol.HOLDFIRST);
	}

}
