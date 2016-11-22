package com.group4inc.wims.actions;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
* This utility class contains the code that sends emails out to the user. Primarily used by the Notification class.
* 
* @see Notification
* @author Elliot Linder (eml160)
*/
public class Email {
	
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	
	/**
	 * Sends an "initial" email to the user, i.e. an email being sent for the first time.
	 * 
	 * @param emailAddress  the email address to send the email to
	 * @param subject  the subject of the email
	 * @param body the body of the email
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 */
	
	public static void sendEmail(String emailAddress, String subject, String body) throws AddressException, MessagingException {
		//this method largely influenced by http://crunchify.com/java-mailapi-example-send-an-email-via-gmail-smtp/
		
		//set up mail server properties
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.server.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		
		//set up Mail Session and prep email for sending
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
		generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress("wims-archive@elliotlinder.net"));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setContent(body, "text/html");
		
		//send email
		Transport transport = getMailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", "someuser", "somepass");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
	
	public static void sendInitialEmail(String emailAddress, String subject, String body) throws AddressException, MessagingException {
		sendEmail(emailAddress, subject, body);
	}
	
	/**
	 * Sends a "reminder" email to the user, i.e. an email being sent for the second+ time. Automatically appends a "REMINDER" tag to the front of the subject.
	 * 
	 * @param emailAddress  the email address to send the email to
	 * @param subject  the subject of the email
	 * @param body the body of the email
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public static void sendReminderEmail(String emailAddress, String subject, String body) throws AddressException, MessagingException {
		sendEmail(emailAddress, subject, body);
	}

}
