

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import projExceptions.DBException;
import projExceptions.MailerException;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String base_url = "http://swen90002-06.cis.unimelb.edu.au:8080/";
	private static final String confirm_servlet = "proj1/Confirm";
	
	private static final String register_email_subject = "Please Confirm your registration.";
	private static final String register_email_message = "You has registered your email address to " +
			                                            "SWEN90002 Email Registration System." +
			                                            "Please Click the following link to " +
			                                            "complete your registration. Thank you.";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// NO GET method yet
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check email validation, and if successful send confirmation email to the corresponding email address.
		System.out.println("Received Post request at Register Servlet.");
		
		String result = "";
		String email = request.getParameter("email");
		if (!Utils.validate_email(email)) {
			// not valid email address
			result = "Invalid Email address";

		} else {
			// valid email address, then send confirmation email
			String token = Utils.generate_random_token();
			String confirm_url = generate_confirm_url(email, token);
			String subject = register_email_subject;
			String message = register_email_message + "\n\n" + confirm_url;
			
			try {
				Mailer.send(email, subject, message);
				result = "success";

				java.util.Date date= new java.util.Date();
				Timestamp timestamp = new Timestamp(date.getTime());
				User.create(email, token, timestamp.toString());
				
			} catch (MailerException ex) {
				result = ex.toString();
			} catch (DBException ex) {
				result = ex.toString();
			}
		}
		
		// send response
		JSONObject obj = new JSONObject();
		obj.put("type", "register");
		obj.put("result", result);
		try {
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();			
		} catch (IOException e) {
			System.out.println(e);
		}		
		
	}
	
	
	/*
	 * Generate Confirmation URL consisting of email and token.
	 */
	private String generate_confirm_url(String email, String token) {
		
		String url = base_url+confirm_servlet+"?";
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

}
