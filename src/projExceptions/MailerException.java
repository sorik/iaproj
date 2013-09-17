package projExceptions;

public class MailerException extends Exception{
	
	public MailerException(String message) {
		super(message);
	}
	
	public MailerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
