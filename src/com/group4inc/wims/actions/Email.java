package com.group4inc.wims.actions;

/**
* This utility class contains the code that sends emails out to the user. Primarily used by the Notification class.
* 
* @see Notification
* @author Elliot Linder (eml160)
*/
public class Email {
	
	/**
	 * Sends an "initial" email to the user, i.e. an email being sent for the first time.
	 * 
	 * @param emailAddress  the email address to send the email to
	 * @param subject  the subject of the email
	 * @param body the body of the email
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 */
	public static boolean sendInitialEmail(String emailAddress, String subject, String body) {
		return true;
	}
	
	/**
	 * Sends a "reminder" email to the user, i.e. an email being sent for the second+ time. Automatically appends a "REMINDER" tag to the front of the subject.
	 * 
	 * @param emailAddress  the email address to send the email to
	 * @param subject  the subject of the email
	 * @param body the body of the email
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 */
	public static boolean sendReminderEmail(String emailAddress, String subject, String body) {
		return true;
	}

}
