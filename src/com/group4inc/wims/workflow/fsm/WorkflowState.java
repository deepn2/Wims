package com.group4inc.wims.workflow.fsm;

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
	private Map<String, Scene> rolesToScenes;
	
	/**
	 * The constructor for a workflow state inside a workflow state machine
	 * @param template - the json object that the state should follow
	 */
	public WorkflowState(Map<String, Scene> rolesToScenes) {
		this.rolesToScenes = rolesToScenes;
	}
	
	public Scene getSceneForRole(String role) {
		return rolesToScenes.get(role);
	}
}
