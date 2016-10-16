package com.group4inc.wims.idm;

public class User {

	private String name;
	private String email;
	private String username;
	private String password;

	/**
	 * Constructor for User objects.
	 *
	 * @param  name the name of the User (e.g. John Smith)
	 * @param  email the email address of the User (e.g. generic@rutgers.edu)
	 * @param  username the username of the User (e.g. myusername)
	 * @param  password the password of the User. NOTE: This is stored in plaintext
	 */
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
	 * Changes the user's name (not to be confused with the username).
	 *
	 * @param  name the new name of the User
	 */
	public void changeName(String name) {
		name = this.name;
	}

	/**
	 * Returns the user's email address.
	 *
	 * @return      the email of the User
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Changes the user's email address
	 *
	 * @param  email the new email of the User
	 */
	public void changeEmail(String email) {
		email = this.email;
	}

	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public void changePassword(String password) {
		password = this.password;
	}
}
