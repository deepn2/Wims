package com.group4inc.wims.workflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.group4inc.wims.workflow.fsm.WorkflowStateMachine;

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
	private Map<String, String> usersToRoles;
	private Map<String, List<WorkflowInstance>> ownersToWorkflowInstances;
	private Set<String> ownerRoles;
	private WorkflowStateMachine fsm;
	
	/**
	 * The constructor for a workflow template.
	 * Will generate the finite state machine and fill all maps pertaining to the
	 * information in the json file.
	 * 
	 * @param template - the json file from the workflow programmer
	 */
	public WorkflowTemplate(Map<String, String> usersToRoles, Set<String> ownerRoles, WorkflowStateMachine fsm) {
		this.usersToRoles = usersToRoles;
		this.ownersToWorkflowInstances = new HashMap<>();
		this.ownerRoles = ownerRoles;
		this.fsm = fsm;
	}
	
	public List<WorkflowInstance> getWorkflowInstance(String username) {
		String role = usersToRoles.get(username);
		
		if (role == null || role.isEmpty()) {
			return new ArrayList<WorkflowInstance>();
		}
		
		if (ownerRoles.contains(role)) {
			return ownersToWorkflowInstances.get(username);
		}
		
		List<WorkflowInstance> activeWorkflowInstances = new ArrayList<>();
		
		Collection<List<WorkflowInstance>> activeWorkflowInstanceLists =  ownersToWorkflowInstances.values();
		
		// merge list of lists into 1 list
		for (List<WorkflowInstance> workflowInstanceList : activeWorkflowInstanceLists) {
			activeWorkflowInstances.addAll(workflowInstanceList);
		}
		
		return activeWorkflowInstances;
	}
	
	/**
	 * This will create a workflow instance.
	 * 
	 * Should only be called by an owner.
	 */
	public void instantiateWorkflow(String username) {
		// check to see if user is an owner
		if (!isOwner(username)) {
			return;
		}
		
		// otherwise, continue with instantiation
		
		WorkflowInstance newWorkflowInstance = new WorkflowInstance(fsm);
		
		// first time this user is making an 
		if (ownersToWorkflowInstances.get(username) == null) {
			List<WorkflowInstance> workflowInstanceList = new ArrayList<>();
			workflowInstanceList.add(newWorkflowInstance);
			ownersToWorkflowInstances.put(username, workflowInstanceList);
		} else {
			ownersToWorkflowInstances.get(username).add(newWorkflowInstance);
		}
	}
	
	public boolean isOwner(String username) {
		String role = usersToRoles.get(username);
		
		if (role == null || role.isEmpty()) {
			return false;
		}
		
		return ownerRoles.contains(username);
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
