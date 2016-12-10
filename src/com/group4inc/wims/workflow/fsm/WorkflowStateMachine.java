package com.group4inc.wims.workflow.fsm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javafx.scene.Scene;

/**
 * This class is the state machine for a workflow.
 * 
 * It holds a list of workflow states.
 * 
 * @author creja_000
 */
public class WorkflowStateMachine implements Serializable {
	private Map<String, WorkflowState> idsToWorkflowStates;
	
	/**
	 * The constructor for the workflow state machine
	 * 
	 * @param template - the json template from the workflow programmer
	 */
	public WorkflowStateMachine(Map<String, WorkflowState> idsToWorkflowStates) {
		this.idsToWorkflowStates = idsToWorkflowStates;
	}
	
	public Scene getSceneForRole(String workflowStateId, String role) {
		return idsToWorkflowStates.get(workflowStateId).getSceneForRole(role);
	}
}
