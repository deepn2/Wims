package com.group4inc.wims.workflow;

import java.io.Serializable;
import java.util.Map;

import javafx.scene.Scene;

/**
 * This class represents a state inside a workflow state machine.
 * 
 * Each workflow state will keep track of scenes for each role in the workflow.
 * 
 * @author crejaud
 */
public class WorkflowState implements Serializable {
	private Map<String, Scene> roleToScene;
	
	/**
	 * The constructor for a workflow state inside a workflow state machine
	 * @param template - the json object that the state should follow
	 */
	public WorkflowState(JSONObject template) {
		// ...
	}
}
