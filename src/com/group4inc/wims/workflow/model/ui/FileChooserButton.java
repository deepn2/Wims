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
	
	/**
	 * If a file chooser button can select multiple files or just one
	 * @return	true if can select multiple files, else false
	 */
	public boolean canSelectMultipleFiles() {
		return canSelectMultipleFiles;
	}
	
	/**
	 * Set the boolean value of whether this file chooser button
	 * can select multiple files or not.
	 * @param b		the newly assigned canSelectMultipleFiles boolean
	 */
	public void setCanSelectMultipleFiles(boolean b) {
		this.canSelectMultipleFiles = b;
	}
	
	/**
	 * Accessor for the files at any time.
	 * @return	the list of files
	 */
	public List<File> getFiles() {
		return files;
	}
	
	/**
	 * Add one file (for single file use)
	 * @param file	the file to be added
	 */
	public void setFile(File file) {
		files.clear();
		files.add(file);
	}
	
	/**
	 * Add a list of files (for multiple file use)
	 * @param files	the list of files to be added
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}
}
