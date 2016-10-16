package com.group4inc.wims.idm;

public class Role {
	
	private String name;
	
	public Role(String name) {
		name = this.name;
		IdMSerDB.addRoleToRoleDB(this);
	}
	
	public String getName() {
		return name;
	}

}
