package org.matheclipse.generic.util;

import java.util.ArrayList;

import org.matheclipse.generic.nested.NestedAlgorithms;


/**
 * Interface for nested list. I.e. for a list which contains elements of type
 * <code>T</code>lists
 */
public class ArrayListAlgorithms<T, L extends ArrayList<T>> extends NestedAlgorithms<T, L> {

	final Class<L> fType; 
	public ArrayListAlgorithms(Class<L> type) {
		fType = type;
	}
	
	public L clone(L list) {
		return (L)list.clone();
	}

	public L copyHead(L list) {
		// same as clone for ArrayList
		return (L)list.clone();
	}
	
	public boolean isInstance(Object object) {
		return fType.isInstance(object);			
	}

}
