package org.matheclipse.core.generic.util;

import java.util.List;



/**
 * Interface for a nested list. I.e. for a list which contains elements of type
 * INestedListElement lists
 */
public interface INestedList<E extends INestedListElement> extends List<E>, Cloneable , INestedListElement {

}
