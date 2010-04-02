package org.matheclipse.core.expression;

import java.util.List;
import java.util.Map;

import org.matheclipse.basic.Config;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.INumber;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.PatternMatcher;

import apache.harmony.math.BigInteger;

public abstract class ExprImpl implements IExpr {
  // protected int hash;

  // @Override
  // public Field<IExpr> getField() {
  // return ExprFieldOld.CONST;
  // }

  public IExpr opposite() {
    return F.function(F.Times, F.CN1, this);
  }

  /**
   * Additional negative method, which works like opposite to fulfill groovy's
   * method signature
   * 
   * @return
   */
  public final IExpr negative() {
    return opposite();
  }

  public IExpr minus(final IExpr that) {
    return F.function(F.Plus, this, F.function(F.Times, F.CN1, that));
  }

  public IExpr plus(final IExpr that) {
    return F.function(F.Plus, this, that);
  }

  public IExpr inverse() {
    return F.function(F.Power, this, F.CN1);
  }

  public IExpr times(final IExpr that) {
    return F.function(F.Times, this, that);
  }

  /**
   * Additional multiply method which works like times to fulfill groovy's
   * method signature
   * 
   * @param that
   * @return
   */
  public final IExpr multiply(final IExpr that) {
    return times(that);
  }

  public final IExpr power(final Integer n) {
    return F.function(F.Power, this, F.integer(n));
  }

  public final IExpr power(final IExpr that) {
    return F.function(F.Power, this, that);
  }

  public IExpr div(final IExpr that) {
    return F.eval(F.function(F.Times, this, F.function(F.Power, that, F.CN1)));
  }

  public IExpr mod(final IExpr that) {
    return F.function(F.Mod, this, that);
  }

  public IExpr and(final IExpr that) {
    return F.function(F.And, this, that);
  }

  public IExpr or(final IExpr that) {
    return F.function(F.Or, this, that);
  }

  public IExpr getAt(final int index) {
    return F.function(F.Part, this, F.integer(index));
  }

  public Object asType(Class clazz) {
    if (clazz.equals(Boolean.class)) {
      if (this.equals(F.True)) {
        return Boolean.TRUE;
      }
      if (this.equals(F.False)) {
        return Boolean.FALSE;
      }
    } else if (clazz.equals(Integer.class)) {
      if (this instanceof IInteger) {
        return Integer.valueOf(((IInteger) this).toInt());
      }
    } else if (clazz.equals(java.math.BigInteger.class)) {
      if (this instanceof IntegerSym) {
        return new BigInteger(((IntegerSym) this).toByteArray());
      }
    } else if (clazz.equals(String.class)) {
      return toString();
    }
    throw new UnsupportedOperationException(
        "ExprImpl.asType() - cast not supported.");
  }

  public abstract ISymbol head();

  public ISymbol topHead() {
    return head();
  }

  public boolean isList() {
    return false;
  }

  public boolean isTrue() {
    return false;
  }

  public boolean isFalse() {
    return false;
  }

  public boolean isSame(IExpr expression) {
    return isSame(expression, Config.DOUBLE_EPSILON);
  }

  public boolean isSame(IExpr expression, double epsilon) {
    return equals(expression);
  }

  public int[] isMatrix() {
    // default: no matrix
    return null;
  }

  public int isVector() {
    // default: no vector
    return -1;
  }

  public boolean isAST(final IExpr header) {
    return false;
  }

  public boolean isAST(final IExpr header, final int sz) {
    return false;
  }

  public boolean isASTSizeGE(final IExpr header, final int size) {
    return false;
  }

  public boolean isAST(final String symbol) {
    return false;
  }

  public boolean isAST(final String symbol, final int size) {
    return false;
  }

  public boolean isFree(final IExpr pattern) {
    final PatternMatcher matcher = new PatternMatcher(pattern);
    return !AST.COPY.some(this, matcher, 1);
  }

  public boolean isNumber() {
    return this instanceof INumber;
  }

  public boolean isLTOrdered(final IExpr obj) {
    return compareTo(obj) < 0;
  }

  public boolean isLEOrdered(final IExpr obj) {
    return compareTo(obj) <= 0;
  }

  public boolean isGTOrdered(final IExpr obj) {
    return compareTo(obj) > 0;
  }

  public boolean isGEOrdered(final IExpr obj) {
    return compareTo(obj) >= 0;
  }

  public boolean isAtom() {
    return true;
  }

  // public INestedList castTo() {
  // return null;
  // }

  public IExpr variables2Slots(final Map<IExpr, IExpr> map,
      final List<IExpr> variableList) {
    return this;
  }

  // public IExpr save() {
  // return (IExpr) super.export();
  // }
  //
  // public IExpr saveHeap() {
  // return (IExpr) super.moveHeap();
  // }

  public String fullFormString() {
    return toString();
  }

  public String internalFormString() {
    return toString();
  }
  
  public List<IExpr> leaves() {
    return null;
  }

  public IExpr apply(List<? extends IExpr> leaves) {
    final IAST ast = F.ast(head());
    for (int i = 0; i < leaves.size(); i++) {
      ast.add(leaves.get(i));
    }
    return ast;
  }

  public IExpr apply(IExpr... leaves) {
    final IAST ast = F.ast(head());
    for (int i = 0; i < leaves.length; i++) {
      ast.add(leaves[i]);
    }
    return ast;
  }

}
