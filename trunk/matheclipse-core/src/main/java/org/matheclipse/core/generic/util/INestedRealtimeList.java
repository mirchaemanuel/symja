package org.matheclipse.core.generic.util;

import java.util.List;



/**
 * Interface for a nested list. I.e. for a list which contains elements of type
 * IRealtimeElement lists
 */
public interface INestedRealtimeList<E extends IRealtimeElement> extends List<E>, Cloneable , IRealtimeElement {

}
