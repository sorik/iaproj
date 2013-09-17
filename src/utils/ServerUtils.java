package utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Mailer;
import model.User;

import org.json.simple.JSONObject;

import projExceptions.DBException;
import projExceptions.MailerException;

/*
 * Provide the methods that Servlets need.
 */
public class ServerUtils {

	private static final String base_url = "http://swen90002-06.cis.unimelb.edu.au:8080/";
	private static final String confirm_servlet = "proj1/Confirm";
	
			
	/*
	 * Set HttpServletResponse according to the ServerResponse
	 */
	public static void response(ServerResponse resType, HttpServletResponse response, String email, String param) {
		// If it was GET Request, then need to set 'SendRedirect' to response.
		// If it was POST Request, should not set 'SendRedirect' to response, as the request is sent by ajax.
		try {
		    switch (resType) {
		    case SENT_CONFIRMATION:
		    	// send mail
		    	Mailer.send(MailType.REGISTER, email, param);
		    	break;
		    case SENT_PASSWORD:
		    	// send mail
		    	Mailer.send(MailType.PASSWORD, email, param);
		    	response.sendRedirect("notification.html?type=reset&result="+resType.toString());
		    	break;
		    case SENT_REMINDER:
		    	// from POST Reminder
		    	Mailer.send(MailType.REMINDER, email, param);
		    	break;
		    case SENT_RESET:
		    	// from GET Reset
	    		delete_cookie(response);
	    		response.sendRedirect("notification.html?type=reset&result="+resType.toString());
		    	Mailer.send(MailType.RESET, email, param);  	
		    	break;
		    case ALREADY_REGISTERED:
		    	// from POST Reminder
		    	break;
		    case ALREADY_CONFIRMED:
		    	break;
		    case ALREADY_SENT_CONFIRMATION:
		    	// 
		    	break;
		    case ALREADY_SENT_REMINDER:
		    	// from POST Reminder
		    	break;
		    case LOGGED_IN:
		    	add_cookie(response, email, param);
				break;
		    case LOGGED_OUT:
	    		delete_cookie(response);
	    		response.sendRedirect("notification.html?type=logout&result="+resType.toString());
		    	break;
		    case DELETED:
	    		delete_cookie(response);
	    		response.sendRedirect("notification.html?type=logout&result="+resType.toString());		
	    		System.out.println("aaaa");
	    		break;
		    case NOT_MATCH_LOGGIN_INFO:
		    	response.sendRedirect("notification.html?type=logout&result="+resType.toString());
		    	break;
		    case INVALID_CONFIRM_URL:
		    	response.sendRedirect("notification.html?type=confirm&result="+resType.toString());
		    	break;
		    case INVALID_EMAIL:
		    	//
		    	break;
		    case INVALID_PASSWORD:
		    	//
		    	break;
		    case EMAIL_ERROR:
		    	//
		    	break;
		    case NO_SUCH_EMAIL_ERROR:
		    	//
		    	break;
		    }			
		} catch (MailerException e) {
			System.out.println(e);
			resType = ServerResponse.EMAIL_ERROR;
		} catch (IOException e) {
			System.out.println(e);
			resType = ServerResponse.EMAIL_ERROR;			
		}

	    
		// set response
		JSONObject obj = new JSONObject();
		obj.put("result", resType.toString());
		try {
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/*
	 * Get cookies from the request and validates the information (email and access-token)
	 */
	public static CookieInfo get_cookie(HttpServletRequest request) {
		
		String email = "";
		String token = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null ) {
			for (int i = 0; i < cookies.length ; i++) {
				if ("email".equals(cookies[i].getName())) {
					email = cookies[i].getValue();
				} else if ("access-token".equals(cookies[i].getName())) {
					token = cookies[i].getValue();
				}				
			}			
		}
		
		if (email.isEmpty() || token.isEmpty()){
			// no email or token in cookies
			return new CookieInfo(email, token, false);
		}

		// check database
		try {
			String db_token = User.get(email, "access_token");
			if (token.equals(db_token)) {
				// email and token matched
				return new CookieInfo(email, token, true);
			} else {
				// email and token not matched
				return new CookieInfo(email, token, false);
			}
		} catch (DBException e) {
			// no such email
			return new CookieInfo(email, token, false);
		}
	}
	
	
	/*
	 * Check the time different of token issued and current time.
	 */
	public static boolean is_within_mins(int mins, String timestamp) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		try {
			java.util.Date parsedDate = dateFormat.parse(timestamp);
			long comp = parsedDate.getTime();
			
			long current = System.currentTimeMillis();
			
			long diff = Math.abs(comp - current);
			
			if (diff/(1000*60) >= mins) {
				return false;
			}
			
			return true;

		} catch (ParseException e) {
			// not matched format, return false to resend email.
			return false;
		}

	}
	
	public static String get_base_url() {
		return ServerUtils.base_url;
	}
	/*
	 * Generate Confirmation URL consisting of email and token.
	 */
	public static String generate_confirm_url(String email, String token) {
		
		String url = ServerUtils.base_url + ServerUtils.confirm_servlet+"?";
		
		//append parameters
		try {
			url += "email=";
			url += URLEncoder.encode(email, "UTF-8");
			url += "&token=";
			url += URLEncoder.encode(token, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
		}

		return url;
	}
	
	/*
	 * Create cookie that will be available during 24 hours
	 */
	private static Cookie generate_cookie(String key, String value) {
		
		Cookie cookie = new Cookie(key, value);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(1440);   // 60*24 = 1mins 
		return cookie;
	}
	
	/*
	 * Create cookie that will be deleted
	 */
	private static Cookie remove_cookie(String key) {
		Cookie cookie = new Cookie(key, "");
		cookie.setMaxAge(0);
		return cookie;
	}
	
	/*
	 * Add cookie for login information (email and access-token) to the response
	 */
	private static void add_cookie(HttpServletResponse response, String email, String access_token) {

		response.addCookie(generate_cookie("email", email));
		response.addCookie(generate_cookie("access-token", access_token));
	}
	
	/*
	 * Delete cookie for login information (email and access-token) to the response
	 */
	private static void delete_cookie(HttpServletResponse response) {

		response.addCookie(remove_cookie("email"));
		response.addCookie(remove_cookie("access-token"));
	}
}
