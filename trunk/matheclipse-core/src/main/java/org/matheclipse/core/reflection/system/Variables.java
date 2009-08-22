package org.matheclipse.core.reflection.system;

import static org.matheclipse.basic.Util.checkCanceled;
import static org.matheclipse.core.expression.F.List;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.VisitorCollectionBoolean;

/**
 * Determine the variable symbols of an expression
 */
public class Variables extends AbstractFunctionEvaluator {
	
	public static class VariablesVisitor extends VisitorCollectionBoolean {
		public VariablesVisitor(int hOffset, Collection<IExpr> collection) {
			super(hOffset, collection);
		}

		public boolean visit(ISymbol symbol) {
			if ((symbol.getAttributes() & ISymbol.CONSTANT) == ISymbol.CONSTANT) {
				return false;
			}
			return true;
		}
	}

	public Variables() {
	}

	/**
	 */
	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2) {
			return null;
		}
		return call(ast.get(1));
	}

	public static IAST call(final IExpr expression) {
		final Set<IExpr> set = new TreeSet<IExpr>();
		expression.accept(new VariablesVisitor(1, set));
		
		final Iterator<IExpr> iter = set.iterator();
		final IAST list = List();
		while (iter.hasNext()) {
			checkCanceled();
			list.add(iter.next());
		}
		return list;
	}

	@Override
	public void setUp(final ISymbol symbol) {
	}

}
