package org.matheclipse.gwt.server.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.matheclipse.core.interfaces.ISymbol;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

@Entity
public class UserSymbolEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -558354731625069770L;

	@Id
	private Long id;

	private String userId;

	private String symbolName;

	private Text source;

	private int attributes;

	public UserSymbolEntity() {
		super();
	}

	public UserSymbolEntity(User user, String symbolName, String source) {
		this(user, symbolName, source, ISymbol.NOATTRIBUTE);
	}

	public UserSymbolEntity(User user, String symbolName, String source, int attributes) {
		super();
		this.setUserId(user.getUserId());
		this.symbolName = symbolName;
		this.source = new Text(source);
		this.setAttributes(attributes);
	}

	public int getAttributes() {
		return attributes;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * @return the name
	 */
	public String getSymbolName() {
		return symbolName;
	}

	public String getUserId() {
		return userId;
	}

	public void setAttributes(int attributes) {
		this.attributes = attributes;
	}

	/**
	 * @param source
	 *          the source to set
	 */
	public void setSource(String source) {
		this.source = new Text(source);
	}

	/**
	 * @param symbolName
	 *          the symbol name to set
	 */
	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
