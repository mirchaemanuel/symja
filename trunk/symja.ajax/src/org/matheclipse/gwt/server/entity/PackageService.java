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

public class PackageService {
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

	public static PackageEntity save(PackageEntity packageData) {
		Objectify ofy = OS.begin();
		ofy.put(packageData);
		cache.put(packageData.getName(), packageData);
		return packageData;
	}

	public static PackageEntity update(PackageEntity packageData) {
		try {
			Objectify ofy = OS.begin();
			PackageEntity existingEntity = ofy.get(PackageEntity.class, packageData.getId());
			existingEntity.setName(packageData.getName());
			existingEntity.setSource(packageData.getSource());
			ofy.put(existingEntity);

			cache.put(existingEntity.getName(), existingEntity);

			return existingEntity;
		} catch (NotFoundException nfe) {
			return null;
		}
	}

	public static void delete(PackageEntity packageData) {
		cache.remove(packageData.getName());
		Objectify ofy = OS.begin();
		ofy.delete(packageData);
	}

	public static PackageEntity findByName(String packageName) {
		PackageEntity packageData = (PackageEntity) cache.get(packageName);
		if (packageData != null) {
			return packageData;
		}

		Objectify ofy = OS.begin();
		Query<PackageEntity> q = ofy.query(PackageEntity.class);
		q.filter("name", packageName);
		packageData = q.get();
		if (packageData != null) {
			cache.put(packageData.getName(), packageData);
		}
		return packageData;
	}

	public static QueryResultIterable<PackageEntity> getAll() {
		return OS.begin().query(PackageEntity.class);
	}

}
