package com.group4inc.wims.idm;

import java.util.ArrayList;

public class Group {
	
	String name;
	ArrayList<User> members;
	ArrayList<Role> roles;
	
	public Group(String name) {
		name = this.name;
		members = new ArrayList<User>();
	}
	
	public Group(String name, String initmem) {
		name = this.name;
		members = new ArrayList<User>();
		roles = new ArrayList<Role>();
		members.add(IdMSerDB.getUserByUsername(name));
		IdMSerDB.addGroupToGroupDB(this);
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
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public ArrayList<User> getMembers() {
		return members;
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public void addMember(User user) {
		members.add(user);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public void removeMember(User user) {
		members.remove(user);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public void addRole(Role role) {
		roles.add(role);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public void removeRole(Role role) {
		roles.remove(role);
	}

}
