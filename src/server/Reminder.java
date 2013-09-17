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
 * Servlet implementation class Reminder
 * Manage Password reminder process
 */
@WebServlet("/Reminder")
public class Reminder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reminder() {
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
		// If the email is registered and confirmed, send password reminder email to the corresponding email address.

		System.out.println("POST Reminder");
		
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
						// and confirmed as well
						// then send password reminder email.
						
						// should force one reminder email per 5 minutes
						String timst = User.get(email, "reminder-sent-at");
						System.out.println(timst);
						if (ServerUtils.is_within_mins(5, timst)) {
							// already sent reminder mail
							serverRes = ServerResponse.ALREADY_SENT_REMINDER;
						} else {
							// send reminder email
							serverRes = ServerResponse.SENT_REMINDER;
							param = password;
							// save the time to enforce one reminder email per 5 minutes
							java.util.Date date= new java.util.Date();
							Timestamp timestamp = new Timestamp(date.getTime());
							User.set(email, "reminder-sent-at", timestamp.toString());							
						}
					} else {
						// but not yet confirmed
						serverRes = ServerResponse.ALREADY_REGISTERED;
											}
				} else {
					// not registered email
					serverRes = ServerResponse.NO_SUCH_EMAIL_ERROR;
				}

			} catch (DBException e) {
				// should not happen.
				System.out.println(e);
				serverRes = ServerResponse.UNKNOWN;
				param = "";

			}
    	}
		
		ServerUtils.response(serverRes, response, email, param);
	}

}
