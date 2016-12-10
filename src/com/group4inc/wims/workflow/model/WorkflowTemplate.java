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
	 * @param usersToRoles	the mapping of users to roles
	 * @param ownerRoles		the set of roles that are owners
	 * @param fsm						the finite state machine of the template
	 */
	public WorkflowTemplate(Map<String, String> usersToRoles, Set<String> ownerRoles, WorkflowStateMachine fsm) {
		this.usersToRoles = usersToRoles;
		this.ownersToWorkflowInstances = new HashMap<>();
		this.ownerRoles = ownerRoles;
		this.fsm = fsm;
	}

	/**
	 * This will return the list of all workflow instances that a
	 * specfic enduser belongs to.
	 *
	 * @param username 	the enduser's username
	 * @return 					the list of workflow instances that belong to the enduser
	 */
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
	 * @param username	the enduser's username
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

	/**
	 * This will remove a workflow instance
	 *
	 * Should only be called by an owner.
	 * @param username	the enduser's username
	 * @param toRemove	the workflow instance to be removed
	 * @return					true if removed successfully, else false
	 */
	public boolean removeWorkflowInstance(String username, WorkflowInstance toRemove) {
		if(!isOwner(username)) {
			return false;
		}

		else if(ownersToWorkflowInstances.get(username).contains(toRemove)) {
			return ownersToWorkflowInstances.get(username).remove(toRemove);
		}

		return false;

	}

	/**
	 * This will detect if a username is of type owner
	 *
	 * @param username	the enduser's username
	 * @return					true if username corresponds to an owner role, else false
	 */
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
		usersToRoles.put(username, role);
	}

	/**
	 * This will remove a username from whatever role
	 * it belongs to in this workflow template.
	 * @param username - the enduser's username
	 */
	public void removeUserFromTemplate(String username) {
		usersToRoles.remove(username);
	}
}
