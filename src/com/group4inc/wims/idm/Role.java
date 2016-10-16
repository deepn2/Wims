package com.group4inc.wims.idm;

/**
* Role Object.
* 
* <P>This class defines Roles in the context of WIMS.
* 
* <P>Roles are a way to give users permissions. A role can be assigned to a user in two different ways; one by direct assignment to the User object and the other by group assignment. For example, if a user is a member of the "overriders" group then they and all other members of the group could be assigned the "all_overriders" role in one assignment instead of individual assignment.
* 
* @see User
* @see Role
* @see Group
* @author Elliot Linder (eml160)
*/
public class Role {
	
	/**the name of the Role (e.g. all_overriders)*/
	private String name;
	
	/**
	 * Constructor for Role objects.
	 *
	 * @param  name the name of the Role to be constructed
	 */
	public Role(String name) {
		name = this.name;
		IdMSerDB.addRoleToRoleDB(this);
	}
	
	/**
	 * Returns the Role's name.
	 *
	 * @return      the name of the Role
	 */
	public String getName() {
		return name;
	}

}
