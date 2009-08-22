package org.matheclipse.core.form.mathml;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.interfaces.IAST;

/**
 *
 */
public abstract class AbstractOperator extends AbstractConverter {
  protected int fPrecedence;
  protected String fFirstTag;
  protected String fOperator;

  public AbstractOperator(final int precedence, final String firstTag, final String oper) {
    fPrecedence = precedence;
    fFirstTag = firstTag;
    fOperator = oper;
  }
	public AbstractOperator(final int precedence, final String oper) {
		this(precedence, "mrow", oper);
	}
	
  public void precedenceOpen(final StringBuffer buf, final int precedence) {
    if (precedence > fPrecedence) {
      fFactory.tag(buf, "mo", "(");
    }
  }

  public void precedenceClose(final StringBuffer buf, final int precedence) {
    if (precedence > fPrecedence) {
      fFactory.tag(buf, "mo", ")");
    }
  }

  /**
     * Converts a given function into the corresponding MathML output
     * 
     *@param  buf  StringBuffer for MathML output
     *@param  f    The math function which should be converted to MathML
     */
  public boolean convert(final StringBuffer buf, final IAST f, final int precedence) {
    fFactory.tagStart(buf, fFirstTag);
    precedenceOpen(buf, precedence);
    for (int i = 1; i < f.size(); i++) {
      checkCanceled();
			fFactory.convert(buf, f.get(i), fPrecedence);
      if (i < f.size() - 1) {
        if (fOperator.compareTo("") != 0) {
          fFactory.tag(buf, "mo", fOperator);
        }
      }
    }
    precedenceClose(buf, precedence);
    fFactory.tagEnd(buf, fFirstTag);
    return true;
  }

}
