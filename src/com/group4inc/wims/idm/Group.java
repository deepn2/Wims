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
	
	public String getName() {
		return name;
	}
	
	public ArrayList<User> getMembers() {
		return members;
	}
	
	public void addMember(User user) {
		members.add(user);
	}
	
	public void removeMember(User user) {
		members.remove(user);
	}
	
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public void removeRole(Role role) {
		roles.remove(role);
	}

}
