package com.group4inc.wims.workflow.model.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;

public class FileChooserButton extends Button {
	private List<File> files;
	private boolean canSelectMultipleFiles;
	
	public FileChooserButton(String label, boolean canSelectMultipleFiles) {
		super(label);
		files = new ArrayList<>();
		this.canSelectMultipleFiles = canSelectMultipleFiles;
	}
	
	public boolean canSelectMultipleFiles() {
		return canSelectMultipleFiles;
	}
	
	public void setCanSelectMultipleFiles(boolean b) {
		this.canSelectMultipleFiles = b;
	}
	
	public List<File> getFiles() {
		return files;
	}
	
	public void setFile(File file) {
		files.clear();
		files.add(file);
	}
	
	public void setFiles(List<File> files) {
		this.files = files;
	}
}
