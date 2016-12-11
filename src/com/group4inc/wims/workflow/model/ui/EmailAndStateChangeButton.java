package com.group4inc.wims.workflow.model.ui;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import com.group4inc.wims.actions.Email;

import javafx.scene.control.Button;

public class EmailAndStateChangeButton extends Button {
	private List<String> tos = new ArrayList<>();
	private List<String> subjects = new ArrayList<>();
	private List<String> bodies = new ArrayList<>();
	
	private List<String> nextStates = new ArrayList<>();
	private List<String> addToMetadata = new ArrayList<>();
	
	private List<String> requiredIds = new ArrayList<>();
	
	public EmailAndStateChangeButton(String label) {
		super(label);
	}
	
	public void sendEmails() {
		for (int i = 0; i < tos.size(); i++) {
			try {
				Email.sendInitialEmail(tos.get(i), subjects.get(i), bodies.get(i));
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setNextStates(List<String> nextStates) {
		this.nextStates = nextStates;
	}
	
	public void setAddToMetadata(List<String> addToMetadata) {
		this.addToMetadata = addToMetadata;
	}
	
	public void addEmail(String to, String subject, String body) {
		tos.add(to);
		subjects.add(subject);
		bodies.add(body);
	}
}
