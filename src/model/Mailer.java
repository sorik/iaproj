package model;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import projExceptions.MailerException;
import utils.MailType;



/*
 * Send an email to a corresponding email address with a message.
 * Use postfix as the mail server (port 25)
 */
public class Mailer {

	private static String sender = "no-reply@swen90002-06.cis.unimelb.edu.au";
	
	/*
	 * Send an email to the corresponding email address.
	 * The email subject and content are determined by MailType and param.
	 */
	public static void send(MailType type, String email, String param) throws MailerException{
		
		Properties props = System.getProperties();
		props.setProperty("mail.host", "localhost");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.starttls.enable", "true");
      
        Session session = Session.getInstance(props, null);

		try{
	        // Create a default MimeMessage object.
	        MimeMessage mimeMsg = new MimeMessage(session);
	        mimeMsg.setFrom(new InternetAddress(sender));
	        mimeMsg.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(email));
	        mimeMsg.setSubject(type.getSubject());
	        mimeMsg.setText(type.getMessage()+param);

	        Transport.send(mimeMsg);
			 
		} catch (AddressException ex) {
	        System.out.println(ex);
	        throw new MailerException(ex.toString());
	    } catch (MessagingException ex) {
	        System.out.println(ex);
	        throw new MailerException(ex.toString());
	    }		
	}

}
