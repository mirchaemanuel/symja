package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.visit.AbstractVisitorInt;

public class LeafCount extends AbstractFunctionEvaluator {

	/**
	 * Calculate the number of leaves in an AST
	 */
	public static class LeafCountVisitor extends AbstractVisitorInt {
		int fHeadOffset;

		public LeafCountVisitor() {
			this(1);
		}

		public LeafCountVisitor(int hOffset) {
			fHeadOffset = hOffset;
		}

		public int visit(IAST list) {
			int sum = 0;
			for (int i = fHeadOffset; i < list.size(); i++) {
				sum += list.get(i).accept(this);
			}
			return sum;
		}
	}
	
	public LeafCount() {
	}

	@Override
	public IExpr evaluate(final IAST functionList) {
		if (functionList.size() != 2) {
			return null;
		}
		IExpr expr = functionList.get(1);
		int leafCount = 0;
		if (expr instanceof IAST) {
			leafCount= expr.accept(new LeafCountVisitor(0));
		} else {
			leafCount = expr.isAtom()?1:0;
		}
		return F.integer(leafCount); 
	}
}
