package org.matheclipse.gwt.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Text;

@Entity
public class PackageEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1938325500353258616L;

	@Id
	private Long id;

	private String name;

	private Text source;

	private String publicSymbols;

	public PackageEntity() {
		super();
	}

	public PackageEntity(String name, String source, String[] publicSymbols) {
		super();
		this.name = name;
		this.source = new Text(source);
		setPublicSymbols(publicSymbols);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the list auf public available symbol names for this package
	 * 
	 * @return the publicSymbols
	 */
	public String[] getPublicSymbols() {
		String[] list = publicSymbols.split(":");
		return list;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		if (source != null) {
			return source.getValue();
		}
		return null;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the list auf public available symbol names for this package
	 * 
	 * @param publicSymbols
	 *          the publicSymbols to set
	 * @see #getPublicSymbols()
	 */
	public final void setPublicSymbols(String[] publicSymbols) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < publicSymbols.length; i++) {
			buf.append(publicSymbols[i]);
			if (i < publicSymbols.length - 1) {
				// define a delimiter; see also getPublicSymbols() method
				buf.append(':');
			}
		}
		this.publicSymbols = buf.toString();
	}

	/**
	 * @param source
	 *          the source to set
	 */
	public void setSource(String source) {
		this.source = new Text(source);
	}
}
