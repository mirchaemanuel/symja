package org.matheclipse.core.eval.exception;


/**
 *
 */
public class DivisionByZero extends ArithmeticException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8086718582665867096L;
	String fReason;
  public DivisionByZero(final String reason) {
		fReason = reason;
  }
  /* (non-Javadoc)
   * @see java.lang.Throwable#getMessage()
   */
  @Override
public String getMessage() {
    return "Division by zero:: "+fReason;
  }

}
