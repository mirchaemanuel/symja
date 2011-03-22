package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
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
import org.matheclipse.core.visit.IVisitorBoolean;

import com.google.common.base.Predicate;

/**
 *  
 */
public class Exponent extends AbstractFunctionEvaluator {
	public class PowerVisitorPredicate implements IVisitorBoolean {
		int fHeadOffset;

		final Predicate<IExpr> fMatcher;
		IAST fCollector;

		public PowerVisitorPredicate(IAST collector, final Predicate<IExpr> matcher) {
			this(1, collector,matcher);
		}

		public PowerVisitorPredicate(int hOffset, IAST collector, final Predicate<IExpr> matcher) {
			fHeadOffset = hOffset;
			fCollector = collector;
			fMatcher = matcher;
		}

		public boolean visit(IInteger element) {
			return false;
		}

		public boolean visit(IFraction element) {
			return false;
		}

		public boolean visit(IComplex element) {
			return false;
		}

		public boolean visit(INum element) {
			return false;
		}

		public boolean visit(IComplexNum element) {
			return false;
		}

		public boolean visit(ISymbol element) {
			return false;
		}

		public boolean visit(IPattern element) {
			return false;
		}

		public boolean visit(IStringX element) {
			return false;
		}

		public boolean visit(IAST ast) {
			if (ast.isPower()) {
				if (fMatcher.apply(ast.get(1))) {
					fCollector.add(ast.get(2));
					return true;
				}
			}
			for (int i = fHeadOffset; i < ast.size(); i++) {
				 ast.get(i).accept(this);
			}
			return false;
		}
	}

	public Exponent() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		Validate.checkRange(ast, 3, 4);
		IAST collector = F.Max();
		IExpr expnd = F.evalExpandAll(ast.get(1));
		if (expnd.isAST()) {
			IAST result = (IAST) expnd;
			IExpr form = ast.get(2);
			if (ast.size() == 4) {
				ISymbol sym = Validate.checkSymbolType(ast, 3);
				collector = F.ast(sym);
			}
			
			final PatternMatcher matcher = new PatternMatcher(form);
			result.accept(new PowerVisitorPredicate(collector,matcher));
			if (collector.size() > 1) {
				return collector;
			}
		}
		return F.CNInfinity;
	}

}