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
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.group4inc.wims.idm.Domain;
import com.group4inc.wims.workflow.fsm.WorkflowStateMachine;
import com.group4inc.wims.workflow.model.MyEntry;
import com.group4inc.wims.workflow.model.WorkflowTemplate;

public class WorkflowParser {
	private WorkflowTemplate workflowTemplate;
	private JSONParser parser = new JSONParser();
	private Domain domain;
	private List<String> errors = new ArrayList<>();
	
	public WorkflowParser(File file, Domain domain) throws FileNotFoundException, IOException, ParseException, WorkflowParseException {
		JSONObject fileJSON = parseFileIntoJSONObject(file);
		
		// file parse successful
		this.domain = domain;
		this.workflowTemplate = parseJSONObjectIntoWorkflowTemplate(fileJSON);
	}
	
	public WorkflowTemplate getWorkflowTemplate() {
		return workflowTemplate;
	}
	
	private JSONObject parseFileIntoJSONObject(File file) throws FileNotFoundException, IOException, ParseException {
		JSONObject fileJSON = (JSONObject) parser.parse(new FileReader(file.getPath()));
		return fileJSON;
	}
	
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
	
	private Map<String, String> parseJSONArrayIntoUsersToRolesMap(JSONArray rolesJSON) throws WorkflowParseException {
		Map<String, String> usersToRoles = new HashMap<>();
		
		for (int i = 0; i < rolesJSON.size(); i++) {
			Object roleObject = rolesJSON.get(i);
			if (roleObject instanceof JSONObject) {
				// valid JSONObject
				JSONObject roleJSON = (JSONObject) roleObject;
				List<Map.Entry<String, String>> entries = parseJSONObjectIntoUsersToRolesEntries(roleJSON, i);
				for (Map.Entry<String, String> entry : entries)
					usersToRoles.put(entry.getKey(), entry.getValue());
			} else {
				throw new WorkflowParseException(i + "th role in roles JSONArray is an invalid JSONObject");
			}
		}
		
		return null;
	}
	
	private List<Map.Entry<String, String>> parseJSONObjectIntoUsersToRolesEntries(JSONObject roleJSON, int i) throws WorkflowParseException {
		List<Map.Entry<String, String>> entries = new ArrayList<>();
		
		// parse role into string
		Object roleStringObj = roleJSON.get(WorkflowLanguageGlobal.ROLE);
		String role;
		if (roleStringObj instanceof JSONObject) {
			role = (String) roleStringObj;
		} else {
			throw new WorkflowParseException(i + "th role in roles has invalid String role");
		}
		
		// parse usernames into JSONArray
		Object usernamesObj = roleJSON.get(WorkflowLanguageGlobal.USERNAMES);
		JSONArray usernamesJSON;
		if (usernamesObj instanceof JSONArray) {
			usernamesJSON = (JSONArray) usernamesObj;
		} else {
			throw new WorkflowParseException(i + "th role in roles has invalid usernames JSONArray");
		}
		
		// add entries to list
		for (int j = 0; j < usernamesJSON.size(); j++) {
			Object usernameObj = usernamesJSON.get(j);
			if (usernameObj instanceof String) {
				String username = (String) usernameObj;
				Map.Entry<String, String> entry = new MyEntry<String, String>(username, role);
				entries.add(entry);
			} else {
				throw new WorkflowParseException(i + "th role in roles has invalid username type in usernames JSONArray at "+j+"th position");
			}
		}
		
		return entries;
	}
	
	private Set<String> parseJSONArrayIntoOwnersSet(JSONArray ownersJSON) throws WorkflowParseException {
		Set<String> ownerRoles = new HashSet<String>();
		
		for (int i = 0; i < ownersJSON.size(); i++) {
			Object roleObj = ownersJSON.get(i);
			if (roleObj instanceof String) {
				String role = (String) roleObj;
				ownerRoles.add(role);
			} else {
				throw new WorkflowParseException(i + "th role in owners JSONArray is not of type String");
			}
		}
		
		return ownerRoles;
	}
	
	private WorkflowStateMachine parseJSONArrayIntoWorkflowStateMachine(JSONArray statesJSON) {
		return null;
	}
}
