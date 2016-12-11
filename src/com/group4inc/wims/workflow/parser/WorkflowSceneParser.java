package com.group4inc.wims.workflow.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.group4inc.wims.workflow.model.MyEntry;
import com.group4inc.wims.workflow.model.ui.EmailAndStateChangeButton;
import com.group4inc.wims.workflow.model.ui.FileChooserButton;
import com.group4inc.wims.workflow.model.ui.FileChooserEventHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * WorkflowSceneParser to cleanup the main WorkflowParser
 * 
 * This is package-private, so it can only be accessed within the same parser package
 * @author crejaud
 */
class WorkflowSceneParser {
	private MyEntry<String, Scene> entry;
	private int stateIndex, sceneIndex;
	private Map<String, Text> idToText = new HashMap<>();
	private Map<String, TextField> idToTextField = new HashMap<>();
	private Map<String, CheckBox> idToCheckBox = new HashMap<>();
	private Map<String, FileChooserButton> idToFileChooser = new HashMap<>();
	// make map for file downloaders
	private Map<String, ChoiceBox<String>> idToDropDown = new HashMap<>();
	private Map<String, EmailAndStateChangeButton> idToButton = new HashMap<>();
	private Map<String, Node> requiredNodes = new HashMap<>();
	
	private static final String[] NODE_TYPES = {
			WorkflowLanguageGlobal.TEXTS,
			WorkflowLanguageGlobal.TEXTFIELDS,
			WorkflowLanguageGlobal.CHECKBOXES,
			WorkflowLanguageGlobal.FILE_UPLOADERS,
			WorkflowLanguageGlobal.FILE_DOWNLOADERS,
			WorkflowLanguageGlobal.DROPDOWN_LISTS,
			WorkflowLanguageGlobal.BUTTONS
	};
	
	/**
	 * A subparser class for WorkflowParser.
	 * 
	 * Parses a JSONObject which is supposed to represent a scene, into a Scene javafx class.
	 * @param sceneJSON					JSONObject representing the scene
	 * @param stateIndex				the state index
	 * @param sceneIndex				the scene index
	 * @throws WorkflowParseException	sceneJSON does not fully represent a scene
	 */
	public WorkflowSceneParser(JSONObject sceneJSON, int stateIndex, int sceneIndex) throws WorkflowParseException {
		// store stateIndex and sceneIndex to remove redundancy of continuously sending it to all methods
		this.stateIndex = stateIndex;
		this.sceneIndex = sceneIndex;
		
		// get the role string
		String role = parseRoleFromJSONObject(sceneJSON);
		
		// get the scene
		Scene scene = parseSceneFromJSONObject(sceneJSON);
		
		// make the entry!
		entry = new MyEntry<>(role, scene);
	}
	
