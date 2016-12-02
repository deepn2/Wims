package com.group4inc.wims.workflow;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an instance of a workflow from a workflow template.
 * A workflow instance will have current metadata, a state machine to follow, and the list of
 * current states.
 * 
 * @author crejaud
 */
public class WorkflowInstance implements Serializable {
	private JSONObject metadata;
	private WorkflowStateMachine fsm;
	private List<Integer> currentStates;
	
	/**
	 * The constructor for a workflow instance.
	 * Will set the current states to the first state and
	 * initialize metadata.
	 * 
	 * @param fsm - The workflow state machine
	 */
	public WorkflowInstance(WorkflowStateMachine fsm) {
		// ...
	}
	
	/**
	 * Override a step in the workflow instance.
	 * 
	 * Should only be accessible from the owner of a workflow instance.
	 */
	public void overrideState() {
		// ...
	}
}
