package com.group4inc.wims.idm;

import java.util.ArrayList;

public class IdMSerDB {
	
	private static ArrayList<User> userDB = new ArrayList<User>();
	private static ArrayList<Group> groupDB = new ArrayList<Group>();
	private static ArrayList<Role> roleDB = new ArrayList<Role>();
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public static void addUserToUserDB(User user) {
		userDB.add(user);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public static void removeUserFromUserDB(User user) {
		userDB.remove(user);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public static User getUserByUsername(String username) {
		boolean found = false;
		int i = 0;
		User out = null;
		
		while(!found) {
			if(userDB.get(i).getUsername().equals(username)) {
				out = userDB.get(i);
				found = true;
			}
			
			else {
				i++;
			}
		}
		
		return out;
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public static void addGroupToGroupDB(Group group) {
		groupDB.add(group);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public static void removeGroupFromGroupDB(Group group) {
		groupDB.remove(group);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public static void addRoleToRoleDB(Role role) {
		roleDB.add(role);
	}
	
	/**
	 * Returns the user's name (not to be confused with the username).
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the name of the User
	 */
	public static void removeRoleFromRoleDB(Role role) {
		roleDB.remove(role);
	}	
	

}
