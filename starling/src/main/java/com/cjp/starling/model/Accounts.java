package com.cjp.starling.model;

/**
 * Accounts object containing AccountUID and DefaultCategory
 * 
 * @author Chris Parker
 *
 */
public class Accounts {

	/**
	 * Accounts AccountUID
	 */
	private String accountUID;
	
	/**
	 * Accounts DefaultCategory
	 */
	private String defaultCategory;

	public String getAccountUID() {
		return accountUID;
	}

	public void setAccountUID(String accountUID) {
		this.accountUID = accountUID;
	}

	public String getDefaultCategory() {
		return defaultCategory;
	}

	public void setDefaultCategory(String defaultCategory) {
		this.defaultCategory = defaultCategory;
	}
}
