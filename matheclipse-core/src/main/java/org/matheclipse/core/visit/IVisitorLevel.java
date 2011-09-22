package org.matheclipse.core.visit;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.IPatternSequence;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * A visitor which could be used in the
 * <code>org.matheclipse.core.interfaces.IExpr#accept()</code> method.
 * 
 * @param <T>
 * 
 * @see org.matheclipse.core.interfaces.IExpr
 */
public interface IVisitorLevel {

	public abstract int visit(IInteger element);

	public abstract int visit(IFraction element);

	public abstract int visit(IComplex element);

	public abstract int visit(INum element);

	public abstract int visit(IComplexNum element);

	public abstract int visit(ISymbol element);

	public abstract int visit(IPattern element);

	public abstract int visit(IPatternSequence element);

	public abstract int visit(IStringX element);

	public abstract int visit(IAST ast);

}