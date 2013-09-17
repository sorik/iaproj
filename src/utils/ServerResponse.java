package utils;

/*
 * Define the response type that server will send to client.
 * The string of each enum type should be matched with the div id of notification.html.
 */
public enum ServerResponse {

	// send register email
	SENT_CONFIRMATION("sent-confirmation"),
	// send password email
	SENT_PASSWORD("sent-password"),
	// send password reminder email
	SENT_REMINDER("sent-reminder"),
	// send reset password email
	SENT_RESET("sent-reset"),
	// already registered
	ALREADY_REGISTERED("already-registered"),
	// already confirmed
	ALREADY_CONFIRMED("already-confirmed"),
	// try to register within 5 mins of registration
	ALREADY_SENT_CONFIRMATION("already-sent-confirmation"),
	// try to remind password within 5 mins
	ALREADY_SENT_REMINDER("already-sent-reminder"),
	// not registered email, when User throws NOT FOUND Exception
	NO_SUCH_EMAIL_ERROR("no-such-email-error"),
	// email and password is not matched
	NOT_MATCH_EMAIL_PASSWORD("not-match-email-password"),
	// email and token is not matched
	INVALID_CONFIRM_URL("invalid-confirm-url"),
	// successfully logged-in
	LOGGED_IN("logged-in"),
	// not match logged information (email and access-token in cookie)
	NOT_MATCH_LOGGIN_INFO("not-match-logged-info"),
	// successfully logged-out
	LOGGED_OUT("logged-out"),
	// successfully delete the mail
	DELETED("deleted"),
	
	// invalid email format or empty email
	INVALID_EMAIL("invalid-email"),
	// empty password
	INVALID_PASSWORD("invalid-password"),
	
	// error while sending email, when Mailer throws Exception
	EMAIL_ERROR("email-error"),
	
	
	// unknown response
	UNKNOWN("unknown");
	
	private String str;
	
	private ServerResponse(String str) {
		this.str = str;
	}

	public String toString() {
		return this.str;
	}
}
