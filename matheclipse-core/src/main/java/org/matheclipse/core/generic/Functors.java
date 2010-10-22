package org.matheclipse.core.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.IPatternMatcher;
import org.matheclipse.core.patternmatching.PatternMatcherAndEvaluator;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class Functors {
	private static class AppendFunctor implements Function<IExpr, IExpr> {
		protected final IAST fAST;

		/**
		 * 
		 * @param ast
		 *          the AST which should be cloned in the {@code apply} method
		 */
		public AppendFunctor(final IAST ast) {
			fAST = ast;
		}

		@Override
		public IExpr apply(final IExpr arg) {
			final IAST ast = fAST.clone();
			ast.add(arg);
			return ast;
		}

	}

	private static class ConstantFunctor implements Function<IExpr, IExpr> {
		final IExpr fConstant;

		public ConstantFunctor(final IExpr expr) {
			fConstant = expr;
		}

		@Override
		public IExpr apply(final IExpr arg) {
			return fConstant;
		}

	}

	private static class ReplaceArgFunctor implements Function<IExpr, IExpr> {
		private final IAST fConstant;
		private final int fPosition;

		/**
		 * 
		 * @param ast
		 *          the complete AST which should be cloned in the {@code apply}
		 *          method
		 * @param position
		 *          the position which should be replaced in the
		 *          <code>apply()</code> method.
		 */
		public ReplaceArgFunctor(final IAST ast, int position) {
			fConstant = ast;
			fPosition = position;
		}

		@Override
		public IExpr apply(final IExpr arg) {
			final IAST ast = fConstant.clone();
			ast.set(fPosition, arg);
			return ast;
		}

	}

	private static class RulesFunctor implements Function<IExpr, IExpr> {
		private final Map<? extends IExpr, ? extends IExpr> fEqualRules;

		/**
		 * 
		 * @param ast
		 *          the complete AST which should be cloned in the {@code apply}
		 *          method
		 * @param position
		 *          the position which should be replaced in the
		 *          <code>apply()</code> method.
		 */
		public RulesFunctor(Map<? extends IExpr, ? extends IExpr> rulesMap) {
			fEqualRules = rulesMap;
		}

		@Override
		public IExpr apply(final IExpr arg) {
			return fEqualRules.get(arg);
		}

	}

	private static class RulesPatternFunctor implements Function<IExpr, IExpr> {
		private final Map<IExpr, IExpr> fEqualRules;
		private final List<PatternMatcherAndEvaluator> fMatchers;

		/**
		 * 
		 * @param ast
		 *          the complete AST which should be cloned in the {@code apply}
		 *          method
		 * @param position
		 *          the position which should be replaced in the
		 *          <code>apply()</code> method.
		 */
		public RulesPatternFunctor(Map<IExpr, IExpr> equalRules, List<PatternMatcherAndEvaluator> matchers) {
			fEqualRules = equalRules;
			fMatchers = matchers;
		}

		@Override
		public IExpr apply(final IExpr arg) {
			IExpr temp = fEqualRules.get(arg);
			if (temp != null) {
				return temp;
			}
			// for (IPatternMatcher<IExpr> matcher : fMatchers) {
			IPatternMatcher<IExpr> matcher;
			for (int i = 0; i < fMatchers.size(); i++) {
				temp = fMatchers.get(i).eval(arg);
				if (temp != null) {
					return temp;
				}
			}
			return null;
		}
	}

	/**
	 * Return a function which clones the given AST and appends the argument to
	 * the cloned AST in the <code>apply()</code> method.
	 * 
	 * @param ast
	 * @return
	 */
	public static Function<IExpr, IExpr> append(@Nonnull IAST ast) {
		return new AppendFunctor(ast);
	}

	/**
	 * Return a function which returns the given expression in the
	 * <code>apply()</code> method.
	 * 
	 * @param expr
	 * @return
	 */
	public static Function<IExpr, IExpr> constant(@Nonnull IExpr expr) {
		return new ConstantFunctor(expr);
	}

	/**
	 * Replace the argument at the first position in the AST
	 * 
	 * @param ast
	 * @return
	 */
	public static Function<IExpr, IExpr> replace1st(@Nonnull IAST ast) {
		return new ReplaceArgFunctor(ast, 1);
	}

	/**
	 * Replace the argument at the second position in the AST
	 * 
	 * @param ast
	 * @return
	 */
	public static Function<IExpr, IExpr> replace2nd(@Nonnull IAST ast) {
		return new ReplaceArgFunctor(ast, 2);
	}

	/**
	 * Replace the argument at the given position in the AST
	 * 
	 * @param ast
	 * @param ith
	 * @return
	 */
	public static Function<IExpr, IExpr> replaceArg(@Nonnull IAST ast, int position) {
		return new ReplaceArgFunctor(ast, position);
	}

	/**
	 * Create a functor from the given map, which calls the
	 * <code>rulesMap.get()</code> in the functors <code>apply</code>method.
	 * 
	 * @param rulesMap
	 * @return
	 */
	public static Function<IExpr, IExpr> rules(Map<? extends IExpr, ? extends IExpr> rulesMap) {
		return new RulesFunctor(rulesMap);
	}

	public static Function<IExpr, IExpr> rules(@Nonnull IAST rulesAST) throws WrongArgumentType {
		Map<IExpr, IExpr> equalRules = new HashMap<IExpr, IExpr>();
		List<PatternMatcherAndEvaluator> matchers = new ArrayList<PatternMatcherAndEvaluator>();
		Predicate<IExpr> patternQ = new Predicate<IExpr>() {
			@Override
			public boolean apply(IExpr input) {
				return input instanceof IPattern;
			}
		};
		if (rulesAST.isList()) {
			// assuming multiple rules in a list
			IAST rule;

			for (final IExpr expr : rulesAST) {
				if (expr.isAST(F.Rule, 3)) {
					rule = (IAST) expr;
					if (rule.get(1).isFree(patternQ)) {
						equalRules.put(rule.get(1), rule.get(2));
					} else {
						matchers.add(new PatternMatcherAndEvaluator(F.SetDelayed, rule.get(1), rule.get(2)));
					}
				} else {
					throw new WrongArgumentType(rulesAST, rulesAST, -1, "Rule expression (x->y) expected: ");
				}
			}
		} else {
			if (rulesAST.isAST(F.Rule, 3)) {
				// a single rule
				if (rulesAST.get(1).isFree(patternQ)) {
					equalRules.put(rulesAST.get(1), rulesAST.get(2));
				} else {
					matchers.add(new PatternMatcherAndEvaluator(F.SetDelayed, rulesAST.get(1), rulesAST.get(2)));
				}
			} else {
				throw new WrongArgumentType(rulesAST, rulesAST, -1, "Rule expression (x->y) expected: ");
			}
		}
		if (matchers.size() > 0) {
			return new RulesPatternFunctor(equalRules, matchers);
		}
		return rules(equalRules);
	}

	private Functors() {

	}
}
