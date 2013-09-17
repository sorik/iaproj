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
 * Servlet implementation class Login
 * Manage Login process
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		
		// Take an email and password and check their validation.
		// If successful issue access token, and save it to db.
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
        String param = "";		
		ServerResponse serverRes = ServerResponse.UNKNOWN;
		
		if (email.isEmpty()) {
			serverRes = ServerResponse.INVALID_EMAIL;
		}
		
		if (password.isEmpty()) {
			serverRes = ServerResponse.INVALID_PASSWORD;
		}
		
		try {
			String db_password = User.get(email, "password");
			if (db_password.equals(password)) {
				// logged in
				// issue access token and save it to the db.
				String access_token = Utils.generate_random_token();
				User.set(email, "access_token", access_token);
				param = access_token;
				serverRes = ServerResponse.LOGGED_IN;
			} else {
				// not matched
				serverRes = ServerResponse.NOT_MATCH_EMAIL_PASSWORD;
			}
		} catch (DBException ex) {
			// not found
			serverRes = ServerResponse.NO_SUCH_EMAIL_ERROR;
		}

		ServerUtils.response(serverRes, response, email, param);
		

	}

}
