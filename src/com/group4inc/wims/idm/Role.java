package com.group4inc.wims.idm;

public class Role {
	
	private String name;
	
	public Role(String name) {
		name = this.name;
		IdMSerDB.addRoleToRoleDB(this);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public String getName() {
		return name;
	}

}