	/**
	 * Get the role from a scene JSONObject.
	 * 
	 * @param sceneJSON					JSONObject representing the scene
	 * @return							role string
	 * @throws WorkflowParseException	sceneJSON does not fully represent a scene
	 */
	private String parseRoleFromJSONObject(JSONObject sceneJSON) throws WorkflowParseException {
		Object roleObj = sceneJSON.get(WorkflowLanguageGlobal.ROLE);
		String role;
		if (roleObj instanceof String) {
			role = (String) roleObj;
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + " role is not of type String.");
		}
		return role;
	}
	
	/**
	 * Get the javafx scene from the JSONObject.
	 * 
	 * @param sceneJSON					JSONObject representing the scene
	 * @return							javafx scene
	 * @throws WorkflowParseException	sceneJSON does not fully represent a scene
	 */
	private Scene parseSceneFromJSONObject(JSONObject sceneJSON) throws WorkflowParseException {
		GridPane pane = new GridPane();
		
		// set all nodes to the pane at appropriate x/y coordinate
		for (String nodeType : NODE_TYPES)
			parseNodesFromJSONObject(sceneJSON, nodeType, pane);
		
		Scene scene = new Scene(pane, 200, 200);
		return scene;
	}
	
	/**
	 * A general method to simplify code.
	 * 
	 * Finds the buttons, texts, textfields, etc (Nodes) from the JSONObject
	 * and creates them.
	 * 
	 * @param sceneJSON					JSONObject representing the scene
	 * @param nodeType					type of Node (ex: texts, buttons, etc)
	 * @param pane						GridPane where the Nodes belong
	 * @throws WorkflowParseException	sceneJSON does not fully represent a scene
	 */
	private void parseNodesFromJSONObject(JSONObject sceneJSON, String nodeType, GridPane pane) throws WorkflowParseException {
		Object obj = sceneJSON.get(nodeType);
		JSONArray nodesJSON;
		if (obj != null) {
			if (obj instanceof JSONArray) {
				nodesJSON = (JSONArray) obj;
				for (int i = 0; i < nodesJSON.size(); i++)
					parseNodeFromJSONObject(nodesJSON.get(i), nodeType, pane);
			} else {
				throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + " " + nodeType + " is not of type JSONArray.");
			}
		}
	}
	
	/**
	 * A general method to simplify code.
	 * 
	 * Finds a button, text, textfields, etc (Node) from the JSONObject
	 * and creates them.
	 * 
	 * @param nodeObj					the node object to be parsed into a json object
	 * @param nodeType					type of Node
	 * @param pane						GridPane where the Nodes belong
	 * @throws WorkflowParseException	nodeJSON does not fully represent a node
	 */
	private void parseNodeFromJSONObject(Object nodeObj, String nodeType, GridPane pane) throws WorkflowParseException {
		JSONObject nodeJSON;
		if (nodeObj != null) {
			if (nodeObj instanceof JSONObject) {
				nodeJSON = (JSONObject) nodeObj;
				switch(nodeType) {
				case WorkflowLanguageGlobal.TEXTS:
					parseTextFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.TEXTFIELDS:
					parseTextFieldFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.CHECKBOXES:
					parseCheckBoxFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.FILE_UPLOADERS:
					parseFileUploaderFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.FILE_DOWNLOADERS:
					parseFileDownloaderFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.DROPDOWN_LISTS:
					parseDropdownListFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.BUTTONS:
					parseButtonFromJSONObject(nodeJSON, pane);
					break;
			}
			} else {
				throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " is not of type JSONArray.");
			}
		}
	}
	
	/**
	 * Create a button based on the JSONObject 
	 * and add it to the pane.
	 * 
	 * @param buttonJSON				JSONObject representing a button
	 * @param pane						GridPane where the Nodes belong
	 * @throws WorkflowParseException	buttonJSON does not fully represent a button or if id already exists	
	 */
	private void parseButtonFromJSONObject(JSONObject buttonJSON, GridPane pane) throws WorkflowParseException {
		String nodeType = WorkflowLanguageGlobal.BUTTONS;
		String id = parseId(buttonJSON, nodeType);
		int x = parseX(buttonJSON, nodeType);
		int y = parseY(buttonJSON, nodeType);
		String label = parseLabel(buttonJSON, nodeType);
		//TODO actions
		EmailAndStateChangeButton button = new EmailAndStateChangeButton(label);
		parseEmailsFromJSONObject(buttonJSON, nodeType, button);
		
		parseChangeStatesFromJSONObject(buttonJSON, nodeType, button);
		
		button.setId(id);
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToButton.put(id, button);
		
		// add to pane
		pane.add(button, x, y);
	}

	/**
	 * Create a ChoiceBox based on the dropdownJSON and
	 * add it to the pane.
	 * 
	 * @param dropdownJSON				JSONObject representing a ChoiceBox
	 * @param pane						GridPane where the Nodes belong
	 * @throws WorkflowParseException	dropdownJSON does not fully represent a ChoiceBox or if id already exists
	 */
	private void parseDropdownListFromJSONObject(JSONObject dropdownJSON, GridPane pane) throws WorkflowParseException {
		ChoiceBox<String> cb = new ChoiceBox<>();
		String nodeType = WorkflowLanguageGlobal.DROPDOWN_LISTS;
		String id = parseId(dropdownJSON, nodeType);
		int x = parseX(dropdownJSON, nodeType);
		int y = parseY(dropdownJSON, nodeType);
		String label = parseLabel(dropdownJSON, nodeType);
		String hint = parseHint(dropdownJSON, nodeType);
		ObservableList<String> list = FXCollections.observableArrayList(parseListOfStrings(dropdownJSON, nodeType));
		boolean isRequired = parseIsRequired(dropdownJSON, nodeType);
				
		cb.setId(id);
		cb.setItems(list);
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToDropDown.put(id, cb);
		
		Label l = new Label(label);
		HBox hbox = new HBox();
		hbox.getChildren().addAll(l, cb);

		if (isRequired) {
			requiredNodes.put(id, cb);
		}
		
		// add to pane
		pane.add(hbox, x, y);
	}

	/**
	 * Create a file downloader node based on the fileDownloaderJSON.
	 * 
	 * @param fileDownloaderJSON		JSONObject representing the file downloader
	 * @param pane						GridPane where the file downloader belongs
	 */
	private void parseFileDownloaderFromJSONObject(JSONObject fileDownloaderJSON, GridPane pane) {
		// idk
	}

	/**
	 * Create a file uploader node based on the fileUploaderJSON.
	 * 
	 * @param fileUploaderJSON			JSONObject representing a FileChooserButton
	 * @param pane						GridPaner where the FileChooserButton belongs
	 * @throws WorkflowParseException	fileUploaderJSON does not fully represent a FileChooserButton or id already exists
	 * @see FileChooserButton
	 */
	private void parseFileUploaderFromJSONObject(JSONObject fileUploaderJSON, GridPane pane) throws WorkflowParseException {
		String nodeType = WorkflowLanguageGlobal.FILE_UPLOADERS;
		String id = parseId(fileUploaderJSON, nodeType);
		int x = parseX(fileUploaderJSON, nodeType);
		int y = parseY(fileUploaderJSON, nodeType);
		String label = parseLabel(fileUploaderJSON, nodeType);
		boolean canSelectMultipleFiles = parseCanSelectMultipleFiles(fileUploaderJSON, nodeType);
		boolean isRequired = parseIsRequired(fileUploaderJSON, nodeType);
		
		FileChooserButton fcb = new FileChooserButton(label, canSelectMultipleFiles);
		fcb.setId(id);
		// should show filechooser and set files accordingly to the FileChooserButton
		fcb.setOnAction(new FileChooserEventHandler(fcb));
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToFileChooser.put(id, fcb);
		
		if (isRequired) {
			requiredNodes.put(id, fcb);
		}
		
		// add to pane
		pane.add(fcb, x, y);
	}

	/**
	 * Create a checkbox from a JSONObject.
	 * 
	 * @param checkboxJSON				JSONObject representing a CheckBox
	 * @param pane						GridPane where the CheckBox belongs
	 * @throws WorkflowParseException	checkboxJSON does not fully represent a CheckBox or id already exists
	 */
	private void parseCheckBoxFromJSONObject(JSONObject checkboxJSON, GridPane pane) throws WorkflowParseException {
		CheckBox cb = new CheckBox();
		
		String nodeType = WorkflowLanguageGlobal.CHECKBOXES;
		String id = parseId(checkboxJSON, nodeType);
		int x = parseX(checkboxJSON, nodeType);
		int y = parseY(checkboxJSON, nodeType);
		String label = parseLabel(checkboxJSON, nodeType);
		boolean isRequired = parseIsRequired(checkboxJSON, nodeType);
		
		cb.setId(id);
		cb.setText(label);
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToCheckBox.put(id, cb);
		
		pane.add(cb, x, y);
	}

	/**
	 * Create a text node from a JSONObject.
	 * 
	 * @param textJSON					JSONObject representing a Text node
	 * @param pane						GridPane where the Text node belongs
	 * @throws WorkflowParseException	textJSON does not fully represent a Text node
	 */
	private void parseTextFromJSONObject(JSONObject textJSON, GridPane pane) throws WorkflowParseException {
		Text t = new Text();
		
		String nodeType = WorkflowLanguageGlobal.TEXTFIELDS;
		int x = parseX(textJSON, nodeType);
		int y = parseY(textJSON, nodeType);
		String label = parseLabel(textJSON, nodeType);
		
		t.setText(label);
		
		// add to pane
		pane.add(t, x, y);
	}
	
	/**
	 * Create a TextField node from a JSONObject.
	 * 
	 * @param textfieldJSON				JSONObject representing a TextField
	 * @param pane						GridPane where the TextField belongs
	 * @throws WorkflowParseException	textfieldJSON does not fully represent a TextField or id already exists
	 */
	private void parseTextFieldFromJSONObject(JSONObject textfieldJSON, GridPane pane) throws WorkflowParseException {
		TextField tf = new TextField();
		String nodeType = WorkflowLanguageGlobal.TEXTFIELDS;
		String id = parseId(textfieldJSON, nodeType);
		int x = parseX(textfieldJSON, nodeType);
		int y = parseY(textfieldJSON, nodeType);
		String label = parseLabel(textfieldJSON, nodeType);
		String hint = parseHint(textfieldJSON, nodeType);
		boolean isRequired = parseIsRequired(textfieldJSON, nodeType);
		
		Label l = new Label(label);
		
		tf.setId(id);
		tf.setPromptText(hint);
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToTextField.put(id, tf);

		HBox hbox = new HBox();
		hbox.getChildren().addAll(l, tf);
		
		// add to pane
		pane.add(hbox, x, y);
	}
	
	/** 
	 * Get the ID of a JSONObject
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							id
	 * @throws WorkflowParseException	id does not exist or is of incorrect type
	 */
	private String parseId(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return parseStringNode(jsonObj, nodeType, WorkflowLanguageGlobal.ID);
	}
	
	/**
	 * Get the X coordinate of a JSONObject.
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							x coordinate
	 * @throws WorkflowParseException	x does not exist or is of incorrect type
	 */
	private int parseX(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return parseIntegerNode(jsonObj, nodeType, WorkflowLanguageGlobal.X);
	}
	
	/**
	 * Get the Y coordinate of a JSONObject.
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							y coordinate
	 * @throws WorkflowParseException	y does not exist or is of incorrect type
	 */
	private int parseY(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return parseIntegerNode(jsonObj, nodeType, WorkflowLanguageGlobal.Y);
	}
	
	/**
	 * Get the label from a JSONObject.
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							label string
	 * @throws WorkflowParseException	label does not exist or is of incorrect type
	 */
	private String parseLabel(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return parseStringNode(jsonObj, nodeType, WorkflowLanguageGlobal.LABEL);
	}
	
	/**
	 * Get the flag for if a JSONObject is required or not.
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							true if JSONObject is required, else false
	 * @throws WorkflowParseException	isRequired flag is of incorrect type
	 */
	private boolean parseIsRequired(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return parseBooleanNode(jsonObj, nodeType, WorkflowLanguageGlobal.IS_REQUIRED);
	}
	
	/**
	 * Get the flag for if a JSONObject can select multiple files or not.
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							true if JSONObject can select multiple files, else false
	 * @throws WorkflowParseException	canSelectMultipleFiles flag is of incorrect type
	 */
	private boolean parseCanSelectMultipleFiles(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return parseBooleanNode(jsonObj, nodeType, WorkflowLanguageGlobal.MULTIPLE_FILES);
	}
	
	/**
	 * Get hint string for a JSONObject.
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							hint string
	 * @throws WorkflowParseException	hint does not exist or is of incorrect type
	 */
	private String parseHint(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return parseStringNode(jsonObj, nodeType, WorkflowLanguageGlobal.HINT);
	}
	
	/**
	 * Get the list of strings for a JSONObject.
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @return							list of strings
	 * @throws WorkflowParseException	if list of strings does not exist or is of wrong type or an element of the list is not of type String
	 */
	private List<String> parseListOfStrings(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		List<String> strings = new ArrayList<>();
		Object listObj = jsonObj.get(nodeType);
		JSONArray listJSON;
		if (listObj instanceof JSONArray) {
			listJSON = (JSONArray) listObj;
			for (int i = 0; i < listJSON.size(); i++) {
				Object stringObj = listJSON.get(i);
				if (stringObj instanceof String) {
					String s = (String) stringObj;
					strings.add(s);
				} else {
					throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " " + WorkflowLanguageGlobal.LIST + " at index + " + i + " is not of type String");
				}
			}
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " " + WorkflowLanguageGlobal.LIST + " is not of type JSONArray");
		}
		
		return strings;
	}
	
	/**
	 * Generic method to be called for finding integer values (Ex: x, y coordinates).
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @param valueType					the value type (x, y, etc)
	 * @return							the integer value
	 * @throws WorkflowParseException	if the integer does not exist or is not an integer
	 */
	private int parseIntegerNode(JSONObject jsonObj, String nodeType, String valueType) throws WorkflowParseException {
		Object intObj = jsonObj.get(nodeType);
		int intVal;
		if (intObj instanceof Integer) {
			intVal = (int) intObj;
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " " + valueType + " is not of type Integer");
		}
		return intVal;
	}
	
	/**
	 * Generic method to be called for finding String values (label, hint, etc)
	 * 
	 * @param jsonObj					the JSONObject
	 * @param nodeType					the node type
	 * @param valueType					the value type (label, hint, etc)
	 * @return							the string value
	 * @throws WorkflowParseException	if the string does not exist or is not a string
	 */
	private String parseStringNode(JSONObject jsonObj, String nodeType, String valueType) throws WorkflowParseException {
		Object stringObj = jsonObj.get(nodeType);
		String stringVal;
		if (stringObj instanceof String) {
			stringVal = (String) stringObj;
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " " + valueType + " is not of type String");
		}
		return stringVal;
	}
	
	/**
	 * Generic method to be called for finding boolean values (isRequired, canSelectMultipleFiles)
	 * @param jsonObj		the JSONObject
	 * @param nodeType		the node type
	 * @param valueType		the value type (isRequired, etc)
	 * @return				the boolean value, or false if it was left out
	 */
	private boolean parseBooleanNode(JSONObject jsonObj, String nodeType, String valueType) {
		Object booleanObj = jsonObj.get(nodeType);
		boolean booleanVal;
		if (booleanObj instanceof Boolean) {
			booleanVal = (boolean) booleanObj;
		} else {
			return false;
		}
		return booleanVal;
	}
	
	/**
	 * Checks to see if an id already exists in the set of existing ids.
	 * 
	 * Necessary for ensuring that all ids are unique.
	 * 
	 * @param id	the id we are checking
	 * @return		true if id already exists, else false
	 */
	private boolean idExistsAlready(String id) {
		return !(idToText.containsKey(id) ||
				idToTextField.containsKey(id) ||
				idToCheckBox.containsKey(id) ||
				idToFileChooser.containsKey(id) ||
				idToDropDown.containsKey(id) ||
				idToButton.containsKey(id));
	}
	
	/**
	 * Set the list of email transactions for an EmailAndStateChangeButton.
	 * 
	 * @param buttonJSON				the JSONObject for a button
	 * @param nodeType					the node type
	 * @param button					the EmailAndStateChangeButton to be mutated
	 * @throws WorkflowParseException	if an email do not exist or are of wrong format
	 */
	private void parseEmailsFromJSONObject(JSONObject buttonJSON, String nodeType, EmailAndStateChangeButton button) throws WorkflowParseException {
		Object emailsObj = buttonJSON.get(WorkflowLanguageGlobal.EMAILS);
		JSONArray emailsJSON;
		if (emailsObj == null)
			return;
		if (emailsObj instanceof JSONArray) {
			emailsJSON = (JSONArray) emailsObj;
			for (int i = 0; i < emailsJSON.size(); i++) {
				Object emailObj = emailsJSON.get(i);
				JSONObject emailJSON;
				if (emailObj instanceof JSONObject) {
					emailJSON = (JSONObject) emailObj;
					parseEmailFromJSONObject(emailJSON, nodeType, button, i);
				} else {
					throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " email " + i + " not of type JSONObject");
				}
			}
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " emails is not of type JSONArray");
		}
	}
	
	/**
	 * Sets one email transaction for an EmailAndStateChangeButton.
	 * 
	 * @param emailJSON					the JSONObject for an email
	 * @param nodeType					the node type
	 * @param button					the EmailAndStateChangeButton to be mutated
	 * @param index						the index of the email
	 * @throws WorkflowParseException	if the email attributes are not of type String or they are missing, or if the email address is not a valid
	 */
	private void parseEmailFromJSONObject(JSONObject emailJSON, String nodeType, EmailAndStateChangeButton button, int index) throws WorkflowParseException {
		String to = parseStringNode(emailJSON, nodeType, WorkflowLanguageGlobal.TO);
		// check that 'to' is of email format
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(to);
		
		// to is not of email format
		if (!m.matches()) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " email " + index + " to is not a valid email address.");
		}
		String subject = parseStringNode(emailJSON, nodeType, WorkflowLanguageGlobal.SUBJECT);
		String body = parseStringNode(emailJSON, nodeType, WorkflowLanguageGlobal.BODY);
		
		button.addEmail(to, subject, body);
	}
	
	/**
	 * Set the next states and metadata to be added to the EmailAndStateChangeButton.
	 * 
	 * @param changeStateJSON			the JSONObject for a state change
	 * @param nodeType					the node type
	 * @param button					the EmailAndStateChangeButton to be mutated
	 * @throws WorkflowParseException	if the list of strings don't exist or are of incorrect format
	 */
	private void parseChangeStatesFromJSONObject(JSONObject changeStateJSON, String nodeType, EmailAndStateChangeButton button) throws WorkflowParseException {
		List<String> nextStates = parseListOfStrings(changeStateJSON, WorkflowLanguageGlobal.NEXT_STATES);
		List<String> addToMetadata = parseListOfStrings(changeStateJSON, WorkflowLanguageGlobal.METADATA);
		
		button.setNextStates(nextStates);
		button.setAddToMetadata(addToMetadata);
	}

	
	/**
	 * Gets the scene entry.
	 * @return	the scene entry
	 */
	public MyEntry<String, Scene> getSceneEntry() {
		return entry;
	}

}
