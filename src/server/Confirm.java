package server;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import projExceptions.DBException;
import utils.ServerResponse;
import utils.ServerUtils;
import utils.Utils;

/**
 * Servlet implementation class Confirm
 * Manage the confirmation of email registration process
 */
@WebServlet("/Confirm")
public class Confirm extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		
		// Take an email address and token and confirm the registration if both information is valid.
		// Issue a password for the email and send an email with the random-generated password to the corresponding email.
		System.out.println("GET Confirm");
		
		String email = request.getParameter("email");
		String token = request.getParameter("token");
		System.out.println(email + " " + token);
		
		ServerResponse serverRes = ServerResponse.UNKNOWN;
		String param = "";
		try {
			String db_token = User.get(email, "token");
			if (db_token.equals(token)) {
				// email and token are valid
				// issue a password and send email with the password to the corresponding email address.
				String password = Utils.generate_random_password();
				// save password to database
				User.set(email, "password", password);
				User.set(email, "token", "");
				User.set(email, "token_time", "");
				serverRes = ServerResponse.SENT_PASSWORD;
				param = password;
			} else {
				// not matched
				serverRes = ServerResponse.INVALID_CONFIRM_URL;
			}
		} catch (DBException ex) {
			// the email is not in the database
			serverRes = ServerResponse.NO_SUCH_EMAIL_ERROR;
		}
		
		ServerUtils.response(serverRes, response, email, param);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// NO POST method yet
		
	}

}
