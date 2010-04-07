package org.matheclipse.gwt.server.entity;

import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.ISymbol;

import com.google.appengine.api.datastore.QueryResultIterable;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.NotFoundException;

/**
 * Manage persistency for SymbolEntity
 * 
 */
public class SymbolService {
	public static Cache cache = null;

	static {
		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			cache = cacheFactory.createCache(Collections.emptyMap());
		} catch (CacheException e) {
			// ...
			// e.printStackTrace();
		}
	}

	public static SymbolEntity save(SymbolEntity symbolData) {
		OS.begin().put(symbolData);
		cache.put(symbolData.getName(), symbolData);
		return symbolData;
	}

	/**
	 * Update all symbols entities from the list with the new package name in the
	 * database.
	 * 
	 * @param symbolsList
	 * @param packageName
	 * @return
	 */
	public static boolean update(IAST symbolsList, String packageName) {
		Objectify ofy = OS.begin();
		SymbolEntity symbolData;
		for (int i = 1; i < symbolsList.size(); i++) {
			if (symbolsList.get(i) instanceof ISymbol) {
				try {
					symbolData = ofy.get(SymbolEntity.class, symbolsList.get(i).toString());
					if (!symbolData.getPackageName().equals(packageName)) {
						symbolData.setPackageName(packageName);
						ofy.put(symbolData);
					}
				} catch (NotFoundException nfe) {
					symbolData = new SymbolEntity(symbolsList.get(i).toString(), packageName);
					ofy.put(symbolData);
				}
				cache.put(symbolData.getName(), symbolData);
			}
		}
		return true;
	}

	/**
	 * Update symbol entity in database.
	 * 
	 * @param symbolData
	 * @return
	 */
	public static SymbolEntity update(SymbolEntity symbolData) {
		Objectify ofy = OS.begin();
		try {
			SymbolEntity existingEntity = ofy.get(SymbolEntity.class, symbolData.getName());
			existingEntity.setName(symbolData.getName());
			existingEntity.setPackageName(symbolData.getPackageName());
			ofy.put(existingEntity);
			cache.put(existingEntity.getName(), existingEntity);
			return existingEntity;
		} catch (NotFoundException nfe) {
			return null;
		}
	}

	/**
	 * Delete symbol entity from database.
	 * 
	 * @param symbolData
	 */
	public static void delete(SymbolEntity symbolData) {
		cache.remove(symbolData.getName());
		Objectify ofy = OS.begin();
		ofy.delete(symbolData);
	}

	/**
	 * Delete symbol entity from database.
	 * 
	 * @param symbolName
	 */
	public static void delete(String symbolName) {
		cache.remove(symbolName);
		try {
			Objectify ofy = OS.begin();
			SymbolEntity symbolData = ofy.get(SymbolEntity.class, symbolName);
			ofy.delete(symbolData);
		} catch (NotFoundException nfe) {
		}
	}

	/**
	 * Find symbol entity by name.
	 * 
	 * @param symbolName
	 * @return
	 */
	public static SymbolEntity findByName(String symbolName) {
		SymbolEntity symbolData = (SymbolEntity) cache.get(symbolName);
		if (symbolData != null) {
			return symbolData;
		}
		Objectify ofy = OS.begin();
		try {
			symbolData = ofy.get(SymbolEntity.class, symbolName);
			if (symbolData != null) {
				cache.put(symbolData.getName(), symbolData);
			}
			return symbolData;
		} catch (NotFoundException nfe) {
			return null;
		}
	}

	public static QueryResultIterable<SymbolEntity> getAll() {
		return OS.begin().query(SymbolEntity.class);
	}

}
