package server;


import java.io.IOException;
import java.sql.Timestamp;

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
 * Servlet implementation class Register
 * Manage email registration process
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

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

		// Take an email address.
		// If the email is not in the database, send confirmation email to the corresponding email address.

		System.out.println("POST Register");
		
		ServerResponse serverRes = ServerResponse.UNKNOWN;
		
		String email = request.getParameter("email");
		String param = "";
		if (!Utils.validate_email(email)) {
			// not valid email address
			serverRes = ServerResponse.INVALID_EMAIL;

		} else {
			try {
				if (User.isRegistered(email)) {
					// this email is already registered
					String password = User.get(email, "password");
					if (!password.isEmpty()) {
						// and already confirmed as well
						serverRes = ServerResponse.ALREADY_CONFIRMED;
					} else {
						// but not yet confirmed
						String timst = User.get(email, "token_time");
						if (ServerUtils.is_within_mins(5, timst)) {
							// not yet passed 5 minutes
							serverRes = ServerResponse.ALREADY_SENT_CONFIRMATION;
							
						} else {
							// passed 5 minutes so re-send confirmation email
							String token = User.get(email, "token");
							String confirm_url = ServerUtils.generate_confirm_url(email, token);
							param = confirm_url;
							serverRes = ServerResponse.SENT_CONFIRMATION;
						}
					}
				} else {
					// this email is new, so
					// generate a random token and send a confirmation email
					String token = Utils.generate_random_token();
					String confirm_url = ServerUtils.generate_confirm_url(email, token);
					param = confirm_url;
					serverRes = ServerResponse.SENT_CONFIRMATION;
					
					// save the email and token (token issue time) to the database
					java.util.Date date= new java.util.Date();
					Timestamp timestamp = new Timestamp(date.getTime());
					User.create(email, token, timestamp.toString());
				}

			} catch (DBException e) {
				System.out.println(e);
				param = "";
				serverRes = ServerResponse.UNKNOWN;

			}
    	}
		
		ServerUtils.response(serverRes, response, email, param);
		
	}

}
