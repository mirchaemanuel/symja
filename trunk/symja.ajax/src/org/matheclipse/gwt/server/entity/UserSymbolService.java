package org.matheclipse.gwt.server.entity;

import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import com.google.appengine.api.datastore.QueryResultIterable;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

public class UserSymbolService {
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

	public static UserSymbolEntity save(UserSymbolEntity userSymbolData) {
		Objectify ofy = OS.begin();
		ofy.put(userSymbolData);
		cachePut(userSymbolData);
		return userSymbolData;
	}

	private static void cachePut(UserSymbolEntity userSymbolData) {
		String cacheKey = userSymbolData.getUserId() + "|" + userSymbolData.getSymbolName();
		cache.put(cacheKey, userSymbolData);
	}

	private static void cacheRemove(UserSymbolEntity userSymbolData) {
		String cacheKey = userSymbolData.getUserId() + "|" + userSymbolData.getSymbolName();
		cache.remove(cacheKey);
	}

	private static UserSymbolEntity cacheGet(UserSymbolEntity userSymbolData) {
		String cacheKey = userSymbolData.getUserId() + "|" + userSymbolData.getSymbolName();
		return (UserSymbolEntity) cache.get(cacheKey);
	}

	private static UserSymbolEntity cacheGet(String userId, String symbolName) {
		String cacheKey = userId + "|" + symbolName;
		return (UserSymbolEntity) cache.get(cacheKey);
	}

	/**
	 * 
	 * @param userSymbolData
	 * @return <code>null</code> if only an existing symbol was modified for the
	 *         user ID
	 */
	public static UserSymbolEntity modify(UserSymbolEntity userSymbolData) {
		try {
			Objectify ofy = OS.begin();
			UserSymbolEntity existingEntity = null;
			Query<UserSymbolEntity> q = ofy.query(UserSymbolEntity.class);
			q.filter("userId", userSymbolData.getUserId());
			q.filter("symbolName", userSymbolData.getSymbolName());
			existingEntity = q.get();
			if (existingEntity == null) {
				return save(userSymbolData);
			}
			existingEntity.setSource(userSymbolData.getSource());
			existingEntity.setAttributes(userSymbolData.getAttributes());
			ofy.put(existingEntity);
			cachePut(existingEntity);
			return null;
		} catch (NotFoundException nfe) {
			return null;
		}
	}

	public static void delete(UserSymbolEntity userSymbolData) {
		cacheRemove(userSymbolData);
		Objectify ofy = OS.begin();
		ofy.delete(userSymbolData);
	}

	public static UserSymbolEntity findByUserIdSymbol(String userId, String symbolName) {
		UserSymbolEntity userSymbolData = cacheGet(userId, symbolName);
		if (userSymbolData != null) {
			return userSymbolData;
		}

		Objectify ofy = OS.begin();
		Query<UserSymbolEntity> q = ofy.query(UserSymbolEntity.class);
		q.filter("userId", userId);
		q.filter("symbolName", symbolName);
		userSymbolData = q.get();
		if (userSymbolData != null) {
			cachePut(userSymbolData);
		}
		return userSymbolData;
	}

	public static QueryResultIterable<UserSymbolEntity> getAll(String userId) {
		Objectify ofy = OS.begin();
		Query<UserSymbolEntity> q = ofy.query(UserSymbolEntity.class);
		q.filter("userId", userId);
		return q;
	}

	public static int countAll(String userId) {
		Objectify ofy = OS.begin();
		Query<UserSymbolEntity> q = ofy.query(UserSymbolEntity.class);
		q.filter("userId", userId);
		return q.countAll();
	}

	public static QueryResultIterable<UserSymbolEntity> getAll() {
		return OS.begin().query(UserSymbolEntity.class);
	}

}
