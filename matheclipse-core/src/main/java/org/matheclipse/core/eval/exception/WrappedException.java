package org.matheclipse.core.eval.exception;

public class WrappedException extends RuntimeException {
  
  Throwable fThrowable;
  
  public WrappedException(Throwable throwable){
    fThrowable = throwable;
  }
  
  @Override
  public String getMessage() {
    return fThrowable.getMessage();
  }

}
