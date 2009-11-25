package org.matheclipse.core.convert;

import static org.matheclipse.core.expression.F.List;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.visit.VisitorCollectionBoolean;

/**
 * Check the number of variables for expressions.
 * 
 */
public class ExprVariables {
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

  /**
   * Get a list of variables, which are contained in the given expression.
   * 
   * @param expression
   * @return
   */
  // public static IAST getVariables(final IExpr expression) {
  // final Set<IExpr> set = new TreeSet<IExpr>();
  // expression.accept(new VariablesVisitor(1, set));
  //
  // final Iterator<IExpr> iter = set.iterator();
  // final IAST list = List();
  // while (iter.hasNext()) {
  // list.add(iter.next());
  // }
  // return list;
  // }

  private final Set<IExpr> set = new TreeSet<IExpr>();

  /**
   * 
   */
  public ExprVariables(final IExpr expression) {
    super();
    expression.accept(new VariablesVisitor(1, set));
  }

  /**
   * Add the variables of the given expression
   * 
   * @param expression
   */
  public void add(final IExpr expression) {
    expression.accept(new VariablesVisitor(1, set));
  }

  /**
   * @return the varList
   */
  public IAST getVarList() {
    final Iterator<IExpr> iter = set.iterator();
    final IAST list = List();
    while (iter.hasNext()) {
      list.add(iter.next());
    }
    return list;
  }

  /**
   * Check if the expression contains the given number of variables.
   * 
   * @param expr
   * @return <code>true</code> if the expr contains the given number of
   *         variables.
   */
  public boolean isSize(int size) {
    return set.size() == size;
  }
}
