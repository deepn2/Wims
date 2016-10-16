package com.group4inc.wims.workflow;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a workflow template.
 * 
 * A domain has many workflow templates, however a workflow template does not need to know about a domain.
 * 
 * This class has quick a bit of information.
 * This class knows about the connection of usernames to roles.
 * It also knows which users are owners of workflow instances as well
 * as which users are owners according to their role.
 * 
 * This class also builds the workflow state machine to be passed to 
 * workflow instances.
 * 
 * @author crejaud
 */
public class WorkflowTemplate implements Serializable {
	private Map<String, String> endusersToRoles;
	private Map<String, List<WorkflowInstance>> ownerOfWorkflowInstances;
	private Set<String> ownerRoleTypes;
	private WorkflowStateMachine fsm;
	
	/**
	 * The constructor for a workflow template.
	 * Will generate the finite state machine and fill all maps pertaining to the
	 * information in the json file.
	 * 
	 * @param template - the json file from the workflow programmer
	 */
	public WorkflowTemplate(JSONObject template) {
		// ...
	}
	
	/**
	 * This will create a workflow instance.
	 * 
	 * Can only be called from owners.
	 */
	public void instantiate() {
		// ...
	}
	
	/**
	 * This will add a username to a specific role
	 * in case it was not specified in the
	 * initial creation of the workflow template.
	 * @param username - the enduser's username
	 * @param role - the new role of the enduser
	 */
	public void addUserToRole(String username, String role) {
		// ...
	}
	
	/**
	 * This will remove a username from whatever role
	 * it belongs to in this workflow template.
	 * @param username - the enduser's username
	 */
	public void removeUserFromTemplate(String username) {
		// ...
	}
}
