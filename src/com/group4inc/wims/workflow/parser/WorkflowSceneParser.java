package com.group4inc.wims.workflow.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.group4inc.wims.workflow.model.MyEntry;
import com.group4inc.wims.workflow.model.ui.FileChooserButton;
import com.group4inc.wims.workflow.model.ui.FileChooserEventHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

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
	private Map<String, ChoiceBox> idToDropDown = new HashMap<>();
	private Map<String, Button> idToButton = new HashMap<>();
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
	
	public WorkflowSceneParser(JSONObject sceneJSON, int stateIndex, int sceneIndex) throws WorkflowParseException {
		// store stateIndex and sceneIndex to remove redundancy of continuously sending it to all methods
		this.stateIndex = stateIndex;
		this.sceneIndex = sceneIndex;
		
		// get the role string
		String role = getRoleFromJSONObject(sceneJSON);
		
		// get the scene
		Scene scene = getSceneFromJSONObject(sceneJSON);
		
		// make the entry!
		entry = new MyEntry<>(role, scene);
	}
	
	private String getRoleFromJSONObject(JSONObject sceneJSON) throws WorkflowParseException {
		Object roleObj = sceneJSON.get(WorkflowLanguageGlobal.ROLE);
		String role;
		if (roleObj instanceof String) {
			role = (String) roleObj;
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + " role is not of type String.");
		}
		return role;
	}
	
	private Scene getSceneFromJSONObject(JSONObject sceneJSON) throws WorkflowParseException {
		GridPane pane = new GridPane();
		
		// set all nodes to the pane at appropriate x/y coordinate
		for (String nodeType : NODE_TYPES)
			setNodesFromJSONObject(sceneJSON, nodeType, pane);
		
		Scene scene = new Scene(pane, 200, 200);
		return scene;
	}
	
	private void setNodesFromJSONObject(JSONObject sceneJSON, String nodeType, GridPane pane) throws WorkflowParseException {
		Object obj = sceneJSON.get(nodeType);
		JSONArray nodesJSON;
		if (obj != null) {
			if (obj instanceof JSONArray) {
				nodesJSON = (JSONArray) obj;
				for (int i = 0; i < nodesJSON.size(); i++)
					setNodeFromJSONObject(nodesJSON.get(i), nodeType, pane);
			} else {
				throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + " " + nodeType + " is not of type JSONArray.");
			}
		}
	}
	
	private void setNodeFromJSONObject(Object nodeObj, String nodeType, GridPane pane) throws WorkflowParseException {
		JSONObject nodeJSON;
		if (nodeObj != null) {
			if (nodeObj instanceof JSONObject) {
				nodeJSON = (JSONObject) nodeObj;
				switch(nodeType) {
				case WorkflowLanguageGlobal.TEXTS:
					setTextFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.TEXTFIELDS:
					setTextFieldFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.CHECKBOXES:
					setCheckBoxFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.FILE_UPLOADERS:
					setFileUploaderFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.FILE_DOWNLOADERS:
					setFileDownloaderFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.DROPDOWN_LISTS:
					setDropDownListFromJSONObject(nodeJSON, pane);
					break;
				case WorkflowLanguageGlobal.BUTTONS:
					setButtonFromJSONObject(nodeJSON, pane);
					break;
			}
			} else {
				throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " is not of type JSONArray.");
			}
		}
	}
	
	private void setButtonFromJSONObject(JSONObject buttonJSON, GridPane pane) throws WorkflowParseException {
		Button b = new Button();
		String nodeType = WorkflowLanguageGlobal.BUTTONS;
		String id = getId(buttonJSON, nodeType);
		int x = getX(buttonJSON, nodeType);
		int y = getY(buttonJSON, nodeType);
		String label = getLabel(buttonJSON, nodeType);
		//TODO actions
		
		b.setId(id);
		b.setText(label);
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToButton.put(id, b);
		
		// add to pane
		pane.add(b, x, y);
	}

	private void setDropDownListFromJSONObject(JSONObject dropdownJSON, GridPane pane) throws WorkflowParseException {
		ChoiceBox<String> cb = new ChoiceBox<>();
		String nodeType = WorkflowLanguageGlobal.DROPDOWN_LISTS;
		String id = getId(dropdownJSON, nodeType);
		int x = getX(dropdownJSON, nodeType);
		int y = getY(dropdownJSON, nodeType);
		String label = getLabel(dropdownJSON, nodeType);
		String hint = getHint(dropdownJSON, nodeType);
		ObservableList<String> list = getListOfStrings(dropdownJSON, nodeType);
		boolean isRequired = isRequired(dropdownJSON, nodeType);
				
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

	private void setFileDownloaderFromJSONObject(JSONObject fileDownloaderJSON, GridPane pane) {
		// idk
	}

	private void setFileUploaderFromJSONObject(JSONObject fileUploaderJSON, GridPane pane) throws WorkflowParseException {
		String nodeType = WorkflowLanguageGlobal.FILE_UPLOADERS;
		String id = getId(fileUploaderJSON, nodeType);
		int x = getX(fileUploaderJSON, nodeType);
		int y = getY(fileUploaderJSON, nodeType);
		String label = getLabel(fileUploaderJSON, nodeType);
		boolean canSelectMultipleFiles = canSelectMultipleFiles(fileUploaderJSON, nodeType);
		boolean isRequired = isRequired(fileUploaderJSON, nodeType);
		
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

	private void setCheckBoxFromJSONObject(JSONObject checkboxJSON, GridPane pane) throws WorkflowParseException {
		CheckBox cb = new CheckBox();
		
		String nodeType = WorkflowLanguageGlobal.CHECKBOXES;
		String id = getId(checkboxJSON, nodeType);
		int x = getX(checkboxJSON, nodeType);
		int y = getY(checkboxJSON, nodeType);
		String label = getLabel(checkboxJSON, nodeType);
		boolean isRequired = isRequired(checkboxJSON, nodeType);
		
		cb.setId(id);
		cb.setText(label);
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToCheckBox.put(id, cb);
		
		pane.add(cb, x, y);
	}

	private void setTextFromJSONObject(JSONObject textJSON, GridPane pane) throws WorkflowParseException {
		Text t = new Text();
		
		String nodeType = WorkflowLanguageGlobal.TEXTFIELDS;
		String id = getId(textJSON, nodeType);
		int x = getX(textJSON, nodeType);
		int y = getY(textJSON, nodeType);
		String label = getLabel(textJSON, nodeType);
		
		t.setId(id);
		t.setText(label);
		
		if (idExistsAlready(id)) {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " has an id which is not unique.");
		}
		idToText.put(id, t);
		
		// add to pane
		pane.add(t, x, y);
	}
	
	private void setTextFieldFromJSONObject(JSONObject textfieldJSON, GridPane pane) throws WorkflowParseException {
		TextField tf = new TextField();
		String nodeType = WorkflowLanguageGlobal.TEXTFIELDS;
		String id = getId(textfieldJSON, nodeType);
		int x = getX(textfieldJSON, nodeType);
		int y = getY(textfieldJSON, nodeType);
		String label = getLabel(textfieldJSON, nodeType);
		String hint = getHint(textfieldJSON, nodeType);
		boolean isRequired = isRequired(textfieldJSON, nodeType);
		
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
	
	private String getId(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return getStringNode(jsonObj, nodeType, WorkflowLanguageGlobal.ID);
	}
	
	private int getX(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return getIntegerNode(jsonObj, nodeType, WorkflowLanguageGlobal.X);
	}
	
	private int getY(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return getIntegerNode(jsonObj, nodeType, WorkflowLanguageGlobal.Y);
	}
	
	private String getLabel(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return getStringNode(jsonObj, nodeType, WorkflowLanguageGlobal.LABEL);
	}
	
	private boolean isRequired(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return getBooleanNode(jsonObj, nodeType, WorkflowLanguageGlobal.IS_REQUIRED);
	}
	
	private boolean canSelectMultipleFiles(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return getBooleanNode(jsonObj, nodeType, WorkflowLanguageGlobal.MULTIPLE_FILES);
	}
	
	private String getHint(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		return getStringNode(jsonObj, nodeType, WorkflowLanguageGlobal.HINT);
	}
	
	private ObservableList<String> getListOfStrings(JSONObject jsonObj, String nodeType) throws WorkflowParseException {
		ObservableList<String> strings = FXCollections.observableArrayList();
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
	
	private int getIntegerNode(JSONObject jsonObj, String nodeType, String valueType) throws WorkflowParseException {
		Object intObj = jsonObj.get(nodeType);
		int intVal;
		if (intObj instanceof Integer) {
			intVal = (int) intObj;
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " " + valueType + " is not of type Integer");
		}
		return intVal;
	}
	
	private String getStringNode(JSONObject jsonObj, String nodeType, String valueType) throws WorkflowParseException {
		Object stringObj = jsonObj.get(nodeType);
		String stringVal;
		if (stringObj instanceof String) {
			stringVal = (String) stringObj;
		} else {
			throw new WorkflowParseException("Scene " + sceneIndex + " of state " + stateIndex + "-" + nodeType + " " + valueType + " is not of type String");
		}
		return stringVal;
	}
	
	private boolean getBooleanNode(JSONObject jsonObj, String nodeType, String valueType) {
		Object booleanObj = jsonObj.get(nodeType);
		boolean booleanVal;
		if (booleanObj instanceof Boolean) {
			booleanVal = (boolean) booleanObj;
		} else {
			return false;
		}
		return booleanVal;
	}
	
	private boolean idExistsAlready(String id) {
		return !(idToText.containsKey(id) ||
				idToTextField.containsKey(id) ||
				idToCheckBox.containsKey(id) ||
				idToFileChooser.containsKey(id) ||
				idToDropDown.containsKey(id) ||
				idToButton.containsKey(id));
	}

	public MyEntry<String, Scene> getSceneEntry() {
		return entry;
	}

}
