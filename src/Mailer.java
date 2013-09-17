
import projExceptions.MailerException;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



/*
 * Sends an email to a corresponding email address with a message.
 */
public class Mailer {

	private static String sender = "no-reply@swen90002-06.cis.unimelb.edu.au";
	
	public static void send(String email, String subject, String message) throws MailerException{

        String result ="";
		
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
	        mimeMsg.setSubject(subject);
	        mimeMsg.setText(message);

	        Transport.send(mimeMsg);
			result = "success";
			 
		} catch (AddressException ex) {
	        System.out.println(ex);
	        throw new MailerException(ex.toString());
	    } catch (MessagingException ex) {
	        System.out.println(ex);
	        throw new MailerException(ex.toString());
	    }
	}

}
