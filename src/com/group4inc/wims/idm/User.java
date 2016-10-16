package com.group4inc.wims.idm;

import java.util.ArrayList;

/**
* User Object.
* 
* <P>This class defines Users in the context of WIMS.
* 
* <P>Individual users are required in WIMS for accessing the system to perform tasks.
*  
* @author Elliot Linder (eml160)
*/
public class User {
	
	/**the name of the User (e.g. John Smith)*/
	private String name;
	/**the email address of the User (e.g. generic@rutgers.edu)*/
	private String email;
	/**the username of the User (e.g. myusername)*/
	private String username;
	/**the password of the User. NOTE: This is stored in plaintext*/
	private String password;
	/**the domains that the User belongs to*/
	private ArrayList<Domain> domains = new ArrayList<Domain>();

	/**
	 * Constructor for User objects.
	 *
	 * @param  name the name of the User to be constructed
	 * @param  email the email address of the User to be constructed
	 * @param  username the username of the User tot be constructed
	 * @param  password the password of the User to be constructed
	 * @param  initdomain the initial domain that the user is a part of
	 */
	public User(String name, String email, String username, String password, String initdomain) {
		name = this.name;
		email = this.email;
		username = this.username;
		password = this.password;
		domains.add(IdMSerDB.getDomainByName(initdomain));
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
	 * Returns the user's username (not to be confused with the name).
	 * 
	 * @return      the username of the User
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the user's password.
	 *
	 * @return      the password of the User
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Changes the user's password.
	 *
	 * @param  password  the new password to be set for the User
	 */
	public void changePassword(String password) {
		password = this.password;
	}
	
	/**
	 * Adds a user to a Domain.
	 *
	 * @param  domain  The Domain object that the User is to be added to.
	 * @see Domain
	 */
	public void addDomain(Domain domain) {
		domains.add(domain);
	}
	
	/**
	 * Removes a User from a Domain.
	 *
	 * @param  domain  The Domain object that the User is being removed from.
	 * @see Domain
	 */
	public void removeRole(Domain domain) {
		domains.remove(domain);
	}
	
	/**
	 * Returns the User's Domain membership.
	 *
	 * @return      an ArrayList of Domains that the user has been added to.
	 * @see Domain
	 */
	public ArrayList<Domain> getDomain() {
		return domains;
	}
}
