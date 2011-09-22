package org.matheclipse.core.expression;

import java.util.List;
import java.util.Map;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.ConditionException;
import org.matheclipse.core.generic.Predicates;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;
import org.matheclipse.core.visit.IVisitor;
import org.matheclipse.core.visit.IVisitorBoolean;
import org.matheclipse.core.visit.IVisitorInt;
import org.matheclipse.core.visit.IVisitorLevel;

import com.google.common.base.Predicate;

/**
 * A concrete pattern implementation
 * 
 */
public class Pattern extends ExprImpl implements IPattern {

	private static final long serialVersionUID = 7617138748475243L;

	//
	// private static final ObjectFactory<Pattern> FACTORY = new
	// ObjectFactory<Pattern>() {
	// @Override
	// protected Pattern create() {
	// if (Config.SERVER_MODE && currentQueue().getSize() >=
	// Config.PATTERN_MAX_POOL_SIZE) {
	// throw new PoolMemoryExceededException("PatternImpl",
	// currentQueue().getSize());
	// }
	// return new Pattern();
	// }
	// };

	/**
	 * 
	 */
	public static Pattern valueOf(final Symbol symbol, final IExpr check, final boolean def) {
		Pattern p = new Pattern();
		p.fSymbol = symbol;
		p.fCondition = check;
//		p.fIndex = 0;
		p.fDefault = def;
		return p;
	}

	/**
	 * 
	 * @param numerator
	 * @return
	 */
	public static Pattern valueOf(final Symbol symbol, final IExpr check) {
		Pattern p = new Pattern();
		p.fSymbol = symbol;
		p.fCondition = check;
//		p.fIndex = 0;
		return p;
	}

	public static Pattern valueOf(final Symbol symbol) {
		Pattern p = new Pattern();
		p.fSymbol = symbol;
		p.fCondition = null;
//		p.fIndex = 0;
		return p;
	}

	/**
	 * The expression which should check this pattern
	 */
	IExpr fCondition;

	/**
	 * Index for the pattern-matcher
	 * 
	 * @see org.matheclipse.core.patternmatching.PatternMatcher
	 */
//	int fIndex = 0;

	/**
	 * The associated symbol for this pattern
	 */
	Symbol fSymbol;

	/**
	 * Use default value, if not matching was found
	 */
	boolean fDefault = false;

	// public PatternImpl(final SymbolImpl symbol) {
	// this(symbol, null);
	// }

	private Pattern() {
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Pattern) {
			Pattern pattern = (Pattern) obj;
			if (fSymbol == null) {
				if ((fCondition != null) && (pattern.fCondition != null)) {
					return (pattern.fSymbol == null) && fCondition.equals(pattern.fCondition);
				}
				return (pattern.fSymbol == null) && (fCondition == pattern.fCondition);
			}
			if (pattern.fSymbol == null) {
				return false;
			}
			if ((fCondition != null) && (pattern.fCondition != null)) {
				return fSymbol.equals(pattern.fSymbol) && fCondition.equals(pattern.fCondition);
			}
			return fSymbol.equals(pattern.fSymbol) && (fCondition == pattern.fCondition);
		}
		return false;
	}

	public IExpr getCondition() {
		return fCondition;
	}

	/**
	 * @return
	 */
