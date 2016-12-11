package com.group4inc.wims.workflow.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.group4inc.wims.idm.Domain;
import com.group4inc.wims.workflow.fsm.WorkflowState;
import com.group4inc.wims.workflow.fsm.WorkflowStateMachine;
import com.group4inc.wims.workflow.model.MyEntry;
import com.group4inc.wims.workflow.model.WorkflowTemplate;
import com.group4inc.wims.workflow.parser.WorkflowParseException;

import javafx.scene.Scene;

public class WorkflowParser {
	private WorkflowTemplate workflowTemplate;
	private JSONParser parser = new JSONParser();
	private Domain domain;

	public WorkflowParser(File file, Domain domain) throws FileNotFoundException, IOException, ParseException, WorkflowParseException {
		JSONObject fileJSON = parseFileIntoJSONObject(file);

		// file parse successful
		this.domain = domain;
		this.workflowTemplate = parseJSONObjectIntoWorkflowTemplate(fileJSON);
	}

	public WorkflowTemplate getWorkflowTemplate() {
		return workflowTemplate;
	}

	/**
	 * Parse the file into a JSONObject.
	 * 
	 * @param file						the file
	 * @return							a JSONObject
	 * @throws FileNotFoundException	if the file is not found
	 * @throws IOException				if the file is locked or inaccessible
	 * @throws ParseException			if the file cannot be parsed into a JSONObject
	 */
	private JSONObject parseFileIntoJSONObject(File file) throws FileNotFoundException, IOException, ParseException {
		JSONObject fileJSON = (JSONObject) parser.parse(new FileReader(file.getPath()));
		return fileJSON;
	}

	/**
	 * Parse a JSONObject into a WorkflowTemplate.
	 * 
	 * @param fileJSON					the JSONObject representing a WorkflowTemplate
	 * @return							a WorkflowTemplate
	 * @throws WorkflowParseException	if the JSONObject does not fully represent a WorkflowTemplate
	 */
	private WorkflowTemplate parseJSONObjectIntoWorkflowTemplate(JSONObject fileJSON) throws WorkflowParseException {
		// parse ROLES
		if (!fileJSON.containsKey(WorkflowLanguageGlobal.ROLES)) {
			throw new WorkflowParseException("Roles JSONArray must be included");
		}
		Object rolesObject = fileJSON.get(WorkflowLanguageGlobal.ROLES);
		JSONArray rolesJSON = null;
		if (rolesObject instanceof JSONArray) {
			// roles is a valid JSONArray
			rolesJSON = (JSONArray) rolesObject;
		} else {
			throw new WorkflowParseException("Roles is not a valid JSONArray");
		}
		Map<String, String> usersToRoles = parseJSONArrayIntoUsersToRolesMap(rolesJSON);

		// parse OWNERS
		if (!fileJSON.containsKey(WorkflowLanguageGlobal.OWNERS)) {
			throw new WorkflowParseException("Owners JSONArray must be included");
		}
		Object ownersObject = fileJSON.get(WorkflowLanguageGlobal.OWNERS);
		JSONArray ownersJSON = null;
		if (ownersObject instanceof JSONArray) {
			// owners is a valid JSONArray
			ownersJSON = (JSONArray) ownersObject;
		} else {
			throw new WorkflowParseException("Owners is not a valid JSONArray");
		}
		Set<String> ownerRoles = parseJSONArrayIntoOwnersSet(ownersJSON);

		// parse STATES
		if (!fileJSON.containsKey(WorkflowLanguageGlobal.STATES)) {
			throw new WorkflowParseException("States JSONArray must be included");
		}
		Object statesObject = fileJSON.get(WorkflowLanguageGlobal.STATES);
		JSONArray statesJSON = null;
		if (statesObject instanceof JSONArray) {
			// states is a valid JSONArray
			statesJSON = (JSONArray) statesObject;
		} else {
			throw new WorkflowParseException("States is not a valid JSONArray");
		}
		WorkflowStateMachine fsm = parseJSONArrayIntoWorkflowStateMachine(statesJSON);

		return new WorkflowTemplate(usersToRoles, ownerRoles, fsm);
	}

