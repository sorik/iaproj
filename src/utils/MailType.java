package utils;

/*
 * Define the mail type that this server handles.
 * Each type should have the subject and content for the email.
 */
public enum MailType {
	
	REGISTER("Please Confirm your registration.", 
			"You has registered your email address to " +
            "SWEN90002 Email Registration System." +
            "Please Click the following link to " +
            "complete your registration. Thank you." +
            "\n\n"),
	REMINDER("Please check your password.", 
			"You requested to remind you your password." +
	        "Please check your password below.\n\n" +
			"Your password is "),
	PASSWORD("Please check your password.",
			"You has successfully registered your email address to " +
            "SWEN90002 Email Registration System." +
            "Please check your password below and " +
            "login to our system. Thank you.\n\n" +
            "Your password is "),
	RESET("Please check your password.",
			"You has successfully reset your password." +
	        "Please check your password below. Thank you.\n\n" +
	        "Your password is ");
	

	private String subject;
	private String message;
	
	private MailType(String subject, String message) {
		this.subject = subject;
		this.message = message;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public String getMessage() {
		return this.message;
	}
	
};