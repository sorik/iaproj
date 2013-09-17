package utils;

/*
 * Contains cookie data parsed from HttpServletRequest
 */
public class CookieInfo {

	public String email;
	public String access_token;
	
	public boolean is_logged_in;
	
	
	public CookieInfo(String email, String access_token, boolean is_logged_in){
		this.email = email;
		this.access_token = access_token;
		this.is_logged_in = is_logged_in;
	}
 }
