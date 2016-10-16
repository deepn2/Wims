package com.group4inc.wims.workflow;

import java.io.Serializable;
import java.util.List;

/**
 * This class is the state machine for a workflow.
 * 
 * It holds a list of workflow states.
 * 
 * @author creja_000
 */
public class WorkflowStateMachine implements Serializable {
	private List<WorkflowState> states;
	
	/**
	 * The constructor for the workflow state machine
	 * 
	 * @param template - the json template from the workflow programmer
	 */
	public WorkflowStateMachine(JSONObject template) {
		// ...
	}
}
