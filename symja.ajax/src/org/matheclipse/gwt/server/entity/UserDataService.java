package org.matheclipse.gwt.server.entity;

import java.util.Collections;
import java.util.Date;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

public class UserDataService {
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

	public static UserDataEntity save(UserDataEntity userData) {
		Objectify ofy = OS.begin();
		ofy.put(userData);
		cachePut(userData);
		return userData;
	}

	private static void cachePut(UserDataEntity userData) {
		cache.put(userData.getUser().getUserId(), userData);
	}

	private static void cacheRemove(UserDataEntity userData) {
		cache.remove(userData.getUser().getUserId());
	}

	private static UserDataEntity cacheGet(UserDataEntity userData) {
		return (UserDataEntity) cache.get(userData.getUser().getUserId());
	}

	private static UserDataEntity cacheGet(String userId) {
		return (UserDataEntity) cache.get(userId);
	}

	/**
	 * 
	 * @param userData
	 * @return <code>null</code> if only an existing symbol was modified for the
	 *         user ID
	 */
	public static UserDataEntity update(UserDataEntity userData, Date lastChanges) {
		try {
			UserDataEntity existingEntity = findByUserId(userData.getUser());
			Objectify ofy = OS.begin();
			existingEntity.setUser(userData.getUser());
			existingEntity.setSymbolCounter(userData.getSymbolCounter());
			existingEntity.setLastChanges(lastChanges);
			ofy.put(existingEntity);
			cachePut(existingEntity);
			return null;
		} catch (NotFoundException nfe) {
			return null;
		}
	}

	public static void delete(UserDataEntity userData) {
		cacheRemove(userData);
		Objectify ofy = OS.begin();
		ofy.delete(userData);
	}

	/**
	 * 
	 * @param user
	 * @return <code>null</code> if no entry was found
	 */
	public static UserDataEntity findByUserId(User user) {
		String userId = user.getUserId();
		return findByUserId(userId);
	}

	/**
	 * 
	 * @param user
	 * @return <code>null</code> if no entry was found
	 */
	public static UserDataEntity findByUserId(String userId) {
		UserDataEntity userData = cacheGet(userId);
		if (userData != null) {
			return userData;
		}

		Objectify ofy = OS.begin();
		Query<UserDataEntity> q = ofy.query(UserDataEntity.class);
		q.filter("userId", userId);
		userData = q.get();
		if (userData != null) {
			cachePut(userData);
		}
		return userData;
	}

	public static QueryResultIterable<UserDataEntity> getAll() {
		return OS.begin().query(UserDataEntity.class);
	}

}
