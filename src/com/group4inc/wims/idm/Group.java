package com.group4inc.wims.idm;

import java.util.ArrayList;

/**
* Group Object.
* 
* <P>This class defines Groups in the context of WIMS.
* 
* <P>Groups are used in WIMS to easily manage multiple User objects. For example, an "overriders" group can be defined for those users who can override steps in the workflow process.
* 
* @see User
* @see Domain  
* @author Elliot Linder (eml160)
*/
public class Group {
	
	/**the name of the Group (e.g. overriders)*/
	private String name;
	/**the members of the Group (e.g. admin, john)*/
	private ArrayList<User> members;
	/**the domain of the Group (e.g. Rutgers)*/
	private Domain domain;
	
	/**
	 * Constructor for Group objects. This also instantiates empty members and roles ArrayLists.
	 *
	 * @param  name the name of the Group to be constructed
	 */
	public Group(String name) {
		name = this.name;
		members = new ArrayList<User>();
	}
	
	/**
	 * Constructor for Group objects. This also instantiates the members ArrayList with a member. An empty roles ArrayList is instantiated.
	 *
	 * @param  name the name of the Group to be constructed
	 * @param  initmem the username of the User to be added to the Group
	 */
	public Group(String name, String initmem) {
		name = this.name;
		members = new ArrayList<User>();
		members.add(IdMSerDB.getUserByUsername(initmem));
		IdMSerDB.addGroupToGroupDB(this);
	}
	
	/**
	 * Returns the Group's name.
	 *
	 * @return      the name of the Group
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns an ArrayList of Users containing the Group's members.
	 *
	 * @return      the ArrayList of User's in the Group.
	 */
	public ArrayList<User> getMembers() {
		return members;
	}
	
	/**
	 * Adds a user as a member of the Group.
	 *
	 * @param  user  The User object to be added to the Group.
	 * @see User
	 */
	public void addMember(User user) {
		members.add(user);
	}
	
	/**
	 * Removes a User member from the group.
	 *
	 *@param  user  The User object to be removed from the Group.
	 *@see User
	 */
	public void removeMember(User user) {
		members.remove(user);
	}
	
	/**
	 * Adds a role to the Group.
	 *
	 * @param  role  The Role object to be added to the Group.
	 * @see Domain
	 */
	public void addRole(Domain role) {
		roles.add(role);
	}
	
	/**
	 * Removes a role from the Group.
	 *
	 * @param  role  The Role object to be removed from the Group.
	 * @see Domain
	 */
	public void removeRole(Domain role) {
		roles.remove(role);
	}

}
