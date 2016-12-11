package com.group4inc.wims.workflow.model.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

public class FileChooserEventHandler implements EventHandler<ActionEvent>{

	private FileChooserButton fcb;
	
	public FileChooserEventHandler(FileChooserButton fcb) {
		this.fcb = fcb;
	}
	
	@Override
	public void handle(ActionEvent event) {
		FileChooser fc = new FileChooser();
		if (!fcb.canSelectMultipleFiles()) {
			fcb.setFile(fc.showOpenDialog(fcb.getScene().getWindow()));
		} else {
			fcb.setFiles(fc.showOpenMultipleDialog(fcb.getScene().getWindow()));
		}
	}

}
