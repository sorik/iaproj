package server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import projExceptions.DBException;
import utils.CookieInfo;
import utils.ServerResponse;
import utils.ServerUtils;

/**
 * Servlet implementation class Logout
 * Manage Logout process
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// NO POST method yet

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Take an email and an access-token.
		// Check their validation.
		// If validate, logout the email that is currently logged in
		
		System.out.println("GET Logout");
		
		ServerResponse serverRes = ServerResponse.UNKNOWN;
		
		CookieInfo cookieInfo = ServerUtils.get_cookie(request);
		if (cookieInfo.is_logged_in) {
			// successfully logged out
			try {
				User.set(cookieInfo.email, "access-token", "");
				// remove access-token from the database
				// remove cookie
				serverRes = ServerResponse.LOGGED_OUT;
				
			} catch (DBException e) {
				// should not be here
				System.out.println(e);
				serverRes = ServerResponse.UNKNOWN;
			}
			
		} else {
			serverRes = ServerResponse.NOT_MATCH_LOGGIN_INFO;
		}
		
		ServerUtils.response(serverRes, response, cookieInfo.email, "");
	}
		
}
