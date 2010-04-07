package org.matheclipse.gwt.server;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.ISymbolObserver;
import org.matheclipse.gwt.server.entity.PackageEntity;
import org.matheclipse.gwt.server.entity.PackageService;
import org.matheclipse.gwt.server.entity.SymbolEntity;
import org.matheclipse.gwt.server.entity.SymbolService;

/**
 * Load an associated package from the appengine datastore, if a new
 * &quot;symbol starting with an uppercase character&quot; is defined.
 * 
 */
public class SymbolObserver implements ISymbolObserver {
	public SymbolObserver() {
		super();
	}

	/**
	 * Load an associated package from the appengine datastore, if a new
	 * &quot;symbol starting with an uppercase character&quot; is defined.
	 * 
	 */
	@Override
	public boolean createSymbol(String symbolName) {
		try {
			SymbolEntity symbolEntity = SymbolService.findByName(symbolName);
			if (symbolEntity != null) {
				String packageName = symbolEntity.getPackageName();
				PackageEntity packageEntity = PackageService.findByName(packageName);
				if (packageEntity != null) {
					PackageLoader.loadPackage(EvalEngine.get(), packageEntity);
					return true;
				}
			}
		} catch (Exception ex) {
			if (Config.SHOW_STACKTRACE) {
				ex.printStackTrace();
			}
		}
		return false;
	}

}
