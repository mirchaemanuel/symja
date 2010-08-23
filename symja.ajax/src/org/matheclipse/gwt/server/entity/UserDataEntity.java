package org.matheclipse.gwt.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Unindexed;

@Entity
public class UserDataEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371048871447450995L;

	@Id
	private Long id;

	private String userId;
	
	@Unindexed
	private User user;

	@Unindexed
	private int symbolCounter;

	private Date lastChanges;

	public UserDataEntity() {
		super();
	}

	public UserDataEntity(User user) {
		this(user, new Date());
	}

	public UserDataEntity(User user, Date lastChanges) {
		super();
		this.userId = user.getUserId();
		this.user = user;
		this.symbolCounter = 0;
		this.lastChanges = lastChanges;
	}

	/**
	 * @return the lastChanges
	 */
	public Date getLastChanges() {
		return lastChanges;
	}

	/**
	 * @return the symbolCounter
	 */
	public int getSymbolCounter() {
		return symbolCounter;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param lastChanges
	 *          the lastChanges to set
	 */
	public void setLastChanges(Date lastChanges) {
		this.lastChanges = lastChanges;
	}

	/**
	 * @param symbolCounter
	 *          the symbolCounter to set
	 */
	public void setSymbolCounter(int symbolCounter) {
		this.symbolCounter = symbolCounter;
	}

	/**
	 * Increment the symbol counter by 1.
	 */
	public void incSymbolCounter() {
		this.symbolCounter++;
	}

	/**
	 * Decrement the symbol counter by 1.
	 */
	public void decSymbolCounter() {
		this.symbolCounter--;
	}

	/**
	 * @param user
	 *          the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
