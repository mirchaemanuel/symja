/*
 * $Id: ToRational.java 2756 2009-07-18 11:13:30Z kredel $
 */

package edu.jas.arith;


/**
 * Interface with toRational method.
 * @author Heinz Kredel
 */

public interface ToRational /*extends Cloneable*/ {

    /**
     * Return a BigRational approximation of this Element.
     * @return a BigRational approximation of this.
     */
    public BigRational toRational();

}
