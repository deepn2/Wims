package com.group4inc.wims.idm;

import java.util.List;

import com.group4inc.wims.workflow.WorkflowTemplate;

public class Admin extends User {
	
	public Admin(String name, String email, String username, String password, String initdomain) {
		super(name, email, username, password, initdomain);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get the list of workflow templates
	 * @return list of workflow templates
	 */
	public List<WorkflowTemplate> getWorkflowTemplates() {
		// ...
		return null;
	}
	
	/**
	 * View the list of pending invites from users that want to
	 * join the admin's domain
	 */
	public void viewPendingUserInvites() {
		// ...
	}
	
	/**
	 * Upload a workflow template using the workflow langauge
	 * @param template - the workflow language
	 */
	public void uploadWorkflow(JSONObject template) {
		// ...
	}
	
	/**
	 * Check if the workflow language is in a valid format
	 * @param template - the workflow language
	 * @return empty if valid, otherwise gives errors
	 */
	public String isWorkflowLanguageValid(JSONObject template) {
		return "";
	}

}
