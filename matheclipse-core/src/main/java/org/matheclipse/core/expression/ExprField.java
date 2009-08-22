package org.matheclipse.core.expression;

import org.apache.commons.math.Field;
import org.apache.commons.math.FieldElement;
import org.matheclipse.core.interfaces.IExpr;

public class ExprField implements Field<IExpr> {
	public final static ExprField CONST = new ExprField();

	@Override
	public IExpr getOne() {
		return F.C1;
	}

	@Override
	public IExpr getZero() {
		return F.C0;
	}

	@Override
	public Class<? extends FieldElement<IExpr>> getRuntimeClass() {
		return IExpr.class;
	}
}
