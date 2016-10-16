package com.group4inc.wims.actions;

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
	String name;
	/**The User who needs the notification (e.g. dean) */
	User user;
	/**The amount of time to wait in seconds prior to sending a reminder email (e.g. 259200) */
	Long secToWait;
	/**The subject of the notification email (e.g. Reminder!) */
	String subject;
	/**The body of the notification email (e.g. Hello, Remember to approve or deny a request!) */
	String body;
	
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
		
	}
	
	/**
	 * Sends a reminder email to the user after the waiting period defined in secToWait.
	 *
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 */
	public boolean sendReminder() {
		return true;
	}

}
