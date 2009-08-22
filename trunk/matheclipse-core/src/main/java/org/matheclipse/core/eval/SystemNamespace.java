package org.matheclipse.core.eval;

/**
 * 
 */
public class SystemNamespace extends Namespace {
	public final static Namespace DEFAULT = new SystemNamespace();

	static {
		DEFAULT.add("org.matheclipse.core.reflection.system");
	}

	public SystemNamespace() {
		super();
	}

}
