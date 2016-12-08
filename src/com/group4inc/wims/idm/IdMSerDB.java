package com.group4inc.wims.idm;

import java.util.HashMap;
import java.util.Map;

/**
* Object to act as a database for the Identiy Management portion of the program.
* 
* <P>This class acts as a database, containing multiple ArrayList objects, one for all User objects, another for all Group objects, and the final for all Role objects.
* 
* @see User
* @see Domain  
* @see Group
* @author Elliot Linder (eml160)
*/
public class IdMSerDB {
	
	/**the ArrayList containing all User objects created.
	 * @see User*/
	private static Map<String, User> userDB = new HashMap<String, User>();
	/**the ArrayList containing all Group objects created.
	 * @see Group*/
	private static Map<String, Group> groupDB = new HashMap<String, Group>();
	/**the ArrayList containing all Role objects created.
	 * @see Domain*/
	private static Map<String, Domain> domainDB = new HashMap<String, Domain>();
	/**the User object that stores the current active User of the program**/
	private static User activeUser;
	
	/**
	 * Adds a User object to the UserDB (ArrayList of User objects).
	 *
	 * @param  user  The User object to be added to the UserDB
	 * @see User
	 */
	public static void addUserToUserDB(User user) {
		if(user != null)
			userDB.put(user.getUsername(), user);
	}
	
	/**
	 * Removes a User object from the UserDB (ArrayList of User objects).
	 *
	 * @param  user  The User object to be removed from the UserDB
	 * @see User
	 */
	public static void removeUserFromUserDB(User user) {
		userDB.remove(user.getUsername());
	}
	
	/**
	 * Returns the User object that has the unique username passed in the parameters of the method.
	 * 
	 * @param username The username of the User to be retrieved
	 * @param password The password of the User to be retrieved
	 */
	public static User getUser(String username, String password) {
		User out = null;
		
		User possible = userDB.get(username);
		if(possible != null) {
			if(PasswordOps.comparePasswords(possible.getPassword(), password))
				out = possible;
		}
		
		return out;	
	}
	
	public static User getActiveUser() {
		return activeUser;
	}
	
	public static void setActiveUser(User user) {
		activeUser = user;
	}
	/**
	 * Returns a User object after searching by the User's username property. Will return NULL if no match is found, case-sensitive search!
	 *
	 * @param  username  The username of the User object to be retrieved.
	 * @return      the User object which has the username that was being searched.
	 * @see User
	 */
	public static User getUserByUsername(String username) {
		return userDB.get(username);
	}
	
	/**
	 * Returns a User object after searching by the User's username property. Will return NULL if no match is found, case-sensitive search!
	 *
	 * @param  username  The username of the User object to be retrieved.
	 * @return      the User object which has the username that was being searched.
	 * @see User
	 */
	public static Domain getDomainByName(String domainname) {
		return domainDB.get(domainname);
	}
	
	/**
	 * Adds a Group object to the GroupDB (ArrayList of Group objects).
	 *
	 * @param  group  The Group object to be added to the GroupDB.
	 * @see Group
	 */
	public static void addGroupToGroupDB(Group group) {
		if(group != null)
			groupDB.put(group.getName(), group);
	}
	
	/**
	 * Removes a Group object from the GroupDB (ArrayList of Group objects).
	 *
	 * @param  group  The Group object to be removed from the GroupDB
	 * @see Group
	 */
	public static void removeGroupFromGroupDB(Group group) {
		groupDB.remove(group.getName());
	}
	
	/**
	 * Adds a Domain object to the DomainDB (ArrayList of Role objects).
	 *
	 * @param  domain  The Domain object to be added to the DomainDB
	 * @see Domain
	 */
	public static void addDomainToDomainDB(Domain domain) {
		domainDB.put(domain.getName(), domain);
	}
	
	/**
	 * Removes a Role object from the Role (ArrayList of Role objects).
	 *
	 * @param  role  The Role object to be removed from the RoleDB
	 * @see Domain
	 */
	public static void removeDomainFromDomainDB(Domain domain) {
		domainDB.remove(domain.getName());
	}	
	
	/**
	 * Returns the UserDB ArrayList (ArrayList of all User objects).
	 *
	 * @return  An ArrayList of all Users created.
	 * @see User
	 */
	public static Map<String, User> getUserDB() {
		return userDB;
	}
	
	/**
	 * Returns the GroupDB ArrayList (ArrayList of all Group objects).
	 *
	 * @return  An ArrayList of all Groups created.
	 * @see Group
	 */
	public static Map<String, Group> getGroupDB() {
		return groupDB;
	}
	
	/**
	 * Returns the RoleDB ArrayList (ArrayList of all Role objects).
	 *
	 * @return  An ArrayList of all Roles created.
	 * @see Domain
	 */
	public static Map<String, Domain> getDomainDB() {
		return domainDB;
	}

}
