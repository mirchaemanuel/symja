package org.matheclipse.gwt.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SymbolEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1948677556534530717L;

	@Id
	private String name;

	private String packageName;

	public SymbolEntity() {
		super();
	}

	public SymbolEntity(String name, String packageName) {
		super();
		this.name = name;
		this.packageName = packageName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *          the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