	/**
	 * Parse a JSONArray into the usersToRoles mapping.
	 * 
	 * @param rolesJSON					the JSONArray representing roles
	 * @return							the usersToRoles mapping
	 * @throws WorkflowParseException	if the JSONArray does not fully represent the roles mapping
	 */
	private Map<String, String> parseJSONArrayIntoUsersToRolesMap(JSONArray rolesJSON) throws WorkflowParseException {
		Map<String, String> usersToRoles = new HashMap<>();

		for (int roleIndex = 0; roleIndex < rolesJSON.size(); roleIndex++) {
			Object roleObject = rolesJSON.get(roleIndex);
			JSONObject roleJSON;
			if (roleObject instanceof JSONObject) {
				// valid JSONObject
				roleJSON = (JSONObject) roleObject;
			} else {
				throw new WorkflowParseException("Role " + roleIndex + " is an invalid JSONObject");
			}
			List<Map.Entry<String, String>> entries = parseJSONObjectIntoUsersToRolesEntries(roleJSON, roleIndex);
			for (Map.Entry<String, String> entry : entries)
				usersToRoles.put(entry.getKey(), entry.getValue());
		}

		return null;
	}

	/**
	 * Parse a JSONObject into a map entries for the usersToRoles mapping.
	 * 
	 * @param roleJSON					the JSONObject representing the map entry
	 * @param roleIndex					the index of the role for error message displaying reasons
	 * @return							the list of map entry
	 * @throws WorkflowParseException	if the JSONObject does not fully represent the map entries
	 */
	private List<Map.Entry<String, String>> parseJSONObjectIntoUsersToRolesEntries(JSONObject roleJSON, int roleIndex) throws WorkflowParseException {
		List<Map.Entry<String, String>> entries = new ArrayList<>();

		if (!roleJSON.containsKey(WorkflowLanguageGlobal.ROLE)) {
			throw new WorkflowParseException("Role " + roleIndex + " is missing role keyword.");
		}
		// parse role into string
		Object roleStringObj = roleJSON.get(WorkflowLanguageGlobal.ROLE);
		String role;
		if (roleStringObj instanceof JSONObject) {
			role = (String) roleStringObj;
		} else {
			throw new WorkflowParseException("Role " + roleIndex + " has invalid String role");
		}

		if (!roleJSON.containsKey(WorkflowLanguageGlobal.USERNAMES)) {
			throw new WorkflowParseException("Role " + roleIndex + " is missing usernames keyword.");
		}
		// parse usernames into JSONArray
		Object usernamesObj = roleJSON.get(WorkflowLanguageGlobal.USERNAMES);
		JSONArray usernamesJSON;
		if (usernamesObj instanceof JSONArray) {
			usernamesJSON = (JSONArray) usernamesObj;
		} else {
			throw new WorkflowParseException("Role " + roleIndex + " has invalid usernames JSONArray");
		}

		// add entries to list
		for (int usernameIndex = 0; usernameIndex < usernamesJSON.size(); usernameIndex++) {
			Object usernameObj = usernamesJSON.get(usernameIndex);
			String username;
			if (usernameObj instanceof String) {
				username = (String) usernameObj;
			} else {
				throw new WorkflowParseException("Role " + roleIndex + "'s username "+usernameIndex+" is not of type String");
			}
			Map.Entry<String, String> entry = new MyEntry<String, String>(username, role);
			entries.add(entry);
		}

		return entries;
	}

	/**
	 * Parse a JSONArray into the set of Owners.
	 * 
	 * @param ownersJSON				the JSONArray representing the owner roles
	 * @return							the set of owner roles
	 * @throws WorkflowParseException	if the JSONArray does not fully represent the owner roles
	 */
	private Set<String> parseJSONArrayIntoOwnersSet(JSONArray ownersJSON) throws WorkflowParseException {
		Set<String> ownerRoles = new HashSet<String>();

		for (int ownerIndex = 0; ownerIndex < ownersJSON.size(); ownerIndex++) {
			Object roleObj = ownersJSON.get(ownerIndex);
			String role;
			if (roleObj instanceof String) {
				role = (String) roleObj;
			} else {
				throw new WorkflowParseException("Owner " + ownerIndex + " in owners JSONArray is not of type String");
			}
			ownerRoles.add(role);
		}

		return ownerRoles;
	}