//	public int getIndex() {
//		return fIndex;
//	}

	/**
	 * @return
	 */
	public ISymbol getSymbol() {
		return fSymbol;
	}

	@Override
	public int hashCode() {
		if (fSymbol == null) {
			return 199;
		}
		return fSymbol.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.matheclipse.parser.interfaces.IExpr#hierarchy()
	 */
	public int hierarchy() {
		return PATTERNID;
	}

	// public boolean isString(final String str) {
	// if (fSymbol == null) {
	// return str == null;
	// }
	// return fSymbol.toString().equals(str);
	// }

//	public void setIndex(final int i) {
//		fIndex = i;
//	}

	// public String toString() {
	// if (fCheck == null) {
	// return fSymbol.toString() + "_";
	// }
	// return fSymbol.toString() + "_" + fCheck.toString();
	// }

	// @Override
	// public boolean move(final ObjectSpace os) {
	// if (super.move(os)) {
	// if (fSymbol != null) {
	// fSymbol.move(os);
	// }
	// if (fCondition != null) {
	// fCondition.move(os);
	// }
	// }
	// return false;
	// }
	// public Pattern copy() {
	// // Pattern p;
	// // if (Config.SERVER_MODE) {
	// // p = FACTORY.object();
	// // } else {
	// // p = new Pattern();
	// // }
	// Pattern p= new Pattern();
	// p.fSymbol = fSymbol;
	// if (fCondition != null) {
	// p.fCondition = fCondition.copy();
	// } else {
	// p.fCondition = null;
	// }
	// return p;
	// }
	//
	// public Pattern copyNew() {
	// Pattern r = new Pattern();
	// r.fSymbol = fSymbol;
	// if (fCondition != null) {
	// r.fCondition = fCondition.copyNew();
	// } else {
	// r.fCondition = null;
	// }
	// return r;
	// }

	// public void recycle() {
	// if (fCondition != null) {
	// fCondition.recycle();
	// }
	// FACTORY.recycle(this);
	// }

	// public Text toText() {
	// final TextBuilder tb = TextBuilder.newInstance();
	// if (fSymbol == null) {
	// if (fCondition == null) {
	// tb.append("_");
	// } else {
	// tb.append("_" + fCondition.toString());
	// }
	// } else {
	// if (fCondition == null) {
	// tb.append(fSymbol.toString() + "_");
	// } else {
	// tb.append(fSymbol.toString() + "_" + fCondition.toString());
	// }
	// }
	// return tb.toText();
	// }
	@Override
	public String internalFormString(boolean symbolsAsFactoryMethod, int depth) {
		if (symbolsAsFactoryMethod) {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("$p(");
			if (fSymbol == null) {
				buffer.append("(ISymbol)null");
				if (fCondition != null) {
					buffer.append("," + fCondition.internalFormString(symbolsAsFactoryMethod, 0));
				}
				if (fDefault) {
					if (fCondition == null) {
						buffer.append(",null");
					}
					buffer.append(",true");
				}
			} else {
				buffer.append("\"" + fSymbol.toString() + "\"");
				if (fCondition != null) {
					buffer.append("," + fCondition.internalFormString(symbolsAsFactoryMethod, 0));
				}
				if (fDefault) {
					// if (fCondition == null) {
					// buffer.append(",null");
					// }
					buffer.append(",true");
				}
			}
			buffer.append(")");
			return buffer.toString();
		}
		return toString();
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		if (fSymbol == null) {
			buffer.append('_');
			if (fDefault) {
				buffer.append('.');
			}
			if (fCondition != null) {
				buffer.append(fCondition.toString());
			}
		} else {
			if (fCondition == null) {
				buffer.append(fSymbol.toString());
				buffer.append('_');
				if (fDefault) {
					buffer.append('.');
				}
			} else {
				buffer.append(fSymbol.toString());
				buffer.append('_');
				if (fDefault) {
					buffer.append('.');
				}
				buffer.append(fCondition.toString());
			}
		}
		return buffer.toString();
	}

	public String fullFormString() {
		StringBuffer buf = new StringBuffer();
		if (fSymbol == null) {
			buf.append("Blank[");
			if (fCondition != null) {
				buf.append(fCondition.fullFormString());
			}
			buf.append(']');
		} else {
			buf.append("Pattern[");
			buf.append(fSymbol.toString());
			buf.append(", ");
			buf.append("Blank[");
			if (fCondition != null) {
				buf.append(fCondition.fullFormString());
			}
			buf.append("]]");
		}

		return buf.toString();
	}

	/**
	 * Compares this expression with the specified expression for order. Returns a
	 * negative integer, zero, or a positive integer as this expression is
	 * canonical less than, equal to, or greater than the specified expression.
	 */
	public int compareTo(final IExpr obj) {
		if (obj instanceof Pattern) {
			int cp;
			if (fSymbol == null) {
				if (((Pattern) obj).fSymbol == null) {
					cp = -1;
				} else {
					cp = 0;
				}
			} else if (((Pattern) obj).fSymbol == null) {
				cp = 1;
			} else {
				cp = fSymbol.compareTo(((Pattern) obj).fSymbol);
			}
			if (cp != 0) {
				return cp;
			}
			if (fCondition == null) {
				if (((Pattern) obj).fCondition != null) {
					return -1;
				}
				return 0;
			} else {
				if (((Pattern) obj).fCondition == null) {
					return 1;
				} else {
					return fCondition.compareTo(((Pattern) obj).fCondition);
				}
			}
		}
		return (hierarchy() - (obj).hierarchy());
	}

	public ISymbol head() {
		return F.PatternHead;
	}

	public boolean isBlank() {
		return (fSymbol == null);
	}

	public boolean isConditionMatched(final IExpr expr) {
		if (fCondition == null) {
			return true;
		}
		if (expr.head().equals(fCondition)) {
			return true;
		}
		EvalEngine engine = EvalEngine.get();
		boolean traceMode = false;
		try {
			traceMode = engine.isTraceMode();
			engine.setTraceMode(false);
			final Predicate<IExpr> matcher = Predicates.isTrue(engine, fCondition);
			return matcher.apply(expr);
		} finally {
			if (traceMode) {
				engine.setTraceMode(true);
			}
		}
	}

	@Override
	public IExpr variables2Slots(final Map<IExpr, IExpr> map, final List<IExpr> variableList) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean accept(IVisitorBoolean visitor) {
		return visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public int accept(IVisitorInt visitor) {
		return visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public int accept(IVisitorLevel visitor) {
	  return visitor.visit(this);
	}
	
	/**
	 * Use default value, if not matching was found.
	 * 
	 * @return
	 */
	public boolean isDefault() {
		return fDefault;
	}

	/**
	 * Groovy operator overloading
	 */
	public boolean isCase(IExpr that) {
		final PatternMatcher matcher = new PatternMatcher(this);
		if (matcher.apply(that)) {
			return true;
		}
		return false;
	}

}