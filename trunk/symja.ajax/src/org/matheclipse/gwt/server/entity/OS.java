package org.matheclipse.gwt.server.entity;

import com.googlecode.objectify.ObjectifyService;

public class OS extends ObjectifyService {
	public static synchronized void initialize() {
		factory.register(PackageEntity.class);
		factory.register(SymbolEntity.class);
		factory.register(UserDataEntity.class);
		factory.register(UserSymbolEntity.class);
	}
}