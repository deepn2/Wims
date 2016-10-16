package com.group4inc.wims.idm;

import java.util.ArrayList;

public class IdMSerDB {
	
	private static ArrayList<User> userDB = new ArrayList<User>();
	private static ArrayList<Group> groupDB = new ArrayList<Group>();
	private static ArrayList<Role> roleDB = new ArrayList<Role>();
	
	public static void addUserToUserDB(User user) {
		userDB.add(user);
	}
	
	public static void removeUserFromUserDB(User user) {
		userDB.remove(user);
	}
	
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
	
	public static void addGroupToGroupDB(Group group) {
		groupDB.add(group);
	}
	
	public static void removeGroupFromGroupDB(Group group) {
		groupDB.remove(group);
	}
	
	public static void addRoleToRoleDB(Role role) {
		roleDB.add(role);
	}
	
	public static void removeRoleFromRoleDB(Role role) {
		roleDB.remove(role);
	}	
	

}
