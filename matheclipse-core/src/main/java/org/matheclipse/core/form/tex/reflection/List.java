package org.matheclipse.core.form.tex.reflection;

import static org.matheclipse.basic.Util.checkCanceled;

import org.matheclipse.core.form.tex.AbstractConverter;
import org.matheclipse.core.interfaces.IAST;

public class List extends AbstractConverter {

  /** constructor will be called by reflection */
  public List() {
  }

  /**
   * Converts a given function into the corresponding MathML output
   * 
   *@param  buf  StringBuffer for MathML output
   *@param  f    The math function which should be converted to MathML
   */
  public boolean convert(final StringBuffer buf, final IAST f, final int precedence) {
    buf.append("\\{ ");
    if (f.size() > 1) {
      fFactory.convert(buf, f.get(1), 0);
      for (int i = 2; i < f.size(); i++) {
      	checkCanceled();
				buf.append(",");
        fFactory.convert(buf, f.get(i), 0);
      }
    }
    buf.append("\\} ");
    return true;
  }
}