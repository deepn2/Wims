package com.group4inc.wims.idm;

import java.util.ArrayList;

/**
* Object to act as a database for the Identiy Management portion of the program.
* 
* <P>This class acts as a database, containing multiple ArrayList objects, one for all User objects, another for all Group objects, and the final for all Role objects.
* 
* @see User
* @see Role  
* @see Group
* @author Elliot Linder (eml160)
*/
public class IdMSerDB {
	
	/**the ArrayList containing all User objects created.
	 * @see User*/
	private static ArrayList<User> userDB = new ArrayList<User>();
	/**the ArrayList containing all Group objects created.
	 * @see Group*/
	private static ArrayList<Group> groupDB = new ArrayList<Group>();
	/**the ArrayList containing all Role objects created.
	 * @see Role*/
	private static ArrayList<Role> roleDB = new ArrayList<Role>();
	
	/**
	 * Adds a User object to the UserDB (ArrayList of User objects).
	 *
	 * @param  user  The User object to be added to the UserDB
	 * @see User
	 */
	public static void addUserToUserDB(User user) {
		userDB.add(user);
	}
	
	/**
	 * Removes a User object from the UserDB (ArrayList of User objects).
	 *
	 * @param  user  The User object to be removed from the UserDB
	 * @see User
	 */
	public static void removeUserFromUserDB(User user) {
		userDB.remove(user);
	}
	
	/**
	 * Returns a User object after searching by the User's username property. Will return NULL if no match is found, case-sensitive search!
	 *
	 * @param  username  The username of the User object to be retrieved.
	 * @return      the User object which has the username that was being searched.
	 * @see User
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
	 * Adds a Group object to the GroupDB (ArrayList of Group objects).
	 *
	 * @param  group  The Group object to be added to the GroupDB.
	 * @see Group
	 */
	public static void addGroupToGroupDB(Group group) {
		groupDB.add(group);
	}
	
	/**
	 * Removes a Group object from the GroupDB (ArrayList of Group objects).
	 *
	 * @param  group  The Group object to be removed from the GroupDB
	 * @see Group
	 */
	public static void removeGroupFromGroupDB(Group group) {
		groupDB.remove(group);
	}
	
	/**
	 * Adds a Role object to the RoleDB (ArrayList of Role objects).
	 *
	 * @param  role  The Role object to be added to the RoleDB
	 * @see Role
	 */
	public static void addRoleToRoleDB(Role role) {
		roleDB.add(role);
	}
	
	/**
	 * Removes a Role object from the Role (ArrayList of Role objects).
	 *
	 * @param  role  The Role object to be removed from the RoleDB
	 * @see Role
	 */
	public static void removeRoleFromRoleDB(Role role) {
		roleDB.remove(role);
	}	
	
	/**
	 * Returns the UserDB ArrayList (ArrayList of all User objects).
	 *
	 * @return  An ArrayList of all Users created.
	 * @see User
	 */
	public static ArrayList<User> getUserDB() {
		return userDB;
	}
	
	/**
	 * Returns the GroupDB ArrayList (ArrayList of all Group objects).
	 *
	 * @return  An ArrayList of all Groups created.
	 * @see Group
	 */
	public static ArrayList<Group> getGroupDB() {
		return groupDB;
	}
	
	/**
	 * Returns the RoleDB ArrayList (ArrayList of all Role objects).
	 *
	 * @return  An ArrayList of all Roles created.
	 * @see Role
	 */
	public static ArrayList<Role> getRoleDB() {
		return roleDB;
	}

}
