package com.group4inc.wims.idm;

public class User {
	
	private String name;
	private String email;
	private String username;
	private String password;
	
	public User(String name, String email, String username, String password) {
		name = this.name;
		email = this.email;
		username = this.username;
		password = this.password;
		IdMSerDB.addUserToUserDB(this);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @return      the name of the User
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public void changeName(String name) {
		name = this.name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void changeEmail(String email) {
		email = this.email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void changePassword(String password) {
		password = this.password;
	}
}
