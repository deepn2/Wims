package com.group4inc.wims.actions;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.group4inc.wims.idm.User;

/**
* This class defines the Notification object that is created by the Workflow to handle notifications.
*  
* <P>A notification is sent to a user when it is their turn to complete a task or if a certain period of time expires (e.g. 4 days after initial notification to complete task). 
* 
* @see Workflow
* @author Elliot Linder (eml160)
*/
public class Notification {
	
	/**The name of the notification (e.g. remindDean) */
	private String name;
	/**The User who needs the notification (e.g. dean) */
	private User user;
	/**The amount of time to wait in seconds prior to sending a reminder email (e.g. 259200) */
	private Long secToWait;
	/**The subject of the notification email (e.g. Reminder!) */
	private String subject;
	/**The body of the notification email (e.g. Hello, Remember to approve or deny a request!) */
	private String body;
	
	/**
	 * Constructor for Notification objects.
	 *
	 * @param  name the name of the Notification to be constructed
	 * @param  user the User who needs the notification that is being constructed
	 * @param  secToWait the amount of time to wait in seconds prior to sending a reminder Notification email.
	 * @param  subject  the subject of the Notification email being constructed
	 * @param  body  The body of the notification email being constructed
	 */
	public Notification(String name, User user, Long secToWait, String subject, String body) {
		this.name = name;
		this.user = user;
		this.secToWait = secToWait;
		this.subject = subject;
		this.body = body;
	}
	
	/**
	 * Returns the name of the notification.
	 * @return  the name of the notification
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the notification.
	 * @param name  new name of the notification
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the User object who needs the notification. 
	 * @return  the User object who needs the notification
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the User object who needs the notification.
	 * @param user  new User object of the notification
	 */
	public void setUser(User user) {
		if(com.group4inc.wims.idm.IdMSerDB.getUserDB().containsValue(user))
			this.user = user;
	}
	
	/**
	 * Returns the amount of time to wait prior to sending the reminder email.
	 * @return  the Long of the amount of time to wait
	 */
	public Long getSecToWait() {
		return secToWait;
	}

	/**
	 * Sets the amount of time to wait prior to sending the reminder email.
	 * @param secToWait  new amount of time to wait
	 */
	public void setSecToWait(Long secToWait) {
		this.secToWait = secToWait;
	}
	
	/**
	 * Returns the subject of the email.
	 * @return  the String subject line of the email
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * Sets the subject of the email.
	 * @param subject  new String subject line of the email
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * Returns the body of the email.
	 * @return  the body of the email
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Sets the body of the email.
	 * @param body  new String body of the email
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Sends an initial email to the user.
	 *
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 */
	public void sendInitial() {
		try {
			Email.sendInitialEmail(user.getEmail(), subject, body);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a reminder email to the user after the waiting period defined in secToWait.
	 */
	public void sendReminder() {
		try {
			Email.sendReminderEmail(user.getEmail(), subject, body);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
