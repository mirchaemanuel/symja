package org.matheclipse.core.eval.exception;

/**
 * Signals that an operation is performed upon vectors or matrices whose
 * dimensions disagree.
 * 
 */
public class DimensionException extends RuntimeException {

    /**
     * Constructs a dimension exception with no detail message.
     */
    public DimensionException() {
        super();
    }

    /**
     * Constructs a dimension exception with the specified message.
     * 
     * @param message the error message.
     */
    public DimensionException(String message) {
        super(message);
    }

}