	/**
	 * Parse JSONArray into a WorkflowStateMachine.
	 * 
	 * @param statesJSON				the JSONArray representing states
	 * @return							the WorkflowStateMachine
	 * @throws WorkflowParseException	if the JSONArray does not fully represent the states
	 */
	private WorkflowStateMachine parseJSONArrayIntoWorkflowStateMachine(JSONArray statesJSON) throws WorkflowParseException {
		Map<String, WorkflowState> idsToWorkflowStates = new HashMap<>();

		for(int stateIndex = 0; stateIndex < statesJSON.size(); stateIndex++) {
			Object stateObj = statesJSON.get(stateIndex);
			JSONObject stateJSON;
			if (stateObj instanceof JSONObject) {
				stateJSON = (JSONObject) stateObj;
			} else {
				throw new WorkflowParseException("State " + stateIndex + " is not a valid JSONObject");
			}
			Map.Entry<String, WorkflowState> entry = parseJSONObjectIntoWorkflowState(stateJSON, stateIndex);
			idsToWorkflowStates.put(entry.getKey(), entry.getValue());
		}
		return null;
	}

	/**
	 * Parse JSONObject into a WorkflowState.
	 * 
	 * @param stateJSON					the JSONObject representing a state
	 * @param stateIndex				the index of the state in the list of states
	 * @return							the entry of the workflow state where the key is the id
	 * @throws WorkflowParseException	if the state or id does not exist or is of the wrong format
	 */
	private Map.Entry<String, WorkflowState> parseJSONObjectIntoWorkflowState(JSONObject stateJSON, int stateIndex) throws WorkflowParseException {
		if (!stateJSON.containsKey(WorkflowLanguageGlobal.ID)) {
			throw new WorkflowParseException("State " + stateIndex + " is missing id keyword.");
		}
		Object idObj = stateJSON.get(WorkflowLanguageGlobal.ID);
		String id;
		if (idObj instanceof String) {
			id = (String) idObj;
		} else {
			throw new WorkflowParseException("State " + stateIndex + " id is not of type String.");
		}

		if (!stateJSON.containsKey(WorkflowLanguageGlobal.SCENES)) {
			throw new WorkflowParseException("State " + stateIndex + " is missing scenes keyword.");
		}
		Object scenesObj = stateJSON.get(WorkflowLanguageGlobal.SCENES);
		JSONArray scenesJSON;
		if (scenesObj instanceof JSONArray) {
			scenesJSON = (JSONArray) scenesObj;
		} else {
			throw new WorkflowParseException("State " + stateIndex + " scenes are not of type JSONArray.");
		}

		Map<String, Scene> rolesToScenes = new HashMap<>();

		for (int sceneIndex = 0; sceneIndex < scenesJSON.size(); sceneIndex++) {
			Object sceneObj = scenesJSON.get(sceneIndex);
			JSONObject sceneJSON;
			if (sceneObj instanceof JSONObject) {
				sceneJSON = (JSONObject) sceneObj;
			} else {
				throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + " is an invalid JSONObject");
			}
			Map.Entry<String, Scene> entry = parseJSONObjectIntoScene(sceneJSON, stateIndex, sceneIndex);
			rolesToScenes.put(entry.getKey(), entry.getValue());
		}

		return new MyEntry<>(id, new WorkflowState(rolesToScenes));
	}

	/**
	 * Parse a JSONObject into a Scene.
	 * 
	 * @param sceneJSON					the JSONObject representing a Scene
	 * @param stateIndex				the state index
	 * @param sceneIndex				the scene index
	 * @return							the javafx scene
	 * @throws WorkflowParseException	if the JSONObject does not fully represent the Scene
	 */
	private Map.Entry<String, Scene> parseJSONObjectIntoScene(JSONObject sceneJSON, int stateIndex, int sceneIndex) throws WorkflowParseException {
		WorkflowSceneParser sceneParser = new WorkflowSceneParser(sceneJSON, stateIndex, sceneIndex);

		return sceneParser.getSceneEntry();
	}
}
