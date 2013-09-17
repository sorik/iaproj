

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import projExceptions.DBException;
import projExceptions.MailerException;

/**
 * Servlet implementation class Confirm
 */
@WebServlet("/Confirm")
public class Confirm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String confirm_email_subject = "Please check your password.";
	private static final String confirm_email_message = "You has successfully registered your email address to " +
			                                            "SWEN90002 Email Registration System." +
			                                            "Please check your password below and " +
			                                            "login to our system. Thank you.";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Confirm() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Check email and token. 
		// If successful confirm the registration and send an email with random-generated password.
		System.out.println("Received GET request at Register Servlet.");
		
		String email = request.getParameter("email");
		String token = request.getParameter("token");
		System.out.println(email + " " + token);
		
		// XXX check db
		try {
			String db_token = User.get(email, "token");
			
			if (db_token.equals(token)) {
				
			} else {
				// not matched
			}
		} catch (DBException ex) {
			// not found
		}
		
		// if successful, send an email with password and save the password on db
		String password = Utils.generate_random_password();
		String subject = confirm_email_subject;
		String message = confirm_email_message + "\n\n" + "Your password is " + password;
		
		String result ="";
		try {
			Mailer.send(email, subject, message);
			result = "success";
			// XXX save pwd to database
			User.set(email,  "password", password);
			
		} catch (MailerException ex) {
			System.out.println(ex);
			result = "fail";
		} catch (DBException ex) {
			// not found
		}
		
		// send response
		try {
			response.sendRedirect("notification.html?type=confirm&result="+result);
		} catch (IOException e) {
			System.out.println(e);
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// NO POST method yet
		
	}

}
