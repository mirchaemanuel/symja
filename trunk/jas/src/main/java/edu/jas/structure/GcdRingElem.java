/*
 * $Id: GcdRingElem.java 3876 2012-02-05 12:50:21Z kredel $
 */

package edu.jas.structure;


/**
 * Gcd ring element interface.
 * Empty interface since gcd and egcd is now in RingElem.
 * Adds greatest common divisor and extended greatest common divisor.
 * @param <C> gcd element type
 * @author Heinz Kredel
 */

public interface GcdRingElem<C extends GcdRingElem<C>> 
                 extends RingElem<C> {

}
