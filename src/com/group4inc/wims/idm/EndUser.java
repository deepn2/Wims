package com.group4inc.wims.idm;

import java.util.List;

import com.group4inc.wims.workflow.model.WorkflowInstance;

public class EndUser extends User {

	public EndUser(String name, String email, String username, String password, String initdomain) {
		super(name, email, username, password, initdomain);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * Get the list of all active workflows pertaining to this user
	 * @return list of active workflows
	 */
	public List<WorkflowInstance> getActiveWorkflows() {
		// ...
		return null;
	}

}
