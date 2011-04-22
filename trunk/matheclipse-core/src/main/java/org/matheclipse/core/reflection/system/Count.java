package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.LevelSpecification;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IComplex;
import org.matheclipse.core.interfaces.IComplexNum;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IFraction;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.reflection.system.LeafCount.LeafCountVisitor;
import org.matheclipse.core.visit.AbstractVisitorInt;
import org.matheclipse.generic.nested.LevelSpec;

/**
 * 
 * 
 */
public class Count implements IFunctionEvaluator {
	public static class CountVisitor extends AbstractVisitorInt {
		LevelSpec level;
		final PatternMatcher matcher;

		public CountVisitor(final IExpr pattern, LevelSpec level) {
			super();
			this.matcher = new PatternMatcher(pattern);
			this.level = level;
		}

		private int visitElement(IExpr element) {
			if (level.isInRange()) {
				if (matcher.apply(element)) {
					return 1;
				}
			}
			return 0;
		}

		public int visit(IInteger element) {
			return visitElement(element);
		}

		public int visit(IFraction element) {
			return visitElement(element);
		}

		public int visit(IComplex element) {
			return visitElement(element);
		}

		public int visit(INum element) {
			return visitElement(element);
		}

		public int visit(IComplexNum element) {
			return visitElement(element);
		}

		public int visit(ISymbol element) {
			return visitElement(element);
		}

		public int visit(IPattern element) {
			return visitElement(element);
		}

		public int visit(IStringX element) {
			return visitElement(element);
		}

		public int visit(IAST list) {
			int sum = 0;
			if (level.isInRange()) {
				if (matcher.apply(list)) {
					sum++;
				}
			}
			try {
				level.incCurrentLevel();
				if (level.isInScope()) {
					for (int i = 1; i < list.size(); i++) {
						sum += list.get(i).accept(this);
					}
				}
				return sum;
			} finally {
				level.decCurrentLevel();
			}
		}
	}

	public Count() {
	}

	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 4);
		final LevelSpec level;
		if (ast.size() == 4) {
			level = new LevelSpecification(ast.get(3));
		} else {
			level = new LevelSpec(1);
		}
		int count = ast.get(1).accept(new CountVisitor(ast.get(2), level));
		return F.integer(count);
	}

	public IExpr numericEval(final IAST ast) {
		return evaluate(ast);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDREST);
	}

}
