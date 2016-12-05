package com.group4inc.wims.workflow.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.group4inc.wims.workflow.fsm.WorkflowStateMachine;

/**
 * This class represents an instance of a workflow from a workflow template.
 * A workflow instance will have current metadata, a state machine to follow, and the list of
 * current states.
 * 
 * @author crejaud
 */
public class WorkflowInstance implements Serializable {
	private Map<String, Object> metadata;
	private WorkflowStateMachine fsm;
	private List<String> currentStates;
	
	/**
	 * The constructor for a workflow instance.
	 * Will set the current states to the first state and
	 * initialize metadata.
	 * 
	 * @param fsm - The workflow state machine
	 */
	public WorkflowInstance(WorkflowStateMachine fsm) {
		this.fsm = fsm;
	}
	
	public Map<String, Object> getMetadata() {
		return metadata;
	}
	
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	
	public List<String> getCurrentStates() {
		return currentStates;
	}
	
	public void setCurrentStates(List<String> currentStates) {
		this.currentStates = currentStates;
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
