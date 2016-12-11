package com.group4inc.wims.workflow.model.ui;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import com.group4inc.wims.actions.Email;

import javafx.scene.control.Button;

/**
 * This button represents a button that can send emails and/or 
 * change the state of a WorkflowInstance.
 * 
 * This button does not have an action event since it needs to
 * be assigned from the endusercontroller, since it needs access to the
 * WorkflowInstance to change it's state!
 * 
 * @author crejaud
 *
 */
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
	
	/**
	 * Sends the emails.
	 */
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
	
	/**
	 * Mutator for nextStates
	 * @param nextStates	the list of next states for the workflow instance
	 */
	public void setNextStates(List<String> nextStates) {
		this.nextStates = nextStates;
	}
	
	/**
	 * Mutator for addToMetadata
	 * @param addToMetadata	the list of metadata fields that need to be added on this button click
	 */
	public void setAddToMetadata(List<String> addToMetadata) {
		this.addToMetadata = addToMetadata;
	}
	
	/**
	 * Adding an email (not sent yet)
	 * @param to		the 'to' email address
	 * @param subject	the subject of the email
	 * @param body		the body of the email
	 */
	public void addEmail(String to, String subject, String body) {
		tos.add(to);
		subjects.add(subject);
		bodies.add(body);
	}
}
