package org.matheclipse.generic.nested;

import java.util.List;

import org.matheclipse.core.generic.util.INestedListElement;
import org.matheclipse.generic.interfaces.IArrayFunction;
import org.matheclipse.generic.interfaces.IIterator;

/**
 * Table structure generator (i.e. lists, vectors, matrices, tensors)
 */
public class TableGenerator<T extends INestedListElement, L extends List<T> & INestedListElement> {

	final List<? extends IIterator<T>> fIterList;

	final T fDefaultValue;

	final L fPrototypeList;

	final IArrayFunction<T> fFunction;

	int fIndex;

	Object[] fCurrentIndex;

	private final INestedList<T, L> fCopier;

	public TableGenerator(final List<? extends IIterator<T>> iterList, final L prototypeList, final IArrayFunction<T> function,
			INestedList<T, L> copier) {
		this(iterList, prototypeList, function, copier, null);
	}

	public TableGenerator(final List<? extends IIterator<T>> iterList, final L prototypeList, final IArrayFunction<T> function,
			INestedList<T, L> copier, T defaultValue) {
		fIterList = iterList;
		fPrototypeList = prototypeList;
		fFunction = function;
		fIndex = 0;
		fCurrentIndex = new Object[iterList.size()];
		fCopier = copier;
		fDefaultValue = defaultValue;
	}

	// public T tableEvaluate() {
	// if (fIndex < fIterList.size()) {
	// final IIterator<T> iter = fIterList.get(fIndex);
	// try {
	// iter.setUp();
	// final int index = fIndex++;
	// L result = fCopier.clone(fPrototypeList);
	// L temp;
	// while (iter.hasNext()) {
	// fCurrentIndex[index] = iter.next();
	// result.add(tableEvaluate());
	// temp = fCopier.evaluate(result);
	// if (temp != null) {
	// result = temp;
	// }
	// }
	// return fCopier.castList(result);
	// } finally {
	// --fIndex;
	// iter.tearDown();
	// }
	// }
	// return fFunction.evaluate(fCurrentIndex);
	// }

	public T table() {
		if (fIndex < fIterList.size()) {
			final IIterator<T> iter = fIterList.get(fIndex);
			try {
				if (iter.setUp()) {
					try {
						final int index = fIndex++;
						final L result = fCopier.clone(fPrototypeList);
						while (iter.hasNext()) {
							fCurrentIndex[index] = iter.next();
							result.add(table());
						}
						return fCopier.castList(result);
					} finally {
						iter.tearDown();
					}
				}
				return fDefaultValue;
			} finally {
				--fIndex;
			}
		}
		return fFunction.evaluate(fCurrentIndex);
	}
}
